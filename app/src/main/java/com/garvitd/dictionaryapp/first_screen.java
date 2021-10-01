package com.garvitd.dictionaryapp;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class first_screen extends AppCompatActivity {

    RecyclerView recyclerView;
    String entered_text;
    TextView seeCompleteHistory;

    ImageView speechToText,imageToText;
    SearchView search_view;
    Bitmap imageBitmap;

    NavigationView navigationView_first_screen;
    DrawerLayout drawerLayout_first_screen;
    ActionBarDrawerToggle Toggle_first_screen;

    DatabaseReference databaseReferenceHistory;
    public String userIds;
    FirebaseAuth mAuth;

    List<HistoryModel> historyList;
    ProgressBar progressBarHistory;

    TextView wordOfTheDayText,wordOfTheDayType,wordOfTheDayDef;
    RequestQueue requestQueueWordOfTheDay;
    ImageView wordOfTheDayPronounce;
    TextToSpeech textToSpeechWordOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        progressBarHistory= findViewById(R.id.progressBar_history);
        seeCompleteHistory=findViewById(R.id.seeCompleteHistory);

        wordOfTheDayText=findViewById(R.id.wordOfTheDayText);
        wordOfTheDayType=findViewById(R.id.wordOfTheDayType);
        wordOfTheDayDef=findViewById(R.id.wordOfTheDayDef);

        String date =getYesterdayDate();

        requestQueueWordOfTheDay = Volley.newRequestQueue(this);
        parseJSONWordOfTheDay(date);

        wordOfTheDayPronounce = findViewById(R.id.wordOfTheDaySpeech);
        wordOfTheDayPronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakWordOfTheDay();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        String userEmail =  mAuth.getCurrentUser().getEmail();
        userIds = checkId(userEmail);

        databaseReferenceHistory = FirebaseDatabase.getInstance().getReference("history");
        loadHistory();

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        seeCompleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(first_screen.this,SeeCompleteHistory.class);
                startActivity(intent);
            }
        });

        speechToText=(ImageView)findViewById(R.id.speechToText);
        imageToText=(ImageView)findViewById(R.id.imageToText);

        navigationView_first_screen=(NavigationView)findViewById(R.id.navigationView_first_screen);
        drawerLayout_first_screen=(DrawerLayout)findViewById(R.id.drawer_layout_first_screen);

        Toolbar toolbar1=(Toolbar)findViewById(R.id.tool1);

        setSupportActionBar(toolbar1);

        Toggle_first_screen= new ActionBarDrawerToggle(this,drawerLayout_first_screen,toolbar1,R.string.open,R.string.close);
        drawerLayout_first_screen.addDrawerListener(Toggle_first_screen);
        Toggle_first_screen.syncState();
        Toggle_first_screen.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));

        navigationView_first_screen.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_myProfile:
                        Toast.makeText(first_screen.this, "My Profile", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_logout:
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(getApplicationContext(), starting_page.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_recents:
                        Intent intent1 = new Intent(first_screen.this,SeeCompleteHistory.class);
                        startActivity(intent1);
                }
                return true;
            }
        });


        speechToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Listening...");
                try {
                    startActivityForResult(intent,1);
                } catch (ActivityNotFoundException e){
                    Toast.makeText(first_screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(captureImageIntent, 101);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(first_screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    //TODO Add and alert dialog box here
                }
            }
        });


        search_view = (SearchView) findViewById(R.id.search_view);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view.setIconified(false); //this is used to make possible the thing that the whole search bar is clickable and not only the search icon//
            }
        });

        search_view.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechToText.setVisibility(View.GONE);
                imageToText.setVisibility(View.GONE);
            }
        });

        search_view.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                speechToText.setVisibility(View.VISIBLE);
                imageToText.setVisibility(View.VISIBLE);
                return false;
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                entered_text= "" + search_view.getQuery();
                addHistory();

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

    private void speakWordOfTheDay() {
        String toBeSpokenText = wordOfTheDayText.getText().toString();
        textToSpeechWordOfTheDay = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status== TextToSpeech.SUCCESS){
                    int result = textToSpeechWordOfTheDay.setLanguage(Locale.ENGLISH);
                    if(result== TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(first_screen.this, "Sorry this language is not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            textToSpeechWordOfTheDay.speak(toBeSpokenText,TextToSpeech.QUEUE_FLUSH,null,null);
                        } else {
                            textToSpeechWordOfTheDay.speak(toBeSpokenText, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
            }
        });
    }

    private void parseJSONWordOfTheDay(String date) {

        String APIKey = getString(R.string.WordnikAPIKey);
        String url = "https://api.wordnik.com/v4/words.json/wordOfTheDay?date="+ date +"&api_key="+APIKey;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String word = response.getString("word");
                            wordOfTheDayText.setText(word);
                            JSONArray defArray = response.getJSONArray("definitions");
                            JSONObject defObject = defArray.getJSONObject(0);
                            String def = defObject.getString("text");
                            wordOfTheDayDef.setText(def);
                            String typeOfSpeech = defObject.getString("partOfSpeech");
                            wordOfTheDayType.setText(typeOfSpeech);
                        } catch (JSONException e) {
                            Toast.makeText(first_screen.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(first_screen.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueueWordOfTheDay.add(objectRequest);
    }

    private String getYesterdayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);

        String strDate = simpleDateFormat.format(calendar.getTime());
        return strDate;
    }

    private void loadHistory() {
        historyList = new ArrayList<>();

        DatabaseReference dbRefHistoryLoad = FirebaseDatabase.getInstance().getReference("history").child(userIds);

        dbRefHistoryLoad.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dbHistory : snapshot.getChildren()){
                        HistoryModel historyModel = dbHistory.getValue(HistoryModel.class);
                        Collections.reverse(historyList);
                        historyList.add(historyModel);
                        Collections.reverse(historyList);
                    }
                    progressBarHistory.setVisibility(View.GONE);
                    seeCompleteHistory.setText("See All");
                    HistoryAdapter historyAdapter = new HistoryAdapter(first_screen.this,historyList);
                    recyclerView.setAdapter(historyAdapter);
                }
                progressBarHistory.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(first_screen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addHistory() {

        String historyWords = entered_text;
        String userEmail =  mAuth.getCurrentUser().getEmail();

        if(!TextUtils.isEmpty(historyWords)){
            String uniqueIds = databaseReferenceHistory.push().getKey();

            HistoryModel historyModel = new HistoryModel(userEmail,historyWords);

            databaseReferenceHistory.child(userIds).child(uniqueIds).setValue(historyModel);

        } else {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    public String checkId(String s) {
        int pos = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '@' ){
                pos = i;
                break;
            }
        }
        return s.substring(0,pos);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case 1 :
                if(resultCode==RESULT_OK && null!=data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    entered_text = result.get(0);
                    addHistory();
                    Intent intent = new Intent(first_screen.this,word_meaning.class);
                    intent.putExtra(Intent.EXTRA_TEXT,entered_text);
                    startActivity(intent);
                }
                break;

            case 101 :
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    detectTextFromImage();
                }

        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(@NonNull  FirebaseVisionText firebaseVisionText) {
                List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
                if(blockList.size()==0){
                    Toast.makeText(first_screen.this, "The image provided does not have any text", Toast.LENGTH_SHORT).show();
                    //TODO Add Alert Dialog Box
                } else {
                    for(FirebaseVisionText.Block block : firebaseVisionText.getBlocks()){
                        entered_text = block.getText();
                        addHistory();
                        Intent intent = new Intent(first_screen.this,word_meaning.class);
                        intent.putExtra(Intent.EXTRA_TEXT,entered_text);
                        startActivity(intent);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                Toast.makeText(first_screen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}