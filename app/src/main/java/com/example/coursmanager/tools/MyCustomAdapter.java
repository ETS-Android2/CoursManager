package com.example.coursmanager.tools;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.example.coursmanager.R;

import java.util.ArrayList;

public class MyCustomAdapter extends ArrayAdapter<Boolean> {

    private Context mContext;
    private ArrayList<Boolean> myList;

    public MyCustomAdapter(@NonNull Context context, ArrayList<Boolean> list){
        super(context, 0, list);

        this.mContext = context;
        this.myList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_layout_reread,parent,false);

        CheckBox checkBox = listItem.findViewById(R.id.checkRead);
        checkBox.setBackgroundColor(Color.parseColor("#ffffff"));
        checkBox.setChecked(myList.get(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                }
            }
        });

        return listItem;
    }

}
