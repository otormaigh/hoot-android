package ie.pennylabs.hoot.di.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.di.ViewModelKey
import ie.pennylabs.hoot.feature.settings.SettingsActivity
import ie.pennylabs.hoot.feature.settings.SettingsFragment
import ie.pennylabs.hoot.feature.settings.SettingsViewModel

@Module(includes = [
  SettingsModule.ProvideViewModel::class
])
interface SettingsModule {
  @ContributesAndroidInjector(modules = [
    InjectViewModel::class
  ])
  fun bind(): SettingsFragment

  @Module
  object ProvideViewModel {
    @JvmStatic
    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsViewModel(songDao: SongDao): ViewModel =
      SettingsViewModel(songDao)
  }

  @Module
  object InjectViewModel {
    @JvmStatic
    @Provides
    fun provideSettingsViewModel(factory: ViewModelProvider.Factory, target: SettingsFragment): SettingsViewModel =
      ViewModelProviders.of(target, factory).get(SettingsViewModel::class.java)
  }
}