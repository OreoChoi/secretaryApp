package com.example.junho.secretaryapps.map;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junho.secretaryapps.ApplicationClass;
import com.example.junho.secretaryapps.R;
import com.example.junho.secretaryapps.TTSClass;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

public class MapActivity extends AppCompatActivity {
    MapPOIItem marker;
    MyService gps;
    EditText destEditText, distanceEditText;
    ImageButton clearButton;
    TTSClass ttsSpeech;
    MapView mapView;
    AddressSearch addressSearch;
    ViewGroup mapViewContainer;
    LinearLayout mapContainer;
    Location startLocation, destLocation;
    InputMethodManager imm;
    Intent serviceIntent;
    String distance = "";
    ApplicationClass applicationClass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        destEditText = (EditText) findViewById(R.id.destEditText);
        distanceEditText = (EditText) findViewById(R.id.distanceEditText);
        clearButton = (ImageButton) findViewById(R.id.clearButton);
        addressSearch = new AddressSearch(this);
        applicationClass = (ApplicationClass) getApplicationContext();
        ttsSpeech = new TTSClass(applicationClass);
        startLocation = new Location("start");
        destLocation = new Location("end");
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mapContainer = (LinearLayout) findViewById(R.id.mapView);
        gps = new MyService();

        /* 다음 지도 셋팅 */
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);

        /* 알람 ForeGroundService 시작 */
        serviceIntent = new Intent(MapActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            getApplicationContext().startForegroundService(serviceIntent);
        } else {
            getApplicationContext().startService(serviceIntent);
        }

        /* 알람 서비스 초기화*/
        gps.setMyService(MapActivity.this, MapActivity.this);
        gps.setMapView(mapView);
        setMarker(gps.getLatitude(), gps.getLongitude());

        /* GPS 사용유무 */
        if (gps.isGetLocation()) {
            final double latitude = gps.getLatitude();
            final double longitude = gps.getLongitude();
            startLocation.setLatitude(latitude);
            startLocation.setLongitude(longitude);

            gps.currentMapCenterPrint(latitude, longitude);
        }

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destEditText.setText("");
                distanceEditText.setText("");
            }
        });

        /* 목적지 입력 EditText 검색 시 리스너 */
        destEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        destEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    final String returnAddress = addressSearch.getAddress(destEditText.getText().toString());
                    double latitude = addressSearch.getLatitude();
                    double longitude = addressSearch.getLongitude();

                    destLocation.setLatitude(latitude);
                    destLocation.setLongitude(longitude);

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);

                    alertDialog.setMessage("[" + returnAddress + "]" + " \n 이곳이 도착지가 맞습니까?");
                    setDestinationMarker(latitude, longitude);
                    gps.currentMapCenterPrint(latitude, longitude);

                    alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            destEditText.setText(returnAddress);
                            imm.hideSoftInputFromWindow(destEditText.getWindowToken(), 0);
                            distanceEditText.requestFocus();
                            imm.toggleSoftInputFromWindow(distanceEditText.getWindowToken(), 0, 0);
                        }
                    });

                    alertDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Toast.makeText(MapActivity.this, "입력을 취소하셨습니다.", Toast.LENGTH_SHORT).show();
                            gps.currentMapCenterPrint(gps.getLatitude(), gps.getLongitude());
                            imm.hideSoftInputFromWindow(destEditText.getWindowToken(), 0);
                        }
                    });
                    alertDialog.show();

                }
                return false;
            }
        });

        /* 알람 거리입력 EditText 검색 시 리스너 */
        distanceEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        distanceEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (gps.isOnGPS()) {
                        final double alarmDistance;

                        if (distanceEditText.getText().toString().contains("m")) {
                            distance = distanceEditText.getText().toString();
                            distance = distance.substring(0, distance.indexOf("m"));
                            alarmDistance = Double.parseDouble(distance);
                        } else {
                            distance = distanceEditText.getText().toString();
                            alarmDistance = Double.parseDouble(distance);
                        }

                        if (alarmDistance > 10000) {
                            Toast.makeText(MapActivity.this, "10km를 초과할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            imm.hideSoftInputFromWindow(distanceEditText.getWindowToken(), 0);
                        } else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapActivity.this);

                            alertDialog.setTitle("알람 셋팅");
                            alertDialog.setMessage("알람을 셋팅하시겠습니까?");

                            alertDialog.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            distanceEditText.setText(distance + "m");
                                            gps.setDestLocation(destLocation);
                                            gps.setAlarmDistance(alarmDistance);
                                            imm.hideSoftInputFromWindow(distanceEditText.getWindowToken(), 0);

                                            MapPolyline polyline = new MapPolyline();

                                            polyline.setLineColor(Color.argb(0, 255, 255, 255));
                                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(gps.getLatitude(), gps.getLongitude()));
                                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(gps.getDestLatitude(), gps.getDestLongitude()));
                                            mapView.addPolyline(polyline);

                                            int padding = 300;
                                            MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                                            mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
                                        }
                                    });

                            alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    imm.hideSoftInputFromWindow(distanceEditText.getWindowToken(), 0);
                                }
                            });
                            alertDialog.show();
                        }
                    } else {
                        gps.showSettingsAlert();
                    }
                }
                return false;
            }
        });
    }

    /* 지도에 현 위치 Marker setting */
    private void setMarker(double latitude, double longitude) {
        marker = new MapPOIItem();
        marker.setItemName("현 위치");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(marker);
    }

    /* 지도에 도착지 Marker setting */
    private void setDestinationMarker(double latitude, double longitude) {
        MapPOIItem destMarker = new MapPOIItem();
        destMarker.setItemName("도착지");
        destMarker.setTag(0);
        destMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        destMarker.setMarkerType(MapPOIItem.MarkerType.RedPin);
        destMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(destMarker);
    }
}
