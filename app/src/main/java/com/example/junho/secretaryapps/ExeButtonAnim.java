package com.example.junho.secretaryapps;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class ExeButtonAnim {
    Context context;
    SoundPool sp;
    int soundID;
    Animation exeImgOpen, exeImgClose, exeImgWave;

    public ExeButtonAnim(Context context){
        this.context = context;
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID = sp.load(context, R.raw.secstart, 1);
        exeImgOpen = AnimationUtils.loadAnimation(context, R.anim.exeimg_open_anim);
        exeImgClose = AnimationUtils.loadAnimation(context, R.anim.exeimg_close_anim);
        exeImgWave = AnimationUtils.loadAnimation(context, R.anim.exeimg_wave_anim);
    }

    //실행 버튼 누를 시 실행되는 애니메이션
    public void exeStart(ImageView exeImgView, ImageView exeRoundImgView) {
        sp.play(soundID, 1, 1, 0, 0, 1);
        exeImgView.startAnimation(exeImgClose);
        exeImgView.setVisibility(View.INVISIBLE);
        exeRoundImgView.setVisibility(View.VISIBLE);
        exeRoundImgView.startAnimation(exeImgOpen);
        exeRoundImgView.startAnimation(exeImgWave);
    }

    //실행시 버튼 누를 시 실행되는 애니메이션
    public void exeEnd(ImageView exeImgView, ImageView exeRoundImgView) {
        exeRoundImgView.startAnimation(exeImgClose);
        exeRoundImgView.setVisibility(View.INVISIBLE);
        exeImgView.setVisibility(View.VISIBLE);
        exeImgView.startAnimation(exeImgOpen);
    }
}
