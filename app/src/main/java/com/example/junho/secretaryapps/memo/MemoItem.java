package com.example.junho.secretaryapps.memo;

public class MemoItem {
    String currentLocation;
    String date;
    String title;
    String content;
    int memoIndex;
    int backColor;

    public MemoItem(int memoIndex, String currentLocation, String date, String content, String title, int backColor) {
        this.memoIndex = memoIndex;
        this.currentLocation = currentLocation;
        this.date = date;
        this.content = content;
        this.title = title;
        this.backColor = backColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMemoIndex() {
        return memoIndex;
    }

    public void setMemoIndex(int backColor) {
        this.memoIndex = memoIndex;
    }

    public void setBackColor(int backColor){
        this.backColor = backColor;
    }

    public int getBackColor(){
        return backColor;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getCurrentLocation(){
        return currentLocation;
    }
}
