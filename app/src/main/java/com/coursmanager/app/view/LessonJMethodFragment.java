package com.coursmanager.app.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private TextView tNextRead;
    private Button bDone;
    private Calendar myCalendar;
    private String nextRead;
    @SuppressLint("SimpleDateFormat")
    final private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lesson_jmethod, container, false);

        this.tNextRead = rootView.findViewById(R.id.tNextRead);
        this.bDone = rootView.findViewById(R.id.bDone);
        this.bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LessonMainActivity) Objects.requireNonNull(getActivity())).nbReading += 1;
                try {
                    if(((LessonMainActivity) getActivity()).nbReading == 1)
                        myCalendar.setTime(sdf.parse(((LessonMainActivity) getActivity()).currentLesson.getDateJ0()));
                    else
                        myCalendar.setTime(sdf.parse(nextRead));

                    myCalendar.add(Calendar.DAY_OF_MONTH, ((LessonMainActivity) getActivity()).currentLesson.getRhythm()*((LessonMainActivity) getActivity()).nbReading);
                    nextRead = sdf.format(myCalendar.getTime());

                    updatePrint();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        rootView.findViewById(R.id.bFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                rootView.findViewById(R.id.bFinish).setVisibility(View.GONE);
                bDone.setVisibility(View.GONE);
                tNextRead.setText(getResources().getText(R.string.lessonFinished));
                ((LessonMainActivity) Objects.requireNonNull(getActivity())).currentLesson.setFinish(true);
=======
                ((LessonMainActivity) Objects.requireNonNull(getActivity())).currentLesson.setFinish(true);
                updatePrint();
>>>>>>> 9b2d2663016fbd9c798c671e7b78774c82c5c7b2
            }
        });

        updatePrint();

        return rootView;
    }

    @SuppressLint("SetTextI18n")
    public void updatePrint(){
        try {
            Date nextRead = sdf.parse(this.nextRead);
            myCalendar = Calendar.getInstance();

<<<<<<< HEAD
            if(myCalendar.getTime().before(nextRead)){
                bDone.setVisibility(View.GONE);
                tNextRead.setText(getResources().getText(R.string.nextRead) + sdf.format(nextRead));
=======
            if(((LessonMainActivity) Objects.requireNonNull(getActivity())).currentLesson.isFinish()){
                bDone.setVisibility(View.GONE);
                tNextRead.setText(getResources().getString(R.string.finish));
>>>>>>> 9b2d2663016fbd9c798c671e7b78774c82c5c7b2
                tNextRead.setVisibility(View.VISIBLE);
            }else {
                if (myCalendar.getTime().before(nextRead)) {
                    bDone.setVisibility(View.GONE);
                    tNextRead.setText(getResources().getString(R.string.nextRead) + sdf.format(nextRead));
                    tNextRead.setVisibility(View.VISIBLE);
                } else {
                    tNextRead.setVisibility(View.GONE);
                    bDone.setVisibility(View.VISIBLE);
                }
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
