package com.example.coursmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.model.Lesson;

public class LessonDetailPostActivity extends AppCompatActivity {

    private Fragment fragmentDetails;
    private Fragment fragmentPostCard;
    private Fragment fragmentReReading;
    private FragmentManager fm;
    private Fragment active;
    private int activeFragment;

    private LessonManager lessonManager;
    private Lesson currentLesson;

    public int objective;
    public int nbReading;
    public String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lesson_detail_post);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_details:
                        fm.beginTransaction().hide(active).show(fragmentDetails).commit();
                        active = fragmentDetails;
                        activeFragment = 1;
                        return true;
                    case R.id.navigation_post_card:
                        fm.beginTransaction().hide(active).show(fragmentPostCard).commit();
                        active = fragmentPostCard;
                        activeFragment = 2;
                        return true;
                    case R.id.navigation_rereading:
                        fm.beginTransaction().hide(active).show(fragmentReReading).commit();
                        active = fragmentReReading;
                        activeFragment = 3;
                        return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("lessonName"));

        this.lessonManager = new LessonManager(getApplicationContext());
        this.lessonManager.open();

        this.currentLesson = lessonManager.getLesson(intent.getLongExtra("idLesson", 0));

        this.nbReading = currentLesson.getNbRead();


        this.fm = getSupportFragmentManager();

        this.fragmentDetails = LessonDetailsFragment.newInstance(intent.getLongExtra("idLesson", 0));
        this.fragmentPostCard = LessonPostFragment.newInstance(intent.getLongExtra("idLesson", 0));
        this.fragmentReReading = LessonReReadingFragment.newInstance(currentLesson.getObjective(), currentLesson.getNbRead());

        if(savedInstanceState == null) {
            Log.d("Test", "tessssssst");
            this.activeFragment = 1;
        }else {
            this.activeFragment = savedInstanceState.getInt("active");
        }

        //Switch is usefull when the screen orientation change, onCreate will be re-called and the active fragment will not be the 1 by default !
        switch (activeFragment) {
            case 1:
                this.active = fragmentDetails;
                fm.beginTransaction().add(R.id.fragment_container, fragmentReReading, "3").hide(fragmentReReading).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentPostCard, "2").hide(fragmentPostCard).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentDetails, "1").commit();
                break;
            case 2:
                this.active = fragmentPostCard;
                fm.beginTransaction().add(R.id.fragment_container, fragmentReReading, "3").hide(fragmentReReading).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentDetails, "1").hide(fragmentDetails).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentPostCard, "2").commit();
                break;
            case 3:
                this.active = fragmentReReading;
                fm.beginTransaction().add(R.id.fragment_container, fragmentPostCard, "2").hide(fragmentPostCard).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentDetails, "1").hide(fragmentDetails).commit();
                fm.beginTransaction().add(R.id.fragment_container, fragmentReReading, "3").commit();
                break;
            default:
                break;
        }

    }

    @Override
    public void onPause(){
        super.onPause();

        currentLesson.setNote(note);
        currentLesson.setFinish(nbReading == objective);
        currentLesson.setObjectve(objective);
        currentLesson.setNbRead(nbReading);

        lessonManager.updateLesson(currentLesson);

        fm.beginTransaction().hide(active).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("Before", String.valueOf(activeFragment));
        outState.putInt("active", activeFragment);
    }

}
