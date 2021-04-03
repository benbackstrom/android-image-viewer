package com.example.imageviewer.model.download

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.http.HttpResponseCache
import androidx.annotation.WorkerThread
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

const val RESPONSE_CODE_SUCCESS = 200

class DownloadsDataSource @Inject constructor() {

    @WorkerThread
    fun requestBitmap(url: String): Bitmap? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        if (response.code != RESPONSE_CODE_SUCCESS)
            return null

        val byteStream = response.body?.byteStream() ?: return null

        return BitmapFactory.decodeStream(byteStream)
    }
}