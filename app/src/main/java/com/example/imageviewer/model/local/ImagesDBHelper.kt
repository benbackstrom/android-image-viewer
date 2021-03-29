package com.example.imageviewer.model.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dagger.hilt.android.qualifiers.ApplicationContext

class ImagesDBHelper(@ApplicationContext context: Context): SQLiteOpenHelper(
    context,
    ImagesDBContract.DATABASE_NAME,
    null,
    ImagesDBContract.DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(ImagesDBContract.CREATE_TABLE_STATEMENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Not expecting this in v1
    }
}