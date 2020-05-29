package com.example.croppredictionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;;import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends AppCompatActivity {

    //private Spinner spinner;
    //private EditText editText;
    Button Location;
    TextView Textview1, Textview2,Textview3, Textview4,Textview5,Textview6,tv;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("script");
        PyObject obj = pyf.callAttr("test");

        //tv=findViewById(R.id.text);
        //tv.setText(obj.toString());

        Location=findViewById(R.id.location);
        Textview1=findViewById(R.id.Textview1);
        Textview2=findViewById(R.id.Textview2);
        Textview3=findViewById(R.id.Textview3);
        Textview4=findViewById(R.id.Textview4);
        Textview5=findViewById(R.id.Textview5);
        Textview6=findViewById(R.id.Textview6);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == (PackageManager.PERMISSION_GRANTED)) {

                    getLocation();
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

    }

    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                Location location=task.getResult();
                if(location != null){
                    try {
                        Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        Textview1.setText(Html.fromHtml("<font color= '#6200EE'><b>Latitude :</b><br></font>"+ addresses.get(0).getLatitude()));
                        Textview2.setText(Html.fromHtml("<font color= '#6200EE'><b>Longitude :</b><br></font>"+ addresses.get(0).getLongitude()));
                        Textview3.setText(Html.fromHtml("<font color= '#6200EE'><b>Country Name :</b><br></font>"+ addresses.get(0).getCountryName()));
                        Textview4.setText(Html.fromHtml("<font color= '#6200EE'><b>Locality :</b><br></font>"+ addresses.get(0).getLocality()));
                        Textview5.setText(Html.fromHtml("<font color= '#6200EE'><b>Address :</b><br></font>"+ addresses.get(0).getAddressLine(0)));

                    } catch (IOException e) {
                        e.printStackTrace();
                        Textview6.setText("Couldn't fetch");

                    }



                }
            }
        });
    }
}
