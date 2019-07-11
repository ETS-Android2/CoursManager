package com.example.coursmanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.coursmanager.R;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity activity;
    public Button bPost, bMemo;
    public long idLesson, idSubject;
    public String name;

    public CustomDialog(Activity a, long id, long idS, String n){
        super(a);
        this.activity = a;
        this.idLesson = id;
        this.idSubject = idS;
        this.name = n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog);

        findViewById(R.id.bMemo).setOnClickListener(this);
        findViewById(R.id.bPost).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bMemo:
                Intent intent = new Intent(activity.getApplicationContext(), LessonDetailsActivity.class);
                intent.putExtra("idLesson", idLesson);
                intent.putExtra("lessonName", name);
                intent.putExtra("idSubject", idSubject);
                activity.startActivity(intent);
                break;
            case R.id.bPost:
                break;
            default:
                break;
        }
        dismiss();
    }
}
