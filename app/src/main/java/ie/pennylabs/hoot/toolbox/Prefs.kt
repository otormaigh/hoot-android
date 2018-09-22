package ie.pennylabs.hoot.toolbox

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ie.pennylabs.hoot.BuildConfig

val Context.prefs: SharedPreferences
  get() = getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

private const val HAS_ACCEPTED_GDPR = "has_accepted_gdpr"
var SharedPreferences.hasAcceptedGdpr: Boolean
  set(value) {
    edit { putBoolean(HAS_ACCEPTED_GDPR, value) }
  }
  get() = getBoolean(HAS_ACCEPTED_GDPR, false)

private const val ENABLE_PERFORMANCE = "enable_performance"
var SharedPreferences.enablePerformance: Boolean
  set(value) {
    edit { putBoolean(ENABLE_PERFORMANCE, value) }
  }
  get() = getBoolean(ENABLE_PERFORMANCE, false)

private const val ENABLE_CRASH_REPORTING = "enable_crash_reporting"
var SharedPreferences.enableCrashReporting: Boolean
  set(value) {
    edit { putBoolean(ENABLE_CRASH_REPORTING, value) }
  }
  get() = getBoolean(ENABLE_CRASH_REPORTING, false)

private const val ENABLE_ANALYTICS = "enable_analytics"
var SharedPreferences.enableAnalytics: Boolean
  set(value) {
    edit { putBoolean(ENABLE_ANALYTICS, value) }
  }
  get() = getBoolean(ENABLE_ANALYTICS, false)