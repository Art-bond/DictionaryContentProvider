package ru.bilchuk.dictionary.data.datastores.db;

import android.provider.BaseColumns;

public final class DBMetaData {

    public static final String DB_NAME = "dictionary.db";

    public static final int DB_VERSION = 1;

    public static class TranslationTableMetaData implements BaseColumns {

        public static final String TABLE_NAME = "TRANSLATIONS";

        public static final String COLUMN_KEYWORD = "keyword";

        public static final String COLUMN_TRANSLATION = "translation";

        public static final String COLUMN_IMAGE = "image";
    }
}
