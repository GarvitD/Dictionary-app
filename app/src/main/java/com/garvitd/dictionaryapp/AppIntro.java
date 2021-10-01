package com.garvitd.dictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AppIntro extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        mFirebaseAuth=FirebaseAuth.getInstance();

    }
    @Override
    protected void onStart() {
        super.onStart();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    Intent intent = new Intent(getApplicationContext(),first_screen.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(),starting_page.class);
                    startActivity(intent);
                }
            }
        },500);
    }

}


