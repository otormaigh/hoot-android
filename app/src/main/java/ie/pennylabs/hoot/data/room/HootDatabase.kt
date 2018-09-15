package ie.pennylabs.hoot.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.data.model.SongDao

@Database(entities = [Song::class], version = 1)
abstract class HootDatabase : RoomDatabase() {
  abstract fun songDao(): SongDao
}