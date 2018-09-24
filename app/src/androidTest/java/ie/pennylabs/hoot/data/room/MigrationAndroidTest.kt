package ie.pennylabs.hoot.data.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
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

  private fun getMigratedRoomDatabase(): HootDatabase {
    val database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
      HootDatabase::class.java, testDbName)
      .addMigrations(MIGRATION_1_2)
      .build()
    // close the database and release any stream resources when the test finishes
    testHelper.closeWhenFinished(database)
    return database
  }
}