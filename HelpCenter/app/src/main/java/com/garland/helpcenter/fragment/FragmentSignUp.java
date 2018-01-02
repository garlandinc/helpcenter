package com.garland.helpcenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Data;
import com.garland.helpcenter.utility.FragmentCallback;
import com.garland.helpcenter.utility.Utility;

/**
 * Created by lemon on 10/28/2017.
 */

public class FragmentSignUp extends Fragment {
    private View view;
    private FragmentCallback callback;
    private EditText mail,pass,ph1,ph2,n2;
    private Spinner div,dis;
    private String[] arr,arr2;
    private ArrayAdapter<String> disAdapter;
    private static String currentDiv,currentDis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view=inflater.inflate(R.layout.fragment_signup,container,false);
            mail=(EditText)view.findViewById(R.id.id_email);
            n2=(EditText)view.findViewById(R.id.id_name);
            pass=(EditText)view.findViewById(R.id.id_password);
            div=((Spinner)view.findViewById(R.id.division_spinner));
            dis=((Spinner)view.findViewById(R.id.district_spinner));
            ph1=((EditText)view.findViewById(R.id.id_phone1));
            ph2=((EditText)view.findViewById(R.id.id_phone2));
            arr=Utility.getContinent().keySet().toArray(new String[0]);
            currentDiv=arr[0];
            arr2=Utility.getContinent().get(currentDiv).toArray(new String[0]);
            currentDis=arr2[0];
            ArrayAdapter<String> divAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr);
            disAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr2);
            div.setAdapter(divAdapter);
            dis.setAdapter(disAdapter);

            div.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String si=arr[position];
                    if(si.equals(currentDiv)) return;
                    currentDiv=si;
                    arr2=Utility.getContinent().get(currentDiv).toArray(new String[0]);
                    currentDis=arr2[0];
                    disAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr2);
                    dis.setAdapter(disAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            dis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String si=arr2[position];
                    if(si.equals(currentDis)) return;
                    currentDis=si;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            view.findViewById(R.id.id_sign_up).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mailText=mail.getText().toString();
                    String passText=pass.getText().toString();
                    if(!mailText.isEmpty()&&!passText.isEmpty()) {
                        String phone1=ph1.getText().toString();
                        String phone2=ph2.getText().toString();
                        String name=n2.getText().toString();
                        callback.onCallback(new Data(mailText,passText,dis.getSelectedItem().toString(),div.getSelectedItem().toString(), name, phone1,phone2), MainActivity.CODE_SIGN_UP);
                        MainActivity.TITLE="Read First";
                        MainActivity.MESSAGE=getActivity().getString(R.string.text);
                        new DialogInsert().show(getActivity().getSupportFragmentManager(),"");
                        mail.setText("");
                        pass.setText("");
                        ph1.setText("");
                        ph2.setText("");
                        n2.setText("");
                    }
                }
            });
        } catch (Exception e) {
            MainActivity.TITLE="An Unexpected Error Occurred !";
            MainActivity.MESSAGE=e.getMessage();
            new DialogInsert().show(getActivity().getSupportFragmentManager(),"");
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        callback=(FragmentCallback)context;
        super.onAttach(context);
    }
}
