package com.garvitd.dictionaryapp;

import android.provider.BaseColumns;

public class HistoryContract {

    private HistoryContract(){}

    public static final class HistoryEntry implements BaseColumns{
        public static final String TABLE_NAME="historyList";
        public static final String COLUMN_NAME="name";
        public static final String COLUMN_TIMESTAMP="timestamp";
    }
}
