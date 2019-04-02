package com.example.coursmanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.SubjectManager;
import com.example.coursmanager.controller.UEManager;
import com.example.coursmanager.model.Subject;
import com.example.coursmanager.model.UE;

public class SubjectActivity extends AppCompatActivity {

    private SubjectManager subjectManager;
    protected long idUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        subjectManager = new SubjectManager(this);
        subjectManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("ueName"));
        this.idUE = intent.getLongExtra("idUE", 0);

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitSubject();
            }
        });
    }

    // Update the listView of UEs
    public void updatePrint(){
        Cursor c = subjectManager.getAllSubjectUE(this.idUE);
        String[] fromFieldNames = new String[] {subjectManager.KEY_NAME_SUBJECT, subjectManager.KEY_FINISH_SUBJECT};
        int[] toViewIDs = new int[] {R.id.textNameUE, R.id.textPercentageUE};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, c, fromFieldNames, toViewIDs, 0);
        ListView myList = findViewById(R.id.listViewSubject);
        myList.setAdapter(myCursorAdapter);

        // Delete an item when long click on it
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askToDeleteSubject(id);
                return true;
            }
        });

        // Open the activity of the clicked ue
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LessonActivity.class);
                intent.putExtra("idSubject", id);
                intent.putExtra("subjectName", subjectManager.getSubject(id).getNameSubject());
                startActivity(intent);
            }
        });
    }

    // When you click on the button to add Subject
    public void submitSubject() {
        final EditText editText = new EditText(this);

        editText.setHint(R.string.defaultNameSubject);

        new AlertDialog.Builder(this)
                .setTitle(R.string.titleAddSubject)
                .setMessage("")
                .setView(editText)
                .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create a Subject in db
                        if(subjectManager.addSubject(new Subject(0, editText.getText().toString(), idUE, false)) == -1){
                            Toast.makeText(getApplicationContext(), R.string.subjectAddError, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.subjectAddGood, Toast.LENGTH_LONG).show();
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

    public void askToDeleteSubject(final long id){
        new AlertDialog.Builder(this)
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        subjectManager.deleteSubject(subjectManager.getSubject(id));
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
