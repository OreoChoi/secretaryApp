package com.example.junho.secretaryapps;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Locale;

public class InteractAnimThread extends InteractAnim implements Runnable{
    ImageView rotationImageView;
    TextView coverTxtView, reUseTxtView;
    Handler mainHandler;
    Context context;
    InteractSpeech interactSpeech;
    Animation iconRotate;
    TextToSpeech tts;

    public InteractAnimThread(ImageView rotationImageView, TextView coverTxtView,
                              TextView reUseTxtView, Context context, Handler mainHandler, TextToSpeech tts){
        this.rotationImageView = rotationImageView;
        this.coverTxtView = coverTxtView;
        this.reUseTxtView = reUseTxtView;
        this.mainHandler = mainHandler;
        this.context = context;
        this.tts = tts;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        Looper.prepare();
        try {
            interactSpeech = new InteractSpeech(context, tts);
            iconRotate = AnimationUtils.loadAnimation(context, R.anim.iconrotate);
            rotationImageView.startAnimation(iconRotate);
            super.alpha(coverTxtView);
            super.nextSetText(mainHandler,"이제 기동을 시작합니다",1,1000);

            mainHandler.sendEmptyMessage(0);

            super.alpha(reUseTxtView);
            interactSpeech.speech(reUseTxtView);

            super.nextSetText(mainHandler,"저는 음성인식으로 움직이는\n 비서입니다",2);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"제 이름은 Secretary라고 합니다",2);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"앞으로 잘 부탁 드립니다",2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }

}
