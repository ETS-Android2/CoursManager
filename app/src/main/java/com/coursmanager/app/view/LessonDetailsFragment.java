package com.coursmanager.app.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.LessonManager;
import com.coursmanager.app.model.Lesson;


public class LessonDetailsFragment extends Fragment {

    private LessonManager lessonManager;
    private EditText textNote;
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

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    public void save(){
        ((LessonDetailPostActivity) getActivity()).note = textNote.getText().toString();
    }

}
