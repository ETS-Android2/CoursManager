package com.example.coursmanager.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.model.Lesson;
import com.example.coursmanager.tools.MyCustomAdapter;

import java.util.ArrayList;

public class LessonReReadingFragment extends Fragment {

    private View rootView;
    public TextView textTotal, textGoal;

    public int objective;
    public int nbReading;

    public LessonReReadingFragment() {
    }

    public static LessonReReadingFragment newInstance(int obj, int nbR) {
        LessonReReadingFragment fragment = new LessonReReadingFragment();

        Bundle args = new Bundle();
        args.putInt("objective", obj);
        args.putInt("nbReading", nbR);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.objective = getArguments().getInt("objective");
        this.nbReading = getArguments().getInt("nbReading");
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

    public void updatePrint(){
        textTotal.setText(String.valueOf(nbReading) + " / " + String.valueOf(objective));

        GridView myList = rootView.findViewById(R.id.gridList);
        myList.setAdapter(new MyCustomAdapter(getContext(), buildListFinished(objective, nbReading), this));

        textGoal.setText(String.valueOf(objective));
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    private void save(){
        ((LessonDetailPostActivity) getActivity()).objective = objective;
        ((LessonDetailPostActivity) getActivity()).nbReading = nbReading;
    }

}
