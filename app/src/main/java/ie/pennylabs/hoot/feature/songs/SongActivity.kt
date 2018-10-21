package ie.pennylabs.hoot.feature.songs

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.extension.hasNotificationAccess
import ie.pennylabs.hoot.feature.gdpr.GdprBottomSheet
import ie.pennylabs.hoot.service.AlbumCoverService
import kotlinx.android.synthetic.main.activity_songs.*

class SongActivity : AppCompatActivity() {
  private var notificationAccessDialog: AlertDialog? = null
  private var gdprBottomSheet: GdprBottomSheet? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_songs)

    if (!hasNotificationAccess()) {
      notificationAccessDialog = AlertDialog.Builder(this)
        .setTitle("Enable notification access")
        .setPositiveButton("Okay") { _, _ -> startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        .show()
    }

    val adapter = SongsAdapter { playFromSearch(it) }
    rvSongs.adapter = adapter
    rvSongs.layoutManager = LinearLayoutManager(this)

    app.database.songDao().fetchAll().observe(this, Observer {
      if (it != null) adapter.submitList(it)
    })

    AlbumCoverService.fetch(this)
  }

  override fun onResume() {
    super.onResume()
    gdprBottomSheet = GdprBottomSheet.show(this)
  }

  override fun onPause() {
    super.onPause()
    notificationAccessDialog?.dismiss()
    gdprBottomSheet?.dismiss()
  }

  private fun playFromSearch(song: Song) {
    val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
    intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, song.artist)
    intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, song.title)
    intent.putExtra(SearchManager.QUERY, song.sanitisedString)
    startActivity(intent)
  }
}