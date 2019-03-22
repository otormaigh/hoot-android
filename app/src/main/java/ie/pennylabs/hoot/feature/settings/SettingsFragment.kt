package ie.pennylabs.hoot.feature.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.toolbox.extensions.viewModelProvider

class SettingsFragment : PreferenceFragmentCompat() {
  private val viewModel by viewModelProvider { SettingsViewModel(requireActivity().app.database.songDao()) }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)

    findPreference<Preference>(getString(R.string.btnRefreshCovers))?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      viewModel.refreshMissingCovers(requireContext()).observe(this, Observer { showProgress ->
        if (showProgress != null) activity?.findViewById<View>(R.id.progress)?.isVisible = showProgress
      })
      true
    }
  }
}
