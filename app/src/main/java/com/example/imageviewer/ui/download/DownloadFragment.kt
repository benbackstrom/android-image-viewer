package com.example.imageviewer.ui.download

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.example.imageviewer.databinding.DownloadFragmentBinding
import com.example.imageviewer.model.Image
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.MalformedURLException
import java.net.URI

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private val viewModel by viewModels<DownloadViewModel>()
    private lateinit var binding: DownloadFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DownloadFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backIcon.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.downloadLiveData.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                binding.progress.visibility = View.GONE
                Toast.makeText(context, "Image Downloaded!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }

        binding.downloadButton.setOnClickListener {
            binding.progress.visibility = View.VISIBLE

            viewLifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.Main) {
                verifyAndDownloadImage()
            }
        }
    }

    private suspend fun verifyAndDownloadImage() {
        val title = binding.titleEditText.text
        if (title == null || title.isEmpty()) {
            showBadDataDialog("You must provide a title to download this image.")
            return
        }

        val fileName = binding.fileNameEditText.text
        if (fileName == null || fileName.isEmpty()) {
            showBadDataDialog("You must provide a file name for the downloaded image.")
            return
        }

        val fileAlreadyExists = withContext(Dispatchers.IO) {
            viewModel.fileAlreadyExists(fileName.toString())
        }

        if (fileAlreadyExists) {
            showBadDataDialog("A file with this name already exists.")
            return
        }

        val compressFormat = when {
            fileName.endsWith(".jpg", ignoreCase = true) ||
            fileName.endsWith(".jpeg", ignoreCase = true) -> Bitmap.CompressFormat.JPEG
            fileName.endsWith(".png", ignoreCase = true) -> Bitmap.CompressFormat.PNG
            fileName.endsWith(".webp", ignoreCase = true) -> Bitmap.CompressFormat.WEBP
            else -> {
                showBadDataDialog("You must provide a valid file name for the downloaded image. The file name must end in JPG/JPEG, PNG or WEBP.")
                return
            }
        }

        val description = binding.descriptionEditText.text
        if (description == null || description.isEmpty()) {
            showBadDataDialog("You must provide a description to download this image.")
            return
        }

        val url = binding.urlEditText.text
        if (url == null || url.isEmpty()) {
            showBadDataDialog("You must provide a valid URL to download this image.")
            return
        }

        try {
            URI(url.toString())
        } catch (e: MalformedURLException) {
            showBadDataDialog("You must provide a valid URL to download this image.")
            return
        }

        val image = Image(fileName.toString(), title.toString(), description.toString(), System.currentTimeMillis(), compressFormat)

        viewModel.download(image, url.toString())
    }

    private fun showBadDataDialog(message: String) {
        val context = context ?: return

        binding.progress.visibility = View.GONE

        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }
}