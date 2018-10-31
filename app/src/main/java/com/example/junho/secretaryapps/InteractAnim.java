package com.example.junho.secretaryapps;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

/*
 * alphaReverse(TextView t)
 *
 * TextView가 서서히 alpha값이 0으로 변경
 * TextView의 alpha값이 0으로 도달한 경우 다음 Text를 setText합니다.
 * setRepeatMode(REVERSE)에 의해 다시 alpha값이 서서히 1으로 변경됩니다.
 *
 *
 * resultTextAnim()
 *
 *
 * */
public class InteractAnim {
    AlphaAnimation alphaReverse = new AlphaAnimation(1, 0);
    private static final int PLAYING_TIME = 2000;
    private static final int NEXT_DELAY_TIME = 5000;

    public void alpha(TextView t) {
        alphaReverse.setDuration(PLAYING_TIME);
        alphaReverse.setFillAfter(true);
        alphaReverse.setRepeatCount(1);
        alphaReverse.setRepeatMode(Animation.REVERSE);
        t.startAnimation(alphaReverse);
    }

    public void nextSetText(Handler mainHandler, String nextTxt, int what) throws InterruptedException {
        Message msg = Message.obtain();
        msg.obj = nextTxt;
        msg.what = what;

        mainHandler.sendMessageDelayed(msg, PLAYING_TIME);
        Thread.sleep(NEXT_DELAY_TIME);
    }

    public void nextSetText(Handler mainHandler, String nextTxt, int what, int delayTime) throws InterruptedException {
        Message msg = Message.obtain();
        msg.obj = nextTxt;
        msg.what = what;

        mainHandler.sendMessageDelayed(msg, PLAYING_TIME);
        Thread.sleep(NEXT_DELAY_TIME + delayTime);
    }
}

