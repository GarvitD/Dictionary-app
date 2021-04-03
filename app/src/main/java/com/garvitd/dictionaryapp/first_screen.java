package com.garvitd.dictionaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;

public class first_screen extends AppCompatActivity {

    ImageView signoutBtn;
    SearchView search_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

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
                Intent intent= new Intent(getApplicationContext(),word_search.class);
                startActivity(intent);

            }
        });


    }
}