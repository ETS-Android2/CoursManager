package com.coursmanager.app.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.PostCardManager;
import com.coursmanager.app.model.PostCard;

import java.io.File;
import java.io.IOException;

public class EditionPostCardActivity extends AppCompatActivity {

    private boolean creation;
    private PostCardManager postCardManager;
    private PostCard currentPostCard;
    private long idLesson;
    private long idPostCard;

    private EditText editRecto;
    private EditText editVerso;
    private ImageView imageVerso;

    private String mCameraFileName;
    private Uri image;

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
        this.imageVerso = findViewById(R.id.imageVerso);

        imageVerso.setVisibility(View.GONE);

        postCardManager = new PostCardManager(this);
        postCardManager.open();

        Intent intent = getIntent();
        this.idLesson = intent.getLongExtra("idLesson", 0);
        this.creation = intent.getBooleanExtra("creation", true);

        if(creation) {
            idPostCard = postCardManager.addPostCard(new PostCard(0, "", "", "", idLesson));
            currentPostCard = postCardManager.getPostCard(idPostCard);
        }else{
            currentPostCard = postCardManager.getPostCard(intent.getLongExtra("idPost", 0));
            idPostCard = currentPostCard.getIdPostCard();

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
                verifyPermissionCamera(1);
            }
        });
    }

    private boolean verifyPermissionCamera(int request){
        String[] permissions = {Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            takePict();

            return true;
        }else{
            ActivityCompat.requestPermissions(this, permissions, request);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perm, @NonNull int[] grantResult){
        if(grantResult[0] == PackageManager.PERMISSION_GRANTED)
            verifyPermissionCamera(requestCode);
    }

    private void takePict(){
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outFile = new File(Environment.getExternalStorageDirectory().toString() + "/CoursManager/PostCards/" + idPostCard + "_verso.jpg");
        mCameraFileName = outFile.toString();
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            if(data != null) {
                image = data.getData();
                imageVerso.setImageURI(image);
                imageVerso.setVisibility(View.VISIBLE);
                editVerso.setVisibility(View.INVISIBLE);
            }
            if(image == null && mCameraFileName != null) {
                image = Uri.fromFile(new File(mCameraFileName));
                imageVerso.setImageURI(image);
                imageVerso.setVisibility(View.VISIBLE);
                editVerso.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void savePostCard(){
        currentPostCard.setRecto(editRecto.getText().toString());
        currentPostCard.setVerso(editVerso.getText().toString());

        postCardManager.updatePostCard(currentPostCard);

        finish();
    }

}
