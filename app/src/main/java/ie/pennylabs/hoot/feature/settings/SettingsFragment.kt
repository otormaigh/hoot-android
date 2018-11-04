package ie.pennylabs.hoot.feature.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.toolbox.extensions.viewModelProvider

class SettingsFragment : PreferenceFragmentCompat() {
  private val viewModel by viewModelProvider { SettingsViewModel(requireActivity().app.database.songDao()) }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)

    findPreference(getString(R.string.btnRefreshCovers)).onPreferenceClickListener = Preference.OnPreferenceClickListener {
      viewModel.refreshMissingCovers(requireContext())
      true
    }
  }
}
