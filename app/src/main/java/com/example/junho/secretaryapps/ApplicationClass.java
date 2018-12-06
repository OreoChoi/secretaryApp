package com.example.junho.secretaryapps;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class ApplicationClass extends Application {
    private final String dbName = "secretary";
    TextToSpeech textToSpeech;
    SQLiteDatabase sqliteDB;
    SpeechRecognizer speechRecognizer;
    Intent recognitionIntent;

    public ApplicationClass(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setTextToSpeech();
        setRecognition();
        dbOpen();
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /* Speech To Text */
    private void setRecognition() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        recognitionIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,5000);
    }

    public Intent getRecognitionIntent(){
        return recognitionIntent;
    }

    public SpeechRecognizer getSpeechRecognizer(){
        return speechRecognizer;
    }

    /* Text To Speech */
    private void setTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.KOREAN);
                }
            }
        });
        textToSpeech.setSpeechRate(1.5f);
    }

    public TextToSpeech getTextToSpeech(){
        return textToSpeech;
    }


    /* SQLiteDataBase */

    private void dbOpen(){
        sqliteDB = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
    }

    public SQLiteDatabase getSqliteDB(){
        return sqliteDB;
    }



}






