package ie.pennylabs.hoot

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.crashlytics.android.core.CrashlyticsCore
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import ie.pennylabs.hoot.data.room.HootDatabase
import ie.pennylabs.hoot.toolbox.enableAnalytics
import ie.pennylabs.hoot.toolbox.enableCrashReporting
import ie.pennylabs.hoot.toolbox.enablePerformance
import ie.pennylabs.hoot.toolbox.prefs
import io.fabric.sdk.android.Fabric
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

    FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(prefs.enableAnalytics)
    FirebasePerformance.getInstance().isPerformanceCollectionEnabled = prefs.enablePerformance
    Fabric.with(this, CrashlyticsCore.Builder()
      .disabled(!prefs.enableCrashReporting)
      .build())
  }
}

val Context.app: HootApplication
  get() = applicationContext as HootApplication