package com.example.bashapabokoi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginTabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        EditText num = root.findViewById(R.id.phone_no);
        EditText pass = root.findViewById(R.id.pass);
        TextView forgotPass = root.findViewById(R.id.forgot_pass);
        Button login = root.findViewById(R.id.but_login);

        num.setTranslationX(800);
        pass.setTranslationX(800);
        forgotPass.setTranslationX(800);
        login.setTranslationX(800);

        num.setAlpha(0f);
        pass.setAlpha(0f);
        forgotPass.setAlpha(0f);
        login.setAlpha(0f);

        num.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), com.example.bashapabokoi.abbehSaleh.PhoneNumberVerificationActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
