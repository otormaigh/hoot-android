package ie.pennylabs.hoot

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.room.Room
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import ie.pennylabs.hoot.data.room.HootDatabase
import ie.pennylabs.hoot.data.room.MIGRATION_1_2
import ie.pennylabs.hoot.data.room.MIGRATION_2_3
import ie.pennylabs.hoot.data.room.MIGRATION_3_4
import ie.pennylabs.hoot.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class HootApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {
  @Inject
  lateinit var activityInjector: DispatchingAndroidInjector<Activity>
  @Inject
  lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

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

    DaggerAppComponent.builder()
      .application(this)
      .build()
      .inject(this)
  }

  override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector
  override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> = supportFragmentInjector
}

val Context.app: HootApplication
  get() = applicationContext as HootApplication