package ie.pennylabs.hoot.data.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationAndroidTest {
  @Rule
  @JvmField
  val testHelper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    HootDatabase::class.java.canonicalName,
    FrameworkSQLiteOpenHelperFactory())
  private val testDbName = "hoot_1_test.db"

  @Test
  fun testMigration1To2() {
    testHelper.createDatabase(testDbName, 1).use {
      val values = ContentValues()
      values.put("time", 123456789)
      values.put("title", "This is a title")
      it.insert("Song", SQLiteDatabase.CONFLICT_REPLACE, values)
    }
    testHelper.runMigrationsAndValidate(testDbName, 2, true, MIGRATION_1_2)

    val songs = getMigratedRoomDatabase().songDao().fetchAllSync()
    assertThat(songs.size)
      .isEqualTo(1)
    assertThat(songs.first().time)
      .isEqualTo(123456789)

    assertThat(songs.first().rawString)
      .isEqualTo("This is a title")
  }

  @Test
  fun testMigration2To3() {
    testHelper.createDatabase(testDbName, 2).use {
      it.insert("song", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
        put("time", 123456789)
        put("raw_string", "This is a title")
      })
    }
    testHelper.runMigrationsAndValidate(testDbName, 3, true, MIGRATION_2_3)

    val songs = getMigratedRoomDatabase().songDao().fetchAllSync()
    assertThat(songs.size)
      .isEqualTo(1)
    assertThat(songs.first().time)
      .isEqualTo(123456789)
    assertThat(songs.first().rawString)
      .isEqualTo("This is a title")
//    assertThat(songs.first().albumCover)
//      .isEqualTo("")

    val albumCovers = getMigratedRoomDatabase().albumCoverDao().fetchAllSync()
    assertThat(albumCovers.size)
      .isEqualTo(0)
  }

  @Test
  fun testMigration3To4() {
    testHelper.createDatabase(testDbName, 3).use {
      it.insert("song", SQLiteDatabase.CONFLICT_REPLACE, ContentValues().apply {
        put("time", 123456789)
        put("raw_string", "This is a title")
        put("album_cover", "https://albumcover.png")
      })
    }
    testHelper.runMigrationsAndValidate(testDbName, 4, true, MIGRATION_3_4)

    val songs = getMigratedRoomDatabase().songDao().fetchAllSync()
    assertThat(songs.size)
      .isEqualTo(1)
    assertThat(songs.first().time)
      .isEqualTo(123456789)
    assertThat(songs.first().rawString)
      .isEqualTo("This is a title")
    assertThat(songs.first().fakeAlbumCover)
      .isEqualTo("https://albumcover.png")
    assertThat(songs.first().realAlbumCover)
      .isEqualTo("")
  }

  private fun getMigratedRoomDatabase(): HootDatabase {
    val database = Room.databaseBuilder(InstrumentationRegistry.getInstrumentation().targetContext,
      HootDatabase::class.java, testDbName)
      .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
      .build()
    // close the database and release any stream resources when the test finishes
    testHelper.closeWhenFinished(database)
    return database
  }
}