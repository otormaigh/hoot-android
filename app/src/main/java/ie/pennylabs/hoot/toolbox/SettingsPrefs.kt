package ie.pennylabs.hoot.toolbox

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ie.pennylabs.hoot.BuildConfig

val Context.settingsPrefs: SharedPreferences
  get() = getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)

object CoverArtSource {
  const val KEY = "cover_art_source"

  object Values {
    const val FAKE = "fake"
    const val REAL = "real"
  }
}

var SharedPreferences.coverArtSource: String
  set(value) {
    edit { putString(CoverArtSource.KEY, value) }
  }
  get() = getString(CoverArtSource.KEY, CoverArtSource.Values.FAKE) ?: CoverArtSource.Values.FAKE