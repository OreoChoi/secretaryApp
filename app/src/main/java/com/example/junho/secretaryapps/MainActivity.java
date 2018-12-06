package com.example.junho.secretaryapps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.junho.secretaryapps.permission.PermissionActivity;
import com.example.junho.secretaryapps.recognition.RecognitionActivity;

import java.security.MessageDigest;

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
    ApplicationClass applicationClass;
    TTSClass ttsSpeech;
    public static int mode;

    @SuppressLint("ClickableViewAccessibility")
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
        applicationClass = (ApplicationClass) getApplicationContext();
        ttsSpeech = new TTSClass(applicationClass);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != CHECKER ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != CHECKER ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != CHECKER) {

            Intent perIntent = new Intent(this, PermissionActivity.class);
            startActivityForResult(perIntent, PERMISSION_CODE);

        } else {
            SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
            mode = pref.getInt("mode", 0);

            if (mode == STT_MODE_SWITCH) {
                Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                startActivity(intent);
                finish();
            } else if (mode == TOUCH_MODE_SWITCH) {
                Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                startActivity(intent);
                finish();
            } else {
                animThread = new InitialStartThread(rotationImgView, coverTxtView, reUseTxtView, this, mainHandler);
                mainThread = new Thread(animThread);

                mainThread.setDaemon(true);
                mainThread.start();
            }

        }

        /* Speech recognition mode select*/
        sttModeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("mode", STT_MODE_SWITCH);
                    editor.commit();


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
                    SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("mode", TOUCH_MODE_SWITCH);
                    editor.commit();

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

    /* permission 획득 후에 애니메이션을 실행합니다. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (data.getBooleanExtra("result", true)) {

            } else {
                SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
                int mode = pref.getInt("mode", 0);

                if (mode == STT_MODE_SWITCH) {
                    Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                    startActivity(intent);
                    finish();
                } else if (mode == TOUCH_MODE_SWITCH) {
                    Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
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

    /* Mode 초기화 메소드*/
    @Deprecated
    public void setDefaultMode(){
        SharedPreferences pref = getSharedPreferences("ModeSwitch",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("mode",0);
        editor.commit();
    }

    /* Application Hash키를 얻는 메소드 */
    @Deprecated
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }
}
