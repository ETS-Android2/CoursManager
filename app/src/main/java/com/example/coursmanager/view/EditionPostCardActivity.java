package com.example.coursmanager.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;
import com.example.coursmanager.model.PostCard;

public class EditionPostCardActivity extends AppCompatActivity {

    private boolean creation;
    private PostCardManager postCardManager;
    private PostCard currentPostCard;
    private long idLesson;

    private EditText editName;
    private EditText editRecto;
    private EditText editVerso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edition_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.editionPost);

        this.editName = findViewById(R.id.editNamePost);
        this.editRecto = findViewById(R.id.editRecto);
        this.editVerso = findViewById(R.id.editVerso);

        postCardManager = new PostCardManager(this);
        postCardManager.open();

        Intent intent = getIntent();
        this.idLesson = intent.getLongExtra("idLesson", 0);
        this.creation = intent.getBooleanExtra("creation", true);

        if(creation)
            currentPostCard = new PostCard(0, "", "", "", idLesson);
        else {
            currentPostCard = postCardManager.getPostCard(intent.getLongExtra("idPost", 0));

            editName.setText(currentPostCard.getTheme());
            editRecto.setText(currentPostCard.getRecto());
            editVerso.setText(currentPostCard.getVerso());
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePostCard();
            }
        });
    }

    public void savePostCard(){
        currentPostCard.setTheme(editName.getText().toString());
        currentPostCard.setRecto(editRecto.getText().toString());
        currentPostCard.setVerso(editVerso.getText().toString());

        if(creation)
            postCardManager.addPostCard(currentPostCard);
        else
            postCardManager.updatePostCard(currentPostCard);

        finish();
    }

}
