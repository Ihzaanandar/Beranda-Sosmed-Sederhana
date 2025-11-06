package com.raja.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raja.post.R
import com.raja.post.databinding.ItemPostBinding
import com.raja.post.model.Post

class PostAdapter(
    private val items: MutableList<Post>,
    private val onEdit: (position: Int) -> Unit,
    private val onDelete: (position: Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = items[position]
        holder.binding.tvUsernamePost.text = post.username
        holder.binding.tvCaptionPost.text = post.caption

        // File: PostAdapter.kt
        // ... di dalam onBindViewHolder
        // Muat gambar profil dari properti baru
        Glide.with(holder.itemView.context)
            .load(post.profileImageRes) // <-- GUNAKAN properti baru di sini
            .into(holder.binding.imgProfilePost)

        // Muat gambar post utama (ini sudah benar)
        Glide.with(holder.itemView.context)
            .load(post.imageUri)
            .into(holder.binding.imgPost)
        // ...


        // menu tiga titik
        holder.binding.btnMenuPost.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.menuInflater.inflate(R.menu.menu_post_options, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_edit -> {
                        onEdit(holder.bindingAdapterPosition)
                        true
                    }
                    R.id.menu_delete -> {
                        onDelete(holder.bindingAdapterPosition)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount(): Int = items.size

    // helper untuk update & delete dari activity
    fun updateItem(position: Int, newPost: Post) {
        if (position in 0 until items.size) {
            items[position] = newPost
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun addItemAtTop(post: Post) {
        items.add(0, post)
        notifyItemInserted(0)
    }
}
