package com.example.test_jump;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    ArrayList<ListViewScores> scoreInfo;
//    private int listLength = 0;
    public ListAdapter(ArrayList<ListViewScores> scoreInfo){
        this.scoreInfo = scoreInfo;
//        this.listLength = scoreInfo.size();
    }
    @Override
    public int getCount() {
        return scoreInfo.size();
    }

    @Override
    public Object getItem(int i) {
        return scoreInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            Context context = viewGroup.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            view = inflater.inflate(R.layout.custom_listview, viewGroup, false);
        }
        ((TextView)view.findViewById(R.id.textRanking)).setText((i+1)+"위");
        ((TextView)view.findViewById(R.id.textName)).setText(scoreInfo.get(i).getName());
        ((TextView)view.findViewById(R.id.textScore)).setText(Integer.toString(scoreInfo.get(i).getScore()));

        return view;
    }
}
