package com.example.junho.secretaryapps;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.INVISIBLE;

/*
 * AnimThread
 *
 * perCheckResult = true 일 때 Animation Thread 시작합니다.
 * perCheckResult = false 일 때 MainActivity를 onDestroy합니다.
 *
 * */

public class ResultAnimThread extends ResultAnim implements Runnable{
    ImageView rotationImageView;
    Animation iconrotate;
    TextView coverTxtView, reUseTxtView;
    Handler mainHandler;



    public ResultAnimThread(ImageView rotationImageView, TextView coverTxtView, TextView reUseTxtView,Context context,Handler mainHandler){

        this.rotationImageView = rotationImageView;
        this.coverTxtView = coverTxtView;
        this.reUseTxtView = reUseTxtView;
        this.mainHandler = mainHandler;
        iconrotate = AnimationUtils.loadAnimation(context, R.anim.iconrotate);

    }

    @Override
    public void run() {
        Looper.prepare();
        try {
            animationStart(iconrotate);
            rotationImageView.setAnimation(iconrotate);

            super.alphaReverse(coverTxtView);
            super.resultTextAnim(mainHandler,"이제 기동을 시작합니다",1,1000);
            mainHandler.sendEmptyMessage(0);

            /* coverLayout 애니메이션 종료
             * frameLayout 애니메이션 시작 */

            super.alphaReverse(reUseTxtView);
            super.resultTextAnim(mainHandler,"저는 음성인식으로 움직이는\n 비서입니다",2);

            super.alphaReverse(reUseTxtView);
            super.resultTextAnim(mainHandler,"제 이름은 Secreatary라고 합니다",2);

            super.alphaReverse(reUseTxtView);
            super.resultTextAnim(mainHandler,"앞으로 잘 부탁 드립니다",2);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }

    public void animationStart(Animation ani) {
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
