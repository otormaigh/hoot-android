package ie.pennylabs.hoot.data.model.api

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.squareup.moshi.Json

@Entity(tableName = "album_cover")
data class AlbumCover(
  @PrimaryKey val id: String,
  val datetime: Long,
  val type: String?,
  val animated: Boolean,
  val width: Int,
  val height: Int,
  val size: Int,
  @field:Json(name = "is_ad") val isAd: Boolean,
  val link: String,
  @field:Json(name = "is_album") val isAlbum: Boolean,
  val hasBeenConsumed: Boolean)


@Dao
interface AlbumCoverDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(data: List<AlbumCover>)

  @Query("SELECT * FROM album_cover ORDER BY datetime DESC")
  fun fetchAll(): LiveData<List<AlbumCover>>

  @Query("SELECT * FROM album_cover WHERE hasBeenConsumed = 0")
  fun fetchAllUnconsumed(): LiveData<List<AlbumCover>>

  @Query("SELECT COUNT(id) FROM album_cover WHERE hasBeenConsumed = 0")
  fun unconsumedCount(): Int
}