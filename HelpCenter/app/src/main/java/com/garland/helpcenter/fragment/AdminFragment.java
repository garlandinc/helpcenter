package com.garland.helpcenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Bin;
import com.garland.helpcenter.utility.Data;
import com.garland.helpcenter.utility.FragmentCallback;
import com.garland.helpcenter.utility.Stock;
import com.garland.helpcenter.utility.Utility;

/**
 * Created by lemon on 10/29/2017.
 */

public class AdminFragment extends Fragment {
    private View view;
    private EditText title,message,associationMsg,cmdPath,cmdVal;
    private Spinner serial,div,dis;
    private FragmentCallback callback;
    private String[] arr,arr2;
    private String[] serials={"1","2","3"};
    private ArrayAdapter<String> disAdapter;
    private static String currentDiv;

    //private DatabaseReference helpcenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_admin,container,false);
        setHasOptionsMenu(true);
        title= (EditText) view.findViewById(R.id.id_admin_title);
        message= (EditText) view.findViewById(R.id.id_admin_msg);
        view.findViewById(R.id.id_admin_msg_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCallback(new Bin(Integer.parseInt(serial.getSelectedItem().toString()),title.getText().toString(),message.getText().toString()),MainActivity.CODE_SHARE_MESSAGE);
                title.setText("");
                message.setText("");
            }
        });
        serial= (Spinner) view.findViewById(R.id.id_admin_title_spinner);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, serials);
        serial.setAdapter(adapter);
        serial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title.setText(MainActivity.getPref("t"+(position+1),""));
                message.setText(MainActivity.getPref("m"+(position+1),""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        div=((Spinner)view.findViewById(R.id.division_spinner));
        dis=((Spinner)view.findViewById(R.id.district_spinner));
        arr= Utility.getContinent().keySet().toArray(new String[0]);
        currentDiv=arr[0];
        arr2=Utility.getContinent().get(currentDiv).toArray(new String[0]);
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
                disAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr2);
                dis.setAdapter(disAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        associationMsg= (EditText) view.findViewById(R.id.id_inline_input);
        view.findViewById(R.id.id_inline_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onCallback(new Data(dis.getSelectedItem().toString(),div.getSelectedItem().toString(),associationMsg.getText().toString()),MainActivity.CODE_UPDATE_ASSOCIATION_MSG);
                associationMsg.setText("");
            }
        });

        cmdPath= (EditText) view.findViewById(R.id.id_admin_cmd_path);
        cmdVal= (EditText) view.findViewById(R.id.id_admin_cmd_val);
        cmdVal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    callback.onCallback(new Stock(cmdPath.getText().toString().split(","), cmdVal.getText().toString()),MainActivity.CODE_CMD);
                }
                return false;
            }
        });
        title.setOnLongClickListener(LONG_CLICK_EDIT_HANDLE);
        message.setOnLongClickListener(LONG_CLICK_EDIT_HANDLE);
        associationMsg.setOnLongClickListener(LONG_CLICK_EDIT_HANDLE);
        cmdVal.setOnLongClickListener(LONG_CLICK_EDIT_HANDLE);
        cmdPath.setOnLongClickListener(LONG_CLICK_EDIT_HANDLE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_admin,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_cmd_path){
            MainActivity.MESSAGE="association:mail,password,id,division,district,active,phone1,phone2,division,district,name\n\ncontinent:division:district:message,phone1,phone2,name\n\n@/#\n\n";
            new DialogInsert().show(getActivity().getSupportFragmentManager(),"");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        callback=(FragmentCallback)context;
        super.onAttach(context);
    }

    private View.OnLongClickListener LONG_CLICK_EDIT_HANDLE=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ((EditText)v).setText("");
            return false;
        }
    };
}
