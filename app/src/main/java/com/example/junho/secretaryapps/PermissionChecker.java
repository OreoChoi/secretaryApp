package com.example.junho.secretaryapps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/*
 * PerChecker
 *
 * Permission을 User가 허용한지 확인합니다.
 *
 * User에게 Permission이 할당되지 않았다면 종료
 * User에게 Permission이 할당 되었다면 Secretary를 기동합니다.
 *
 *    PS.추후 이 기능은 가운데에 액티비티가 하나 더 추가될 예정입니다.
 *    앱이 권한을 가지고 있는지 검사하며 권한이 없는 경우 화면을 전환하지 않습니다.
 *    권한을 한눈에 볼수 요청하는 액티비티입니다.
 *
 * */

public class PermissionChecker {
    public static final int CHECKER = PackageManager.PERMISSION_GRANTED;

    public boolean checkPermission(Context c,Activity activity) {
        Context context = c.getApplicationContext();

        int permissionAudio = ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO);
        int permissionStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionAudio == CHECKER && permissionStorage == CHECKER && permissionLocation == CHECKER) {

            return true;

        } else {

            //Intent intent = new Intent(this,);

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(context,"권한이 없습니다. 앱 정보에서 직접 권한을 획득 해야 합니다",Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

            return false;

        }
    }
}
