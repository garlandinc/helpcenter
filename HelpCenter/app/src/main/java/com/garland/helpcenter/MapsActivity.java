package com.garland.helpcenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.garland.helpcenter.engine.CommonCenter;
import com.garland.helpcenter.engine.Description;
import com.garland.helpcenter.engine.Engine;
import com.garland.helpcenter.fragment.DialogInsert;
import com.garland.helpcenter.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private Location location;
    private Spinner spinner;
    private EditText editText;
    private ImageButton button;
    private ArrayAdapter<String> adapter;
    private FloatingActionButton fab,outFab;
    private LinearLayout linearLayout;
    private RelativeLayout out_layout;
    private TextView unitText;

    private LatLng lastSearch=null;
    private Marker marker;
    private TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (client == null)
                client = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this).addConnectionCallbacks(this).addApi(LocationServices.API).build();
            if(Utility.engine==null) {
                try{
                    AssetManager manager=getAssets();
                    Utility.engine=new Engine();
                    Utility.engine.read(manager.open("data.data"),manager.open("center.data"),manager.open("time.data"));
                } catch (Exception e) {
                    MainActivity.TITLE="Error on Map";
                    MainActivity.MESSAGE=e.getMessage();
                    new DialogInsert().show(getSupportFragmentManager(),"");
                }
            }
        } catch (Exception e) {
            MainActivity.TITLE="Error on Map";
            MainActivity.MESSAGE=e.getMessage();
            new DialogInsert().show(getSupportFragmentManager(),"");
        }
        spinner= (Spinner) findViewById(R.id.spinner_unit);
        editText= (EditText) findViewById(R.id.input_roll);
        fab= (FloatingActionButton) findViewById(R.id.floatingActionButton);
        try {
            linearLayout= (LinearLayout) findViewById(R.id.linearLayout);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fireFab();
                }
            });
            button= (ImageButton) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    find();
                }
            });
            unitText= (TextView) findViewById(R.id.unit_text);
            out_layout= (RelativeLayout) findViewById(R.id.relative_lay);
            outputText= (TextView) findViewById(R.id.output_text);
            outFab= (FloatingActionButton) findViewById(R.id.fab_out);
            adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, Utility.engine.data.keySet().toArray(new String[0]));
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    unitText.setText(spinner.getSelectedItem().toString());
                    editText.setText("0");
                    editText.setSelection(1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId== EditorInfo.IME_ACTION_SEARCH)
                        find();
                    return false;
                }
            });
            outFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ObjectAnimator animator=ObjectAnimator.ofFloat(linearLayout,"scaleY",1F,0.5F,0F).setDuration(300);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            out_layout.setVisibility(View.GONE);
                        }
                    });
                    animator.start();
                }
            });
        } catch (Exception e) {
            MainActivity.TITLE="Error";
            MainActivity.MESSAGE=""+e.getMessage();
            new DialogInsert().show(getSupportFragmentManager(),"");
        }

    }

    private void fireFab() {
        fab.animate().scaleX(.10F).scaleY(.10F).scaleXBy(.10F).scaleYBy(.10F).setDuration(500).setInterpolator(new ReverseInterpolator(new AccelerateInterpolator())).start();

        if(linearLayout.getVisibility()==View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(linearLayout,"scaleY",0F,.5F,1F).setDuration(300).start();
            fab.setImageResource(R.drawable.ic_close_black_24dp);
        }
        else {
            fab.setImageResource(R.drawable.ic_search_black_24dp);
            ObjectAnimator animator=ObjectAnimator.ofFloat(linearLayout,"scaleY",1F,0.5F,0F).setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    super.onAnimationCancel(animation);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    linearLayout.setVisibility(View.GONE);
                    super.onAnimationEnd(animation);
                }

            });
            animator.start();
        }
    }

    private void setMarkers(Collection<CommonCenter> values) {
        for(CommonCenter center:values) {
            LatLng latLng=new LatLng(center.location.latitude,center.location.longitude);
            mMap.addMarker(new MarkerOptions().position(latLng).title(center.area).zIndex(1));/*.snippet("NO")).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_local_library_black_24dp)));*/
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.966096, 89.817135),17F));
    }

    private void find() {
        String text=editText.getText().toString();
        if(text.isEmpty()) return;
        editText.setText("0");
        editText.setSelection(1);
        String unit=spinner.getSelectedItem().toString();
        Description description=Utility.engine.findLoc(unit,unit+text);
        if(description!=null) {
            loadDescription(unit,description,unit+text);
        }
        else {
            MainActivity.TITLE="Roll doesn't found!";
            MainActivity.MESSAGE="May be your Input is wrong, Please input a correct roll or contact with University Administration...";
            new DialogInsert().show(getSupportFragmentManager(),"");
        }
    }

    private void loadDescription(String unit, Description description, String roll) {
        if(marker!=null)
            marker.remove();
        CommonCenter center=Utility.engine.getCenter(unit,description.centerId);
        LatLng latLng=new LatLng(center.location.latitude,center.location.longitude);
        marker=mMap.addMarker(new MarkerOptions().title(center.area).position(latLng).zIndex(5));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        if(linearLayout.getVisibility()==View.VISIBLE){
            fireFab();
        }
        if(out_layout.getVisibility()==View.GONE){
            out_layout.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(out_layout,"scaleY",0F,.5F,1F).setDuration(300).start();
        }
        String message="Roll:"+roll+"\nExam Center:"+center.area+"\nRoom:"+description.room+"\nTotal Examinee on that Room:"+description.totalStudent+"\nUnit:"+unit+"\n"+Utility.engine.timeUnit.get(unit)+"\n";

        outputText.setText(message);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {

                new AlertDialog.Builder(MapsActivity.this).setTitle("Exam Center").setMessage(""+marker.getTitle()+"").setPositiveButton("Navigate Me", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+marker.getPosition().latitude+","+marker.getPosition().longitude));
                        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }
                }).setNegativeButton("Ok", null).create().show();
                return false;
            }
        });
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.966096, 89.817135),17));
    }

    private boolean isPlayAvailable() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)==ConnectionResult.SUCCESS;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isPlayAvailable())
            client.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(client!=null)
            client.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.LOCATION_HARDWARE},105);
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(client);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==105) {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public LatLng getLastLatLang() {
        if(location==null)
            return new LatLng(22.966096, 89.817135);
        return new LatLng(location.getLatitude(),location.getLongitude());
    }

    private void reloadMap() {
        if(mMap==null) return;
        LatLng latLng=getLastLatLang();
        final MarkerOptions markerOptions=new MarkerOptions().title("Your Location").position(latLng);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
        mMap.addMarker(markerOptions);
        CameraUpdate update=CameraUpdateFactory.newLatLngZoom(latLng,17);
        mMap.animateCamera(update);
    }

    private boolean isLastSearch(LatLng latLng) {
        if(this.lastSearch==null) return false;
        return this.lastSearch.latitude==latLng.latitude&&this.lastSearch.longitude==latLng.longitude;
    }

    private class ReverseInterpolator implements Interpolator {
        public  final Interpolator interpolator;

        public ReverseInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
        }

        public ReverseInterpolator() {
            this(new LinearInterpolator());
        }


        @Override
        public float getInterpolation(float input) {
            return 1-interpolator.getInterpolation(input);
        }
    }
}
