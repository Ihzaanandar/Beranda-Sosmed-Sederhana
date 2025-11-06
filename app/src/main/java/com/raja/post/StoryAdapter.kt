package com.raja.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raja.post.databinding.ItemStoryBinding
import com.raja.post.model.Story

class StoryAdapter(
    private val items: List<Story>
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = items[position]
        holder.binding.tvUsernameStory.text = story.username
        // Glide dipakai agar bisa memuat drawable atau URL
        Glide.with(holder.itemView.context)
            .load(story.imageRes)
            .into(holder.binding.imgProfileStory)
    }

    override fun getItemCount(): Int = items.size
}
