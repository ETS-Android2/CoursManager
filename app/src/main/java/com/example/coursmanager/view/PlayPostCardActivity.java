package com.example.coursmanager.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;

public class PlayPostCardActivity extends AppCompatActivity {

    private TextView tPostCard;
    private Cursor c;
    private PostCardManager postCardManager;
    private boolean recto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        this.postCardManager = new PostCardManager(this);
        this.postCardManager.open();
        this.c = postCardManager.getAllPostCardLesson(intent.getLongExtra("idLesson", 0));
        this.tPostCard = findViewById(R.id.tPostCard);
        this.recto = true;

        c.moveToFirst();
        this.tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));

        FloatingActionButton fabPrevious = findViewById(R.id.fabPrevious);
        fabPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!c.isFirst()) {
                    c.moveToPrevious();
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                }else
                    Toast.makeText(getApplicationContext(), R.string.first ,Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fabReplay = findViewById(R.id.fabReplay);
        fabReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!recto) {
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                    recto = true;
                }
            }
        });

        FloatingActionButton fabNext = findViewById(R.id.fabNext);
        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!c.isLast()) {
                    c.moveToNext();
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                }else
                    Toast.makeText(getApplicationContext(), R.string.last ,Toast.LENGTH_LONG).show();
            }
        });

        tPostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
                tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
                recto = false;
            }
        });
    }

}
