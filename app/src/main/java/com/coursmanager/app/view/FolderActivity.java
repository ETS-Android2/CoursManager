package com.coursmanager.app.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.FolderManager;
import com.coursmanager.app.controller.LessonManager;
import com.coursmanager.app.model.Folder;
import com.coursmanager.app.tools.MySQLite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FolderActivity extends AppCompatActivity {

    private String DB_FILEPATH;
    private String NEW_DB_FILEPATH;
    private FolderManager folderManager;
    private LessonManager lessonManager;
    public SharedPreferences sharedPref;
    private String currentTheme;
    public int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        this.currentTheme = sharedPref.getString("theme", "light");
        this.order = sharedPref.getInt("orderFolder", 1);

        this.DB_FILEPATH = this.getDatabasePath(MySQLite.DATABASE_NAME).toString();
        this.NEW_DB_FILEPATH = Environment.getExternalStorageDirectory().toString() + "/CoursManager/" + MySQLite.DATABASE_NAME;

        new File(Environment.getExternalStorageDirectory().toString() + "/CoursManager").mkdirs();

        //Log.d("DEBUG", NEW_DB_FILEPATH);

        setAppTheme(this);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        folderManager = new FolderManager(this);
        folderManager.open();

        lessonManager = new LessonManager(this);
        lessonManager.open();

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

            case R.id.action_delete_all:
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

            case R.id.action_import:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.importDatas)
                        .setMessage(R.string.askImport)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                verifyPermissionImportExport(2);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                break;

            case R.id.action_export:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.exportDatas)
                        .setMessage(R.string.askExport)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                verifyPermissionImportExport(1);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
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

        c = lessonManager.getAllLessonsToRead();
        fromFieldNames = new String[] {LessonManager.KEY_NAME_LESSON};
        toViewIDs = new int[] {R.id.textNameLesson};
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout_lesson_to_read, c, fromFieldNames, toViewIDs, 0);
        /*myCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if(view.getId() == R.id.textNameFolder){
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(FolderManager.KEY_NAME_FOLDER)));
                }else{
                    ((TextView) view).setText(cursor.getString(cursor.getColumnIndex(FolderManager.KEY_PERCENTAGE_FOLDER))+" %");
                }

                return true;
            }
        });*/

        ListView myList = findViewById(R.id.listViewLessonToRead);
        myList.setAdapter(myCursorAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), LessonMainActivity.class);
                intent.putExtra("idLesson", id);
                intent.putExtra("lessonName", lessonManager.getLesson(id).getNameLesson());
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

    /**
     *
     * @param request: 1 export / 2 import
     * @return: true if there is access, else false
     */
    private boolean verifyPermissionImportExport(int request) {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1]) == PackageManager.PERMISSION_GRANTED){
            if(request == 1) {
                try {
                    exportDatabase(NEW_DB_FILEPATH, DB_FILEPATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(request == 2) {
                try {
                    importDatabase(NEW_DB_FILEPATH, DB_FILEPATH);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recreate();
            }
            return true;
        }else{
            ActivityCompat.requestPermissions(this, permissions, request);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perm, @NonNull int[] grantResult){
        //If permissions have been accepted we can import
        if(grantResult[0] == PackageManager.PERMISSION_GRANTED)
            verifyPermissionImportExport(requestCode);
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String newDbPath, String oldDbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.

        folderManager.close();
        File newDb = new File(newDbPath);
        File oldDb = new File(oldDbPath);
        if (newDb.exists()) {
            FolderActivity.FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            folderManager.open();
            Toast.makeText(getApplicationContext(), R.string.imported, Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(getApplicationContext(), R.string.cantFindFile, Toast.LENGTH_LONG).show();
        return false;
    }

    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean exportDatabase(String saveDbPath, String currentDbPath) throws IOException {
        File savedDb = new File(saveDbPath);
        File currentDb = new File(currentDbPath);

        if (currentDb.exists() && savedDb.getParentFile().exists()) {
            FolderActivity.FileUtils.copyFile(new FileInputStream(currentDb), new FileOutputStream(savedDb));
            Toast.makeText(getApplicationContext(), R.string.exported, Toast.LENGTH_LONG).show();
            return true;
        }
        Toast.makeText(getApplicationContext(), R.string.cantFindFile, Toast.LENGTH_LONG).show();
        return false;
    }

    public static class FileUtils {
        /**
         * Creates the specified <code>toFile</code> as a byte for byte copy of the
         * <code>fromFile</code>. If <code>toFile</code> already exists, then it
         * will be replaced with a copy of <code>fromFile</code>. The name and path
         * of <code>toFile</code> will be that of <code>toFile</code>.<br/>
         * <br/>
         * <i> Note: <code>fromFile</code> and <code>toFile</code> will be closed by
         * this function.</i>
         *
         * @param fromFile
         *            - FileInputStream for the file to copy from.
         * @param toFile
         *            - FileInputStream for the file to copy to.
         */
        public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
            FileChannel fromChannel = null;
            FileChannel toChannel = null;
            try {
                fromChannel = fromFile.getChannel();
                toChannel = toFile.getChannel();
                fromChannel.transferTo(0, fromChannel.size(), toChannel);
            } finally {
                try {
                    if (fromChannel != null) {
                        fromChannel.close();
                    }
                } finally {
                    if (toChannel != null) {
                        toChannel.close();
                    }
                }
            }
        }
    }

}
