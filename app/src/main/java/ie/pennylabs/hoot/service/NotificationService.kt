package ie.pennylabs.hoot.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.Song
import kotlinx.coroutines.experimental.launch

class NotificationService : NotificationListenerService() {
  override fun onListenerConnected() = activeNotifications.forEach { insert(it) }
  override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) = insert(statusBarNotification)

  private fun insert(statusBarNotifications: StatusBarNotification?) {
    if (statusBarNotifications == null) return
    if (statusBarNotifications.packageName != "com.google.intelligence.sense") return
    if (statusBarNotifications.notification.channelId != "com.google.intelligence.sense.ambientmusic.MusicNotificationChannel") return

    val time = statusBarNotifications.notification.`when`
    val title = statusBarNotifications.notification.extras?.getString(Notification.EXTRA_TITLE)
      ?: "UNKNOWN"

    launch { app.database.songDao().insert(Song(time, title)) }
  }
}