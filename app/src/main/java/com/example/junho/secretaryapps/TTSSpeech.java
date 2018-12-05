package com.example.junho.secretaryapps;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Locale;

public class TTSSpeech {
    Context context;
    TextToSpeech tts;

    public TTSSpeech(Context context) {
        this.context = context;
        ttsSetter();
    }

    //TTS객체를 생성합니다.
    public void ttsSetter() {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        tts.setSpeechRate(1.5f);
    }

    //안드로이드 버전을 체크해 버전에 맞는 tts엔진을 실행합니다.
    public void speech(TextView tx){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ttsGreater21(tx.getText().toString());
        }else{
            ttsUnder20(tx.getText().toString());
        }
    }
    public void speech(String str){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ttsGreater21(str);
        }else{
            ttsUnder20(str);
        }
    }

    //TTS엔진을 멈추고 해제합니다.
    public void ttsClear(){
        tts.stop();
        tts.shutdown();
        tts = null;
    }

    public void ttsStop(){
        tts.stop();
    }

    //롤리팝 이전 TTS엔진
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text){
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"MessageId");
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,map);
    }

    //롤리팝 이후 TTS엔진
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text){
        String utteranceId=this.hashCode()+"";
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,utteranceId);
    }
}
