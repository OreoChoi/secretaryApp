package com.example.junho.secretaryapps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.junho.secretaryapps.initialstart.InitialStartThread;
import com.example.junho.secretaryapps.interact.InteractSpeech;
import com.example.junho.secretaryapps.permission.PermissionActivity;
import com.example.junho.secretaryapps.recognition.RecognitionActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final int CHECKER = PackageManager.PERMISSION_GRANTED;
    private static final int PERMISSION_CODE = 1001;
    public static final int STT_MODE_SWITCH = 1;
    public static final int TOUCH_MODE_SWITCH = 2;

    TextView coverTxtView, reUseTxtView;
    ImageView rotationImgView,touchModeImgView, sttModeImgView;
    LinearLayout coverLayout;
    InitialStartThread animThread;
    Thread mainThread;
    TextToSpeech tts;
    InteractSpeech interactSpeech = new InteractSpeech(this,tts);
    public static int modeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        coverTxtView = (TextView) findViewById(R.id.coverTxtView1);
        reUseTxtView = (TextView) findViewById(R.id.reUseTxtView1);
        rotationImgView = (ImageView) findViewById(R.id.rotationImageView);
        coverLayout = (LinearLayout) findViewById(R.id.coverLayout);
        sttModeImgView = (ImageView) findViewById(R.id.sttModeImgView);
        touchModeImgView = (ImageView) findViewById(R.id.touchModeImgView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != CHECKER ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != CHECKER ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != CHECKER) {

            Intent perIntent = new Intent(this, PermissionActivity.class);
            startActivityForResult(perIntent, PERMISSION_CODE);
        }else{
            animThread = new InitialStartThread(rotationImgView, coverTxtView, reUseTxtView, this, mainHandler,tts);
            mainThread = new Thread(animThread);

            mainThread.setDaemon(true);
            mainThread.start();
        }

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status !=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        tts.setSpeechRate(1.5f);
        interactSpeech = new InteractSpeech(getApplicationContext(),tts);




        sttModeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    modeSwitch = 1;
                    Intent intent = new Intent(MainActivity.this,RecognitionActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        touchModeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    modeSwitch = 2;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("result", true)) {
                animThread = new InitialStartThread(rotationImgView, coverTxtView, reUseTxtView, this, mainHandler,tts);
                mainThread = new Thread(animThread);

                mainThread.setDaemon(true);
                mainThread.start();
            }
        }
    }

    /*추후 애스크 스레드로 교체할 것*/
    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String setResult = (String) msg.obj;
            switch (msg.what) {
                case 0:
                    coverLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    touchModeImgView.setVisibility(View.VISIBLE);
                    sttModeImgView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    coverTxtView.setText(setResult);
                    break;
                case 3:
                    reUseTxtView.setText(setResult);
                    interactSpeech.speech(reUseTxtView);
                  break;
            }
        }
    };
}
