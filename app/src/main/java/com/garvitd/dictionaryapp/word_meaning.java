package com.garvitd.dictionaryapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class word_meaning extends AppCompatActivity {

    Toolbar toolbar;
    TextToSpeech tts;
    String url;
    private TextView showDef,wikipediaLink,mText;
    String text;
    ImageView imageForWord;
    RequestQueue mRequestQueue;
    ProgressBar progressBarLoadCall;
    CheckBox saveWordCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        showDef= (TextView)findViewById(R.id.meaning);
        wikipediaLink=(TextView)findViewById(R.id.wikipediaLink);
        imageForWord = findViewById(R.id.imageForWord);
        progressBarLoadCall = findViewById(R.id.progressBarLoadCall);
        saveWordCheckBox = findViewById(R.id.saveWordCheckBox);

        Intent intent = getIntent();
        String text=intent.getStringExtra(Intent.EXTRA_TEXT);

        mRequestQueue= Volley.newRequestQueue(this);
        parseJSON(text);

        DictionaryRequest dr = new DictionaryRequest(this,showDef);
        url = dictionaryEntries();
        dr.execute(url);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(text);

        String link= "https://en.wikipedia.org/wiki/"+ text;

        wikipediaLink.setText(link);

        wikipediaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(word_meaning.this,WebView.class);
                intent1.putExtra(Intent.EXTRA_TEXT,link);
                startActivity(intent1);
            }
        });

        ImageButton pronounce= (ImageButton)findViewById(R.id.pronounce);
        pronounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts= new TextToSpeech(word_meaning.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status==TextToSpeech.SUCCESS){
                            int result= tts.setLanguage(Locale.getDefault());
                            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error","Sorry, this language is not supported.");
                            }
                            else {
                                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        }
                    }
                });
            }
        });

        saveWordCheckBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(saveWordCheckBox.isChecked()){
                    saveWordCheckBox.setButtonTintList(getColorStateList(R.color.saveYellow));
                } else{
                    saveWordCheckBox.setButtonTintList(getColorStateList(R.color.white));
                }
            }
        });
    }

    private void parseJSON(String text) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&formatversion=2&prop=pageimages|pageterms&piprop=thumbnail&pithumbsize=600&titles="+ text;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject object = response.getJSONObject("query");
                            JSONArray array = object.getJSONArray("pages");
                            JSONObject page = array.getJSONObject(0);
                            JSONObject thumbnail = page.getJSONObject("thumbnail");
                            String req_url = thumbnail.getString("source");
                            loadImage(req_url);
                            progressBarLoadCall.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            progressBarLoadCall.setVisibility(View.GONE);
                            Toast.makeText(word_meaning.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarLoadCall.setVisibility(View.GONE);
                Toast.makeText(word_meaning.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        mRequestQueue.add(request);
    }

    private void loadImage(String imageUrl) {
        Glide.with(word_meaning.this)
                .load(imageUrl)
                .centerCrop()
                .into(imageForWord);
    }

    private String dictionaryEntries() {

        final String language = "en-gb";
        Intent intent=getIntent();
        final String word =intent.getStringExtra(Intent.EXTRA_TEXT);
        final String fields = "definitions";
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }

}