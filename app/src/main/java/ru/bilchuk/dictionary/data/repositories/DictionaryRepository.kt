package ru.bilchuk.dictionary.data.repositories

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import io.reactivex.Completable
import io.reactivex.Single
import ru.bilchuk.dictionary.data.datastores.db.DBMetaData
import ru.bilchuk.dictionary.data.datastores.providers.DictionaryProviderMetaData
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository
import ru.bilchuk.dictionary.domain.repositories.models.DictionaryItemModel
import java.util.*

class DictionaryRepository(context: Context) : IDictionaryRepository {
    private val contentResolver: ContentResolver = context.contentResolver

    override fun add(dictionaryItemModel: DictionaryItemModel): Completable {
        return Completable.fromAction {
            val contentValues = ContentValues()
            contentValues.put(
                DBMetaData.TranslationTableMetaData.COLUMN_KEYWORD,
                dictionaryItemModel.keyword
            )
            contentValues.put(
                DBMetaData.TranslationTableMetaData.COLUMN_TRANSLATION,
                dictionaryItemModel.translation
            )
            contentValues.put(
                DBMetaData.TranslationTableMetaData.COLUMN_IMAGE,
                dictionaryItemModel.imageUri
            )
            contentResolver.insert(DictionaryProviderMetaData.TRANSLATES_CONTENT_URI, contentValues)
        }
    }

    override fun getWords(): Single<List<DictionaryItemModel>> {
        return Single.fromCallable {
            val items: MutableList<DictionaryItemModel> = ArrayList()
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(
                    DictionaryProviderMetaData.TRANSLATES_CONTENT_URI, null,
                    null, null, DBMetaData.TranslationTableMetaData._ID + " DESC"
                )
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        items.add(fromCursor(cursor))
                    }
                }
            } finally {
                cursor?.close()
            }
            items
        }
    }

    override fun delete(id: Long): Completable {
        return Completable.complete()
    }

    companion object {
        @SuppressLint("Range")
        private fun fromCursor(cursor: Cursor): DictionaryItemModel {
            val id = cursor.getLong(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData._ID))
            val keyword =
                cursor.getString(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData.COLUMN_KEYWORD))
            val translation =
                cursor.getString(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData.COLUMN_TRANSLATION))
            val imageUri =
                cursor.getString(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData.COLUMN_IMAGE))
            val model = DictionaryItemModel()
            model.id = id
            model.keyword = keyword
            model.translation = translation
            model.imageUri = imageUri
            return model
        }
    }

}