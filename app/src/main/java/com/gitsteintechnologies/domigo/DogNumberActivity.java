package com.gitsteintechnologies.domigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class DogNumberActivity extends AppCompatActivity {

    Integer no_dogs;
    Button submit_btn;
    UserInfoDatabase userInfoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        setContentView(R.layout.activity_dog_number);
        getSupportActionBar().hide();

        submit_btn = findViewById(R.id.nextbtn);

        NumberPicker np = findViewById(R.id.numberPicker);

        np.setMinValue(0);
        np.setMaxValue(15);


        np.setOnValueChangedListener(onValueChangeListener);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DogNumberActivity.this,AboutDog.class);
                intent.putExtra("No_Dogs",no_dogs);
                Log.d("TEST",no_dogs+"");
                if(no_dogs!=0)
                startActivity(intent);
                else
                    Toast.makeText(DogNumberActivity.this,"Sorry ! You need to have a dog to enjoy our services",Toast.LENGTH_LONG).show();
            }
        });
    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    Toast.makeText(DogNumberActivity.this,
                            "Cool ! You have "+numberPicker.getValue()+" Dogs", Toast.LENGTH_SHORT).show();

                    no_dogs=numberPicker.getValue();

                }
            };



    }
