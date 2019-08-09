package com.coursmanager.app.view;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.FolderManager;
import com.coursmanager.app.model.Folder;

public class FolderActivity extends AppCompatActivity {

    private FolderManager folderManager;
    public SharedPreferences sharedPref;
    private String currentTheme;
    public int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.currentTheme = sharedPref.getString("theme", "light");
        this.order = sharedPref.getInt("orderFolder", 1);

        Log.d("ORDER", String.valueOf(order));

        setAppTheme(this);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        folderManager = new FolderManager(this);
        folderManager.open();

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFolder();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_folder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.actionDeleteAll:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.deleteAll)
                        .setMessage(R.string.confirmDeleteAll)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                folderManager.deleteAllFolder();
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
                CustomOrderDialog cod = new CustomOrderDialog(FolderActivity.this);
                cod.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    static public void setAppTheme(Context c) {
        switch (c.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getString("theme", "light")) {
            case "light":
                c.setTheme(R.style.AppTheme_Light);
                break;
            case "dark":
                c.setTheme(R.style.AppTheme_Dark);
                break;
            default:
                break;
        }
    }

    public void updatePrint(){
        Cursor c = folderManager.getAllFolder(order);
        String[] fromFieldNames = new String[] {FolderManager.KEY_NAME_FOLDER, FolderManager.KEY_PERCENTAGE_FOLDER};
        int[] toViewIDs = new int[] {R.id.textNameFolder, R.id.textPercentFolder};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_folder, c, fromFieldNames, toViewIDs, 0);
        myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view.getId() == R.id.textNameFolder){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(FolderManager.KEY_NAME_FOLDER)));
                }else{
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(FolderManager.KEY_PERCENTAGE_FOLDER))+" %");
                }

                return true;
            }
        });

        GridView myGrid = findViewById(R.id.gridView);
        myGrid.setAdapter(myCursorAdapter);

        // Delete an item when long click on it
        myGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomLongClickDialog cd = new CustomLongClickDialog(FolderActivity.this, id, 1);
                cd.show();
                return true;
            }
        });

        // Open the activity of the clicked ue
        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UEActivity.class);
                intent.putExtra("idFolder", id);
                intent.putExtra("folderName", folderManager.getFolder(id).getNameFolder());
                startActivity(intent);
            }
        });

    }

    public void submitFolder() {
        final EditText editTextFolder = new EditText(this);
        editTextFolder.setHint(R.string.defaultNameFolder);

        new AlertDialog.Builder(this)
                .setTitle(R.string.titleAddFolder)
                .setMessage("")
                .setView(editTextFolder)
                .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create a Folder in db
                        if(folderManager.addFolder(new Folder(0, editTextFolder.getText().toString(), 0)) == -1){
                            Toast.makeText(getApplicationContext(), R.string.folderAddError, Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.folderAddGood, Toast.LENGTH_LONG).show();
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

    public void askToDeleteFolder(final long id){
        new AlertDialog.Builder(this)
                .setTitle(R.string.askDelete)
                .setMessage("")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        folderManager.deleteFolder(folderManager.getFolder(id));
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
    public void onBackPressed(){
        CustomExitDialog d = new CustomExitDialog(this);
        d.show();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(!sharedPref.getString("theme", "light").equals(currentTheme))
            recreate();
        else
            updatePrint();
    }

}
