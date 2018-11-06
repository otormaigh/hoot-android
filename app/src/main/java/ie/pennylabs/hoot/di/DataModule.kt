package ie.pennylabs.hoot.di

import androidx.room.Room
import dagger.Module
import dagger.Provides
import ie.pennylabs.hoot.HootApplication
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.data.room.HootDatabase
import ie.pennylabs.hoot.data.room.MIGRATION_1_2
import ie.pennylabs.hoot.data.room.MIGRATION_2_3
import ie.pennylabs.hoot.data.room.MIGRATION_3_4
import javax.inject.Singleton

@Module
object DataModule {
  @Provides
  @Singleton
  @JvmStatic
  fun provideDatabase(application: HootApplication): HootDatabase =
    Room.databaseBuilder(application, HootDatabase::class.java, "hoot.db")
      .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
      .build()

  @Provides
  @JvmStatic
  fun provideSongDao(database: HootDatabase): SongDao =
    database.songDao()
}