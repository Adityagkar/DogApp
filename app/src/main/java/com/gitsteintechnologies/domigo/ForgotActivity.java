package com.gitsteintechnologies.domigo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ForgotActivity extends AppCompatActivity {
    EditText input;
    TextView change_label;
    Button proceed;
    int count;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        change_label=findViewById(R.id.textView18);
        proceed=findViewById(R.id.proceedbtn);
        input=findViewById(R.id.email3);

        count=3;

        switch (count){
            case 1:// user finally changes password

            case 2: // users enters passcode and it gets verfied

            case 3:// sending email to the email id entered by the user


        }


    }
}
