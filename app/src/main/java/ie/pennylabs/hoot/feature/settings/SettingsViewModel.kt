package ie.pennylabs.hoot.feature.settings

import android.content.Context
import androidx.lifecycle.LiveData
import ie.pennylabs.hoot.arch.HootViewModel
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.service.AlbumCoverService
import ie.pennylabs.hoot.toolbox.SingleLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel
@Inject
constructor(private val songDao: SongDao) : HootViewModel() {

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