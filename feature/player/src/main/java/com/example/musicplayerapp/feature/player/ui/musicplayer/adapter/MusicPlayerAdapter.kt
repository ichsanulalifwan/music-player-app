package com.example.musicplayerapp.feature.player.ui.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayerapp.data.player.api.model.Song
import com.example.musicplayerapp.feature.player.databinding.ItemMusicListBinding
import com.example.musicplayerapp.core.ui.R as RCore

class MusicPlayerAdapter : RecyclerView.Adapter<MusicPlayerAdapter.ViewHolder>() {

    private lateinit var onItemClickListener: OnItemClickListener
    private var items = ArrayList<Song>()
    private var originalItems = ArrayList<Song>()

    fun setData(items: List<Song>?) {
        if (items == null) return
        this.items.clear()
        this.originalItems.clear()
        this.items.addAll(items)
        this.originalItems.addAll(items)
        notifyDataSetChanged()
    }

    fun filterByArtist(query: String?) {
        if (query.isNullOrEmpty()) {
            // If query is empty, restore the original list
            items.clear()
            items.addAll(originalItems)
        } else {
            // Filter items by title containing the query (case-insensitive)
            items.clear()
            items.addAll(originalItems.filter { it.artist.name.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemMusicListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Song) {
            binding.apply {
                tvSongTitle.text = item.title
                tvSongArtist.text = item.artist.name
                tvSongAlbum.text = item.album.title

                Glide.with(this.root)
                    .load(item.album.cover)
                    .centerCrop()
                    .placeholder(RCore.drawable.ill_error_image)
                    .error(RCore.drawable.ill_error_image)
                    .into(imgSongCover)
            }

            itemView.setOnClickListener {
                onItemClickListener.onItemClicked(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(item: Song)
    }
}
