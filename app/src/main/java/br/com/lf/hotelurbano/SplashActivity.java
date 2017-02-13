package br.com.lf.hotelurbano;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Thread thread =  new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                    startActivity(new Intent(SplashActivity.this, HoteisActivity.class));
                    finish();
                }catch (InterruptedException e ){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


}
