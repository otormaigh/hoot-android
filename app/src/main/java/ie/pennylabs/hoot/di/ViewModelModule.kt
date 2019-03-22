package ie.pennylabs.hoot.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ie.pennylabs.hoot.arch.ViewModelFactory
import ie.pennylabs.hoot.data.model.SongDao
import ie.pennylabs.hoot.feature.settings.SettingsViewModel
import ie.pennylabs.hoot.feature.songs.SongsViewModel

@Module
object ViewModelModule {
  @JvmStatic
  @Provides
  fun provideViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory =
    viewModelFactory

  @JvmStatic
  @Provides
  @IntoMap
  @ViewModelKey(SettingsViewModel::class)
  fun provideSettingsViewModel(songDao: SongDao): ViewModel =
    SettingsViewModel(songDao)

  @JvmStatic
  @Provides
  @IntoMap
  @ViewModelKey(SongsViewModel::class)
  fun provideSongsViewModel(songDao: SongDao): ViewModel =
    SongsViewModel(songDao)
}