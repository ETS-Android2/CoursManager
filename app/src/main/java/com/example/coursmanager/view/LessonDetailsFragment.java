package com.example.coursmanager.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.model.Lesson;


public class LessonDetailsFragment extends Fragment {

    private LessonManager lessonManager;
    private EditText textNote;
    private CheckBox checkFinish;
    private Lesson currentLesson;

    public LessonDetailsFragment() {
    }

    public static LessonDetailsFragment newInstance(long aId){
        LessonDetailsFragment fm = new LessonDetailsFragment();

        Bundle args = new Bundle();
        args.putLong("idLesson", aId);
        fm.setArguments(args);

        return fm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.lessonManager = new LessonManager(getContext());
        this.lessonManager.open();

        this.currentLesson = lessonManager.getLesson(getArguments().getLong("idLesson", 0));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lesson_details, container, false);

        TextView textDetails = rootView.findViewById(R.id.textDetails);
        textDetails.setText(currentLesson.getNameTeacher());

        this.textNote = rootView.findViewById(R.id.editNote);
        this.textNote.setText(currentLesson.getNote());

        this.checkFinish = rootView.findViewById(R.id.checkBox);
        this.checkFinish.setChecked(currentLesson.getFinish());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    public void save(){
        ((LessonDetailPostActivity) getActivity()).note = textNote.getText().toString();
        ((LessonDetailPostActivity) getActivity()).finish = checkFinish.isChecked();
    }

}
