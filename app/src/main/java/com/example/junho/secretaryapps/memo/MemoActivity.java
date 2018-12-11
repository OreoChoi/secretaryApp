package com.example.junho.secretaryapps.memo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.junho.secretaryapps.ApplicationClass;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.TTSClass;
import com.example.junho.secretaryapps.map.AddressSearch;
import com.example.junho.secretaryapps.map.MapLocation;
import com.example.junho.secretaryapps.recognition.RecogAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.junho.secretaryapps.MainActivity.STT_MODE_SWITCH;
import static com.example.junho.secretaryapps.MainActivity.TOUCH_MODE_SWITCH;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.DELAY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.FINISH;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.READY;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.RECOGNITION;
import static com.example.junho.secretaryapps.recognition.RecognitionActivity.SET_TEXT;

public class MemoActivity extends AppCompatActivity {
    /* Variable 선언 */
    private final int SPEECH = 99;
    public static int flag = 0;
    int memoIndex = 0;
    String returnAddress;

    /* View 선언 */
    EditText titleText, contentText;
    TextView daysText, currentLocationText;
    LinearLayout memoLayout;
    ImageView white_circle, black_circle, pale_green_circle, yellow_circle, pale_yellow_circle,
            apricot_circle, pink_circle, purple_circle, pale_blue_circle, cyan_circle, gray_circle, orange_circle;
    android.support.v7.widget.Toolbar toolbar;

    /* Object 선언 */
    BackColor backColor;
    RecogAdapter recogAdapter;
    Intent reIntent, memoSetter;
    ApplicationClass applicationClass;
    TTSClass ttsSpeech;
    MemoAdapter memoAdapter;
    MapLocation gps;
    AddressSearch addressSearch;
    MemoDB memoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        /* View 초기화 */
        titleText = (EditText) findViewById(R.id.titleText);
        contentText = (EditText) findViewById(R.id.contentText);
        daysText = (TextView) findViewById(R.id.daysText);
        currentLocationText = (TextView) findViewById(R.id.currentLocationText);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
        memoLayout = (LinearLayout) findViewById(R.id.memo_layout);

        /* Object 초기화 */
        applicationClass = (ApplicationClass) getApplicationContext();
        reIntent = getIntent();
        recogAdapter = new RecogAdapter(applicationClass, handler);
        memoAdapter = new MemoAdapter(getApplicationContext());
        addressSearch = new AddressSearch(this);
        ttsSpeech = new TTSClass(applicationClass);
        memoDB = new MemoDB(applicationClass);
        backColor = new BackColor(   );

        /* Toolbar 셋팅 */
        toolbar.getNavigationIcon();
        toolbar.setTitle("메모");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(android.R.drawable.ic_menu_save);

        SharedPreferences pref = getSharedPreferences("ModeSwitch", Activity.MODE_PRIVATE);
        int mode = pref.getInt("mode", 0);

        memoSetter = getIntent();

        if (memoSetter.hasExtra("location")) {
            memoIndex = memoSetter.getExtras().getInt("memoIndex");
            currentLocationText.setText(memoSetter.getExtras().getString("location"));
            daysText.setText(memoSetter.getExtras().getString("date"));
            contentText.setText(memoSetter.getExtras().getString("content"));
            titleText.setText(memoSetter.getExtras().getString("title"));
            memoLayout.setBackgroundColor(memoSetter.getExtras().getInt("backColor"));
        } else {
            gps = new MapLocation(this, this);
            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                returnAddress = addressSearch.getAddress(latitude, longitude);
                currentLocationText.setText(returnAddress);
                gps.stopUsingGPS();
            } else {
                currentLocationText.setText("주소 호출에 실패했습니다.");
            }

            setCurrentDate();

            if (mode == STT_MODE_SWITCH) {
                flag = 1;
                handler.sendEmptyMessageDelayed(SPEECH, 1000);
            } else if (mode == TOUCH_MODE_SWITCH) {

            }
        }
    }

    /* Toolbar를 inflate합니다. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.memo_menu, menu);
        return true;
    }

    /* Back 클릭 시 호출 */
    @Override
    public void onBackPressed() {
        resultIntent(false);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        memoDB.dbClose();
    }

    /* Toolbar의 기능 정의 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (memoIndex > 0) {
                    updateMemo(memoIndex);
                } else {
                    saveMemo();
                }
                resultIntent(true);
                finish();
                return true;

            case R.id.memo_delete:
                AlertDialog.Builder delBuilder = new AlertDialog.Builder(MemoActivity.this);

                delBuilder.setTitle("정말 삭제하시겠습니까?");
                delBuilder.setNeutralButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (memoIndex > 0) {
                            memoDB.memoDelect(memoIndex);
                            resultIntent(true);
                            finish();
                        } else {
                            applicationClass.toast("아직 저장된 메모 아닙니다.");
                        }
                    }
                });
                delBuilder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                delBuilder.show();

                return true;
            case R.id.memo_background_change:
                final View innerView = getLayoutInflater().inflate(R.layout.memo_background, null);

                AlertDialog.Builder backBuilder = new AlertDialog.Builder(MemoActivity.this);
                backBuilder.setTitle("속지 변경");
                backBuilder.setView(innerView);

                innerView.setPadding(80, 30, 0, 0);

                white_circle = (ImageView) innerView.findViewById(R.id.white_circle);
                black_circle = (ImageView) innerView.findViewById(R.id.black_circle);
                pale_green_circle = (ImageView) innerView.findViewById(R.id.pale_green_circle);
                pale_blue_circle = (ImageView) innerView.findViewById(R.id.pale_blue_circle);
                pale_yellow_circle = (ImageView) innerView.findViewById(R.id.pale_yellow_circle);
                yellow_circle = (ImageView) innerView.findViewById(R.id.yellow_circle);
                gray_circle = (ImageView) innerView.findViewById(R.id.gray_circle);
                cyan_circle = (ImageView) innerView.findViewById(R.id.cyan_circle);
                orange_circle = (ImageView) innerView.findViewById(R.id.orange_circle);
                pink_circle = (ImageView) innerView.findViewById(R.id.pink_circle);
                purple_circle = (ImageView) innerView.findViewById(R.id.purple_circle);
                apricot_circle = (ImageView) innerView.findViewById(R.id.apricot_circle);

                ImageView.OnClickListener onClickListener = new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.white_circle:
                                memoLayout.setBackgroundColor(backColor.getWHITE_INT());
                                break;
                            case R.id.black_circle:
                                int white = backColor.getWHITE_INT();
                                memoLayout.setBackgroundColor(backColor.getBLACK_INT());
                                currentLocationText.setTextColor(white);
                                daysText.setTextColor(white);
                                contentText.setTextColor(white);
                                contentText.setHintTextColor(white);
                                titleText.setTextColor(white);
                                titleText.setHintTextColor(white);
                                break;
                            case R.id.cyan_circle:
                                memoLayout.setBackgroundColor(backColor.getCYAN_INT());
                                break;
                            case R.id.pale_blue_circle:
                                memoLayout.setBackgroundColor(backColor.getPALE_BLUE_INT());
                                break;
                            case R.id.pale_green_circle:
                                memoLayout.setBackgroundColor(backColor.getPALE_GREEN_INT());
                                break;
                            case R.id.pale_yellow_circle:
                                memoLayout.setBackgroundColor(backColor.getPALE_YELLOW_INT());
                                break;
                            case R.id.yellow_circle:
                                memoLayout.setBackgroundColor(backColor.getYELLOW_INT());
                                break;
                            case R.id.purple_circle:
                                memoLayout.setBackgroundColor(backColor.getPURPLE_INT());
                                break;
                            case R.id.gray_circle:
                                memoLayout.setBackgroundColor(backColor.getGRAY_INT());
                                break;
                            case R.id.apricot_circle:
                                memoLayout.setBackgroundColor(backColor.getAPRICOT_INT());
                                break;
                            case R.id.orange_circle:
                                memoLayout.setBackgroundColor(backColor.getORANGE_INT());
                                break;
                            case R.id.pink_circle:
                                memoLayout.setBackgroundColor(backColor.getPINK_INT());
                                break;
                        }
                    }
                };

                white_circle.setOnClickListener(onClickListener);
                black_circle.setOnClickListener(onClickListener);
                cyan_circle.setOnClickListener(onClickListener);
                apricot_circle.setOnClickListener(onClickListener);
                orange_circle.setOnClickListener(onClickListener);
                pale_green_circle.setOnClickListener(onClickListener);
                pale_blue_circle.setOnClickListener(onClickListener);
                pale_yellow_circle.setOnClickListener(onClickListener);
                yellow_circle.setOnClickListener(onClickListener);
                pink_circle.setOnClickListener(onClickListener);
                purple_circle.setOnClickListener(onClickListener);
                gray_circle.setOnClickListener(onClickListener);

                backBuilder.setNegativeButton("나가기",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                backBuilder.show();
                return true;
            case R.id.memo_lock:
                return true;
            case R.id.memo_shared:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    recogAdapter.recogStart();
                    break;
                case DELAY:
                    recogAdapter.recogListening();
                    break;
                case FINISH:
                    recogAdapter.recogDestory();
                    break;
                case RECOGNITION:
                    s="";
                    s = (String) msg.obj;
                    functionCall(s);
                    break;
                case SET_TEXT:
                    s = (String) msg.obj;
                    functionCall(s);
                    break;
                case SPEECH:
                    functionCall(s);
                    break;

            }
        }
    };

    /* 음성인식 처리 메소드 */
    public void functionCall(String recogText) {
        switch (flag) {
            case 1:
                ttsSpeech.speech("내용을 말씀해주세요");
                handler.sendEmptyMessageDelayed(READY, 1000);
                break;
            case 2:
                contentText.setText(recogText);
                handler.sendEmptyMessageDelayed(READY, 1000);
                break;
            case 3:
                titleText.setText(recogText);
                saveMemo();
                break;
        }
    }

    /* 현재 date를 setting합니다.*/
    public void setCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yy년 MM월 dd일 HH:mm a", Locale.KOREA);
        Date currentTime = new Date();
        String dDate = formatter.format(currentTime);

        daysText.setText(dDate);
    }

    /* 메모 저장 */
    public void saveMemo() {
        try {
            String c1 = currentLocationText.getText().toString();
            String c2 = daysText.getText().toString();
            String c3 = contentText.getText().toString();
            String c4 = titleText.getText().toString();

            ColorDrawable color = (ColorDrawable) memoLayout.getBackground();
            int c5 = color.getColor();
            memoDB.memoCreate();

            memoDB.memoInsert(c1, c2, c3, c4, c5);

            applicationClass.toast("저장중...");
            resultIntent(true);

        } catch (SQLException e) {
            resultIntent(false);
        }
    }

    /* 메모 업데이트 */
    public void updateMemo(int index) {
        try {
            String c1 = contentText.getText().toString();
            String c2 = titleText.getText().toString();
            ColorDrawable color = (ColorDrawable) memoLayout.getBackground();
            int c3 = color.getColor();

            memoDB.memoUpdate(index, c1, c2, c3);

            applicationClass.toast("저장중...");
            resultIntent(true);
        } catch (SQLException e) {
            resultIntent(false);
        }
    }

    public void resultIntent(Boolean b) {
        Intent reIntent = new Intent();
        if (b) {
            setResult(RESULT_OK, reIntent);
        } else {
            setResult(RESULT_CANCELED, reIntent);
        }
        finish();
    }

    /* clipBoard */
    public void clipBoard(Context context, String s) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", s);
        clipboardManager.setPrimaryClip(clipData);
    }
}