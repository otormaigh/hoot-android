package ie.pennylabs.hoot.feature.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.service.AlbumCoverService
import ie.pennylabs.hoot.toolbox.SingleLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SettingsViewModel(private val songDao: SongDao) : ViewModel(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  fun refreshMissingCovers(context: Context): LiveData<Boolean> {
    val resultLiveData = SingleLiveData<Boolean>()
    resultLiveData.postValue(true)
    launch {
      songDao.fetchAllSync()
        .filter { it.realAlbumCover.isEmpty() }
        .forEach { launch { AlbumCoverService.fetchRealAlbumCover(context, it.time) } }

      resultLiveData.postValue(false)
    }

    return resultLiveData
  }
}