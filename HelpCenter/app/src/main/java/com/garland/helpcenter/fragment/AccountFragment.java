package com.garland.helpcenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Data;
import com.garland.helpcenter.utility.FragmentCallback;

/**
 * Created by lemon on 10/29/2017.
 */

public class AccountFragment extends Fragment {
    private View view;
    private EditText textMessage;
    private String name,div,dis,phone;
    private TextView textView;
    private FragmentCallback callback;
    private Button button;
    private String active="no";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view=inflater.inflate(R.layout.fragment_account,container,false);
            textMessage= (EditText) view.findViewById(R.id.id_acc_text);
            textView= (TextView) view.findViewById(R.id.text_msg);
            textMessage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(textMessage.getText().toString().isEmpty())
                        button.setEnabled(false);
                    else button.setEnabled(true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            button= (Button) view.findViewById(R.id.id_acc_btn);
            button.setEnabled(false);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg=textMessage.getText().toString()+"\n\nThis Message Updated by:\n"+name+"\nContact:"+phone+"";
                    callback.onCallback(new Data(dis,div,msg),MainActivity.CODE_UPDATE_ASSOCIATION_MSG);
                    textMessage.setText("");
                }
            });
            updateUI();
        } catch (Exception e) {
            callback.onCallback(e,MainActivity.EXCEPTION);
        }
        return view;
    }

    private void analyze() {
        try {
            String txt= MainActivity.getPrefFromUID();
            String[] vals=txt.split("~");
            name=vals[0];
            div=vals[1];
            dis=vals[2];
            phone=vals[3];
            active=vals[4];
        } catch (Exception e) {
            callback.onCallback(null,MainActivity.CODE_LOG_OUT);
        }
    }

    private void updateUI() {
        analyze();
        if(active.contains("no")){
            textMessage.setText("Your Account is not activated...\nBecause of security reason we permit only activated person to post their association.\nIf you prove yourself that you are from "+dis+" District then we activate your account...");
            textMessage.setEnabled(false);
        }
        textView.setText("Association of: "+dis);
    }

    @Override
    public void onAttach(Context context) {
        callback=(FragmentCallback)context;
        super.onAttach(context);
    }
}
