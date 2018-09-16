package ie.pennylabs.hoot.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction

@Entity
data class Song(
  @PrimaryKey
  val time: Long,
  val title: String)

@Dao
interface SongDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(song: Song)

  @Transaction
  fun insertUnique(song: Song) {
    val songs = fetchByTitleSince(song.title, System.currentTimeMillis() - 600_000)
    if (songs.isEmpty()) {
      insert(song)
    }
  }

  @Query("SELECT * FROM Song WHERE time > :since AND title = :title")
  fun fetchByTitleSince(title: String, since: Long): List<Song?>

  @Query("SELECT * FROM Song ORDER BY time DESC")
  fun fetchAll(): LiveData<List<Song>>
}
