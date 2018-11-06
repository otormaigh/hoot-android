package ie.pennylabs.hoot.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ie.pennylabs.hoot.service.AlbumCoverService
import ie.pennylabs.hoot.service.NotificationService

@Module
interface ServiceModule {
  @ContributesAndroidInjector
  fun bindNotificationService(): NotificationService

  @ContributesAndroidInjector
  fun bindAlbumCoverService(): AlbumCoverService
}