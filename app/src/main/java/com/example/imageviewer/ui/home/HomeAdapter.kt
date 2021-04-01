package com.example.imageviewer.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.DirectoryProvider
import com.example.imageviewer.R
import com.example.imageviewer.databinding.HomeListItemBinding
import com.example.imageviewer.model.Image
import com.squareup.picasso.Picasso
import java.io.File
import javax.inject.Inject

class HomeAdapter @Inject constructor(): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    @Inject
    lateinit var directoryProvider: DirectoryProvider
    var images: List<Image> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < images.size) {
            val image = images[position]
            holder.applyData(image)
        }
    }

    override fun getItemCount() = images.size

    class ViewHolder(private val binding: HomeListItemBinding): RecyclerView.ViewHolder(binding.root) {

        @Inject
        lateinit var directoryProvider: DirectoryProvider

        fun applyData(image: Image) {
            loadImage(image, binding.imageView)
            binding.titleView.text = image.title
            binding.fileNameView.text = image.fileName
        }

        private fun loadImage(image: Image, imageView: ImageView) {
            val rootPath = directoryProvider.directory?.path ?: return
            val localImageUri = rootPath + File.separator + image.fileName

            Picasso.get()
                .load(localImageUri)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(imageView)
        }
    }
}
