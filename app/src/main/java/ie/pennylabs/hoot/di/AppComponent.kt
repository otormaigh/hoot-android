package ie.pennylabs.hoot.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import ie.pennylabs.hoot.HootApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
  ActivityBuilder::class,
  AndroidInjectionModule::class,
  AndroidSupportInjectionModule::class,
  FragmentBuilder::class,
  ServiceBuilderModule::class,
  DataModule::class,
  ApiModule::class,
  ViewModelModule::class
])
interface AppComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: HootApplication): Builder

    fun build(): AppComponent
  }

  fun inject(application: HootApplication)
}