package com.coursmanager.app.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.PostCardManager;

import java.io.File;


public class LessonPostFragment extends Fragment {

    private long idLesson;
    private PostCardManager postCardManager;
    private View rootView;
    private Cursor c;

    public LessonPostFragment() {
    }

    public static LessonPostFragment newInstance(long aId){
        LessonPostFragment fm = new LessonPostFragment();

        Bundle args = new Bundle();
        args.putLong("idLesson", aId);
        fm.setArguments(args);

        return fm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.postCardManager = new PostCardManager(getContext());
        this.postCardManager.open();

        this.c = postCardManager.getAllPostCardLesson(idLesson);

        this.idLesson = getArguments().getLong("idLesson", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_lesson_post, container, false);

        updatePrint();

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPostCard();
            }
        });

        FloatingActionButton fabPlay = rootView.findViewById(R.id.fabPlay);
        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c.getCount() != 0) {
                    Intent intent = new Intent(getContext(), PlayPostCardActivity.class);
                    intent.putExtra("idLesson", idLesson);
                    startActivity(intent);
                }else
                    Toast.makeText(getContext(), R.string.noPostCard, Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fabShuffle = rootView.findViewById(R.id.fabShuffle);
        fabShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(c.getCount() != 0) {
                    Intent intent = new Intent(getContext(), ShufflePostCardActivity.class);
                    intent.putExtra("idLesson", idLesson);
                    startActivity(intent);
                }else
                    Toast.makeText(getContext(), R.string.noPostCard, Toast.LENGTH_LONG).show();
            }
        });

        return this.rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        updatePrint();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updatePrint(){
        c = postCardManager.getAllPostCardLesson(idLesson);
        String[] fromFieldNames = new String[] {PostCardManager.KEY_ID_POSTCARD};
        int[] toViewIDs = new int[] {R.id.textIdPost};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_layout_postcard, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view instanceof TextView){
                    ((TextView) view).setText(String.valueOf(cursor.getPosition()+1));
                }

                return true;
            }
        });

        GridView myList = rootView.findViewById(R.id.gridList);
        myList.setAdapter(myCursorAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EditionPostCardActivity.class);
                intent.putExtra("creation", false);
                intent.putExtra("idLesson", idLesson);
                intent.putExtra("idPostCard", id);
                startActivity(intent);
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askToDeletePostCard(id);
                return true;
            }
        });
    }

    public void submitPostCard(){
        Intent intent = new Intent(getContext(), EditionPostCardActivity.class);
        intent.putExtra("creation", true);
        intent.putExtra("idLesson", idLesson);
        startActivity(intent);
    }

    public void askToDeletePostCard(final long id){
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        postCardManager.deletePostCard(postCardManager.getPostCard(id));
                        new File(Environment.getExternalStorageDirectory().toString() + "/CoursManager/PostCards/" + id + "_verso.jpg").delete();
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
