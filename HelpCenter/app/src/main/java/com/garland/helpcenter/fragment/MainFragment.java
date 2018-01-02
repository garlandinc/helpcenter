package com.garland.helpcenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garland.helpcenter.MainActivity;
import com.garland.helpcenter.R;
import com.garland.helpcenter.utility.Callback;
import com.garland.helpcenter.utility.FragmentCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by lemon on 10/29/2017.
 */

public class MainFragment extends Fragment {
    private View view;
    private TextView t1,t2,t3,m1,m2,m3,t,m;
    private FragmentCallback callback;
    private DatabaseReference ref;
    private String msg,tit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_main,container,false);
        t1= (TextView) view.findViewById(R.id.id_title1);
        t2= (TextView) view.findViewById(R.id.id_title2);
        t3= (TextView) view.findViewById(R.id.id_title3);
        m1= (TextView) view.findViewById(R.id.message1);
        m2= (TextView) view.findViewById(R.id.message2);
        m3= (TextView) view.findViewById(R.id.message3);
        t= (TextView) view.findViewById(R.id.title);
        m= (TextView) view.findViewById(R.id.message);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        callback=(FragmentCallback)context;
        msg=MainActivity.getPref("m","");
        tit=MainActivity.getPref("t","");
        callback.addCallback(new Callback() {
            @Override
            public void doTask(Object obj) {
                ref=(DatabaseReference)obj;
                ref.addValueEventListener(t1v);
            }
        }, MainActivity.FOR_MAIN_FRAGMENT);
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        ref.removeEventListener(t1v);
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        t1.setText(MainActivity.getPref("t1","Message From VC"));
        m1.setText(MainActivity.getPref("m1","Come and Love our Campus.It's give you The best opportunity..."));
        t2.setText(MainActivity.getPref("t2","Message From Our Help Center"));
        m2.setText(MainActivity.getPref("m2","Hello everyone, How are you...\nKeep Connected with us..."));
        t3.setText(MainActivity.getPref("t3","Message From Our Campus"));
        m3.setText(MainActivity.getPref("m3","Come and get admitted in our varsity.It is the 1st Digital University in Our Country, with great lab facilities..."));
    }

    private ValueEventListener t1v=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot data) {
            String t1t=data.child("title1").getValue().toString();
            String t2t=data.child("title2").getValue().toString();
            String t3t=data.child("title3").getValue().toString();
            String m1t=data.child("message1").getValue().toString();
            String m2t=data.child("message2").getValue().toString();
            String m3t=data.child("message3").getValue().toString();
            if(data.hasChild("title")){
                tit=data.child("title").getValue().toString();
                msg=data.child("message").getValue().toString();
            }
            callback.onCallback(null,MainActivity.CONNECT_OFF);
            t1.setText(t1t);
            t2.setText(t2t);
            t3.setText(t3t);
            m1.setText(m1t);
            m2.setText(m2t);
            m3.setText(m3t);
            if(!tit.isEmpty()){
                m.setText(msg);
                t.setText(tit);
            }
            MainActivity.setPref(new String[]{"t1","t2","t3","t","m1","m2","m3","m"},new String[]{t1t,t2t,t3t,tit,m1t,m2t,m3t,msg});
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
