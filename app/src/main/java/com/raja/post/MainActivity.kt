package com.raja.post

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.raja.post.adapter.PostAdapter
import com.raja.post.adapter.StoryAdapter
import com.raja.post.databinding.ActivityMainBinding
import com.raja.post.model.Post
import com.raja.post.model.Story

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val stories = mutableListOf<Story>()
    private val posts = mutableListOf<Post>()
    private val profileImages = listOf(
        R.drawable.my_profile,
        R.drawable.profile1,
        R.drawable.profile2,
        R.drawable.profile3
    )


    private lateinit var storyAdapter: StoryAdapter
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapters()
        loadDummyData()

        binding.btnAddPost.setOnClickListener {
            val bottomSheet = AddPostBottomSheet { username, caption, imageUri ->
                val randomProfile = profileImages.random()
                val newPost = Post(username, caption, imageUri, randomProfile)
                postAdapter.addItemAtTop(newPost)
            }
            bottomSheet.show(supportFragmentManager, "AddPostBottomSheet")
        }


    }

    private fun setupAdapters() {
        // Story adapter horizontal
        storyAdapter = StoryAdapter(stories)
        binding.storyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
        }

        // Post adapter vertical with edit & delete callbacks
        postAdapter = PostAdapter(posts,
            onEdit = { pos ->
                val current = posts.getOrNull(pos) ?: return@PostAdapter
                // buka bottom sheet prefilled
                val sheet = EditPostBottomSheet(current) { updatedPost ->
                    posts[pos] = updatedPost
                    postAdapter.updateItem(pos, updatedPost)
                }
                sheet.show(supportFragmentManager, "EditPost")
            },
            onDelete = { pos ->
                // dialog konfirmasi
                AlertDialog.Builder(this)
                    .setTitle("Hapus Post")
                    .setMessage("Yakin ingin menghapus postingan ini?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus") { _, _ ->
                        postAdapter.removeItem(pos)
                    }
                    .show()
            }
        )

        binding.postRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = postAdapter
        }
    }

    private fun loadDummyData() {
        // Pastikan file drawable ini ada: story1..story4, post1..post2, my_profile (untuk profile)
        stories.clear()
        stories.addAll(
            listOf(
                Story("Anis", R.drawable.story1),
                Story("Ganjar", R.drawable.story2),
                Story("Roy", R.drawable.story3),
                Story("Bahlil", R.drawable.story4),
                Story("Gufron", R.drawable.story5),
                Story("Elham", R.drawable.story6)
            )
        )
        storyAdapter.notifyDataSetChanged()
        fun drawableToUri(drawableId: Int): Uri {
            return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${packageName}/${drawableId}")
        }
        posts.clear()
        posts.addAll(
            listOf(
                Post("intan_dwi", "Liburan ke pantai! 🏖️", drawableToUri(R.drawable.post1), profileImages.random()),
                Post("minda_04", "Hari yang menyenangkan! ☀️", drawableToUri(R.drawable.post2), profileImages.random())
            )
        )
        postAdapter.notifyDataSetChanged()
    }

}
