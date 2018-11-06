package ie.pennylabs.hoot.feature.songs

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.android.AndroidInjection
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.extension.hasNotificationAccess
import ie.pennylabs.hoot.feature.gdpr.GdprBottomSheet
import ie.pennylabs.hoot.feature.settings.SettingsActivity
import ie.pennylabs.hoot.service.AlbumCoverService
import kotlinx.android.synthetic.main.activity_songs.*
import javax.inject.Inject

class SongsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
  @Inject
  lateinit var viewModel: SongsViewModel

  private var notificationAccessDialog: AlertDialog? = null
  private var gdprBottomSheet: GdprBottomSheet? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_songs)

    if (!hasNotificationAccess()) {
      notificationAccessDialog = AlertDialog.Builder(this)
        .setTitle("Enable notification access")
        .setPositiveButton("Okay") { _, _ -> startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)) }
        .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        .show()
    }

    val adapter = SongsAdapter(
      { playFromSearch(it) },
      { AlbumCoverService.fetchRealAlbumCover(this, it.time) })
    rvSongs.adapter = adapter
    rvSongs.layoutManager = GridLayoutManager(this, 2)

    viewModel.fetchSongs().observe(this, Observer {
      if (it != null) adapter.submitList(it)
    })

    AlbumCoverService.fetch(this)

    swipeRefresh.setOnRefreshListener(this)
  }

  override fun onResume() {
    super.onResume()
    onRefresh()
    gdprBottomSheet = GdprBottomSheet.show(this)
  }

  override fun onPause() {
    super.onPause()
    notificationAccessDialog?.dismiss()
    gdprBottomSheet?.dismiss()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menuSettings -> startActivity(Intent(this, SettingsActivity::class.java))
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onRefresh() {
    swipeRefresh.isRefreshing = false
    rvSongs.adapter?.notifyDataSetChanged()
  }

  private fun playFromSearch(song: Song) {
    val intent = Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH)
    intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, song.artist)
    intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE, song.title)
    intent.putExtra(SearchManager.QUERY, song.sanitisedString)
    startActivity(intent)
  }
}