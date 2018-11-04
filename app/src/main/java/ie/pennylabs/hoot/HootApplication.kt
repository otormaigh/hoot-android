package ie.pennylabs.hoot

import android.app.Application
import android.content.Context
import androidx.room.Room
import ie.pennylabs.hoot.data.room.HootDatabase
import ie.pennylabs.hoot.data.room.MIGRATION_1_2
import ie.pennylabs.hoot.data.room.MIGRATION_2_3
import ie.pennylabs.hoot.data.room.MIGRATION_3_4
import timber.log.Timber

class HootApplication : Application() {
  val database by lazy {
    Room.databaseBuilder(applicationContext, HootDatabase::class.java, "hoot.db")
      .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
      .build()
  }

  override fun onCreate() {
    super.onCreate()

    @Suppress("ConstantConditionIf")
    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String =
          "(${element.fileName}:${element.lineNumber})"
      })
    }
  }
}

val Context.app: HootApplication
  get() = applicationContext as HootApplication