package com.example.junho.secretaryapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PermissionActivity extends AppCompatActivity {

    PermissionAnimation secAnim = new PermissionAnimation();
    LinearLayout locationTxtLayout,storageTxtLayout,audioTxtLayout;
    ImageButton imageButton, imageButton2, imageButton3;
    Button allowButton;
    PermissionChecker perChecker = new PermissionChecker();
    Intent resIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        locationTxtLayout = (LinearLayout) findViewById(R.id.locationTxtLayout);
        storageTxtLayout = (LinearLayout) findViewById(R.id.storageTxtLayout);
        audioTxtLayout = (LinearLayout) findViewById(R.id.audioTxtLayout);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        allowButton = (Button) findViewById(R.id.allowButton);

        allowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                boolean checkResult = perChecker.checkPermission(getApplicationContext(),PermissionActivity.this);
                if(checkResult) {
                    resIntent.putExtra("result",true);
                    setResult(RESULT_OK, resIntent);
                    finish();
                }else{
                    resIntent.putExtra("result",false);
                    setResult(RESULT_OK,resIntent);
                    finish();
                }
            }
        });
    }

    public void visibleLocationTxt(){
        secAnim.setVisibleView((View)locationTxtLayout);
    }
    public void visibleStorageTxt(){
        secAnim.setVisibleView((View)storageTxtLayout);
    }
    public void visibleAudioTxt(){
        secAnim.setVisibleView((View)audioTxtLayout);
    }

}
