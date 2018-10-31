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
public class ResultAnim {
    AlphaAnimation inVis = new AlphaAnimation(1, 0);
    AlphaAnimation vis = new AlphaAnimation(0, 1);
    int setTextDelayTime = 2000;
    int nextTextDelayTime = 5000;


     public void alphaReverse(TextView t) {
        inVis.setDuration(2000);
        inVis.setFillAfter(true);
        inVis.setRepeatCount(1);
        inVis.setRepeatMode(Animation.REVERSE);
        t.startAnimation(inVis);
    }

    public void resultTextAnim(Handler mainHandler, String nextTxt,int what) throws InterruptedException {
        Message msg = Message.obtain();
        msg.obj =  nextTxt;
        msg.what = what;
        mainHandler.sendMessageDelayed(msg,setTextDelayTime);
        Thread.sleep(nextTextDelayTime);
    }

    //Overloding
    public void resultTextAnim(Handler mainHandler, String nextTxt,int what, int delayTime) throws InterruptedException {
        Message msg = Message.obtain();
        msg.obj =  nextTxt;
        msg.what = what;
        mainHandler.sendMessageDelayed(msg,setTextDelayTime);
        Thread.sleep(nextTextDelayTime+delayTime);
    }
}

