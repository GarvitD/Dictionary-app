package com.garvitd.dictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class starting_page extends AppCompatActivity {
    Button startbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
        startbutton=(Button)findViewById(R.id.startbutton);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openloginactivty();
            }
        });
    }
    public void openloginactivty(){
        Intent intent= new Intent(this,login_activity.class);
        startActivity(intent);
    }
}