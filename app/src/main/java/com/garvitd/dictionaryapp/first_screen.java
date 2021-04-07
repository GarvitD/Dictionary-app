package com.garvitd.dictionaryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;

public class first_screen extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private HistoryAdapter mAdapter;
    RecyclerView recyclerView;
    ImageView signoutBtn;
    SearchView search_view;
    /*static DatabaseHelper myDbHelper;
    static boolean databaseOpened=false;
    SimpleCursorAdapter suggestionAdapter;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        HistoryDBHelper dbHelper = new HistoryDBHelper(this);
        mDatabase= dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HistoryAdapter(this,getAllitems());
        recyclerView.setAdapter(mAdapter);

        androidx.appcompat.widget.Toolbar toolbar=findViewById(R.id.tool1);
        setSupportActionBar(toolbar);
        signoutBtn=(ImageView) findViewById(R.id.signoutBtn);
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), login_activity.class);
                startActivity(intent);
            }
        });

        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search_view.setIconified(false); //this is used to make possible the thing that the whole search bar is clickable and not only the search icon//

            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String entered_text= "" + search_view.getQuery();
                additem();

                Intent intent= new Intent(first_screen.this,word_meaning.class);
                intent.putExtra(Intent.EXTRA_TEXT,entered_text);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    private void additem() {
        if(search_view.getQuery().toString().trim().length()==0){
            return;
        }

        String name = search_view.getQuery().toString();
        ContentValues cv = new ContentValues();
        cv.put(HistoryContract.HistoryEntry.COLUMN_NAME,name);

        mDatabase.insert(HistoryContract.HistoryEntry.TABLE_NAME,null,cv);
        mAdapter.swapCursor(getAllitems());
    }

    private Cursor getAllitems(){
        return mDatabase.query(
                HistoryContract.HistoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HistoryContract.HistoryEntry.COLUMN_TIMESTAMP + " DESC"

        );
    }


}