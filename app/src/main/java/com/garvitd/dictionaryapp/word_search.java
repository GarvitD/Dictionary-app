package com.garvitd.dictionaryapp;

import android.database.SQLException;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

public class word_search extends AppCompatActivity {

    SearchView searchView;
    static DatabaseHelper myDbHelper;
    static boolean DatabaseOpened=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);

            }
        });

        myDbHelper = new DatabaseHelper(this);
        if (myDbHelper.checkDatabase()) {
            openDatabase();
        } else {

            LoadDatabaseAsync loadDatabaseAsync = new LoadDatabaseAsync(word_search.this);
            loadDatabaseAsync.execute();

        }


    }

    public static void openDatabase() {
        try {
            myDbHelper.openDatabase();
            DatabaseOpened = true;

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}