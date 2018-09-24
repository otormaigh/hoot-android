package ie.pennylabs.hoot.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.data.model.SongDao

@Database(entities = [Song::class], version = 2)
abstract class HootDatabase : RoomDatabase() {
  abstract fun songDao(): SongDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
  override fun migrate(database: SupportSQLiteDatabase) {
    database.execSQL("ALTER TABLE Song RENAME TO song_old")
    database.execSQL("CREATE TABLE song (time INTEGER NOT NULL, raw_string TEXT NOT NULL, PRIMARY KEY(time))")
    database.execSQL("INSERT INTO song (time, raw_string) SELECT time, title FROM song_old")
    database.execSQL("DROP TABLE song_old")
  }
}
