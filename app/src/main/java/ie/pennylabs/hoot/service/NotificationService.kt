package ie.pennylabs.hoot.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NotificationService : NotificationListenerService(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  override fun onListenerConnected() = activeNotifications.forEach { insert(it) }
  override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) = insert(statusBarNotification)

  private fun insert(statusBarNotifications: StatusBarNotification?) {
    if (statusBarNotifications == null) return
    if (statusBarNotifications.packageName != "com.google.intelligence.sense") return
    if (statusBarNotifications.notification.channelId != "com.google.intelligence.sense.ambientmusic.MusicNotificationChannel") return

    val time = statusBarNotifications.notification.`when`
    val title = statusBarNotifications.notification.extras?.getString(Notification.EXTRA_TITLE)
      ?: "UNKNOWN"

    launch {
      val albumCover = app.database.albumCoverDao().fetchUnconsumed()?.apply {
        hasBeenConsumed = true
        app.database.albumCoverDao().update(this)
      }

      val song = Song(time, title, albumCover?.link ?: "", "")
      app.database.songDao().insertUnique(song)

      AlbumCoverService.fetchRealAlbumCover(applicationContext, time)
    }
  }
}