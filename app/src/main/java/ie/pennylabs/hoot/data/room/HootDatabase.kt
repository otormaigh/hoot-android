package ie.pennylabs.hoot.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.data.model.api.AlbumCover
import ie.pennylabs.hoot.data.model.api.AlbumCoverDao

@Database(entities = [Song::class, AlbumCover::class], version = 4)
abstract class HootDatabase : RoomDatabase() {
  abstract fun songDao(): SongDao
  abstract fun albumCoverDao(): AlbumCoverDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE Song RENAME TO song_old")
    database.execSQL("CREATE TABLE song (time INTEGER NOT NULL, raw_string TEXT NOT NULL, PRIMARY KEY(time))")
    database.execSQL("INSERT INTO song (time, raw_string) SELECT time, title FROM song_old")
    database.execSQL("DROP TABLE song_old")
  }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE Song RENAME TO song_old")
    database.execSQL("CREATE TABLE song (time INTEGER NOT NULL, raw_string TEXT NOT NULL, album_cover TEXT NOT NULL DEFAULT '', PRIMARY KEY(time))")
    database.execSQL("INSERT INTO song (time, raw_string) SELECT time, raw_string FROM song_old")
    database.execSQL("DROP TABLE song_old")

    database.execSQL("CREATE TABLE album_cover (id TEXT NOT NULL, datetime INTEGER NOT NULL, type TEXT, animated INTEGER NOT NULL, width INTEGER NOT NULL, height INTEGER NOT NULL, size INTEGER NOT NULL, is_ad INTEGER NOT NULL, link TEXT NOT NULL, is_album INTEGER NOT NULL, has_been_consumed INTEGER NOT NULL, PRIMARY KEY(id))")
  }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
  override fun migrate(database: SupportSQLiteDatabase) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}