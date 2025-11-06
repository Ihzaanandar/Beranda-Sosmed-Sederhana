package com.raja.post

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raja.post.databinding.BottomsheetAddPostBinding

class AddPostBottomSheet(
    private val onPostAdded: (String, String, Uri?) -> Unit
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

        binding.btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), 100)
        }

        binding.btnSubmitPost.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val caption = binding.etCaption.text.toString().trim()

            when {
                username.isEmpty() -> {
                    Toast.makeText(requireContext(), "Username harus diisi", Toast.LENGTH_SHORT).show()
                }
                caption.isEmpty() -> {
                    Toast.makeText(requireContext(), "Deskripsi belum diisi", Toast.LENGTH_SHORT).show()
                }
                selectedImageUri == null -> {
                    Toast.makeText(requireContext(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    onPostAdded(username, caption, selectedImageUri)
                    dismiss()
                }
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgPreview.setImageURI(selectedImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
