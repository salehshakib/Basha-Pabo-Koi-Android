package com.example.bashapabokoi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
        //todo ehan e edit kora lagbo

        /*HashMap<String, Object> phoneNo = new HashMap<>();
        phoneNo.put("phoneNumber", editContent.getText());*/


        Toast.makeText(context, "Phone Number changed successfully!", Toast.LENGTH_SHORT).show();
    }

    private void editEmail(){
        //todo ehan e edit kora lagbo
        HashMap<String, Object> email = new HashMap<>();
        email.put("email", editContent.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(email);

        Toast.makeText(context, "Email changed successfully!", Toast.LENGTH_SHORT).show();
    }

    private void editAddress(){
    //todo ehan e edit kora lagbo

        HashMap<String , Object> address = new HashMap<>();
        address.put("address", editContent.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(address);
        Toast.makeText(context, "Address changed successfully!", Toast.LENGTH_SHORT).show();
    }
}
