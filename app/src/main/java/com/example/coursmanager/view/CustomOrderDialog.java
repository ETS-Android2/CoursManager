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

        switch (((FolderActivity) activity).order){
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
                switch (i){
                    case R.id.orderDate:
                        ((FolderActivity) activity).order = 1;
                        break;
                    case R.id.orderNameAsc:
                        ((FolderActivity) activity).order = 2;
                        break;
                    case R.id.orderNameDesc:
                        ((FolderActivity) activity).order = 3;
                        break;
                }

                getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit().putInt("orderFolder", ((FolderActivity) activity).order).apply();
                ((FolderActivity) activity).updatePrint();
                dismiss();
            }
        });
    }

}
