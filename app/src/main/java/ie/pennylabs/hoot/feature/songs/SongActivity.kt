package ie.pennylabs.hoot.feature.songs

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.app
import ie.pennylabs.hoot.extension.hasNotificationAccess
import kotlinx.android.synthetic.main.activity_songs.*

class SongActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_songs)

    if (!hasNotificationAccess()) {
      AlertDialog.Builder(this)
        .setTitle("Enable notification access")
        .setPositiveButton("Okay") { _, _ -> startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        .show()
    }

    val adapter = SongsAdapter()
    rvSongs.adapter = adapter
    rvSongs.layoutManager = LinearLayoutManager(this)

    app.database.songDao().fetchAll().observe(this, Observer {
      if (it != null) adapter.submitList(it)
    })
  }
}