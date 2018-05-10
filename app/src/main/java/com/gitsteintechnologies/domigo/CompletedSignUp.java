package com.gitsteintechnologies.domigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStreamReader;

public class CompletedSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_sign_up);

        Button proceed_to_login;

        proceed_to_login=findViewById(R.id.proceedbtn2);

        proceed_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompletedSignUp.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
