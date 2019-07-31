package com.example.coursmanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.coursmanager.R;

public class CustomOrderDialog extends Dialog {

    private Activity activity;

    CustomOrderDialog(@NonNull Activity a){
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_order_dialog);

        RadioGroup rg = findViewById(R.id.groupOrder);
        RadioButton nameAsc = findViewById(R.id.orderNameAsc);
        RadioButton nameDesc= findViewById(R.id.orderNameDesc);
        RadioButton date = findViewById(R.id.orderDate);

        int order = 1;
        if(activity instanceof FolderActivity)
            order = ((FolderActivity) activity).order;
        else if(activity instanceof UEActivity)
            order = ((UEActivity) activity).order;
        else if(activity instanceof SubjectActivity)
            order = ((SubjectActivity) activity).order;
        else
            order = ((LessonActivity) activity).order;

        switch (order){
            case 1:
                date.setChecked(true);
                break;
            case 2:
                nameAsc.setChecked(true);
                break;
            case 3:
                nameDesc.setChecked(true);
                break;
            default:
                date.setChecked(true);
                break;
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.orderDate:
                        if (activity instanceof FolderActivity)
                            ((FolderActivity) activity).order = 1;
                        else if (activity instanceof UEActivity)
                            ((UEActivity) activity).order = 1;
                        else if (activity instanceof SubjectActivity)
                            ((SubjectActivity) activity).order = 1;
                        else
                            ((LessonActivity) activity).order = 1;
                        break;

                    case R.id.orderNameAsc:
                        if (activity instanceof FolderActivity)
                            ((FolderActivity) activity).order = 2;
                        else if (activity instanceof UEActivity)
                            ((UEActivity) activity).order = 2;
                        else if (activity instanceof SubjectActivity)
                            ((SubjectActivity) activity).order = 2;
                        else
                            ((LessonActivity) activity).order = 2;
                        break;

                    case R.id.orderNameDesc:
                        if (activity instanceof FolderActivity)
                            ((FolderActivity) activity).order = 3;
                        else if (activity instanceof UEActivity)
                            ((UEActivity) activity).order = 3;
                        else if (activity instanceof SubjectActivity)
                            ((SubjectActivity) activity).order = 3;
                        else
                            ((LessonActivity) activity).order = 3;
                        break;
                }

                if (activity instanceof FolderActivity){
                    getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("orderFolder", ((FolderActivity) activity).order).apply();
                    ((FolderActivity) activity).updatePrint();
                }
                else if (activity instanceof UEActivity){
                    getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("orderUE", ((UEActivity) activity).order).apply();
                    ((UEActivity) activity).updatePrint();
                }
                else if(activity instanceof SubjectActivity) {
                    getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("orderSubject", ((SubjectActivity) activity).order).apply();
                    ((SubjectActivity) activity).updatePrint();
                }
                else{
                    getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("orderLesson", ((LessonActivity) activity).order).apply();
                    ((LessonActivity) activity).updatePrint();
                }

                dismiss();
            }
        });
    }

}
