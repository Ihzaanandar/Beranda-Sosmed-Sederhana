package com.raja.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raja.post.databinding.BottomsheetAddPostBinding
import com.raja.post.model.Post

class EditPostBottomSheet(
    private val post: Post,
    private val onPostUpdated: (Post) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetAddPostBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View? {
        _binding = BottomsheetAddPostBinding.inflate(inflater, container, false)

        // isi field dengan data lama
        binding.etUsername.setText(post.username)
        binding.etCaption.setText(post.caption)
        post.imageUri?.let {
            try {
                binding.imgPreview.setImageURI(it)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar Baru"), 101)
        }

        binding.btnSubmitPost.text = "Simpan Perubahan"
        binding.btnSubmitPost.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val caption = binding.etCaption.text.toString().trim()

            if (username.isEmpty() || caption.isEmpty()) {
                Toast.makeText(requireContext(), "Lengkapi semua data", Toast.LENGTH_SHORT).show()
            } else {
                val updatedPost = post.copy(
                    username = username,
                    caption = caption,
                    imageUri = selectedImageUri ?: post.imageUri
                )
                onPostUpdated(updatedPost)
                dismiss()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgPreview.setImageURI(selectedImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
