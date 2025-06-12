package com.connect.eduhubconnect.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.viewmodel.UserViewModel
import com.connect.eduhubconnect.utils.ResultState
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment : Fragment() {

    private var imageUri: Uri? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>

    private val userViewModel: UserViewModel by viewModels()

    private var selectedImageView: ImageView? = null

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it
                displaySelectedImage(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        val selectImageButton = view.findViewById<Button>(R.id.btnSelect)
        val postButton = view.findViewById<Button>(R.id.btnPost)
        val contentEditText = view.findViewById<EditText>(R.id.contentText)
        selectedImageView = view.findViewById(R.id.selectedImageView)

        selectImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        postButton.setOnClickListener {
            val content = contentEditText.text.toString().trim()
            val userId = tokenManager.getUserId()

            if (imageUri != null && content.isNotEmpty()) {
                if (userId != null) {
                    uploadPost(userId, content)
                }
            } else {
                Toast.makeText(requireContext(), "Please select an image and write content.", Toast.LENGTH_SHORT).show()
            }
        }

        observeUploadStatus()
        return view
    }

    private fun uploadPost(userId: Long, content: String) {
        val contentBody = createTextPart(content)
        val imagePart = imageUri?.let { createImagePart(it) }

        if (imagePart != null) {
            userViewModel.uploadPost(userId, contentBody, imagePart)
        }
    }

    private fun observeUploadStatus() {
        lifecycleScope.launchWhenStarted {
            userViewModel.uploadStatus.collect { state ->
                when (state) {
                    is ResultState.Loading -> {
                        // Show loading
                    }
                    is ResultState.Success -> {
                        Toast.makeText(requireContext(), state.data, Toast.LENGTH_SHORT).show()
                        resetForm()
                    }
                    is ResultState.Error -> {
                        Toast.makeText(requireContext(), "Upload failed: ${state.exception}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun resetForm() {
        imageUri = null
        selectedImageView?.setImageDrawable(null)
        view?.findViewById<EditText>(R.id.contentText)?.setText("")
    }

    private fun displaySelectedImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(selectedImageView!!)
    }

//    private fun createFileFromUri(context: Context, uri: Uri): File {
//        val fileName = getFileNameFromUri(context, uri)
//        val tempFile = File.createTempFile(fileName, null, context.cacheDir)
//        tempFile.outputStream().use { outputStream ->
//            context.contentResolver.openInputStream(uri)?.copyTo(outputStream)
//        }
//        return tempFile
//    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var name = "temp_image"
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        returnCursor?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }


    private fun createTextPart(content: String): RequestBody {
        return content.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun createImagePart(uri: Uri): MultipartBody.Part {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "upload_image.jpg")
        file.outputStream().use { inputStream?.copyTo(it) }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}
