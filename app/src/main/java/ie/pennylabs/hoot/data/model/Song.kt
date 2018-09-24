package ie.pennylabs.hoot.data.model

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction

@Entity(tableName = "song")
data class Song(
  @PrimaryKey
  val time: Long,
  @ColumnInfo(name = "raw_string")
  val rawString: String) {

  val title: String
    get() = rawString.split("by").first()
  val artist: String
    get() = rawString.split("by").last()
  val sanitisedString: String
    get() = rawString.replace("by ", "")
}

@Dao
interface SongDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(song: Song)

  @Transaction
  fun insertUnique(song: Song) {
    val songs = fetchByRawSince(song.rawString, System.currentTimeMillis() - 600_000)
    if (songs.isEmpty()) {
      insert(song)
    }
  }

  @Query("SELECT * FROM song WHERE time > :since AND raw_string = :rawString")
  fun fetchByRawSince(rawString: String, since: Long): List<Song?>

  @Query("SELECT * FROM song ORDER BY time DESC")
  fun fetchAll(): LiveData<List<Song>>

  @Query("SELECT * FROM song ORDER BY time DESC")
  fun fetchAllSync(): List<Song>
}
