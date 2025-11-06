// File: Post.kt
package com.raja.post.model

import android.net.Uri
import androidx.annotation.DrawableRes // <-- Tambahkan import ini

data class Post(
    val username: String,
    val caption: String,
    val imageUri: Uri?,
    @DrawableRes val profileImageRes: Int // <-- TAMBAHKAN BARIS INI
)
    