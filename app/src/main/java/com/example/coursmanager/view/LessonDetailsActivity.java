package com.example.coursmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.model.Lesson;

public class LessonDetailsActivity extends AppCompatActivity {

    private LessonManager lessonManager;
    private long idLesson;
    private TextView textDetails;
    private EditText textNote;
    private CheckBox checkFinish;
    private Lesson currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.lessonManager = new LessonManager(this);
        this.lessonManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("lessonName"));
        this.idLesson = intent.getLongExtra("idLesson", 0);
        this.currentLesson = lessonManager.getLesson(idLesson);

        this.textDetails = findViewById(R.id.textDetails);
        this.textDetails.setText(currentLesson.getNameTeacher());

        this.textNote = findViewById(R.id.editNote);
        this.textNote.setText(currentLesson.getNote());

        this.checkFinish = findViewById(R.id.checkBox);
        this.checkFinish.setChecked(currentLesson.getFinish());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirm change and return to LessonActivity
                currentLesson.setNote(textNote.getText().toString());
                currentLesson.setFinish(checkFinish.isChecked());

                lessonManager.updateLesson(currentLesson);
                finish();
            }
        });
    }

}
