package com.example.junho.secretaryapps;

        import android.content.Intent;
        import android.os.Handler;
        import android.os.Message;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1001;
    TextView coverTxtView, reUseTxtView;
    ImageView rotationImageView;
    LinearLayout coverLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        coverTxtView = (TextView) findViewById(R.id.coverTxtView1);
        reUseTxtView = (TextView) findViewById(R.id.reUseTxtView1);
        rotationImageView = (ImageView) findViewById(R.id.rotationImageView);
        coverLayout = (LinearLayout) findViewById(R.id.coverLayout);
        Intent perIntent = new Intent(this,PermissionActivity.class);

        startActivityForResult(perIntent,PERMISSION_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){
            if(data.getBooleanExtra("result",true)) {
                ResultAnimThread animThread = new ResultAnimThread(rotationImageView, coverTxtView, reUseTxtView, this, mainHandler);
                Thread mainThread = new Thread(animThread);

                mainThread.setDaemon(true);
                mainThread.start();
            }
        }
    }

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String setResult = (String) msg.obj;

            switch (msg.what) {
                case 0:
                    coverLayout.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    coverTxtView.setText(setResult);
                    break;
                case 2:
                    reUseTxtView.setText(setResult);
                    break;
            }
        }
    };
}
