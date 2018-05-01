package com.gitsteintechnologies.domigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BookingPlans extends AppCompatActivity {

    Button small;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_plans);

        small = findViewById(R.id.button4);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BookingPlans.this,SmallBreed.class);
                startActivity(intent);
            }
        });
    }
}
