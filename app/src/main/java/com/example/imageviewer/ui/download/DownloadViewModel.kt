package com.example.imageviewer.ui.download

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.imageviewer.model.Image
import com.example.imageviewer.model.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(): ViewModel() {

    val downloadLiveData: MutableLiveData<Image?> by lazy {
        MutableLiveData<Image?>()
    }

    @Inject
    lateinit var imagesRepository: ImagesRepository

    suspend fun download(image: Image, downloadUrl: String) {
        val result = withContext(Dispatchers.IO) {
            imagesRepository.downloadAndSaveImage(image, downloadUrl)
        }

        downloadLiveData.postValue(if (result) image else null)
    }

    @WorkerThread
    fun fileAlreadyExists(fileName: String): Boolean {
        val existingImages = imagesRepository.reloadLocalList()
        for (existingImage in existingImages) {
            if (fileName == existingImage.fileName)
                return true
        }

        return false
    }
}