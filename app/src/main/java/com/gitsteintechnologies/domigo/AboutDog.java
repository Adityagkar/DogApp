package com.gitsteintechnologies.domigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AboutDog extends AppCompatActivity {


    Button nextbtn;
    Integer times;
    EditText name;
    EditText breed;
    EditText age;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String CID_retrieved;// this CID will be retrieved from firebase
    UserInfoDatabase userInfoDatabase;


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

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



        times=getIntent().getIntExtra("No_Dogs",0);
        times--;
        Log.d("TEST",times+"");


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(times!=0){
                    Intent intent=new Intent(AboutDog.this,AboutDog.class);
                                    Log.d("TEST",times+" running");
                    intent.putExtra("No_Dogs",times);

                    //CID_retrieved=databaseReference.child("signup_details").orderByChild("username").equalTo("");
                    CID_retrieved= userInfoDatabase.getUserID();
                    DogInfoDatabase a = new DogInfoDatabase(name.getText().toString(), breed.getText().toString(),age.getText().toString(),CID_retrieved);
                    databaseReference.child("dog_details").push().setValue(a);

                    //code for uploading to database will come her
                    startActivity(intent);
                }

                if(times==0){
                    Intent intent2=new Intent(AboutDog.this,DomigoBookingConfirm.class);
                    startActivity(intent2);
                }



            }
        });



    }
}
