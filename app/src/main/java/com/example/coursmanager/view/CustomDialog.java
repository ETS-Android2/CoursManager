package com.example.coursmanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.coursmanager.R;

public class CustomDialog extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private long idLesson, idSubject;
    private String name;

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
        Intent intent;
        switch (view.getId()){
            case R.id.bMemo:
                intent = new Intent(activity.getApplicationContext(), LessonDetailsActivity.class);
                intent.putExtra("idLesson", idLesson);
                intent.putExtra("lessonName", name);
                intent.putExtra("idSubject", idSubject);
                activity.startActivity(intent);
                break;
            case R.id.bPost:
                intent = new Intent(activity.getApplicationContext(), PostCardsActivity.class);
                intent.putExtra("idLesson", idLesson);
                intent.putExtra("lessonName", name);
                intent.putExtra("idSubject", idSubject);
                activity.startActivity(intent);
                break;
            default:
                break;
        }
        dismiss();
    }
}
