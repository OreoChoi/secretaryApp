package com.example.junho.secretaryapps.map;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressSearch {
    double longitude;
    double latitude;
    Context context;
    String[] splitString;
    String returnAddress;
    Geocoder geocoder;
    List<Address> addressList;

    public AddressSearch(Context context) {
        this.context = context;
        setAddressSearch();
    }

    /* 본 객체의 변수를 초기화 */
    public void setAddressSearch() {
        this.returnAddress = null;
        this.geocoder = new Geocoder(context, Locale.KOREA);
        this.addressList = null;

    }

    /* 경도를 구합니다. */
    public double setLongitude(String s) {
        String temp = s.substring(s.indexOf("=") + 1);
        double longitude = Double.parseDouble(temp);

        return longitude;
    }

    /* 경도를 리턴합니다. */
    public double getLongitude() {
        return longitude;
    }

    /* 위도를 구합니다. */
    public double setLatitude(String s) {
        String temp = s.substring(s.indexOf("=") + 1);
        double latitude = Double.parseDouble(temp);

        return latitude;
    }

    /* 위도를 리턴합니다. */
    public double getLatitude() {
        return latitude;
    }

    /* 입력된 단어로 주소를 검색합니다. */
    public String getAddress(String s) {

        try {
            addressList = geocoder.getFromLocationName(s, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            splitString = addressList.get(0).toString().split(",");
            returnAddress = splitString[0].substring(splitString[0].indexOf("\"") + 1, splitString[0].length() - 2);
            latitude = setLatitude(splitString[10]);
            longitude = setLongitude(splitString[12]);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return returnAddress;
    }

    /* 좌표로 주소를 검색합니다. */
    public String getAddress(double latitude, double longitude) {
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            splitString = addressList.get(0).toString().split(",");
            returnAddress = splitString[0].substring(splitString[0].indexOf("\"") + 1, splitString[0].length() - 2);
        } catch (NullPointerException e) {
            Toast.makeText(context,"주소를 ",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch(IndexOutOfBoundsException e){
            Toast.makeText(context,"주소를 ",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch(IOException e) {
            Toast.makeText(context,"주소를 ",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return returnAddress;
    }
}