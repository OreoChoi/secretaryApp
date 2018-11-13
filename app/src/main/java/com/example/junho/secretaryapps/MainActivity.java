package com.example.junho.secretaryapps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junho.secretaryapps.initialstart.InitialStartThread;
import com.example.junho.secretaryapps.interact.TTSSpeech;
import com.example.junho.secretaryapps.permission.PermissionActivity;
import com.example.junho.secretaryapps.recognition.RecognitionActivity;

public class MainActivity extends AppCompatActivity {
    public static final int CHECKER = PackageManager.PERMISSION_GRANTED;
    private static final int PERMISSION_CODE = 1001;
    public static final int STT_MODE_SWITCH = 1;
    public static final int TOUCH_MODE_SWITCH = 2;

    TextView coverTxtView, reUseTxtView;
    ImageView rotationImgView, touchModeImgView, sttModeImgView;
    LinearLayout coverLayout;
    InitialStartThread animThread;
    Thread mainThread;
    TTSSpeech ttsSpeech;
    public static int modeSwitch, mode;

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
        ttsSpeech = new TTSSpeech(this);

        Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
        /*
         *  권한이 있는지 판별합니다.
         *  권한이 있으면 초기기동 애니메이션을 실행 하거나
         *  초기기동이 아닐 시 사용자가 기존에 사용했던 모드로 이동합니다.
         *  터치 모드 or 음성인식 모드
         * */

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != CHECKER ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != CHECKER ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != CHECKER) {

            Intent perIntent = new Intent(this, PermissionActivity.class);
            startActivityForResult(perIntent, PERMISSION_CODE);

        } else {

            animThread = new InitialStartThread(rotationImgView, coverTxtView, reUseTxtView, this, mainHandler);
            mainThread = new Thread(animThread);

            mainThread.setDaemon(true);
            mainThread.start();

        }


        /* Speech recognition mode select*/
        sttModeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    modeSwitch = 1;

                    ttsSpeech.ttsStop();
                    Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                    startActivity(intent);

                }
                return true;
            }
        });

        /* Touch mode select*/
        touchModeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    modeSwitch = 2;

                    ttsSpeech.ttsStop();

                }
                return true;
            }
        });
    }

    /*Pause시 tts객체를 정지시킵니다.*/
    @Override
    protected void onPause() {
        super.onPause();
        ttsSpeech.ttsStop();
    }

    /*Destroy시 tts객체를 해제합니다.*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ttsSpeech != null) {
            ttsSpeech.ttsClear();
        }
    }

    /* modeSwitch : 음성모드, 터치모드인지 인식하는 변수입니다. */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode",modeSwitch);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
         int mode = savedInstanceState.getInt("modeSwitch");

                if (mode == STT_MODE_SWITCH) {

                    Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                    startActivity(intent);

                } else if (mode == TOUCH_MODE_SWITCH) {

                    Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                    startActivity(intent);

                }
    }

    /* permission 획득 후에 애니메이션을 실행합니다. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("result", true)) {
                animThread = new InitialStartThread(rotationImgView, coverTxtView, reUseTxtView, this, mainHandler);
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
                    ttsSpeech.speech(reUseTxtView);
                    break;
            }
        }
    };
}
