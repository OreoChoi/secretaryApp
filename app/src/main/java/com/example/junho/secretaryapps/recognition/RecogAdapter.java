package com.example.junho.secretaryapps.recognition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.ArrayList;

import static com.example.junho.secretaryapps.memo.MemoActivity.flag;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.DELAY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.FINISH;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.READY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.RECOGNITION;

public class RecogAdapter implements RecognitionListener {
    SpeechRecognizer speechRecognizer;
    Context context;
    Intent intent;
    Handler recogHandler;

    public RecogAdapter(Context context){
        this.context = context;
        recogSetter();
    }
    public RecogAdapter(Context context, Handler handler) {
        this.context = context;
        this.recogHandler = handler;
        recogSetter();
    }

    public void recogSetter() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS,5000);

    }

    //음성인식을 계속 진행합니다.
    public void recogListening() {
        speechRecognizer.startListening(intent);
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

    /* 음성인식을 시작합니다. */
    public void recogStart() {
        speechRecognizer.setRecognitionListener(this);
        speechRecognizer.startListening(intent);
    }

    /* 음성인식 리스너 부분 */
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
        recogHandler.sendEmptyMessageDelayed(DELAY, 2000);
    }

    @Override
    public void onResults(Bundle bundle) {
        //MemoActivity에서만 사용하는 변수입니다.
        flag++;

        String key = SpeechRecognizer.RESULTS_RECOGNITION;
        ArrayList<String> mResult = bundle.getStringArrayList(key);

        String[] rs = new String[mResult.size()];
        mResult.toArray(rs);

        /*핸들러로 넘기기*/
        Message msg = Message.obtain();
        msg.obj = rs[0];
        msg.what = RECOGNITION;
        recogHandler.sendMessage(msg);

        recogHandler.sendEmptyMessage(FINISH);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}

