package ie.pennylabs.hoot.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ie.pennylabs.hoot.feature.songs.SongsActivity

@Module
interface ActivityBuilder {
  @ContributesAndroidInjector
  fun bind(): SongsActivity
}