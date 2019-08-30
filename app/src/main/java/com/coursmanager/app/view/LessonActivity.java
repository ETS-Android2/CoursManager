package com.coursmanager.app.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coursmanager.app.R;
import com.coursmanager.app.controller.LessonManager;
import com.coursmanager.app.model.Lesson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LessonActivity extends AppCompatActivity {

    private LessonManager lessonManager;
    private long idSubject;
    public int order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FolderActivity.setAppTheme(this);
        setContentView(R.layout.activity_lesson);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.order = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).getInt("orderLesson", 1);

        this.lessonManager = new LessonManager(this);
        this.lessonManager.open();

        Intent intent = getIntent();
        setTitle(intent.getStringExtra("subjectName"));
        this.idSubject = intent.getLongExtra("idSubject", 0);

        updatePrint();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                submitLesson();
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
                                lessonManager.deleteAllLessonSubject(idSubject);
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
                CustomOrderDialog cod = new CustomOrderDialog(LessonActivity.this);
                cod.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updatePrint(){
        Cursor c = lessonManager.getAllLessonSubject(idSubject, order);
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
                CustomLongClickDialog cd = new CustomLongClickDialog(LessonActivity.this, id, 4);
                cd.show();
                return true;
            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void submitLesson() {
        LinearLayout layout = new LinearLayout(this);
        final Calendar myCalendar = Calendar.getInstance();
        final EditText editText = new EditText(this);
        final EditText editTextTeach = new EditText(this);
        final EditText editDateJ0 = new EditText(this);
        final CheckBox checkMethodJ = new CheckBox(this);
        final EditText editDateMax = new EditText(this);
        final Spinner selectRythm = new Spinner(this);
        final Spinner selectFirstRead = new Spinner(this);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final DatePickerDialog.OnDateSetListener dateJ0 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                editDateJ0.setText(sdf.format(myCalendar.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener dateMax = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                editDateMax.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editDateJ0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LessonActivity.this, dateJ0, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editDateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LessonActivity.this, dateMax, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editText.setHint(R.string.defaultNameLesson);
        editTextTeach.setHint(R.string.defaultNameTeach);
        editDateJ0.setHint("J0: 1990-12-01");
        editDateMax.setHint("JMax: 1990-12-31");

        ArrayList<String> firstReadArray = new ArrayList<>();
        firstReadArray.add(getResources().getString(R.string.selectFirstJ));
        firstReadArray.add("J+1");
        firstReadArray.add("J+2");
        firstReadArray.add("J+3");
        firstReadArray.add("J+4");
        firstReadArray.add("J+5");
        ArrayAdapter<String> firstReadAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, firstReadArray);
        selectFirstRead.setAdapter(firstReadAdapter);

        ArrayList<String> rythmArray = new ArrayList<>();
        rythmArray.add(getResources().getString(R.string.selectRythm));
        rythmArray.add("3-6-12-...");
        rythmArray.add("4-8-16-...");
        rythmArray.add("5-10-20-...");
        rythmArray.add("6-12-24-...");
        rythmArray.add("7-14-28-...");
        rythmArray.add("8-16-32-...");
        ArrayAdapter<String> rythmAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, rythmArray);
        selectRythm.setAdapter(rythmAdapter);

        checkMethodJ.setText(R.string.jMethod);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editText);
        layout.addView(editTextTeach);
        layout.addView(editDateJ0);
        layout.addView(checkMethodJ);
        layout.addView(editDateMax);
        layout.addView(selectFirstRead);
        layout.addView(selectRythm);

        layout.setGravity(Gravity.CENTER);

        new AlertDialog.Builder(this)
                .setTitle(R.string.titleAddLesson)
                .setMessage("")
                .setView(layout)
                .setPositiveButton(R.string.btnAdd, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Create a UE in db
                        if((checkMethodJ.isChecked() && !editDateMax.getText().toString().isEmpty() && selectFirstRead.getSelectedItemPosition() != 0 && selectRythm.getSelectedItemPosition() != 0) || !checkMethodJ.isChecked()) {
                            //Try to get the first date of rereading
                            try {
                                myCalendar.setTime(sdf.parse(editDateJ0.getText().toString()));
                                myCalendar.add(Calendar.DAY_OF_MONTH, selectFirstRead.getSelectedItemPosition() + 1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (lessonManager.addLesson(new Lesson(0, editText.getText().toString(), editTextTeach.getText().toString(), editDateJ0.getText().toString(), "", false, idSubject, 10, 0, selectRythm.getSelectedItemPosition() + 2, selectFirstRead.getSelectedItemPosition(), editDateMax.getText().toString(), checkMethodJ.isChecked(), sdf.format(myCalendar.getTime()))) == -1) {
                                Toast.makeText(getApplicationContext(), R.string.lessonAddError, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(), R.string.lessonAddGood, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), R.string.lessonAddFields, Toast.LENGTH_LONG).show();
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
