package com.coursmanager.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.coursmanager.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class LessonJMethodFragment extends Fragment {

    private View rootView;
    private TextView tNextRead;
    private Button bDone;
    private Calendar myCalendar;
    private String nextRead;
    final private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public LessonJMethodFragment() {
    }

    public static LessonJMethodFragment newInstance() {
        return new LessonJMethodFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.myCalendar = Calendar.getInstance();
        this.nextRead = ((LessonMainActivity) Objects.requireNonNull(getActivity())).nextRead;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView =  inflater.inflate(R.layout.fragment_lesson_jmethod, container, false);

        this.tNextRead = rootView.findViewById(R.id.tNextRead);
        this.bDone = rootView.findViewById(R.id.bDone);
        this.bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LessonMainActivity) Objects.requireNonNull(getActivity())).nbReading += 1;
                try {
                    myCalendar.setTime(sdf.parse(nextRead));
                    myCalendar.add(Calendar.DAY_OF_MONTH, ((LessonMainActivity) getActivity()).currentLesson.getRhythm()*((LessonMainActivity) getActivity()).nbReading);
                    nextRead = sdf.format(myCalendar.getTime());

                    updatePrint();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        updatePrint();

        return rootView;
    }

    public void updatePrint(){
        try {
            Date nextRead = sdf.parse(this.nextRead);
            myCalendar = Calendar.getInstance();


            if(myCalendar.getTime().before(nextRead)){
                bDone.setVisibility(View.GONE);
                tNextRead.setText("Next read : " + sdf.format(nextRead));
                tNextRead.setVisibility(View.VISIBLE);
            }else{
                tNextRead.setVisibility(View.GONE);
                bDone.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    public void save(){
        ((LessonMainActivity) Objects.requireNonNull(getActivity())).nextRead = nextRead;
    }

}
