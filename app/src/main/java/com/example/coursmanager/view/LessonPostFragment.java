package com.example.coursmanager.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;


public class LessonPostFragment extends Fragment {

    private long idLesson;
    private PostCardManager postCardManager;

    public LessonPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lesson_post, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
