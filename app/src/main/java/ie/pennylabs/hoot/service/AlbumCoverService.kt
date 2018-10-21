package ie.pennylabs.hoot.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import ie.pennylabs.hoot.api.ApiClient
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.api.AlbumCover
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gildor.coroutines.retrofit.awaitResult
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class AlbumCoverService : IntentService("AlbumIntentService"), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  override fun onHandleIntent(intent: Intent?) {
    when (intent?.extras?.getString(REASON)) {
      FETCH -> fetchAlbumCovers()
    }
  }

  private fun fetchAlbumCovers() = launch {
    if (app.database.albumCoverDao().unconsumedCount() < 25) {
      when (val result = ApiClient.instance().getAlbumCovers().awaitResult()) {
        is ru.gildor.coroutines.retrofit.Result.Ok -> saveAlbumCovers(result.value.data)
        is ru.gildor.coroutines.retrofit.Result.Error -> Timber.e("fetchAlbumCovers : Error -> ${result.exception}")
        is ru.gildor.coroutines.retrofit.Result.Exception -> Timber.e("fetchAlbumCovers : Exception -> ${result.exception}")
      }
    }
  }

  private fun saveAlbumCovers(data: List<AlbumCover>) {
    app.database.albumCoverDao()
      .insert(data
        .filter { !it.isAd }
        .filter { !it.animated })
  }

  companion object {
    private const val REASON = "reason"
    private const val FETCH = "fetch"

    fun fetch(context: Context) {
      context.startService(Intent(context, AlbumCoverService::class.java).apply {
        putExtra(REASON, FETCH)
      })
    }
  }
}