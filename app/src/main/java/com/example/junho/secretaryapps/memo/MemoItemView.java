package com.example.junho.secretaryapps.memo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.junho.secretaryapps.R;

public class MemoItemView extends LinearLayout {
    TextView memoTitle;
    TextView memoContent;
    TextView memoDate;

    public MemoItemView(Context context){
        super(context);
        init(context);
    }

    public MemoItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.memo_listview,this,true);

        memoTitle = (TextView) findViewById(R.id.memoTitle);
        memoContent = (TextView) findViewById(R.id.memoContent);
        memoDate = (TextView) findViewById(R.id.memoDate);

    }

    public void setTitle(String title){
        memoTitle.setText(title);
    }

    public void setContent(String content){
        memoContent.setText(content);
    }

    public void setDate(String date){
        memoDate.setText(date);
    }
}
