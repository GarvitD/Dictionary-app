package com.garvitd.dictionaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class register_page extends AppCompatActivity implements View.OnClickListener {
    EditText editTextemail,editTextpassword,editTextFirstname,editTextLastname,confpassword;
    ProgressBar progressbar;
    private FirebaseAuth mAuth;
    TextView backtologin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Toolbar toolbar=findViewById(R.id.tool2);
        editTextemail =(EditText) findViewById(R.id.emailAddress);
        editTextpassword=(EditText) findViewById(R.id.password);
        editTextFirstname=(EditText)findViewById(R.id.firstname);
        confpassword=(EditText)findViewById(R.id.confpassword);
        progressbar=(ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        backtologin=(TextView) findViewById(R.id.backtologin);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openloginactivity();
            }
        });

        findViewById(R.id.signupbutton).setOnClickListener(this);
    }

    private void openloginactivity() {
        Intent intent=new Intent(this,login_activity.class);
        startActivity(intent);
    }

    private void registerUser(){
        String email = editTextemail.getText().toString().trim();
        String password=editTextpassword.getText().toString().trim();
        String first=editTextFirstname.getText().toString().trim();
        String confirmedpass = confpassword.getText().toString().trim();

        if(password.equalsIgnoreCase(confirmedpass)){

        } else {
            confpassword.setError("Passwords do not match");
            confpassword.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextemail.setError("Please enter your email address");
            editTextemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextemail.setError("Please enter a valid email");
            editTextemail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Please enter your password");
            editTextpassword.requestFocus();
            return;
        }
        if(first.isEmpty()){
            editTextFirstname.setError("Please enter your first name");
            editTextemail.requestFocus();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressbar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(register_page.this,first_screen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "An unexpected error occurred.Please try again or check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signupbutton :
                registerUser();
                break;
        }
    }
}