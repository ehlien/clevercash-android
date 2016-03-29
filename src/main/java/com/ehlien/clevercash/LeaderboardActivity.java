package com.ehlien.clevercash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        InitCloudboost.initClient();
    }
}
