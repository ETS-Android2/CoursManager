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
import com.example.coursmanager.controller.Manager;

public class CustomLongClickDialog extends Dialog implements View.OnClickListener {

    private EditText name;
    private Button delete;
    private TextView rename;
    private long id;
    private Manager manager;
    private Activity a;
    private int type; //Type will be usefull for me to know if the Dialog is called by FolderManager(1), UEManager(2), SubjectManager(3) or LessonManager(4)

    public CustomLongClickDialog(@NonNull Activity a, long id, int t) {
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
        delete = findViewById(R.id.btnDelete);
        rename = findViewById(R.id.btnRename);

        delete.setOnClickListener(this);
        rename.setOnClickListener(this);

        switch (type){
            case 1:
                manager = new FolderManager(getContext());
                manager.open();
                name.setText(((FolderManager) manager).getFolder(id).getNameFolder());
                break;
            case 2:
                manager = new FolderManager(getContext());
                manager.open();
                name.setText(((FolderManager) manager).getFolder(id).getNameFolder());
                break;
            case 3:
                manager = new FolderManager(getContext());
                manager.open();
                name.setText(((FolderManager) manager).getFolder(id).getNameFolder());
                break;
            case 4:
                manager = new FolderManager(getContext());
                manager.open();
                name.setText(((FolderManager) manager).getFolder(id).getNameFolder());
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
                        ((FolderActivity) a).askToDeleteFolder(id);
                        break;
                    case 3:
                        ((FolderActivity) a).askToDeleteFolder(id);
                        break;
                    case 4:
                        ((FolderActivity) a).askToDeleteFolder(id);
                        break;
                    default:
                        break;
                }
                dismiss();
                break;
            case R.id.btnRename:
                switch (type){
                    case 1:
                        ((FolderManager) manager).renameAFolder(name.getText().toString(), id);
                        ((FolderActivity) a).updatePrint();
                        break;
                    case 2:
                        ((FolderManager) manager).renameAFolder(name.getText().toString(), id);
                        ((FolderActivity) a).updatePrint();
                        break;
                    case 3:
                        ((FolderManager) manager).renameAFolder(name.getText().toString(), id);
                        ((FolderActivity) a).updatePrint();
                        break;
                    case 4:
                        ((FolderManager) manager).renameAFolder(name.getText().toString(), id);
                        ((FolderActivity) a).updatePrint();
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
