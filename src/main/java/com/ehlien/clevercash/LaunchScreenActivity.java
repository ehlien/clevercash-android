package com.ehlien.clevercash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import io.cloudboost.CloudUser;

public class LaunchScreenActivity extends AppCompatActivity {
    private final int DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (CloudUser.getcurrentUser() != null) {
                    Intent showHomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(showHomeActivity);
                } else {
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, DISPLAY_LENGTH);
    }
}
