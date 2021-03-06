package ie.pennylabs.hoot

import android.app.Application
import android.content.Context
import androidx.room.Room
import ie.pennylabs.hoot.data.room.HootDatabase
import timber.log.Timber

class HootApplication : Application() {
  val database by lazy {
    Room.databaseBuilder(applicationContext, HootDatabase::class.java, "hoot.db")
      .fallbackToDestructiveMigration()
      .build()
  }

  override fun onCreate() {
    super.onCreate()

    @Suppress("ConstantConditionIf")
    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
          return "(${element.fileName}:${element.lineNumber})"
        }
      })
    }
  }
}

val Context.app: HootApplication
  get() = applicationContext as HootApplication