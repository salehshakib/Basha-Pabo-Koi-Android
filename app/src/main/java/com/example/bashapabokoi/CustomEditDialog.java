package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CustomEditDialog extends Dialog implements View.OnClickListener {

    Context context;

    String title, hint;
    int editCode;

    Button yes, no;
    TextView titleText;
    EditText editContent;

    public CustomEditDialog(@NonNull Context context, String title, String hint, int editCode) {
        super(context);

        this.context = context;
        this.title = title;
        this.hint = hint;
        this.editCode = editCode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        yes = findViewById(R.id.btn_yes);
        no = findViewById(R.id.btn_no);
        titleText = findViewById(R.id.custom_dialog_title);
        editContent = findViewById(R.id.editable_content);

        titleText.setText(title);
        editContent.setHint(hint);

        if(editCode == 3){

            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500);
            layoutParams.setMargins(10, 0, 10, 10);

            editContent.setLayoutParams(layoutParams);
            editContent.setGravity(android.view.Gravity.TOP| Gravity.START);
            editContent.setSingleLine(false);
            editContent.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

        }

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_yes:

                if(editCode == 1){

                    editPhoneNo();
                } else if(editCode == 2){

                    editEmail();
                } else if(editCode == 3){

                    editAddress();
                }

                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private void editPhoneNo(){

        Toast.makeText(context, "Phone Number changed successfully!", Toast.LENGTH_SHORT).show();
    }

    private void editEmail(){

        Toast.makeText(context, "Email changed successfully!", Toast.LENGTH_SHORT).show();
    }

    private void editAddress(){

        Toast.makeText(context, "Address changed successfully!", Toast.LENGTH_SHORT).show();
    }
}
