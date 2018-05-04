package com.gitsteintechnologies.domigo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class AboutDog extends AppCompatActivity {


    Button nextbtn,camButton;
    Integer times;
    EditText name;
    EditText breed;
    EditText age;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String CID_retrieved;// this CID will be retrieved from firebase
    UserInfoDatabase userInfoDatabase;
    String dogid_retrieved;
    String userid_retrieved;

    ImageView imageView;
    Uri uri;

    StorageReference storageReference;
    ProgressDialog progressDialog;
   RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_about_dog);
        getSupportActionBar().hide();


        nextbtn = findViewById(R.id.nextbtn);
        name=findViewById(R.id.name);
        breed=findViewById(R.id.breed);
        age=findViewById(R.id.age);
        camButton=findViewById(R.id.camera_btn);
        imageView=findViewById(R.id.circularImageView);
        relativeLayout=findViewById(R.id.relativeLayout);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("dog_details");
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(AboutDog.this);


        userInfoDatabase=new UserInfoDatabase();
        times=getIntent().getIntExtra("No_Dogs",0);
        times--;
        Log.d("TEST",times+"");
        CID_retrieved= userInfoDatabase.getUserID();
        userid_retrieved=CID_retrieved;
        Log.d("KEYFINAL",CID_retrieved);


        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);

                startActivityForResult(intent, 1);

            }
        });








        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dogid_retrieved=databaseReference.child("dog_details").push().getKey();

                if(times>0){
                    Intent intent=new Intent(AboutDog.this,AboutDog.class);
                                    Log.d("TEST",times+" running");
                    intent.putExtra("No_Dogs",times);
                    onImageUploadToFirebase(intent);

                    //CID_retrieved=databaseReference.child("signup_details").orderByChild("username").equalTo("");



                }

                if(times==0){
                    Intent intent2=new Intent(AboutDog.this,DomigoBookingConfirm.class);

                    onImageUploadToFirebase(intent2);

                }


            }
        });



    }

    protected void onActivityResult(int ResultCode, int RequestCode, Intent data) {
        uri = data.getData();
        imageView.setImageURI(uri);

    }


    @RequiresApi(api = Build.VERSION_CODES.DONUT)


    public void onImageUploadToFirebase(Intent intent){

        if(uri!=null){
            Toast.makeText(this,uri+"",Toast.LENGTH_SHORT).show();

            StorageReference storageReference2nd = storageReference.child("upload/" + System.currentTimeMillis() + "." + GetFileExtension(uri));

            storageReference2nd.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            String TempImageName = name.getText().toString()+"."+ GetFileExtension(uri);

                            progressDialog.setTitle("Image is Uploading...");

                            // Showing progressDialog.
                            progressDialog.show();


                            //String dogname,String dog_breed,String dog_age,String CID,String imagename, String url
                            @SuppressWarnings("VisibleForTests")
                            DogInfoDatabase imageUploadInfo = new DogInfoDatabase(name.getText().toString(),breed.getText().toString(),age.getText().toString(),userid_retrieved,TempImageName,taskSnapshot.getDownloadUrl().toString());


                            //Adding image upload id s child element into databaseReference.
                            databaseReference.child(dogid_retrieved).setValue(imageUploadInfo);
                        }
                    })// If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            Toast.makeText(AboutDog.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
            startActivity(intent);


        }
        else {

            Snackbar snackbar = Snackbar
                    .make(relativeLayout, "Please select a Picture !", Snackbar.LENGTH_LONG);
            snackbar.show();




        }


    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


}
