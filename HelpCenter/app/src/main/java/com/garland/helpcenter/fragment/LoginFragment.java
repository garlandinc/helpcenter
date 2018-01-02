package com.garland.helpcenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Data;
import com.garland.helpcenter.utility.FragmentCallback;

/**
 * Created by lemon on 10/28/2017.
 */

public class LoginFragment extends Fragment {
    private View view;
    private FragmentCallback callback;
    private EditText mail,pass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login,container,false);
        mail= (EditText) view.findViewById(R.id.id_login_mail);
        pass= (EditText) view.findViewById(R.id.id_login_password);
        Button btn= (Button) view.findViewById(R.id.id_login_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailText=mail.getText().toString();
                String passText=pass.getText().toString();
                if(!mailText.isEmpty()&&!passText.isEmpty()) {
                    callback.onCallback(new Data(mailText, passText), MainActivity.CODE_LOGIN);
                    mail.setText("");
                    pass.setText("");
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        callback=(FragmentCallback)context;
        super.onAttach(context);
    }
}
