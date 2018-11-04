package ie.pennylabs.hoot.service

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import ie.pennylabs.hoot.BuildConfig
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NotificationService : NotificationListenerService(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.IO

  init {
    if (BuildConfig.DEBUG) {
      saveSong(System.currentTimeMillis(), "Californication by Red hot chilli peppers")
    }
  }

  override fun onListenerConnected() = activeNotifications.forEach { parseNotification(it) }
  override fun onNotificationPosted(statusBarNotification: StatusBarNotification?) = parseNotification(statusBarNotification)

  private fun parseNotification(statusBarNotifications: StatusBarNotification?) {
    if (statusBarNotifications == null) return
    if (statusBarNotifications.packageName != "com.google.intelligence.sense") return
    if (statusBarNotifications.notification.channelId != "com.google.intelligence.sense.ambientmusic.MusicNotificationChannel") return

    val time = statusBarNotifications.notification.`when`
    val rawString = statusBarNotifications.notification.extras?.getString(Notification.EXTRA_TITLE)
      ?: "UNKNOWN"

    saveSong(time, rawString)
  }

  private fun saveSong(time: Long, rawString: String) = launch {
    val albumCover = app.database.albumCoverDao().fetchUnconsumed()?.apply {
      hasBeenConsumed = true
      app.database.albumCoverDao().update(this)
    }

    val song = Song(time, rawString, albumCover?.link ?: "", "")
    app.database.songDao().insertUnique(song)

    AlbumCoverService.fetchRealAlbumCover(applicationContext, time)
  }
}