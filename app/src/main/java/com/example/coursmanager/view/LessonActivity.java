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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.model.Lesson;

import java.util.Date;

public class LessonActivity extends AppCompatActivity {

    private LessonManager lessonManager;
    private long idSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lessonManager = new LessonManager(this);
        lessonManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("subjectName"));
        this.idSubject = intent.getLongExtra("idSubject", 0);

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitLesson();
            }
        });
    }

    public void updatePrint(){
        Cursor c = lessonManager.getAllLessonSubject(idSubject);
        String[] fromFieldNames = new String[] {LessonManager.KEY_NAME_LESSON, LessonManager.KEY_FINISH_LESSON};
        int[] toViewIDs = new int[] {R.id.textNameLesson, R.id.imageStatus};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_lesson, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view instanceof TextView){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(LessonManager.KEY_NAME_LESSON)));
                }else{
                    if((cursor.getInt(cursor.getColumnIndex(LessonManager.KEY_FINISH_LESSON))) == 1){
                        ((ImageView) view).setImageResource(R.drawable.finish);
                    }else{
                        ((ImageView) view).setImageResource(R.drawable.progress);
                    }
                }

                return true;
            }
        });

        ListView myList = findViewById(R.id.listViewLesson);
        myList.setAdapter(myCursorAdapter);

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askToDeleteLesson(id);
                return true;
            }
        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LessonDetailPostActivity.class);
                intent.putExtra("idLesson", id);
                intent.putExtra("lessonName", lessonManager.getLesson(id).getNameLesson());
                startActivity(intent);
            }
        });
    }

    public void submitLesson() {
        LinearLayout layout = new LinearLayout(this);
        final EditText editText = new EditText(this);
        final EditText editTextTeach = new EditText(this);

        editText.setHint(R.string.defaultNameLesson);
        editTextTeach.setHint(R.string.defaultNameTeach);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(editTextTeach);

        new AlertDialog.Builder(this)
                .setTitle(R.string.titleAddLesson)
                .setMessage("")
                .setView(layout)
                .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create a UE in db
                        if(lessonManager.addLesson(new Lesson(0, editText.getText().toString(), editTextTeach.getText().toString(), new Date().getTime(), "",false, idSubject, 10, 0)) == -1){
                            Toast.makeText(getApplicationContext(), R.string.lessonAddError, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.lessonAddGood, Toast.LENGTH_LONG).show();
                        }

                        updatePrint();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public void askToDeleteLesson(final long id){
        new AlertDialog.Builder(this)
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        lessonManager.deleteLesson(lessonManager.getLesson(id));
                        updatePrint();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .setCancelable(false).show();
    }

    @Override
    public void onResume(){
        super.onResume();
        updatePrint();
    }

}
