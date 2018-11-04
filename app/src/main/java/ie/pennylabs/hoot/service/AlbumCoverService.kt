package ie.pennylabs.hoot.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import ie.pennylabs.hoot.api.ApiClient
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.api.AlbumCover
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResult
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AlbumCoverService : IntentService("AlbumIntentService"), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  override fun onHandleIntent(intent: Intent?) {
    when (intent?.extras?.getString(REASON)) {
      FETCH -> fetchAlbumCovers(1)
      RE_FETCH -> fetchAlbumCovers((intent.extras?.getInt(LAST_PAGE) ?: 1) + 1)
      FETCH_REAL -> fetchRealAlbumCover(intent.extras?.getLong(SONG_ID) ?: -1)
    }
  }

  private fun fetchAlbumCovers(page: Int) = launch {
    if (app.database.albumCoverDao().unconsumedCount() < 25) {
      when (
        val result = ApiClient.imgur().getAlbumCovers(page = page).awaitResult()) {
        is ru.gildor.coroutines.retrofit.Result.Ok -> saveAlbumCovers(result.value.data, page)
        is ru.gildor.coroutines.retrofit.Result.Error -> Timber.e("fetchAlbumCovers : Error -> ${result.exception}")
        is ru.gildor.coroutines.retrofit.Result.Exception -> Timber.e("fetchAlbumCovers : Exception -> ${result.exception}")
      }
    }
  }

  private suspend fun saveAlbumCovers(data: List<AlbumCover>, page: Int) {
    app.database.albumCoverDao()
      .insert(data
        .asSequence()
        .filter { !it.isAd }
        .filter { !it.isAlbum }
        .filter { !it.animated }
        .toList())

    delay(250)
    val songsWithoutAlbums = app.database.songDao().fetchSongsWithoutAlbums()
    val unconsumedAlbumCovers = app.database.albumCoverDao().fetchAllUnconsumed()

    songsWithoutAlbums.forEachIndexed { index, song ->
      unconsumedAlbumCovers.getOrNull(index)?.let { albumCover ->
        albumCover.hasBeenConsumed = true
        app.database.albumCoverDao().update(albumCover)
        app.database.songDao().updateAlbumCover(song.time, albumCover.link)
      } ?: return@forEachIndexed
    }

    if (app.database.albumCoverDao().unconsumedCount() < 25) AlbumCoverService.refetch(this@AlbumCoverService, page)
  }

  private fun fetchRealAlbumCover(songId: Long) = launch {
    val song = app.database.songDao().fetchById(songId) ?: return@launch
    when (
      val result = ApiClient.lastFm().trackInfo(song.artist.trimStart(), song.title.trimEnd()).awaitResult()) {
      is ru.gildor.coroutines.retrofit.Result.Ok -> {
        val url = result.value.track.album?.image?.firstOrNull { it.size == "extralarge" }?.text
        if (url?.isNotEmpty() == true) app.database.songDao().updateRealAlbumCover(songId, url)
      }
      is ru.gildor.coroutines.retrofit.Result.Error -> Timber.e("fetchRealAlbumCover : Error -> ${result.exception}")
      is ru.gildor.coroutines.retrofit.Result.Exception -> Timber.e("fetchRealAlbumCover : Exception -> ${result.exception}")
    }
  }

  companion object {
    private const val REASON = "reason"
    private const val FETCH = "fetch"
    private const val RE_FETCH = "re_fetch"
    private const val FETCH_REAL = "FETCH_REAL"

    private const val LAST_PAGE = "last_page"
    private const val SONG_ID = "song_id"

    fun fetch(context: Context) {
      context.startService(Intent(context, AlbumCoverService::class.java).apply {
        putExtra(REASON, FETCH)
      })
    }

    fun refetch(context: Context, lastPage: Int) {
      context.startService(Intent(context, AlbumCoverService::class.java).apply {
        putExtra(REASON, RE_FETCH)
        putExtra(LAST_PAGE, lastPage)
      })
    }

    fun fetchRealAlbumCover(context: Context, songId: Long) {
      context.startService(Intent(context, AlbumCoverService::class.java).apply {
        putExtra(REASON, FETCH_REAL)
        putExtra(SONG_ID, songId)
      })
    }
  }
}