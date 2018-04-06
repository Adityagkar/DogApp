package com.gitsteintechnologies.domigo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);


        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        SplashLauncher obj=new SplashLauncher();
        obj.start();
    }


    private class SplashLauncher extends Thread{
        public void run(){
            try{

                sleep(1000);

            }
            catch(Exception e){
                e.printStackTrace();
            }

            Intent in=new Intent(SplashScreen.this,LoginActivity.class);
            startActivity(in);
            SplashScreen.this.finish();
        }
    }
}
