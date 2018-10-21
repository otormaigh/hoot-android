package ie.pennylabs.hoot.data.model.api

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
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
  @ColumnInfo(name = "is_ad")
  @field:Json(name = "is_ad") val isAd: Boolean,
  val link: String,
  @ColumnInfo(name = "is_album")
  @field:Json(name = "is_album") val isAlbum: Boolean,
  @ColumnInfo(name = "has_been_consumed") var hasBeenConsumed: Boolean)

@Dao
interface AlbumCoverDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  fun insert(data: List<AlbumCover>)

  @Query("SELECT * FROM album_cover WHERE has_been_consumed = 0 LIMIT 1")
  fun fetchUnconsumed(): AlbumCover?

  @Query("SELECT * FROM album_cover WHERE has_been_consumed = 0")
  fun fetchAllUnconsumed(): List<AlbumCover>

  @Update(onConflict = OnConflictStrategy.REPLACE)
  fun update(albumCover: AlbumCover)

  @Query("SELECT COUNT(id) FROM album_cover WHERE has_been_consumed = 0")
  fun unconsumedCount(): Int
}