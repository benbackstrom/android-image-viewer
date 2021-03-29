package com.example.imageviewer.model

data class Image(
    val fileName: String,
    val title: String,
    val description: String,
    val createdTimestamp: Long
)