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
import android.widget.Spinner;
import android.widget.TextView;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Callback;
import com.garland.helpcenter.utility.FragmentCallback;
import com.garland.helpcenter.utility.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by lemon on 10/27/2017.
 */

public class AssociationFragment extends Fragment {

    private View view;
    private Spinner div,dis;
    private String[] arr,arr2;
    private ArrayAdapter<String> disAdapter;
    private static String currentDiv="Barisal",currentDis="Barguna";

    private FragmentCallback callback;
    private TextView associationName,associationCont,associationMsg;
    private DatabaseReference root,distRef;
    private ValueEventListener dataChanged;
    private boolean shouldLoad=true;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            view=inflater.inflate(R.layout.fragment_association,container,false);
            div=((Spinner)view.findViewById(R.id.division_spinner));
            dis=((Spinner)view.findViewById(R.id.district_spinner));
            associationCont= (TextView) view.findViewById(R.id.id_association_cont_text);
            associationName= (TextView) view.findViewById(R.id.id_association_name_text);
            associationMsg= (TextView) view.findViewById(R.id.id_association_msg_text);
            loadMessageUI();
            div.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String si=arr[position];
                    if(si.equals(currentDiv)) return;
                    currentDiv=si;
                    arr2=Utility.getContinent().get(currentDiv).toArray(new String[0]);
                    currentDis=arr2[0];
                    MainActivity.setPref(new String[]{"divPos","disPos"},new int[]{position,0});
                    associationName.setText("Association of:"+currentDis);
                    disAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr2);
                    dis.setAdapter(disAdapter);
                    try {
                        analyze();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
                    associationName.setText("Association of:"+currentDis);
                    MainActivity.setPref("disPos",position);
                    try {
                        analyze();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            analyze();
        } catch (Exception e) {
            callback.onCallback(e,MainActivity.EXCEPTION);
        }

        return view;
    }

    private void analyze() throws Exception {
        shouldLoad=true;
        removeListeners();
        associationCont.setText(MainActivity.getPref(currentDiv+currentDis+"cont","To Get information update you need to connect with internet and keep waiting a Moment"));
        associationMsg.setText(MainActivity.getPref(currentDiv+currentDis+"msg","To Get information updateMessage of your District's Association you need to connect with internet and keep waiting a Moment"));
        distRef=root.child("continent").child(currentDiv).child(currentDis);
        dataChanged=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                if(snap.hasChild("message")){
                    String msg=snap.child("message").getValue().toString();
                    if(shouldLoad&&snap.hasChild("phone2")){
                        shouldLoad=false;
                        associationMsg.setText(msg);
                        String info="Phone No 1:"+snap.child("phone1").getValue().toString()+"\nPhone No 2:"+
                                snap.child("phone2").getValue().toString()+"\nName:"+
                                snap.child("name").getValue();
                        associationCont.setText(info);
                        MainActivity.setPref(currentDiv+currentDis+"cont",info);
                        MainActivity.setPref(currentDiv+currentDis+"msg",msg);
                    }
                    else {
                        associationCont.setText("Your targeted Association is not Activated, To get help continuously from your Association inform to your elder brother of "+currentDis+" in our campus, to activate their Association..");
                        associationMsg.setText(msg+"\n\nThis Message if provide by our System for someones special request...");
                        MainActivity.setPref(currentDiv+currentDis+"msg",msg+"\n\nThis Message if provide by our System for someones special request...");
                        MainActivity.setPref(currentDiv+currentDis+"msg","Your targeted Association is not Activated, To get help continuously from your Association inform to your elder brother of "+currentDis+" in our campus, to activate their Association..");
                    }
                }
                else {
                    String ct="Your targeted Association is not Activated, To get help continuously from your Association inform to your elder brother of "+currentDis+" in our campus, to activate their Association..";
                    String mt="Sorry your Association is not active yet!. Please inform or request to your elder brothers of District:"+currentDis+" in our campus to activate the association for students welfare.";
                    associationMsg.setText(mt);
                    associationCont.setText(ct);
                }

                callback.onCallback(null,MainActivity.CONNECT_OFF);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCallback(null,MainActivity.CONNECT_OFF);
            }
        };
        callback.onCallback(null,MainActivity.REQUEST_ONLINE);
        distRef.addValueEventListener(dataChanged);
    }

    private void removeListeners() {
        if(dataChanged!=null&&distRef!=null)
            distRef.removeEventListener(dataChanged);
    }

    private void loadMessageUI() {
        int i=MainActivity.getPrefInt("divPos",0);
		arr= Utility.getContinent().keySet().toArray(new String[0]);
		currentDiv=arr[i];
		ArrayAdapter<String> divAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr);
		div.setAdapter(divAdapter);
		div.setSelection(i);
		int j=MainActivity.getPrefInt("disPos",0);
		arr2=Utility.getContinent().get(currentDiv).toArray(new String[0]);
		currentDis=arr2[j];
		disAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, arr2);
		dis.setAdapter(disAdapter);
        dis.setSelection(j);
        associationName.setText("Association of:"+dis.getSelectedItem().toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback=(FragmentCallback)context;
        callback.addCallback(new Callback() {
            @Override
            public void doTask(Object obj) {
                root=(DatabaseReference)obj;
            }
        }, MainActivity.FOR_ASSOCIATION_CLIENT);
    }

    @Override
    public void onDetach() {
        removeListeners();
        super.onDetach();
    }
}
