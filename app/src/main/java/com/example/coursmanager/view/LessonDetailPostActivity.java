package com.example.coursmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.controller.PostCardManager;
import com.example.coursmanager.model.Lesson;

public class LessonDetailPostActivity extends AppCompatActivity {

    private Fragment fragmentDetails;
    private Fragment fragmentPostCard;
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentDetails;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private Lesson currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail_post);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("lessonName"));

        this.fragmentDetails = LessonDetailsFragment.newInstance(intent.getLongExtra("idLesson", 0));
        this.fragmentPostCard = new LessonPostFragment();

        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_details:
                        fm.beginTransaction().hide(active).show(fragmentDetails).commit();
                        active = fragmentDetails;
                        return true;
                    case R.id.navigation_post_card:
                        fm.beginTransaction().hide(active).show(fragmentPostCard).commit();
                        active = fragmentPostCard;
                        return true;
                }
                return false;
            }
        };

        fm.beginTransaction().add(R.id.fragment_container1, fragmentDetails).hide(fragmentDetails).commit();
        fm.beginTransaction().add(R.id.fragment_container1, fragmentPostCard).hide(fragmentPostCard).commit();
    }

    /*@Override
    public void onBackPressed(){

    }*/

}
