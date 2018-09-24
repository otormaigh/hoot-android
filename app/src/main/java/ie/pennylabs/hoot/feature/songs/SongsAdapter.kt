package ie.pennylabs.hoot.feature.songs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ie.pennylabs.hoot.R
import ie.pennylabs.hoot.data.model.Song
import ie.pennylabs.hoot.extension.copyToClipboard
import ie.pennylabs.hoot.extension.toIso8601
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_song.*

class SongsAdapter(private val onSongSelected: OnSongSelected) : ListAdapter<Song, SongsAdapter.ViewHolder>(diffCallback) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_song, parent, false))

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    fun bind(song: Song) {
      containerView.setOnClickListener { onSongSelected.invoke(song) }
      containerView.setOnLongClickListener {
        containerView.context.copyToClipboard(song.sanitisedString)
        true
      }

      tvTitle.text = song.rawString
      tvTimestamp.text = song.time.toIso8601()
    }
  }
}

private val diffCallback = object : DiffUtil.ItemCallback<Song>() {
  override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem.time == newItem.time
  override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem === newItem
}
typealias OnSongSelected = (Song) -> Unit