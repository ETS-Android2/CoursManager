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

import java.util.Objects;


public class LessonDetailsFragment extends Fragment {

    private EditText textNote;

    public LessonDetailsFragment() {
    }

    public static LessonDetailsFragment newInstance(){
        return new LessonDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lesson_details, container, false);

        TextView textDetails = rootView.findViewById(R.id.textDetails);
        textDetails.setText(((LessonMainActivity) Objects.requireNonNull(getActivity())).currentLesson.getNameTeacher());

        this.textNote = rootView.findViewById(R.id.editNote);
        this.textNote.setText(((LessonMainActivity) getActivity()).currentLesson.getNote());

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        save();
    }

    public void save(){
        ((LessonMainActivity) Objects.requireNonNull(getActivity())).note = textNote.getText().toString();
    }

}
