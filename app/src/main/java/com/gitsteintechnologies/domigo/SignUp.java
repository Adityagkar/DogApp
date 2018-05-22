package com.gitsteintechnologies.domigo;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity  {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText name, pwd, username, mobile;
    Button camButton, next_btn;
    ImageView imageView;
    Uri uri;
    boolean newflag=true;

    String mylocation;
    UserInfoDatabase userInfoDatabase;


    String userid_retrieved;
    String Storage_Path = "";

    StorageReference storageReference;

    ProgressDialog progressDialog ;
    RelativeLayout linear;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();


        //https://github.com/lopspower/CircularImageView used this
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("signup_details");
        storageReference = FirebaseStorage.getInstance().getReference();

        progressDialog=new ProgressDialog(SignUp.this);

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        pwd = findViewById(R.id.pwd);
        mobile = findViewById(R.id.mobile);
        camButton = findViewById(R.id.camera_btn);
        next_btn = findViewById(R.id.nextbtn);
        imageView = findViewById(R.id.circularImageView);

        linear=findViewById(R.id.linear);





        //submit values to DB on click of submit button
        next_btn.setVisibility(View.VISIBLE);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userid_retrieved=databaseReference.child("signup_details").push().getKey();

                //databaseReference.child(userid_retrieved).setValue(a);

                Log.d("KEYFINAL",userid_retrieved);
              //  onImageUploadToFirebase();
                String username_temp=username.getText().toString();
                boolean b =   retrieve(username_temp);
                Log.e("!!!!!!",""+b);
                if(!b)
                {


                }
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


}


    protected void onActivityResult(int ResultCode, int RequestCode, Intent data) {
        uri = data.getData();
        imageView.setImageURI(uri);

    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)


    public void onImageUploadToFirebase(){
//
       if(uri!=null){


            StorageReference storageReference2nd = storageReference.child("upload/" + System.currentTimeMillis() + "." + GetFileExtension(uri));

            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = username.getText().toString()+"."+ GetFileExtension(uri);

                            progressDialog.setTitle("Image is Uploading...");

                            // Showing progressDialog.
                            progressDialog.show();

//String Username, String Name, String Password, String mobile, String Location, String imagename, String url
                            @SuppressWarnings("VisibleForTests")
                            ImageUploadInfo imageUploadInfo = new ImageUploadInfo(username.getText().toString(),name.getText().toString(),pwd.getText().toString(),mobile.getText().toString()
                            ,TempImageName,taskSnapshot.getDownloadUrl().toString());

                //code for username checking


                             //Adding image upload id s child element into databaseReference.
                            databaseReference.child(userid_retrieved).setValue(imageUploadInfo);
                        }
                    })// If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(SignUp.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            // Setting progressDialog Title.
                            progressDialog.setTitle("Image is Uploading...");

                        }
                    });

            Intent intent=new Intent(SignUp.this,PlacePickerActivity.class);

            startActivity(intent);
             userInfoDatabase = new UserInfoDatabase(username.getText().toString(), userid_retrieved);
        }
        else {



                Snackbar snackbar = Snackbar
                        .make(linear, "Please Upload an image !", Snackbar.LENGTH_LONG);
                snackbar.show();
            }



    }

    boolean flag=true;

    private boolean retrieve(final String username_temp) {
                flag = true;
                DatabaseReference databaseReference2=firebaseDatabase.getReference();

//        final String username_u=mEmailView.getText().toString();
//        final String password_u=mPasswordView.getText().toString();
            // Log.d("TEST","VAlue found !");
            Query query= databaseReference2.child("signup_details").orderByChild("username").equalTo(username_temp);
            Log.d("TEST",username_temp);


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.DONUT)
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    {

                        Log.d("TEST!!!!","VAlue found !");

                        Map<String,Object> map= (Map<String, Object>) dataSnapshot1.getValue();
                        String username_r=map.get("username").toString();

                        if(username_r.equals(username_temp)){
                            flag = false;

                        }
                        Log.d("TEST",""+username_r+" "+username_r+"out");

                    }

                    if(!flag)
                    {
                        Snackbar snackbar = Snackbar.make(linear, "This Username is already taken!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else
                        onImageUploadToFirebase();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("TEST","Inside functn");

                }
            });


            return flag;
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }
}









