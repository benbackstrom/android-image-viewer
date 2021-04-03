package com.example.imageviewer.model

import android.graphics.Bitmap

data class Image(
    val fileName: String,
    val title: String,
    val description: String,
    val createdTimestamp: Long,
    val compressFormat: Bitmap.CompressFormat
)
