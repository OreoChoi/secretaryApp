package com.example.junho.secretaryapps;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;

    TextToSpeech tts;
    TextView coverTxtView, reUseTxtView;
    ImageView rotationImageView;
    LinearLayout coverLayout;
    InteractAnimThread animThread;
    Thread mainThread;
    InteractSpeech interactSpeech = new InteractSpeech(this,tts);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        coverTxtView = (TextView) findViewById(R.id.coverTxtView1);
        reUseTxtView = (TextView) findViewById(R.id.reUseTxtView1);
        rotationImageView = (ImageView) findViewById(R.id.rotationImageView);
        coverLayout = (LinearLayout) findViewById(R.id.coverLayout);
        Intent perIntent = new Intent(this, PermissionActivity.class);

        startActivityForResult(perIntent, PERMISSION_CODE);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status !=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        interactSpeech = new InteractSpeech(getApplicationContext(),tts);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("result", true)) {
                animThread = new InteractAnimThread(rotationImageView, coverTxtView, reUseTxtView, this, mainHandler,tts);
                mainThread = new Thread(animThread);

                mainThread.setDaemon(true);
                mainThread.start();
            }
        }
    }

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String setResult = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    coverLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    coverTxtView.setText(setResult);
                    break;
                case 2:
                    reUseTxtView.setText(setResult);
                    interactSpeech.speech(reUseTxtView);
                    break;
            }
        }
    };

}
