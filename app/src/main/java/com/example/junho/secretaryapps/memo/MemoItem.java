package com.example.junho.secretaryapps.memo;

public class MemoItem {
    String title;
    String content;
    String date;

    public MemoItem(String title,String date, String content){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

}
