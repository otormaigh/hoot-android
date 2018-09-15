package ie.pennylabs.hoot.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Song(
  @PrimaryKey
  val time: Long,
  val title: String) {
}

@Dao
interface SongDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(song: Song)

  @Query("SELECT * FROM Song ORDER BY time desc")
  fun fetchAll(): LiveData<List<Song>>
}