package com.gitsteintechnologies.domigo;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SignUp extends AppCompatActivity implements LocationListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText name, pwd, username, mobile;
    Button camButton, next_btn, locationbtn;
    ImageView imageView;
    Uri uri;
    Location location;
    TextView locationview;

    LocationManager locationManager;
        String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        //https://github.com/lopspower/CircularImageView used this
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.pwd);
        mobile = findViewById(R.id.mobile);
        camButton = findViewById(R.id.camera_btn);
        next_btn = findViewById(R.id.nextbtn);
        imageView = findViewById(R.id.circularImageView);
        locationbtn = findViewById(R.id.button5);
        locationview = findViewById(R.id.locationview);


        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);




        //submit values to DB on click of submit button
        next_btn.setVisibility(View.INVISIBLE);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToDatabase a = new AddToDatabase(name.getText().toString(), username.getText().toString(), pwd.getText().toString(), mobile.getText().toString());
                databaseReference.child("doginfo").push().setValue(a);
            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);

            }
        });


        locationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
                Toast.makeText(getApplicationContext(),"Please wait Retrieving Location !",Toast.LENGTH_LONG).show();
                locationbtn.setVisibility(View.INVISIBLE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, SignUp.this);

            }
        });


    }


    protected void onActivityResult(int ResultCode, int RequestCode, Intent data) {
        uri = data.getData();
        imageView.setImageURI(uri);

    }


    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lo = location.getLongitude();

//        LatLng lan = new LatLng(lat,lo);

        Geocoder geo = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> al = geo.getFromLocation(lat, lo, 1);
            Address address = al.get(0);
            String mylocation = address.getSubLocality();

            locationview.setVisibility(View.VISIBLE);

            locationview.setText(mylocation);
            next_btn.setVisibility(View.VISIBLE);
            // Toast.makeText(this,mylocation,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

        Toast.makeText(this, "gps on", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(SignUp.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this,"callll",Toast.LENGTH_SHORT).show();

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SignUp.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        100);
                            }



            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        100);
            }
            return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 1, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}






    class AddToDatabase {
        public String Name;
        public String Username;
        public String Password;
        public String mobile;
        //public String imageEntry;


        public AddToDatabase(String Name, String Username, String Password, String mobile) {
            this.Name = Name;
            this.Username = Username;
            this.Password = Password;
            this.mobile = mobile;

        }

    }


