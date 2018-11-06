package ie.pennylabs.hoot.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ie.pennylabs.hoot.arch.ViewModelFactory
import ie.pennylabs.hoot.di.feature.SettingsModule

@Module(includes = [
  SettingsModule::class
])
object FeatureModule {
  @JvmStatic
  @Provides
  fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory =
    viewModelFactory
}