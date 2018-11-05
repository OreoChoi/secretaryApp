package com.example.junho.secretaryapps.interact;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.HashMap;

public class InteractSpeech {

    Context context;
    TextToSpeech tts;

    public InteractSpeech(Context context,TextToSpeech tts){
        this.context = context;
        this.tts = tts;
    }

    public void speech(TextView tx){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ttsGreater21(tx.getText().toString());
        }else{
            ttsUnder20(tx.getText().toString());
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text){
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"MessageId");
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,map);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text){
        String utteranceId=this.hashCode()+"";
        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,utteranceId);
    }
}
