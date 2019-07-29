package com.example.coursmanager.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;

import java.util.Random;

public class ShufflePostCardActivity extends AppCompatActivity {

    private TextView tPostCard;
    private Cursor c;
    private boolean recto;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_shuffle_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        PostCardManager postCardManager = new PostCardManager(this);
        postCardManager.open();
        this.c = postCardManager.getAllPostCardLesson(intent.getLongExtra("idLesson", 0));
        this.tPostCard = findViewById(R.id.tPostCard);
        this.recto = true;
        this.max = c.getCount();

        c.moveToPosition(getRandomPosition());
        this.tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));

        FloatingActionButton fabShuffle = findViewById(R.id.fabNext);
        fabShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.moveToPosition(getRandomPosition());
                tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
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

        tPostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
                tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
                recto = false;
            }
        });
    }

    private int getRandomPosition(){
        return new Random().nextInt(max);
    }

}
