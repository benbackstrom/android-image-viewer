package com.example.imageviewer.model.local

object ImagesDBContract {

    const val DATABASE_NAME = "images.sql"
    const val DATABASE_VERSION = 1

    const val TABLE_NAME = "images"
    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_FILE_NAME = "file_name"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_DESCRIPTION = "description"
    const val COLUMN_NAME_CREATED_STAMP = "created_stamp"

    const val CREATE_TABLE_STATEMENT = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (\n" +
            "  $COLUMN_NAME_FILE_NAME TEXT INTEGER PRIMARY KEY,\n" +
            "  $COLUMN_NAME_TITLE TEXT,\n" +
            "  $COLUMN_NAME_DESCRIPTION TEXT,\n" +
            "  $COLUMN_NAME_CREATED_STAMP INTEGER\n" +
            ")"

    const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS $TABLE_NAME"
}