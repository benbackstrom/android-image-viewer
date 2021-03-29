package com.example.imageviewer.model.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.annotation.WorkerThread
import com.example.imageviewer.model.Image
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImagesDataSource @Inject constructor() {

    @Inject
    lateinit var dbHelper: ImagesDBHelper

    @WorkerThread
    fun insert(image: Image): Boolean {
        val db = dbHelper.writableDatabase ?: return false

        val values = ContentValues().apply {
            put(ImagesDBContract.COLUMN_NAME_FILE_NAME, image.fileName)
            put(ImagesDBContract.COLUMN_NAME_TITLE, image.title)
            put(ImagesDBContract.COLUMN_NAME_DESCRIPTION, image.description)
            put(ImagesDBContract.COLUMN_NAME_CREATED_STAMP, image.createdTimestamp)
        }

        // -1 means the query hit an error
        return db.insert(ImagesDBContract.TABLE_NAME, null, values) != -1L
    }

    @WorkerThread
    fun deleteWithFileName(fileName: String): Int {
        val db = dbHelper.writableDatabase ?: return 0
        val selection = "${ImagesDBContract.COLUMN_NAME_FILE_NAME} LIKE ?"
        val selectionArgs = arrayOf(fileName)

        return db.delete(ImagesDBContract.TABLE_NAME, selection, selectionArgs)
    }

    @WorkerThread
    fun updateWithFileName(image: Image): Int {
        val db = dbHelper.writableDatabase ?: return 0

        val values = ContentValues().apply {
            put(ImagesDBContract.COLUMN_NAME_TITLE, image.title)
            put(ImagesDBContract.COLUMN_NAME_FILE_NAME, image.fileName)
            put(ImagesDBContract.COLUMN_NAME_DESCRIPTION, image.description)
            put(ImagesDBContract.COLUMN_NAME_CREATED_STAMP, image.createdTimestamp)
        }

        val selection = "${ImagesDBContract.COLUMN_NAME_FILE_NAME} LIKE ?"
        val selectionArgs = arrayOf(image.fileName)

        return db.update(ImagesDBContract.TABLE_NAME, values, selection, selectionArgs)
    }

    @WorkerThread
    fun selectAll(): List<Image> {
        val results = mutableListOf<Image>()

        val db: SQLiteDatabase? = dbHelper.readableDatabase

        val sortOrder = "${ImagesDBContract.COLUMN_NAME_FILE_NAME} ASC"

        val cursor = db?.query(
            ImagesDBContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            sortOrder
        ) ?: return results

        with (cursor) {
            while (cursor.moveToNext()) {
                val image = Image(
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_FILE_NAME)),
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_TITLE)),
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_DESCRIPTION)),
                    cursor.getLong(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_CREATED_STAMP)))

                results.add(image)
            }
        }

        cursor.close()

        return results
    }

    @WorkerThread
    fun selectImageWithFileName(fileName: String): List<Image> {
        val results = mutableListOf<Image>()

        val db = dbHelper.readableDatabase ?: return results

        val selection = "${ImagesDBContract.COLUMN_NAME_FILE_NAME} = ?"
        val selectionArgs = arrayOf(fileName)

        val sortOrder = "${ImagesDBContract.COLUMN_NAME_TITLE} ASC"

        val cursor = db.query(
            ImagesDBContract.TABLE_NAME,
            null,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        ) ?: return results

        with (cursor) {
            while (cursor.moveToNext()) {
                val image = Image(
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_FILE_NAME)),
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_TITLE)),
                    cursor.getString(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_DESCRIPTION)),
                    cursor.getLong(getColumnIndexOrThrow(ImagesDBContract.COLUMN_NAME_CREATED_STAMP)))

                results.add(image)
            }
        }

        cursor.close()

        return results
    }
}