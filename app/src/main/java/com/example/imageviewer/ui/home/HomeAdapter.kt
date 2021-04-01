package com.example.imageviewer.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.DirectoryProvider
import com.example.imageviewer.R
import com.example.imageviewer.databinding.HomeListItemBinding
import com.example.imageviewer.model.Image
import com.squareup.picasso.Picasso
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Inject

class HomeAdapter @Inject constructor(): RecyclerView.Adapter<HomeViewHolder>() {

    var images: List<Image> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeListItemBinding.inflate(inflater, parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        if (position < images.size) {
            val image = images[position]
            holder.applyData(image)
        }
    }

    override fun getItemCount() = images.size
}

class HomeViewHolder(private val binding: HomeListItemBinding): RecyclerView.ViewHolder(binding.root) {

    private val directoryProvider: DirectoryProvider by lazy {
        EntryPointAccessors.fromApplication(binding.root.context, DirectoryProviderEntryPoint::class.java).provideDirectoryProvider()
    }

    fun applyData(image: Image) {
        loadImage(image, binding.imageView)
        binding.titleView.text = image.title
        binding.fileNameView.text = image.fileName
    }

    private fun loadImage(image: Image, imageView: ImageView) {
        val rootPath = directoryProvider.directory?.path ?: return
        val localImageUri = "file://" + rootPath + File.separator + image.fileName
        Log.i("localImageUri", localImageUri)

        Picasso.get()
            .load(localImageUri)
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(imageView)
    }
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DirectoryProviderEntryPoint {
    fun provideDirectoryProvider(): DirectoryProvider
}