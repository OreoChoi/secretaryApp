package com.example.junho.secretaryapps.recognition;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.calculator.CalculatorActivity;
import com.example.junho.secretaryapps.memo.MemoActivity;

import java.util.ArrayList;

import static com.example.junho.secretaryapps.MainActivity.STT_MODE_SWITCH;
import static com.example.junho.secretaryapps.MainActivity.modeSwitch;

public class RecognitionActivity extends AppCompatActivity {
    TextView recognitionTxtView;
    ImageView exeImgView, exeRoundImgView;
    SpeechRecognizer speechRecognizer;
    SoundPool sp;
    Animation exeImgOpen, exeImgClose, exeImgWave;
    Intent intent, functionIntent;
    int soundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recognition_memo);

        exeImgView = (ImageView) findViewById(R.id.exeImgView);
        exeRoundImgView = (ImageView) findViewById(R.id.exeRoundImgView);
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID = sp.load(this, R.raw.secstart, 1);

        exeImgOpen = AnimationUtils.loadAnimation(RecognitionActivity.this, R.anim.exeimg_open_anim);
        exeImgClose = AnimationUtils.loadAnimation(RecognitionActivity.this, R.anim.exeimg_close_anim);
        exeImgWave = AnimationUtils.loadAnimation(RecognitionActivity.this, R.anim.exeimg_wave_anim);

        recognitionTxtView = (TextView) findViewById(R.id.recognitionTxtView);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        //speechRecognizer.setRecognitionListener(recognitionListener);


        exeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (STT_MODE_SWITCH == modeSwitch) {
                        exeStart();
                        speechRecognizer.startListening(intent);
                    }
                }
                return true;
            }
        });

        exeRoundImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (STT_MODE_SWITCH == modeSwitch) {
                    exeEnd();
                }
                return true;
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            recognitionTxtView.setText("다시 말씀해 주세요");
            exeEnd();
        }

        @Override
        public void onResults(Bundle bundle) {
            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            recognitionTxtView.setText(rs[0]);
            exeEnd();
            functionCall(recognitionTxtView.getText().toString());
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

    public void exeStart() {
        sp.play(soundID, 1, 1, 0, 0, 1);
        exeImgView.startAnimation(exeImgClose);
        exeImgView.setVisibility(View.INVISIBLE);
        exeRoundImgView.setVisibility(View.VISIBLE);
        exeRoundImgView.startAnimation(exeImgOpen);
        exeRoundImgView.startAnimation(exeImgWave);
    }

    public void exeEnd() {
        exeRoundImgView.startAnimation(exeImgClose);
        exeRoundImgView.setVisibility(View.INVISIBLE);
        exeImgView.setVisibility(View.VISIBLE);
        exeImgView.startAnimation(exeImgOpen);
    }

    public void functionCall(String str){

        if(str.contains("계산기")){
            functionIntent = new Intent(this,CalculatorActivity.class);
            startActivity(functionIntent);
        }else if(str.contains("메모")){
            functionIntent = new Intent(this,MemoActivity.class);
            startActivity(functionIntent);
        }else if(str.contains("길찾기")){

        }
    }

}
