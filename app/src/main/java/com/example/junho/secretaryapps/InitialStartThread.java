package com.example.junho.secretaryapps;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class InitialStartThread extends InteractAnim implements Runnable{
    ImageView rotationImageView;
    TextView coverTxtView, reUseTxtView;
    Handler mainHandler;
    Context context;
    Animation iconRotate;

    public InitialStartThread(ImageView rotationImageView, TextView coverTxtView,
                              TextView reUseTxtView, Context context, Handler mainHandler){
        this.rotationImageView = rotationImageView;
        this.coverTxtView = coverTxtView;
        this.reUseTxtView = reUseTxtView;
        this.mainHandler = mainHandler;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {
        Looper.prepare();
        try {
            iconRotate = AnimationUtils.loadAnimation(context, R.anim.iconrotate);
            rotationImageView.startAnimation(iconRotate);
            super.alpha(coverTxtView);
            super.nextSetText(mainHandler,"이제 기동을 시작합니다",2,1000);

            mainHandler.sendEmptyMessage(0);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"저는 음성인식으로 움직이는\n 비서입니다",3);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"제 이름은 Secretary라고 합니다",3);
            super.alpha(reUseTxtView);

            super.nextSetText(mainHandler,"앞으로 잘 부탁 드립니다",3);
            super.alpha(reUseTxtView);

            mainHandler.sendEmptyMessage(1);
            super.nextSetText(mainHandler,"애플리케이션 기본 모드를 선택하여 주세요. 선택하신 모드에 맞춰 조작 방식이 달라집니다.",3);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }
}
