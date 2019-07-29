package com.example.coursmanager.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.FolderManager;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.controller.UEManager;
import com.example.coursmanager.model.Folder;
import com.example.coursmanager.model.UE;

public class UEActivity extends AppCompatActivity {

    // Manager of UE db
    private UEManager ueManager;
    private LessonManager lessonManager;
    private FolderManager folderManager;
    private ProgressBar progress;
    private TextView progressText;
    private long idFolder;
    private Folder currentFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_ue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create and open the ueManager
        this.ueManager = new UEManager(this);
        this.ueManager.open();

        this.lessonManager = new LessonManager(this);
        this.folderManager = new FolderManager(this);
        folderManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("folderName"));
        this.idFolder = intent.getLongExtra("idFolder", 0);
        this.currentFolder = folderManager.getFolder(idFolder);

        this.progress = findViewById(R.id.progressBar);
        this.progressText = findViewById(R.id.progressStatue);

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitUE();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.actionDeleteAll){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.deleteAll)
                    .setMessage(R.string.confirmDeleteAll)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ueManager.deleteAllUEFolder(idFolder);
                            updatePrint();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    // Update the listView of UEs
    public void updatePrint(){
        Cursor c = ueManager.getAllUEFolder(idFolder);
        String[] fromFieldNames = new String[] {ueManager.KEY_NAME_UE, ueManager.KEY_PERCENTAGE_UE};
        int[] toViewIDs = new int[] {R.id.textNameUE, R.id.progressUE};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_ue, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view instanceof TextView){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(ueManager.KEY_NAME_UE)));
                }else{
                    ((ProgressBar) view).setProgress(ueManager.getProgressOfanUE(cursor.getLong(cursor.getColumnIndex("_id"))));
                }

                return true;
            }
        });

        ListView myList = findViewById(R.id.listView);
        myList.setAdapter(myCursorAdapter);

        // Delete an item when long click on it
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                askToDeleteUE(id);
                return true;
            }
        });

        // Open the activity of the clicked ue
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                intent.putExtra("idUE", id);
                intent.putExtra("ueName", ueManager.getUE(id).getNameUE());
                startActivity(intent);
            }
        });

        // Update the progress bar which represent the nb of finished lessons on total nb of lessons
        //getResources().getDrawable(R.drawable.progressbar_states).setBounds(0,0, 150, 50);
        //this.progress.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_states));
        int nbTot = lessonManager.getNumberLessonsInFolder(idFolder);
        int nbFin = lessonManager.getNumberLessonsFinishedInFolder(idFolder);
        // To avoid an empty ring progress bar
        if(nbTot == 0){
            this.progress.setMax(1);
        }else {
            this.progress.setMax(nbTot);
        }
        this.progress.setProgress(nbFin);

        int percent = Math.round(((float)nbFin/(float)nbTot)*100);
        currentFolder.setPercentFolder(percent);
        this.progressText.setText(String.valueOf(percent)+" %");
    }

    // When you click on the button to add UE
    public void submitUE() {
        final EditText editTextUE = new EditText(this);

        editTextUE.setHint(R.string.defaultNameUE);

        new AlertDialog.Builder(this)
                .setTitle(R.string.titleAddUE)
                .setMessage("")
                .setView(editTextUE)
                .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create a UE in db
                        if(ueManager.addUE(new UE(0, editTextUE.getText().toString(), 0, idFolder)) == -1){
                            Toast.makeText(getApplicationContext(), R.string.ueAddError, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.ueAddGood, Toast.LENGTH_LONG).show();
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

    public void askToDeleteUE(final long id){
        new AlertDialog.Builder(this)
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        ueManager.deleteUE(ueManager.getUE(id));
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

    @Override
    public void onPause(){
        super.onPause();
        folderManager.updateFolder(currentFolder);
    }

}
