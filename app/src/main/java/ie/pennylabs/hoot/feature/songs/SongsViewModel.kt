package ie.pennylabs.hoot.feature.songs

import androidx.lifecycle.ViewModel
import ie.pennylabs.hoot.data.model.SongDao
import javax.inject.Inject

class SongsViewModel
@Inject
constructor(private val songDao: SongDao) : ViewModel() {

  fun fetchSongs() = songDao.fetchAll()
}