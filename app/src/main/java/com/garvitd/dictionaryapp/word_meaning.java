package com.garvitd.dictionaryapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class word_meaning extends AppCompatActivity {


    Toolbar toolbar;
    TextToSpeech tts;
    String url;
    private TextView showDef,wikipediaLink;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        showDef= (TextView)findViewById(R.id.meaning);
        wikipediaLink=(TextView)findViewById(R.id.wikipediaLink);

        Intent intent=getIntent();
        String text=intent.getStringExtra(Intent.EXTRA_TEXT);
        DictionaryRequest dr = new DictionaryRequest(this,showDef);
        url = dictionaryEntries();
        dr.execute(url);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(text);

        String link= "https://en.wikipedia.org/wiki/"+ text;

        wikipediaLink.setText(link);

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