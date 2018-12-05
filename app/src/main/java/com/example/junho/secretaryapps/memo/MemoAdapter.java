package com.example.junho.secretaryapps.memo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MemoAdapter extends BaseAdapter {
    ArrayList<MemoItem> items = new ArrayList<MemoItem>();
    Context context;

    public MemoAdapter(Context context){
        this.context = context;
    }

    public void init(){
        this.items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(MemoItem item){
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MemoItemView view = new MemoItemView(context);
        MemoItem item = items.get(position);
        view.setTitle(item.getTitle());
        view.setContent(item.getContent());
        view.setDate(item.getDate());
        view.setBackColor(item.getBackColor());

        return view;
    }
}
