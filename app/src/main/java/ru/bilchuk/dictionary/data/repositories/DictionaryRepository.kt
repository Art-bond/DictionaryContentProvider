package ru.bilchuk.dictionary.data.repositories;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.bilchuk.dictionary.data.datastores.db.DBMetaData;
import ru.bilchuk.dictionary.data.datastores.providers.DictionaryProviderMetaData;
import ru.bilchuk.dictionary.domain.repositories.models.DictionaryItemModel;
import ru.bilchuk.dictionary.domain.repositories.IDictionaryRepository;

public class DictionaryRepository implements IDictionaryRepository {

    private ContentResolver contentResolver;

    public DictionaryRepository(@NonNull Context context) {
        contentResolver = context.getContentResolver();
    }

    @Override
    public Completable add(@NonNull DictionaryItemModel dictionaryItemModel) {
        return Completable.fromAction(() -> {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBMetaData.TranslationTableMetaData.COLUMN_KEYWORD, dictionaryItemModel.getKeyword());
            contentValues.put(DBMetaData.TranslationTableMetaData.COLUMN_TRANSLATION, dictionaryItemModel.getTranslation());
            contentResolver.insert(DictionaryProviderMetaData.TRANSLATES_CONTENT_URI, contentValues);
        });
    }

    @Override
    public Single<List<DictionaryItemModel>> getWords() {
        return Single.fromCallable(() -> {
            List<DictionaryItemModel> items = new ArrayList<>();

            Cursor cursor = null;
            try {
                cursor = contentResolver.query(DictionaryProviderMetaData.TRANSLATES_CONTENT_URI, null,
                        null, null, DBMetaData.TranslationTableMetaData._ID + " DESC");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        items.add(fromCursor(cursor));
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return items;
        });
    }

    @Override
    public Completable delete(long id) {
        return Completable.complete();
    }

    private static DictionaryItemModel fromCursor(@NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData._ID));
        String keyword = cursor.getString(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData.COLUMN_KEYWORD));
        String translation = cursor.getString(cursor.getColumnIndex(DBMetaData.TranslationTableMetaData.COLUMN_TRANSLATION));

        DictionaryItemModel model = new DictionaryItemModel();
        model.setId(id);
        model.setKeyword(keyword);
        model.setTranslation(translation);

        return model;
    }
}
