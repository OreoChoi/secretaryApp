package com.example.junho.secretaryapps;

import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class InteractAnim {
    AlphaAnimation alphaReverse = new AlphaAnimation(1, 0);
    private static final int PLAYING_TIME = 1000;
    private static final int NEXT_DELAY_TIME = 3000;

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

