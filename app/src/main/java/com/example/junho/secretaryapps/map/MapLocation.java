package com.example.junho.secretaryapps.map;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.junho.secretaryapps.TTSSpeech;
import com.example.junho.secretaryapps.permission.PermissionChecker;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class MapLocation extends Service implements LocationListener{
    Context context;
    Activity activity;
    MapView mapView;
    Location currentLocation, destLocation;
    TTSSpeech ttsSpeech;
    MapCircle circle;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false, isNetworkEnabled = false, isGetLocation = false;
    double lon, lat, totalDistance, alarmRingDistance, destLatitude, destLongitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10;
    Handler serviceHandler;

    public MapLocation(Handler hanlder) {
        this.serviceHandler = hanlder;
    }

    public MapLocation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        setTTSSpeech();
        getLocation();
    }

    /* 출발지 위도값 반환 */
    public double getLatitude() {
        if (currentLocation != null) {
            lat = currentLocation.getLatitude();
        }
        return lat;
    }

    /* 출발지 경도값 반환 */
    public double getLongitude() {
        if (currentLocation != null) {
            lon = currentLocation.getLongitude();
        }
        return lon;
    }

    /* 목적지 위도값 반환 */
    public double getDestLatitude() {
        double latitude = 0;
        if (destLocation != null) {
            latitude = destLocation.getLatitude();
        }
        return latitude;
    }

    /* 목적지 경도값 반환 */
    public double getDestLongitude() {
        double longitude = 0;
        if (destLocation != null) {
            longitude = destLocation.getLongitude();
        }
        return longitude;
    }

    /* 목적지 Location 객체와 좌표를 초기화 합니다. */
    public void setDestLocation(Location destination) {
        this.destLocation = destination;
        this.destLatitude = destLocation.getLatitude();
        this.destLongitude = destLocation.getLongitude();
    }

    /* 다음 MapView를 초기화 합니다. */
    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public void setTTSSpeech() {
        ttsSpeech = new TTSSpeech(context);
    }

    /* 사용자가 입력한 알람 거리를 초기화합니다.*/
    public void setAlarmDistance(Double alarmDistance) {
        this.alarmRingDistance = alarmDistance;
    }

    /* 사용자가 입력한 알람 거리를 반환합니다.*/
    public double getAlarmDistance() {
        return alarmRingDistance;
    }

    /* 현 위치부터 도착지까지의 거리를 반환합니다. */
    public double getTotalDistance() {
        this.totalDistance = currentLocation.distanceTo(destLocation);
        return totalDistance;
    }

    public Location getLocation() {
        PermissionChecker permissionChecker = new PermissionChecker(context, activity);

        if (permissionChecker.checkLocationPermission()) {
        }

        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            //GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled) {
                //네트워크와 GPS사용 불가능시 호출되는 부분
            } else {

                this.isGetLocation = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

                        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currentLocation != null) {
                            lat = currentLocation.getLatitude();
                            lon = currentLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (currentLocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (currentLocation != null) {
                                lat = currentLocation.getLatitude();
                                lon = currentLocation.getLongitude();
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

    /* GPS 종료 */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            ttsSpeech.ttsStop();
            ttsSpeech.ttsClear();
        }
    }

    /* GPS & WIFI의 정보가 켜져있는지 확인합니다. */
    public boolean isGetLocation() {
        return this.isGetLocation;
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

    /* onLocationChanged Listener 부분*/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        try {
            double totalDistance = getTotalDistance();  //현재 위치
            double alarmDistance = getAlarmDistance();  //알람이 울릴 위치
            double remainingDistance = totalDistance - alarmDistance; // 남은거리 = 현위치 - 알람이 울릴 위치

            //실시간 위치 circle표시
            if(circle != null) {
                mapView.removeCircle(circle);
            }
            circle = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(currentLatitude, currentLongitude), // center
                    10, // radius
                    Color.argb(180 , 0, 99, 99), // strokeColor
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
                serviceHandler.sendEmptyMessage(0);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord
                (currentLatitude, currentLongitude), 1, true);
    }

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