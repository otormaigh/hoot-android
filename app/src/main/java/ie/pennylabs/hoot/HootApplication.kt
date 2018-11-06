package ie.pennylabs.hoot

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import ie.pennylabs.hoot.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class HootApplication : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {
  @Inject
  lateinit var activityInjector: DispatchingAndroidInjector<Activity>
  @Inject
  lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
  @Inject
  lateinit var serviceInjector: DispatchingAndroidInjector<Service>

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
  override fun serviceInjector(): DispatchingAndroidInjector<Service> = serviceInjector
}