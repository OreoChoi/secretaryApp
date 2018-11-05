package com.example.junho.secretaryapps.initialstart;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.junho.secretaryapps.interact.InteractAnim;
import com.example.junho.secretaryapps.interact.InteractSpeech;
import com.example.junho.secretaryapps.R;


public class InitialStartThread extends InteractAnim implements Runnable{
    ImageView rotationImageView;
    TextView coverTxtView, reUseTxtView;
    Handler mainHandler;
    Context context;
    InteractSpeech interactSpeech;
    Animation iconRotate;
    TextToSpeech tts;

    public InitialStartThread(ImageView rotationImageView, TextView coverTxtView,
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
            /*super.alpha(coverTxtView);
            super.nextSetText(mainHandler,"이제 기동을 시작합니다",2,1000);

            mainHandler.sendEmptyMessage(0);
            super.alpha(reUseTxtView);
            interactSpeech.speech(reUseTxtView);

            super.nextSetText(mainHandler,"저는 음성인식으로 움직이는\n 비서입니다",3);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"제 이름은 Secretary라고 합니다",3);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"앞으로 잘 부탁 드립니다",3);
            super.alpha(reUseTxtView);*/

            super.nextSetText(mainHandler,"애플리케이션 기본 모드를 선택하여 주세요. 선택하신 모드에 맞춰 조작 방식이 달라집니다.",3);
            mainHandler.sendEmptyMessage(1);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }


}
