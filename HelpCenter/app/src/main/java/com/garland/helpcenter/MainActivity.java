package com.garland.helpcenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.garland.helpcenter.fragment.AboutFragment;
import com.garland.helpcenter.fragment.AccountFragment;
import com.garland.helpcenter.fragment.AdminFragment;
import com.garland.helpcenter.fragment.ApplicantAccountFragment;
import com.garland.helpcenter.fragment.AssociationFragment;
import com.garland.helpcenter.fragment.DialogInsert;
import com.garland.helpcenter.fragment.FragmentSignUp;
import com.garland.helpcenter.fragment.LoginFragment;
import com.garland.helpcenter.fragment.MainFragment;
import com.garland.helpcenter.utility.Bin;
import com.garland.helpcenter.utility.Callback;
import com.garland.helpcenter.utility.Data;
import com.garland.helpcenter.utility.FragmentCallback;
import com.garland.helpcenter.utility.Stock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,FragmentCallback{
    public static final int PERMISSION_LOCATION=2343;
    public static final int CODE_SIGN_UP = 190;
    public static final int CODE_LOGIN = 192;
    public static final int CODE_SHARE_MESSAGE=193;
    public static final int FOR_MAIN_FRAGMENT = 590;
    public static final int CODE_UPDATE_ASSOCIATION_MSG = 654;
    public static final int CODE_CMD = 656;
    public static final int FOR_ASSOCIATION_CLIENT = 787;
    public static final int EXCEPTION = 404;
    public static final int REQUEST_ONLINE = 121;
    public static final int FOR_ACCOUNT_FRAG = 876;
    public static final int CONNECT_OFF = 987;
    public static final int CODE_LOG_OUT = 199;


    public static String MESSAGE = "Message";
    public static String TITLE = "Message";

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private static boolean isAssociation=true;

    private static SharedPreferences preferences;
    private boolean loginForAssociation=true;
    private boolean shouldConnect =false;
    private Random random;
    private static String UID="";
    private boolean firstTimeLogIn=false;

    private Handler handler;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            preferences=getSharedPreferences("helpcenter",MODE_PRIVATE);
            firebaseAuth=FirebaseAuth.getInstance();
            database=FirebaseDatabase.getInstance();
            random=new Random();
            handler=new Handler();
            progress=new ProgressDialog(this);
            goToOnline();
        } catch (Exception e) {
            crushReports(e);
        }

    }

    private void goToOnline() {
        database.goOnline();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!shouldConnect) {
                    goToOffline();
                }
            }
        }, random.nextInt(30000)+random.nextInt(120000));
    }

    private void goToOffline() {
        database.goOffline();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToOnline();
            }
        }, random.nextInt(5000)+random.nextInt(5000));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this).setTitle("Are You Sure?").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("Cancel",null).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_uid) {
            String uid="You have no UID. To get UID you have to login first or create account";
            if(!UID.isEmpty()) uid=UID;
            TITLE="Your User ID";
            MainActivity.MESSAGE=uid;
            new DialogInsert().show(getSupportFragmentManager(),"");

        }

        else if(id==R.id.id_action_search) {
            startActivity(new Intent(this,MapsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_account:
                isAssociation=true;
                firstTimeLogIn=false;
                gotoAccount();
                break;
            case R.id.nav_association_activate:
                loadFragment(new FragmentSignUp());
                break;
            case R.id.nav_association_help:
                loadFragment(new AssociationFragment());
                break;
            /*case R.id.nav_create_applicant:
                loadFragment(new ApplicantRegister());
                break;*/
            case R.id.nav_recent_activity:
                loadFragment(new MainFragment());
                break;
            case R.id.nav_search:
                startActivity(new Intent(this,MapsActivity.class));
                break;
            /*case R.id.nav_applicant_account:
                isAssociation=false;
                firstTimeLogIn=false;
                gotoAccount();
                break;*/
            case R.id.nav_exit_app:
                finish();
                break;
            case R.id.nav_about:
                loadFragment(new AboutFragment());
                break;
            case R.id.nav_log_out:
                firebaseAuth.signOut();
                loadFragment(new MainFragment());
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoAccount() {
        try {
            if(firebaseAuth.getCurrentUser()==null) loadFragment(new LoginFragment());
            else{
                load();
                String mail=firebaseAuth.getCurrentUser().getEmail();
                if(mail.equals("lemon@gmail.com")){
                    showAdmin();
                }
                else {
                    if(loginForAssociation) {
                        if(mail.startsWith("applicant_")){
                            showAlert("Sorry!\nYour Account is not relate to that service");
                            return;
                        }
                        else {
                            if(firstTimeLogIn){
                                showAlert("Please wait sometime, for first time log in at new device, it tokks some time...","Look Here...");
                                return;
                            }
                            loadFragment(new AccountFragment());
                        }
                    }
                    else {
                        if(!mail.startsWith("applicant_")){
                            showAlert("Sorry!\nYour Account is not relate to that service");
                            return;
                        }
                        else {
                            if(firstTimeLogIn){
                                showAlert("Please wait sometime, for first time log in at new device, it tokks some time...","Look Here...");
                                return;
                            }
                            loadFragment(new ApplicantAccountFragment());
                        }
                    }
                }
                unload();
            }
        } catch (Exception e) {
            crushReports(e);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public void onCallback(Object obj,int code) {
        switch (code){
            case CODE_SIGN_UP:
                connectOneMin();
                createUser((Data)obj);
                break;
            case CODE_LOGIN:
                connectOneMin();
                logIn((Data)obj);
                break;
            case CODE_SHARE_MESSAGE:
                connectOneMin();
                Bin bin=(Bin)obj;
                shareMessage(bin.getCode(),bin.getTitle(),bin.getMessage());
                break;
            case CODE_UPDATE_ASSOCIATION_MSG:
                connectOn();
                updateAssociationMessage((Data)obj);
                break;
            case CODE_CMD:
                connectOn();
                doCommand((Stock)obj);
                break;
            case EXCEPTION:
                crushReports((Exception)obj);
                break;
            case REQUEST_ONLINE:
                connectOn();
                break;
            case CONNECT_OFF:
                database.goOffline();
                break;
            case CODE_LOG_OUT:
                firebaseAuth.signOut();
                loadFragment(new MainFragment());
                break;
        }
    }

    private void connectOneMin() {
        shouldConnect=true;
        database.goOnline();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(shouldConnect){
                    shouldConnect=false;
                    database.goOffline();
                }
            }
        }, 50000);
    }

    private void connectOff() {
        if(shouldConnect){
            shouldConnect=false;
            database.goOffline();
        }
    }

    private void connectOn() {
        shouldConnect=true;
        database.goOnline();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(shouldConnect){
                    shouldConnect=false;
                    database.goOffline();
                }
            }
        }, 30000);
    }

    private void doCommand(Stock stock) {
        try {
            load();
            DatabaseReference reference=database.getReference("helpcenter");
            for(String path:stock.getStock())
                reference=reference.child(path);
            reference.setValue(stock.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        showToast("Successfully completed");
                    else showToast("Failed to respond the command");
                    unload();
                    connectOff();
                }
            });
        } catch (Exception e) {
            crushReports(e);
        }finally {
            unload();
        }
    }

    public void shareMessage(int code,String title,String message) {
        try {
            load();
            DatabaseReference messageRef=getRoot().child("message");
            messageRef.child("title"+code).setValue(title);
            messageRef.child("message"+code).setValue(message);
        } catch (Exception e) {
            crushReports(e);
        } finally {
            connectOff();
            unload();
        }
    }

    public void load(){
        progress.setMessage("please wait!");
        progress.show();
    }

    public void unload(){
        progress.dismiss();
    }

    private void logIn(final Data data) {
        try {
            load();
            if(firebaseAuth!=null) {
                String email=data.getMail();
                if(!isAssociation) email="applicant_"+data.getMail();
                firebaseAuth.signInWithEmailAndPassword(email,data.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        showToast("Successfully Logged in");
                        loginForAssociation=isAssociation;
                        if(data.getMail().equals("lemon@gmail.com")) {
                            connectOff();
                            unload();
                            showAdmin();
                        }
                        else {
                            UID=firebaseAuth.getCurrentUser().getUid();
                            if(getPref(UID,"").isEmpty()){
                                showAlert("For First time logIn in new device it takes a while...","Please Wait!");
                                firstTimeLogIn=true;
                                getRoot().child("association").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snap) {
                                        String name=snap.child("name").getValue().toString();
                                        String div=snap.child("division").getValue().toString();
                                        String dis=snap.child("district").getValue().toString();
                                        String phone=snap.child("phone1").getValue().toString();
                                        String active=snap.child("active").getValue().toString();
                                        firstTimeLogIn=false;
                                        connectOff();
                                        unload();
                                        showAlert("Your LogIn is Completed!\nEnjoy it!","Welcome "+name);
                                        setPrefForUID(""+name+"~"+div+"~"+dis+"~"+phone+"~"+active);
                                        gotoAccount();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        connectOff();
                                        unload();

                                    }
                                });
                            }
                            else {
                                gotoAccount();
                                connectOff();
                                unload();
                            }
                        }
                    }
                    else {
                        connectOff();
                        unload();
                        showAlert("Error on Login.Please check your internet connection user-id and password");
                    }
                    }
                });
            }
        } catch (Exception e) {
            crushReports(e);
        }
    }

    public void updateAssociationMessage(Data data) {
        load();
        getRoot().child("continent").child(data.getDivision()).child(data.getDistrict()).child("message").setValue(data.getMessage()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                unload();
                if(task.isSuccessful())
                    showToast("Message updated..");
                else {
                    MESSAGE="Sorry! You are not approved user to update this message,or you are in Offline...";
                    TITLE="An Error Occurred!";
                    new DialogInsert().show(getSupportFragmentManager(),"");
                }
                connectOff();

            }
        });
    }

    public void updateApplicants(String uid,String message) {
        DatabaseReference applicantsRoot=getRoot().child("applicants");
        applicantsRoot.child(uid).child("message").setValue(message);
    }

    private DatabaseReference getRoot() {
        return database.getReference("helpcenter");
    }

    private void showAdmin() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setPositiveButton("Admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFragment(new AdminFragment());
            }
        });
        builder.setNegativeButton("Association", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadFragment(new AccountFragment());
            }
        });
        builder.create().show();
    }

    private void showToast(String s) {
        showToast(s,Toast.LENGTH_SHORT);
    }

    private void createUser(final Data data) {
        if(firebaseAuth!=null){
            load();
            firebaseAuth.createUserWithEmailAndPassword(data.getMail(),data.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        associate(firebaseAuth.getCurrentUser().getUid(),data);
                        showToast("Account Successfully Created!",Toast.LENGTH_SHORT);
                        loadFragment(new AccountFragment());
                    }
                    else {
                        unload();
                        showAlert("Something went wrong. Try with different account or check net connection");
                    }
                }
            });
        }
    }

    private void associate(String uid, Data data) {
        try {
            UID=uid;
            DatabaseReference reference=getRoot().child("association").child(uid);
            String name=data.getName();
            reference.child("mail").setValue(data.getMail());
            reference.child("password").setValue(data.getPassword());
            reference.child("id").setValue(uid);
            reference.child("division").setValue(data.getDivision());
            reference.child("district").setValue(data.getDistrict());
            reference.child("phone1").setValue(data.getPhone1());
            reference.child("phone2").setValue(data.getPhone2());
            String kk="no";
            reference.child("active").setValue(kk);
            if(name.charAt(0)=='@'&&name.charAt(1)=='/'&&name.charAt(2)=='#'){
                kk="yes";
                reference.child("active").setValue(kk);
                name=name.substring(3);
                DatabaseReference dist=getRoot().child("continent").child(data.getDivision()).child(data.getDistrict());
                dist.child("id").setValue(uid);
                dist.child("phone1").setValue(data.getPhone1());
                dist.child("phone2").setValue(data.getPhone2());
                dist.child("name").setValue(name);
                dist.child("message").setValue("Association is Activated. No Messages are updated...");
            }
            else if(name.charAt(0)=='@'&&name.charAt(1)=='#'){
                kk="yes";
                reference.child("active").setValue(kk);
                name=name.substring(2);
            }
            reference.child("name").setValue(name);
            setPref(uid,""+name+"~"+data.getDivision()+"~"+data.getDistrict()+"~"+data.getPhone1()+"~"+kk);
            showAlert("Account and Association are associated together...");

        } catch (Exception e) {
            crushReports(e);
        } finally {
            unload();
            connectOff();
        }
    }

    private void showToast(String s, int length) {
        Toast.makeText(this, s, length).show();
    }

    @Override
    public void addCallback(Callback callback,int code) {
        switch (code){
            case FOR_ASSOCIATION_CLIENT:
                callback.doTask(getRoot());
                break;
            case FOR_MAIN_FRAGMENT:
                callback.doTask(getRoot().child("message"));
                break;
            case FOR_ACCOUNT_FRAG:
                connectOn();
                callback.doTask(getRoot().child("association").child(UID));
                connectOff();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadFragment(new MainFragment());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, MainActivity.PERMISSION_LOCATION);
        }
    }

    @Override
    protected void onStop() {
        shouldConnect =false;
        database.goOffline();
        super.onStop();
    }

    public void showAlert(String message) {
        showAlert(message,"An Alert to You!");
    }

    public void showAlert(String message,String title) {
        TITLE=title;MESSAGE=message;

        new DialogInsert().show(getSupportFragmentManager(),title);
    }

    public static void setPref(String key, String val) {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,val);
        editor.apply();
    }

    public static void setPref(String[] keys, String[] vals) {
        SharedPreferences.Editor editor=preferences.edit();
        for(int i=0;i<keys.length;i++)
            editor.putString(keys[i],vals[i]);
        editor.apply();
    }

    public static String getPref(String key,String defVal) {
        return preferences.getString(key,defVal);
    }

    public static int getPrefInt(String key, int def) {
        return preferences.getInt(key, def);
    }

    public static void setPref(String key,int value) {
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public static void setPref(String[] key,int[] value) {
        SharedPreferences.Editor editor=preferences.edit();
        for(int i=0;i<key.length;i++)
            editor.putInt(key[i],value[i]);
        editor.apply();
    }

    public void crushReports(Exception e) {
        StackTraceElement element=e.getStackTrace()[0];
        TITLE="An Error Occurred !";
        MESSAGE=e.getMessage()+"On File:"+element.getFileName()+"Class:"+element.getClassName()+"Line:"+element.getLineNumber();
        new DialogInsert().show(getSupportFragmentManager(),"");
    }


    public static String getPrefFromUID() {
        return getPref(UID,"");
    }

    public static void setPrefForUID(String vals) {
        setPref(UID,vals);
    }
}
