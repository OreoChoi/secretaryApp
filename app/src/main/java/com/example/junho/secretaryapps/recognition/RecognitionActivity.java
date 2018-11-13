package com.example.junho.secretaryapps.recognition;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junho.secretaryapps.ExeButtonAnim;
import com.example.junho.secretaryapps.MemoDB;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.calculator.CalculatorActivity;
import com.example.junho.secretaryapps.interact.TTSSpeech;
import com.example.junho.secretaryapps.memo.MemoActivity;
import com.example.junho.secretaryapps.memo.MemoAdapter;
import com.example.junho.secretaryapps.memo.MemoItem;

import java.util.ArrayList;
import java.util.List;

public class RecognitionActivity extends AppCompatActivity {
    public static final int MEMO_REQUESTCODE = 1001;
    private final int READY = 0, DELAY = 1, FINISH = 2;
    TextView recognitionTxtView;
    ImageView exeImgView;
    ImageView exeRoundImgView;
    ListView memoList;
    MemoAdapter memoAdapter;
    Intent functionIntent;
    ExeButtonAnim exeButtonAnim;
    RecogAdapter recogAdapter;
    TTSSpeech ttsSpeech;
    MemoDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        exeImgView = (ImageView) findViewById(R.id.exeImgView);
        exeRoundImgView = (ImageView) findViewById(R.id.exeRoundImgView);
        recognitionTxtView = (TextView) findViewById(R.id.recognitionTxtView);
        exeButtonAnim = new ExeButtonAnim(this);
        recogAdapter = new RecogAdapter(this);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //액티비티가 Destroy 될때 tts객체를 해제합니다.
        //tts를 싱글톤으로 교체하면 삭제가능합니다.

        ttsSpeech.ttsClear();
    }

    //음성인식 리스너
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
            recognitionTxtView.setText("다시 말씀해 주세요");
            handler.sendEmptyMessageDelayed(DELAY, 1000);
        }

        @Override
        public void onResults(Bundle bundle) {

            String key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            recognitionTxtView.setText(rs[0]);
            //실제 음성 명령을 처리하는 함수입니다.
            functionCall(recognitionTxtView.getText().toString());
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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

    //음성인식 처리 메소드
    public void functionCall(String str) {

        if (str.contains("계산기")) {
            functionIntent = new Intent(this, CalculatorActivity.class);
            startActivity(functionIntent);

            //메모 확인 패턴 다시 만들기
        } else if (str.contains("메모") && str.contains("보여 줘") || str.contains("확인") || str.contains("표시")) {
            memoList = (ListView) findViewById(R.id.listView);
            memoAdapter = new MemoAdapter(getApplicationContext());

            try {
                int lastIndex;

                db = new MemoDB(RecognitionActivity.this);
                db.dbOpen();

                lastIndex = db.lastIndex();

                for (int i = lastIndex; i >= 1; i--) {
                    Cursor cursor = db.listCursor("select * from memo where memoIndex = " + i);

                    if (cursor.moveToFirst()) {
                        String memoTitle = cursor.getString(4);
                        String memoDate = replaceDate(cursor.getString(2));
                        String memoContent = cursor.getString(3);
                        memoAdapter.addItem(new MemoItem(memoTitle, memoDate, memoContent));
                    } else {

                    }
                    cursor.close();
                }
                db.dbClose();

            } catch (SQLException e) {

                Log.d("STRING", "SQL ERROR");

            }

            memoList.setAdapter(memoAdapter);

            memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long ld) {

                    final MemoItem item = (MemoItem) memoAdapter.getItem(position);
                    final List<String> ListItems = new ArrayList<>();

                    ListItems.add("복사");
                    ListItems.add("수정");
                    ListItems.add("삭제");

                    final CharSequence[] cs = ListItems.toArray(new String[ListItems.size()]);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecognitionActivity.this);

                    builder.setTitle("메모 [ 제목: " + item.getTitle() + " ]");
                    builder.setItems(cs, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String selectText = cs[i].toString();

                            if (selectText.equals("복사")) {
                                clipBoard(RecognitionActivity.this, "제목 : " + item.getTitle() + "\n날짜 : "
                                        + item.getDate() + "\n내용 : " + item.getContent());
                            } else if (selectText.equals("수정")) {

                            } else if (selectText.equals("삭제")) {
                                db.dbOpen();
                                db.memoDelect(db.lastIndex()-position);
                                db.dbClose();
                            }
                        }
                    });

                    builder.setNegativeButton("나가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    builder.show();
                }
            });

        } else if (str.contains("메모")) {
            functionIntent = new Intent(this, MemoActivity.class);
            startActivityForResult(functionIntent, MEMO_REQUESTCODE);

        } else if (str.contains("길찾기")) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            toast("메모가 정상적으로 저장 되었습니다.");
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == MEMO_REQUESTCODE) {
                toast("메모를 취소하셨습니다.");
            }
        }
    }

    public void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public String replaceDate(String date) {
        String s = date;
        s = s.replace("년 ", "/");
        s = s.replace("월 ", "/");
        s = s.replace("일", "");

        return s;
    }

    public void clipBoard(Context context, String s) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", s);
        clipboardManager.setPrimaryClip(clipData);
    }

}