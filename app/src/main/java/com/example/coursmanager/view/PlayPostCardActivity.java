package com.example.coursmanager.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;

public class PlayPostCardActivity extends AppCompatActivity {

    private TextView tVerso;
    private Cursor c;
    private PostCardManager postCardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        this.postCardManager = new PostCardManager(this);
        postCardManager.open();

        this.c = postCardManager.getAllPostCardLesson(intent.getLongExtra("idLesson", 0));

        this.tVerso = findViewById(R.id.tVerso);

        c.moveToFirst();
        tVerso.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
        tVerso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tVerso.setBackgroundColor(Color.parseColor("#d10232"));
                tVerso.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
            }
        });
    }

}
