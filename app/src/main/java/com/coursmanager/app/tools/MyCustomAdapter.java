package com.coursmanager.app.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.coursmanager.app.R;
import com.coursmanager.app.view.LessonDetailPostActivity;
import com.coursmanager.app.view.LessonReReadingFragment;

import java.util.ArrayList;

public class MyCustomAdapter extends ArrayAdapter<Boolean> {

    private Context mContext;
    private ArrayList<Boolean> myList;
    private LessonReReadingFragment currentFrament;

    public MyCustomAdapter(@NonNull Context context, ArrayList<Boolean> list, LessonReReadingFragment f){
        super(context, 0, list);

        this.mContext = context;
        this.myList = list;
        this.currentFrament = f;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_layout_reread,parent,false);

        CheckBox checkBox = listItem.findViewById(R.id.checkRead);
        checkBox.setButtonDrawable(R.drawable.checkbox);
        checkBox.setChecked(myList.get(position));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(mContext instanceof LessonDetailPostActivity){
                    if(isChecked)
                        currentFrament.nbReading++;
                    else
                        currentFrament.nbReading--;

                    currentFrament.textTotal.setText(String.valueOf(currentFrament.nbReading) + " / " + String.valueOf(currentFrament.objective));
                }
            }
        });

        return listItem;
    }

}
