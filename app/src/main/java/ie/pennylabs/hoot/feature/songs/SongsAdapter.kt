package ie.pennylabs.hoot.feature.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.toolbox.CoverArtSource
import ie.pennylabs.hoot.toolbox.coverArtSource
import ie.pennylabs.hoot.toolbox.settingsPrefs
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_song.*

class SongsAdapter(private val onSongSelected: OnSongSelected,
                   private val onSongLongPressed: OnSongLongPressed) : ListAdapter<Song, SongsAdapter.ViewHolder>(diffCallback) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_song, parent, false))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(song: Song) {
      containerView.setOnClickListener { onSongSelected.invoke(song) }
      containerView.setOnLongClickListener {
        onSongLongPressed(song)
        true
      }

      tvTitle.text = song.title
      tvArtist.text = song.artist.trimStart()

      val imageUrl = if (containerView.context.settingsPrefs.coverArtSource == CoverArtSource.Values.FAKE) song.fakeAlbumCover
      else song.realAlbumCover

      Glide.with(containerView.context)
        .load(imageUrl)
        .apply(RequestOptions()
          .error(R.drawable.ic_launcher_foreground))
        .into(ivAlbumCover)
    }
  }
}

private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
  override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem.time == newItem.time
  override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem === newItem
}
typealias OnSongSelected = (Song) -> Unit
typealias OnSongLongPressed = (Song) -> Unit