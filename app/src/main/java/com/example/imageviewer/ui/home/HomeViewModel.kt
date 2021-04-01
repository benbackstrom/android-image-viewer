package com.example.imageviewer.ui.home

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
class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var imagesRepository: ImagesRepository

    val imageData: MutableLiveData<List<Image>> by lazy {
        MutableLiveData<List<Image>>()
    }

    init {
        rescan()
    }

    fun rescan() {
        GlobalScope.launch(Dispatchers.Main) {
            val images = withContext(Dispatchers.IO) {
                imagesRepository.reloadLocalList()
            }

            imageData.postValue(images)
        }
    }
}