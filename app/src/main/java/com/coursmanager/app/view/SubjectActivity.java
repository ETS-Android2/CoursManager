package com.coursmanager.app.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.SubjectManager;
import com.coursmanager.app.model.Subject;

public class SubjectActivity extends AppCompatActivity {

    private SubjectManager subjectManager;
    protected long idUE;
    public int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_subject);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.order = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getInt("orderSubject", 1);

        this.subjectManager = new SubjectManager(this);
        this.subjectManager.open();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.actionDeleteAll:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteAll)
                        .setMessage(R.string.confirmDeleteAll)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                subjectManager.deleteAllSubjectUE(idUE);
                                updatePrint();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                break;
            case R.id.action_order:
                CustomOrderDialog cod = new CustomOrderDialog(SubjectActivity.this);
                cod.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // Update the listView of UEs
    public void updatePrint(){
        Cursor c = subjectManager.getAllSubjectUE(idUE, order);
        String[] fromFieldNames = new String[] {subjectManager.KEY_NAME_SUBJECT, subjectManager.KEY_FINISH_SUBJECT};
        int[] toViewIDs = new int[] {R.id.textNameSubject, R.id.progressSubject};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_subject, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view instanceof TextView){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(subjectManager.KEY_NAME_SUBJECT)));
                }else{
                    ((ProgressBar) view).setProgress(subjectManager.getProgressOfaSubject(cursor.getLong(cursor.getColumnIndex("_id"))));
                }

                return true;
            }
        });

        ListView myList = findViewById(R.id.listViewSubject);
        myList.setAdapter(myCursorAdapter);

        // Delete an item when long click on it
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomLongClickDialog cd = new CustomLongClickDialog(SubjectActivity.this, id, 3);
                cd.show();
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

    @Override
    public void onResume(){
        super.onResume();
        updatePrint();
    }

}
