package ie.pennylabs.hoot.extension

import android.content.Context
import android.provider.Settings

fun Context.hasNotificationAccess(): Boolean = Settings.Secure.getString(contentResolver, "enabled_notification_listeners").contains(packageName)
