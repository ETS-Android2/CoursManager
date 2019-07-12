package com.example.coursmanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.PostCardManager;
import com.example.coursmanager.model.PostCard;

public class PostCardsActivity extends AppCompatActivity {

    private long idLesson;
    private PostCardManager postCardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_cards);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        postCardManager = new PostCardManager(this);
        postCardManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("lessonName"));
        this.idLesson = intent.getLongExtra("idLesson", 0);

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPostCard();
            }
        });
    }

    public void updatePrint(){
        Cursor c = postCardManager.getAllPostCardLesson(idLesson);
        String[] fromFieldNames = new String[] {PostCardManager.KEY_ID_POSTCARD};
        int[] toViewIDs = new int[] {R.id.textIdPost};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_postcard, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view instanceof TextView){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(PostCardManager.KEY_ID_POSTCARD)));
                }

                return true;
            }
        });

        GridView myList = findViewById(R.id.gridList);
        myList.setAdapter(myCursorAdapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askToDeletePostCard(id);
                return true;
            }
        });
    }

    public void submitPostCard(){
        postCardManager.addPostCard(new PostCard(0, "", "", "", idLesson));
        updatePrint();
    }

    public void askToDeletePostCard(final long id){
        new AlertDialog.Builder(this)
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        postCardManager.deletePostCard(postCardManager.getPostCard(id));
                        updatePrint();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setCancelable(false).show();
    }

}
