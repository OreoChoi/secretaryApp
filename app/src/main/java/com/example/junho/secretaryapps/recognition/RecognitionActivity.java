package com.example.junho.secretaryapps.recognition;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junho.secretaryapps.ExeButtonAnim;
import com.example.junho.secretaryapps.MemoDB;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.TTSSpeech;
import com.example.junho.secretaryapps.calculator.TouchCalculatorActivity;
import com.example.junho.secretaryapps.map.MapActivity;
import com.example.junho.secretaryapps.memo.MemoActivity;
import com.example.junho.secretaryapps.memo.MemoAdapter;
import com.example.junho.secretaryapps.memo.MemoItem;

import static com.example.junho.secretaryapps.MainActivity.STT_MODE_SWITCH;
import static com.example.junho.secretaryapps.MainActivity.TOUCH_MODE_SWITCH;

public class RecognitionActivity extends AppCompatActivity {
    public static final int MEMO_REQUEST_CODE = 1001, CALCULATE_REQUEST_CODE = 1002, MAP_REQUEST_CODE = 1003;
    public static final int READY = 0, DELAY = 1, FINISH = 2, CALCULATOR_RESULT = 3, INPUT_ERROR = 4, RECOGNITION = 5, SET_TEXT = 6;
    public static final int EMPTY = 0, SUM = 1, SUBTRACT = 2, DIVISION = 3, MULTIPLY = 4,
    MEMO_LIST = 5, MEMO = 6, MAP = 7, CALCULATOR = 8, MODE_CHANGE = 9, MODE_RETURN = 10;

    TextView recognitionTxtView, titleTxtView;
    ImageView exeImgView, exeRoundImgView;
    ListView memoList;
    MemoAdapter memoAdapter;
    MemoDB db;
    Intent answerIntent;
    ExeButtonAnim exeButtonAnim;
    RecogAdapter recogAdapter;
    TTSSpeech ttsSpeech;
    RecogCalculator recogCalculator;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        exeImgView = (ImageView) findViewById(R.id.exeImgView);
        exeRoundImgView = (ImageView) findViewById(R.id.exeRoundImgView);
        recognitionTxtView = (TextView) findViewById(R.id.recognitionTxtView);
        titleTxtView = (TextView) findViewById(R.id.titleTxtView);
        exeButtonAnim = new ExeButtonAnim(this);
        recogAdapter = new RecogAdapter(this, handler);
        memoList = (ListView) findViewById(R.id.listView);
        memoAdapter = new MemoAdapter(getApplicationContext());
        recogCalculator = new RecogCalculator();
        db = new MemoDB(RecognitionActivity.this);

        /* TTS 첫 실행을 위한 임시 스레드*/
        ttsSpeech = new TTSSpeech(this);
        Thread ttsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    ttsSpeech.speech(recognitionTxtView);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ttsThread.start();

        /* 리스트뷰 클릭 리스너 */
        memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long ld) {
                MemoItem item = (MemoItem) memoAdapter.getItem(position);
                int memoIndex = item.getMemoIndex();
                String location = item.getCurrentLocation();
                String date = item.getDate();
                String content = item.getContent();
                String title = item.getTitle();
                int backColor = item.getBackColor();

                answerIntent = new Intent(RecognitionActivity.this, MemoActivity.class);
                answerIntent.putExtra("memoIndex", memoIndex);
                answerIntent.putExtra("location", location);
                answerIntent.putExtra("date", date);
                answerIntent.putExtra("content", content);
                answerIntent.putExtra("title", title);
                answerIntent.putExtra("backColor", backColor);

                startActivityForResult(answerIntent, MEMO_REQUEST_CODE);
            }
        });

        /* 실행 이미지뷰 클릭 시 */
        exeImgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(READY);
                if(ttsSpeech == null) {
                    ttsSpeech = new TTSSpeech(RecognitionActivity.this);
                }
            }
        });

        /* 실행 중 이미지뷰 클릭 시 */
        exeRoundImgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                handler.sendEmptyMessage(FINISH);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            ttsSpeech.ttsClear();
            recogAdapter.recogStopListen();
            recogAdapter.recogDestory();
        }catch(NullPointerException e){
            recogAdapter.recogStopListen();
            recogAdapter.recogDestory();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
            ttsSpeech.ttsClear();
            ttsSpeech = null;
            recogAdapter.recogStopListen();
            recogAdapter.recogDestory();
    }

    /* RecognitionActivity UI Handler*/
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            String s = "";
            switch (msg.what) {
                case READY:
                    exeButtonAnim.exeStart(exeImgView, exeRoundImgView);
                    recogAdapter.recogStart();
                    break;
                case DELAY:
                    recognitionTxtView.setText("다시 말씀해 주세요");
                    recogAdapter.recogListening();
                    break;
                case FINISH:
                    exeButtonAnim.exeEnd(exeImgView, exeRoundImgView);
                    recogAdapter.recogDestory();
                    break;
                case CALCULATOR_RESULT:
                    s = (String) msg.obj;
                    recognitionTxtView.setText(s + "");
                    break;
                case INPUT_ERROR:
                    s = (String) msg.obj;
                    recognitionTxtView.setText("말씀하신 문장입니다\n[" + s + "]\n천천히 발음 해주세요");
                    break;
                case RECOGNITION:
                    s = (String) msg.obj;
                    functionCall(s);
                    recognitionTxtView.setText(s);
                    break;
                case SET_TEXT:
                    s = (String) msg.obj;
                    functionCall(s);
                    recognitionTxtView.setText(s);
                    break;
            }
        }
    };

    /* 음성인식 처리 메소드 */
    public void functionCall(String recogText) {
        Separation separation = new Separation();
        SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
        int mode = pref.getInt("mode", 0);
        switch (separation.wordSeparation(recogText)) {
            case EMPTY:
                break;
            case SUM:
                calculateHandling(recogText, SUM);
                break;
            case SUBTRACT:
                calculateHandling(recogText, SUBTRACT);
                break;
            case DIVISION:
                calculateHandling(recogText, DIVISION);
                break;
            case MULTIPLY:
                calculateHandling(recogText, MULTIPLY);
                break;
            case MEMO_LIST:
                memoAdapter.init();
                memoListView();
                memoList.setAdapter(memoAdapter);
                break;
            case MEMO:
                answerIntent = new Intent(this, MemoActivity.class);
                startActivityForResult(answerIntent, MEMO_REQUEST_CODE);
                break;
            case MAP:
                answerIntent = new Intent(this, MapActivity.class);
                startActivityForResult(answerIntent, MAP_REQUEST_CODE);
                break;
            case CALCULATOR:
                answerIntent = new Intent(this, TouchCalculatorActivity.class);
                startActivityForResult(answerIntent, CALCULATE_REQUEST_CODE);
                break;
            case MODE_CHANGE:
                if (mode == STT_MODE_SWITCH) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("mode", TOUCH_MODE_SWITCH);
                    editor.commit();
                    ttsSpeech.speech("터치모드로 변경되었습니다.");
                } else if (mode == TOUCH_MODE_SWITCH) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("mode", STT_MODE_SWITCH);
                    editor.commit();
                    ttsSpeech.speech("음성모드로 변경되었습니다.");
                }
                break;
            case MODE_RETURN:
                Message msg = Message.obtain();
                if (mode == STT_MODE_SWITCH) {
                    msg.obj = "현재 모드는 음성모드 입니다.";
                    msg.what = SET_TEXT;
                    handler.sendMessage(msg);
                    ttsSpeech.speech("현재 모드는 음성모드 입니다.");
                } else if (mode == TOUCH_MODE_SWITCH) {
                    msg.obj = "현재 모드는 터치모드 입니다.";
                    msg.what = SET_TEXT;
                    handler.sendMessage(msg);
                    ttsSpeech.speech("현재 모드는 터치모드 입니다.");
                }
                break;
        }
    }

    /* 명시적 인텐트 응답 메소드 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case MEMO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    memoAdapter.init();
                    memoListView();
                    memoAdapter.notifyDataSetChanged();
                    memoList.setAdapter(memoAdapter);
                } else if (resultCode == RESULT_CANCELED) {
                    toast("메모를 취소하셨습니다.");
                }
                break;
            case CALCULATE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    toast("계산기가 정상적으로 실행 되었습니다.");
                } else if (resultCode == RESULT_CANCELED) {
                    toast("계산을 취소하셨습니다.");
                }
                break;
            case MAP_REQUEST_CODE:
                break;
            default:
                toast("잘못된 실행입니다.");
                break;
        }
    }

    /* Recognition 계산 Handler 전달 메소드 */
    public void calculateHandling(String recogText, int operator) {
        Message msg = Message.obtain();
        msg.what = CALCULATOR_RESULT;

        try {
            msg.obj = recogCalculator.calculate(recogText, operator);
            handler.sendMessage(msg);
        } catch (NumberFormatException e) {
            msg.obj = recogText;
            msg.what = INPUT_ERROR;
            handler.sendMessage(msg);
        }
    }

    /* 메모 리스트뷰 셋팅 메소드 */
    public void memoListView() {
        try {
            int lastIndex;
            db.dbOpen();
            lastIndex = db.lastIndex();

            for (int i = lastIndex; i >= 1; i--) {
                Cursor cursor = db.listCursor("select * from memo where memoIndex = " + i);

                if (cursor.moveToFirst()) {
                    int memoIndex = cursor.getInt(0);
                    String memoLocation = cursor.getString(1);
                    String memoDate = cursor.getString(2);
                    String memoContent = cursor.getString(3);
                    String memoTitle = cursor.getString(4);
                    int memoColor = cursor.getInt(5);

                    memoAdapter.addItem(new MemoItem(memoIndex, memoLocation, memoDate, memoContent, memoTitle, memoColor));
                } else {

                }
                cursor.close();
            }
            db.dbClose();
        } catch (SQLException e) {
            Log.d("STRING", "SQL ERROR");
        } catch (CursorIndexOutOfBoundsException e) {
            recognitionTxtView.setText("표시할 메모가 없습니다.");
        }
    }

    /* 토스트 메소드 */
    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}