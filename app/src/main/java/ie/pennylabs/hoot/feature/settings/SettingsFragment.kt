package ie.pennylabs.hoot.feature.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.arch.ViewModelFactory
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {
  @Inject
  lateinit var viewModelFactory: ViewModelFactory
  private val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java) }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    AndroidSupportInjection.inject(this)
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)

    findPreference(getString(R.string.btnRefreshCovers)).onPreferenceClickListener = Preference.OnPreferenceClickListener {
      viewModel.refreshMissingCovers(requireContext()).observe(this, Observer { showProgress ->
        if (showProgress != null) activity?.findViewById<View>(R.id.progress)?.isVisible = showProgress
      })
      true
    }
  }
}