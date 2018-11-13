package com.example.junho.secretaryapps.recognition;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
/*
* 음성인식 싱글톤으로 교체 예정
* */
public class RecogAdapter {
    SpeechRecognizer speechRecognizer;
    Context context;
    Intent intent;

    public RecogAdapter(Context context) {

        this.context = context;

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,5000);

        recogSetter();
    }

    public void recogSetter() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    //음성인식을 계속 진행합니다.
    public void recogListening() {

        speechRecognizer.startListening(intent);
    }

    public void recogLength(){

    }

    //recognition 객체를 해제합니다.
    public void recogDestory() {

        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }

    }

    //음성인식을 위해 타 미디어를 정지합니다.
    public void recogStopListen() {

        speechRecognizer.stopListening();

    }

    //음성인식을 정지합니다.
    public void recogCencel() {

        speechRecognizer.cancel();

    }

    public void recogStart(RecognitionListener r) {

        speechRecognizer.setRecognitionListener(r);
        speechRecognizer.startListening(intent);

    }

}
