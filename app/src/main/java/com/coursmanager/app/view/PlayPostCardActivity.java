package com.coursmanager.app.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.PostCardManager;

import java.io.File;

public class PlayPostCardActivity extends AppCompatActivity {

    private TextView tPostCard;
    private ImageView imagePostCard;
    private File image;
    private Cursor c;
    private boolean recto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_play_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        PostCardManager postCardManager = new PostCardManager(this);
        postCardManager.open();
        this.c = postCardManager.getAllPostCardLesson(intent.getLongExtra("idLesson", 0));
        this.tPostCard = findViewById(R.id.tPostCard);
        this.imagePostCard = findViewById(R.id.imagePostCard);

        //Move to good position of the cursor
        if(savedInstanceState == null) {
            c.moveToFirst();
            recto = true;
        }else {
            c.moveToPosition(savedInstanceState.getInt("position", 0));
            recto = savedInstanceState.getBoolean("recto", true);
        }

        //Put the good side of the post-card
        if(recto)
            tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
        else{
            tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
            tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
        }

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
                c.moveToFirst();
                tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                recto = true;
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
                if(recto) {
                    image = new File(Environment.getExternalStorageDirectory().toString() + "/CoursManager/PostCards/" + c.getLong(c.getColumnIndex(PostCardManager.KEY_ID_POSTCARD)) + "_verso.jpg");
                    if(image.exists()){
                        tPostCard.setVisibility(View.GONE);
                        imagePostCard.setImageURI(Uri.fromFile(image));
                        imagePostCard.setVisibility(View.VISIBLE);
                    }else {
                        tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_verso));
                        tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_VERSO_POSTCARD)));
                    }
                    recto = false;
                }else{
                    tPostCard.setBackground(getResources().getDrawable(R.drawable.edit_recto));
                    tPostCard.setText(c.getString(c.getColumnIndex(PostCardManager.KEY_RECTO_POSTCARD)));
                    recto = true;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("position", c.getPosition());
        outState.putBoolean("recto", recto);
    }

}
