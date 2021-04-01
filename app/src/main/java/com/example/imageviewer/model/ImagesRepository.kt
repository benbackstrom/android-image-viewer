package com.example.imageviewer.model

import android.content.ContentProvider
import android.content.Context
import androidx.annotation.WorkerThread
import androidx.constraintlayout.solver.widgets.analyzer.Direct
import com.example.imageviewer.DirectoryProvider
import com.example.imageviewer.model.local.ImagesDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesRepository @Inject constructor() {

    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var directoryProvider: DirectoryProvider
    @Inject
    lateinit var localDataSource: ImagesDataSource

    @WorkerThread
    fun reloadLocalList(): List<Image> {
        // 1. Query list of all records into memory, then remove them all from db.
        val storedImagesMap = HashMap<String, Image>().apply {
            val storedImages = localDataSource.selectAll()
            for (image in storedImages)
                this[image.fileName] = image
        }

        // 2. Find all jpg/jpeg and png files.
        val imageFiles = mutableListOf<File>().apply {
            val foundFiles = directoryProvider.directory?.listFiles() ?: emptyArray()
            for (file in foundFiles) {
                if (file.isFile
                    && (file.name.endsWith(".jpeg", ignoreCase = true)
                        || file.name.endsWith(".jpg", ignoreCase = true)
                        || file.name.endsWith(".png", ignoreCase = true)
                        || file.name.endsWith(".webp", ignoreCase = true)))
                            add(file)
            }
        }

        // 3. For each file name, check if we have a record associated with it.
        //    If so, grab that data and show with the image.
        //    Else, insert with dummy data.
        for (file in imageFiles) {
            val matchingImages = localDataSource.selectImageWithFileName(file.name)
            if (matchingImages.isNotEmpty()) {
                storedImagesMap.remove(file.name)
            } else {
                val image = Image(file.name, "No Title", "No description", System.currentTimeMillis())
                localDataSource.insert(image)
            }
        }

        // 4. Remove all entries without a file from the database.
        for (imageFileName in storedImagesMap.keys) {
            localDataSource.deleteWithFileName(imageFileName)
        }

        // 5. Re-query and return the results.
        return localDataSource.selectAll()
    }
}