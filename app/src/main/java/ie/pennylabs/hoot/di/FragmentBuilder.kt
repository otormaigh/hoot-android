package ie.pennylabs.hoot.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ie.pennylabs.hoot.feature.settings.SettingsFragment

@Module
interface FragmentBuilder {
  @ContributesAndroidInjector
  fun bind(): SettingsFragment
}