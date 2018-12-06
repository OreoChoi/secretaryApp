package com.example.junho.secretaryapps;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.HashMap;

public class TTSClass {
    ApplicationClass applicationClass;
    TextToSpeech textToSpeech;

    public TTSClass(ApplicationClass applicationClass){
        this.applicationClass = applicationClass;
        this.textToSpeech = applicationClass.getTextToSpeech();
    }

    /* TTS 선언 영역 */
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
        textToSpeech.stop();
        textToSpeech.shutdown();
        textToSpeech = null;
    }

    public void ttsStop(){
        textToSpeech.stop();
    }

    /* 롤리팝 이전 TTS 기능 */
    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text){
        HashMap<String, String> map = new HashMap<>();
        map.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"MessageId");
        textToSpeech.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH,map);
    }

    /* 롤리팝 이후 TTS 기능 */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text){
        String utteranceId=this.hashCode()+"";
        textToSpeech.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH,null,utteranceId);
    }
}






