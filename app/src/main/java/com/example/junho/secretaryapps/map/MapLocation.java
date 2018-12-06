package com.example.junho.secretaryapps.map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.example.junho.secretaryapps.permission.PermissionChecker;

import net.daum.mf.map.api.MapView;

public class MapLocation extends MyService {
    Context context;
    Activity activity;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 5;

    public MapLocation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        getLocation();
    }

    @Override
    public double getLatitude() {
        return super.getLatitude();
    }

    @Override
    public double getLongitude() {
        return super.getLongitude();
    }

    @Override
    public double getDestLatitude() {
        return super.getDestLatitude();
    }

    @Override
    public double getDestLongitude() {
        return super.getDestLongitude();
    }

    @Override
    public void setDestLocation(Location destination) {
        super.setDestLocation(destination);
    }

    @Override
    public void setMapView(MapView mapView) {
        super.setMapView(mapView);
    }

    @Override
    public void setAlarmDistance(Double alarmDistance) {
        super.setAlarmDistance(alarmDistance);
    }

    @Override
    public boolean isGetLocation() {
        return super.isGetLocation();
    }

    @Override
    public void currentMapCenterPrint(double latitude, double longitude) {
        super.currentMapCenterPrint(latitude, longitude);
    }

    @Override
    public void stopUsingGPS() {
        super.stopUsingGPS();
    }

    @Override
    public boolean isOnGPS() {
        return super.isOnGPS();
    }

    @Override
    public void showSettingsAlert() {
        super.showSettingsAlert();
    }

    @Override
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
}
