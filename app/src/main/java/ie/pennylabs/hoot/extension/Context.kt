package ie.pennylabs.hoot.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.provider.Settings
import android.widget.Toast
import ie.pennylabs.hoot.R

fun Context.hasNotificationAccess(): Boolean = Settings.Secure.getString(contentResolver, "enabled_notification_listeners").contains(packageName)

fun Context.copyToClipboard(text: String) {
  (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    .primaryClip = ClipData.newPlainText(getString(R.string.app_name), text)

  Toast.makeText(this, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show()
}