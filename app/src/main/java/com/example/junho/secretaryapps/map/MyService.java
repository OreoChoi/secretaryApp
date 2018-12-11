package com.example.junho.secretaryapps.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.junho.secretaryapps.ApplicationClass;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.TTSClass;
import com.example.junho.secretaryapps.permission.PermissionChecker;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;

public class MyService extends Service implements LocationListener, Serializable {
    Context context;
    Activity activity;

    MapView mapView;
    Location currentLocation, destLocation;
    TTSClass ttsSpeech;
    MapCircle circle;
    ApplicationClass applicationClass;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false, isNetworkEnabled = false, isGetLocation = false;
    double startLongitude, startLatitude, alarmRingDistance, destLatitude, destLongitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;
    public static NotificationCompat.Builder builder;
    public static NotificationManager notificationManager;
    private static final String TAG = "MyService";

    public void setMyService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        getLocation();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForegroundService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /* Notification 생성 및 셋팅 */
    public void startForegroundService() {
        Intent notificationIntent = new Intent(this, MapActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "map_service_channel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Map Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setContentTitle("Secretary Apps")
                .setContentText("알람을 설정하지 않았습니다.")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.cyan_circle);

        startForeground(1, builder.build());
    }

    /* 출발지 위도값 반환 */
    public double getLatitude() {
        if (currentLocation != null) {
            startLatitude = currentLocation.getLatitude();
        }
        return startLatitude;
    }

    /* 출발지 경도값 반환 */
    public double getLongitude() {
        if (currentLocation != null) {
            startLongitude = currentLocation.getLongitude();
        }
        return startLongitude;
    }

    /* 목적지 위도값 반환 */
    public double getDestLatitude() {
        if (destLocation != null) {
            destLatitude = destLocation.getLatitude();
        }
        return destLatitude;
    }

    /* 목적지 경도값 반환 */
    public double getDestLongitude() {
        if (destLocation != null) {
            destLongitude = destLocation.getLongitude();
        }
        return destLongitude;
    }

    /* 목적지 Location */
    public void setDestLocation(Location destination) {
        this.destLocation = destination;
    }

    /* 다음 MapView */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    /* 알림 거리 */
    public void setAlarmDistance(Double alarmDistance) {
        this.alarmRingDistance = alarmDistance;
    }

    /* GPS & WIFI의 ON/OFF 확인*/
    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    /* 현재 위치 표시 */
    public void currentMapCenterPrint(double latitude, double longitude) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord
                (latitude, longitude), 1, true);
    }

    /* GPS 종료 */
    public void stopUsingGPS() {
        locationManager.removeUpdates(this);
    }

    /* GPS 설정 ON/OFF 확인 */
    public boolean isOnGPS() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isNetworkEnabled && !isGPSEnabled) {
            isGetLocation = false;
            return false;
        } else {
            isGetLocation = true;
            return true;
        }
    }

    /* GPS 정보를 가져오지 못했을때 설정 창 이동유무 목적의 alert창*/
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. " +
                "\n 설정창으로 가시겠습니까?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivities(new Intent[]{intent});
                    }
                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }

    /*Location 객체와 좌표를 설정합니다.*/
    public Location getLocation() {
        PermissionChecker permissionChecker = new PermissionChecker(context, activity);

        if (permissionChecker.checkLocationPermission()) {
        }

        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled) {
                showSettingsAlert();
            } else {
                this.isGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currentLocation != null) {
                            startLatitude = currentLocation.getLatitude();
                            startLongitude = currentLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (currentLocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (currentLocation != null) {
                                startLatitude = currentLocation.getLatitude();
                                startLongitude = currentLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentLocation;
    }

    /* onLocationChanged Listener 부분*/
    @Nullable
    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        try {
            applicationClass = (ApplicationClass) getApplicationContext();
            ttsSpeech = new TTSClass(applicationClass);
            currentLocation.setLatitude(currentLatitude);
            currentLocation.setLongitude(currentLongitude);

            double totalDistance = currentLocation.distanceTo(destLocation);  //현재 위치
            double remainingDistance = totalDistance - alarmRingDistance; // 남은거리 = 현위치 - 알람이 울릴 위치

            //실시간 위치 circle표시
            if (circle != null) {
                mapView.removeCircle(circle);
            }
            circle = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude), // center
                    10, // radius
                    Color.argb(180, 0, 99, 99), // strokeColor
                    Color.argb(180, 0, 99, 99) // fillColor
            );
            circle.setTag(1234);
            mapView.addCircle(circle);

            if (remainingDistance < 0) {
                ttsSpeech.speech("거리에 도달했습니다.");
            } else {
                String s = Double.toString(remainingDistance);
                s = s.substring(0, s.indexOf("."));
                ttsSpeech.speech("거리로 이동중입니다. 알람까지 남은 거리 : " + s + "m");

                Message msg = Message.obtain();
                msg.obj = s;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e + " : NullpointException");
        }
        currentMapCenterPrint(currentLatitude, currentLongitude);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    builder.setContentText("남은거리 : " + msg.obj + "m");
                    notificationManager.notify(1, builder.build());
                    break;
                case 1:
                    builder.setContentText("거리에 도달했습니다.");
                    notificationManager.notify(1, builder.build());
                    break;
            }
        }
    };

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
