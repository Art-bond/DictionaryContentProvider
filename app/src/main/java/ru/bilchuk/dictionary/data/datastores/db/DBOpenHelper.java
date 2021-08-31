package ru.bilchuk.dictionary.data.datastores.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + DBMetaData.TranslationTableMetaData.TABLE_NAME
            + " ("
            + DBMetaData.TranslationTableMetaData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBMetaData.TranslationTableMetaData.COLUMN_KEYWORD + " TEXT, "
            + DBMetaData.TranslationTableMetaData.COLUMN_TRANSLATION + " TEXT,"
            + DBMetaData.TranslationTableMetaData.COLUMN_IMAGE + " TEXT"
            + ")";

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBMetaData.DB_NAME, null, DBMetaData.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
