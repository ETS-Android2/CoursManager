package com.coursmanager.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.PostCardManager;
import com.coursmanager.app.model.PostCard;

public class EditionPostCardActivity extends AppCompatActivity {

    private boolean creation;
    private PostCardManager postCardManager;
    private PostCard currentPostCard;
    private long idLesson;

    private EditText editName;
    private EditText editRecto;
    private EditText editVerso;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_edition_post_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.editionPost);

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

        findViewById(R.id.bTakePict).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissionCamera();
            }
        });
    }

    private boolean verifyPermissionCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void savePostCard(){
        currentPostCard.setRecto(editRecto.getText().toString());
        currentPostCard.setVerso(editVerso.getText().toString());

        if(creation)
            postCardManager.addPostCard(currentPostCard);
        else
            postCardManager.updatePostCard(currentPostCard);

        finish();
    }

}
