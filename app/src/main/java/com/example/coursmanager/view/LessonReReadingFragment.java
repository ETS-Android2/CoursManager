package com.example.coursmanager.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.tools.MyCustomAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class LessonReReadingFragment extends Fragment {

    private View rootView;
    private TextView textTotal, textGoal;

    private int objective;
    private int nbReading;

    public LessonReReadingFragment() {
    }

    public static LessonReReadingFragment newInstance() {
        LessonReReadingFragment fragment = new LessonReReadingFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.objective = 20;
        this.nbReading = 3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView =  inflater.inflate(R.layout.fragment_lesson_re_reading, container, false);

        this.textTotal = rootView.findViewById(R.id.textTotal);
        this.textGoal = rootView.findViewById(R.id.textGoal);

        updatePrint();

        rootView.findViewById(R.id.buttonM).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(objective >= 0) {
                    if(nbReading == objective)
                        nbReading--;
                    objective--;
                    updatePrint();
                }
            }
        });

        rootView.findViewById(R.id.buttonP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objective++;
                updatePrint();
            }
        });

        return rootView;
    }


    private ArrayList<Boolean> buildListFinished(int tot, int ok){
        ArrayList<Boolean> res = new ArrayList<>();

        for(int i=0 ; i < tot ; i++){
            if(i < ok)
                res.add(true);
            else
                res.add(false);
        }

        return res;
    }

    private void updatePrint(){
        textTotal.setText(String.valueOf(nbReading) + " / " + String.valueOf(objective));

        GridView myList = rootView.findViewById(R.id.gridList);
        myList.setAdapter(new MyCustomAdapter(getContext(), buildListFinished(objective, nbReading)));

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nbReading++;
                textTotal.setText(String.valueOf(nbReading) + " / " + String.valueOf(objective));
            }
        });

        textGoal.setText(String.valueOf(objective));
    }

}
