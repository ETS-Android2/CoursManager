package com.example.coursmanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.coursmanager.R;
import com.example.coursmanager.controller.FolderManager;
import com.example.coursmanager.controller.LessonManager;
import com.example.coursmanager.controller.Manager;
import com.example.coursmanager.controller.SubjectManager;
import com.example.coursmanager.controller.UEManager;

public class CustomLongClickDialog extends Dialog implements View.OnClickListener {

    private EditText name;
    private long id;
    private Manager manager;
    private Activity a;
    private int type; //Type will be usefull for me to know if the Dialog is called by FolderManager(1), UEManager(2), SubjectManager(3) or LessonManager(4)

    CustomLongClickDialog(@NonNull Activity a, long id, int t) {
        super(a);
        this.a = a;
        this.id = id;
        this.type = t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_long_click_dialog);

        name = findViewById(R.id.editRename);
        Button delete = findViewById(R.id.btnDelete);
        TextView rename = findViewById(R.id.btnRename);

        delete.setOnClickListener(this);
        rename.setOnClickListener(this);

        switch (type){
            case 1:
                manager = new FolderManager(getContext());
                manager.open();
                name.setText(((FolderManager) manager).getFolder(id).getNameFolder());
                break;
            case 2:
                manager = new UEManager(getContext());
                manager.open();
                name.setText(((UEManager) manager).getUE(id).getNameUE());
                break;
            case 3:
                manager = new SubjectManager(getContext());
                manager.open();
                name.setText(((SubjectManager) manager).getSubject(id).getNameSubject());
                break;
            case 4:
                manager = new LessonManager(getContext());
                manager.open();
                name.setText(((LessonManager) manager).getLesson(id).getNameLesson());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnDelete:
                switch (type){
                    case 1:
                        ((FolderActivity) a).askToDeleteFolder(id);
                        break;
                    case 2:
                        ((UEActivity) a).askToDeleteUE(id);
                        break;
                    case 3:
                        ((SubjectActivity) a).askToDeleteSubject(id);
                        break;
                    case 4:
                        ((LessonActivity) a).askToDeleteLesson(id);
                        break;
                    default:
                        break;
                }
                dismiss();
                break;
            case R.id.btnRename:
                switch (type){
                    case 1:
                        manager.rename(name.getText().toString(), id);
                        ((FolderActivity) a).updatePrint();
                        break;
                    case 2:
                        manager.rename(name.getText().toString(), id);
                        ((UEActivity) a).updatePrint();
                        break;
                    case 3:
                        manager.rename(name.getText().toString(), id);
                        ((SubjectActivity) a).updatePrint();
                        break;
                    case 4:
                        manager.rename(name.getText().toString(), id);
                        ((LessonActivity) a).updatePrint();
                        break;
                    default:
                        break;
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
