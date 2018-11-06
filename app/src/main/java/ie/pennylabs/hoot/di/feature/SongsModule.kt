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
import ie.pennylabs.hoot.feature.songs.SongsActivity
import ie.pennylabs.hoot.feature.songs.SongsViewModel

@Module(includes = [
  SongsModule.ProvideViewModel::class
])
interface SongsModule {
  @ContributesAndroidInjector(modules = [
    InjectViewModel::class
  ])
  fun bind(): SongsActivity

  @Module
  object ProvideViewModel {
    @JvmStatic
    @Provides
    @IntoMap
    @ViewModelKey(SongsViewModel::class)
    fun provideSongsViewModel(songDao: SongDao): ViewModel =
      SongsViewModel(songDao)
  }

  @Module
  object InjectViewModel {
    @JvmStatic
    @Provides
    fun provideSongsViewModel(factory: ViewModelProvider.Factory, target: SongsActivity): SongsViewModel =
      ViewModelProviders.of(target, factory).get(SongsViewModel::class.java)
  }
}