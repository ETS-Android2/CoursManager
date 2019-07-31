package com.example.coursmanager.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.coursmanager.R;

public class CustomExitDialog extends Dialog implements View.OnClickListener {

    private Activity a;

    CustomExitDialog(@NonNull Activity activity) {
        super(activity);
        this.a = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_exit_dialog);

        Button yes = findViewById(R.id.btn_yes);
        Button no = findViewById(R.id.btn_no);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_yes:
                a.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
    }
}
