package com.example.junho.secretaryapps.memo;


import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junho.secretaryapps.ExeButtonAnim;
import com.example.junho.secretaryapps.MemoDB;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.interact.TTSSpeech;
import com.example.junho.secretaryapps.recognition.RecogAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MemoActivity extends AppCompatActivity {
    private final int READY = 0, DELAY = 1, FINISH = 2;
    Button clearButton, saveButton;
    EditText titleText, contentText;
    TextView daysText, currentLocationText;
    ExeButtonAnim exeButtonAnim;
    RecogAdapter recogAdapter;
    ImageView exeImgView, exeRoundImgView;
    Intent reIntent;
    TTSSpeech ttsSpeech;
    MemoDB db;
    int switchNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        clearButton = (Button) findViewById(R.id.clearButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        titleText = (EditText) findViewById(R.id.titleText);
        contentText = (EditText) findViewById(R.id.contentText);
        daysText = (TextView) findViewById(R.id.daysText);
        currentLocationText = (TextView) findViewById(R.id.currentLocationText);
        ttsSpeech = new TTSSpeech(this);
        reIntent = getIntent();
        recogAdapter = new RecogAdapter(this);

        ttsSpeech.speech("입력하실 메모내용을 말씀해주세요");

        setCurrentData();

        exeImgView = (ImageView) findViewById(R.id.exeImgView);
        exeRoundImgView = (ImageView) findViewById(R.id.exeRoundImgView);
        exeButtonAnim = new ExeButtonAnim(this);

        //실행 이미지뷰 클릭 시
        exeImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    recogAdapter.recogStart(recognitionListener);
                }
                return true;
            }
        });

        //실행중 이미지뷰 클릭 시
        exeRoundImgView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                exeButtonAnim.exeEnd(exeImgView, exeRoundImgView);
                recogAdapter.recogCencel();
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String column1 = currentLocationText.getText().toString();
                String column2 = daysText.getText().toString();
                String column3 = contentText.getText().toString();
                String column4 = titleText.getText().toString();

                db = new MemoDB(MemoActivity.this);

                db.dbOpen();
                db.memoCreate();
                db.memoInsert(column1, column2, column3, column4);
                db.dbClose();

                db = null;
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText("");
                contentText.setText("");
            }
        });
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            handler.sendEmptyMessage(READY);
        }

        @Override
        public void onEndOfSpeech() {
            handler.sendEmptyMessage(FINISH);
        }

        @Override
        public void onError(int i) {
            handler.sendEmptyMessageDelayed(DELAY,1000);
        }

        //음성인식 결과를 표시합니다.
        @Override
        public void onResults(Bundle bundle) {

            //음성인식한 텍스트를 생성합니다.
            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            if (switchNum == 0) {
                contentText.append(rs[0] + " ");
                ttsSpeech.speech("제목을 말씀해주세요");
                recogAdapter.recogListening();

            } else if (switchNum == 1) {
                titleText.setText(rs[0] + " ");
                ttsSpeech.speech("저장 하시겠어요?");
                recogAdapter.recogListening();

            } else {
                if (saveRecognition(rs[0])) {
                    try {
                        String column1 = currentLocationText.getText().toString();
                        String column2 = daysText.getText().toString();
                        String column3 = contentText.getText().toString();
                        String column4 = titleText.getText().toString();

                        db = new MemoDB(MemoActivity.this);

                        db.dbOpen();
                        db.memoCreate();
                        db.memoInsert(column1, column2, column3, column4);
                        db.dbClose();

                        db = null;

                        Log.d("STRING", "SQL SU");

                        resultIntent(1);

                    } catch (SQLException e) {
                        Log.d("STRING", "SQL ERROR");

                    }
                    exeButtonAnim.exeEnd(exeImgView, exeRoundImgView);

                } else {
                    resultIntent(0);
                }
            }

            switchNum++;
        }

        @Override
        public void onPartialResults(Bundle bundle) {}

        @Override
        public void onEvent(int i, Bundle bundle) {}

        @Override
        public void onBeginningOfSpeech() {}

        @Override
        public void onRmsChanged(float v) {}

        @Override
        public void onBufferReceived(byte[] bytes) {}
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case READY:
                    exeButtonAnim.exeStart(exeImgView, exeRoundImgView);
                    break;
                case DELAY:
                    recogAdapter.recogListening();
                    break;
                case FINISH:
                    exeButtonAnim.exeEnd(exeImgView, exeRoundImgView);

                    break;
            }
        }
    };

    public void setCurrentData() {

        /* 현재 날짜 및 시간을 받아와서 TextView에 표시합니다. */
        SimpleDateFormat formatter = new SimpleDateFormat("yy년 MM월 dd일 HH:mm a", Locale.KOREA);
        Date currentTime = new Date();
        String dDate = formatter.format(currentTime);

        daysText.setText(dDate);

    }

    public boolean saveRecognition(String s) {
        if (s.contains("저장") || s.contains("그래") || s.contains("좋아") || s.contains("맘대로")) {
            return true;
        } else {
            return false;
        }
    }

    public void resultIntent(int n) {

        if (n == 1) {
            Intent reIntent = new Intent();
            setResult(RESULT_OK, reIntent);
            finish();

        } else {
            Intent reIntent = new Intent();
            setResult(RESULT_CANCELED, reIntent);
            finish();

        }
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}
