package com.example.junho.secretaryapps.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
    int permissionAudio, permissionStorage, permissionLocation, permissionInternet;
    Context context;
    Activity activity;

    public PermissionChecker(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    /* Location 권한만 검사합니다. */
    public boolean checkLocationPermission(){
        permissionLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionLocation == CHECKER){
            return true;
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)){
                toast("Location 권한이 없습니다. 앱 정보에서 직접 권한을 획득 해야합니다.");
            }else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            return false;
        }
    }

    /* 모든 권한을 검사합니다. */
    public boolean checkAllPermission() {
        permissionAudio = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
        permissionStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        permissionInternet = ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET);

        if (permissionAudio == CHECKER && permissionStorage == CHECKER &&
            permissionLocation == CHECKER && permissionInternet == CHECKER) {
            return true;
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) &&
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.INTERNET)) {

                toast("권한이 없습니다. 앱 정보에서 직접 권한을 획득 해야 합니다");
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET}, 1);
            }
            return false;
        }
    }

    /* 토스트 메소드 */
    public void toast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
