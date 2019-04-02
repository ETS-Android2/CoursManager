package com.example.coursmanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.UEManager;
import com.example.coursmanager.model.UE;

public class MainActivity extends AppCompatActivity {

    // Manager of UE db
    private UEManager ueManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create and open the ueManager
        ueManager = new UEManager(this);
        ueManager.open();

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Update the listView of UEs
    public void updatePrint(){
        Cursor c = ueManager.getAllUE();
        String[] fromFieldNames = new String[] {UEManager.KEY_NAME_UE, UEManager.KEY_PERCENTAGE_UE};
        int[] toViewIDs = new int[] {R.id.textNameUE, R.id.textPercentageUE};
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, c, fromFieldNames, toViewIDs, 0);
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
                        if(ueManager.addUE(new UE(0, editTextUE.getText().toString(), 0)) == -1){
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

}
