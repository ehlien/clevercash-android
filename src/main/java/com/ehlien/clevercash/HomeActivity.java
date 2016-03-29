package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        InitCloudboost.initClient();

        Log.i("HOME:", "USER: " + CloudUser.getcurrentUser().getId());
    }

    public void playButton(View view) {
        Intent showPlayActivity = new Intent(this, PlayActivity.class);
        startActivity(showPlayActivity);
    }

    public void profileButton(View view) {
        Intent showProfileActivity = new Intent(this, ProfileActivity.class);
        startActivity(showProfileActivity);
    }

    public void leaderboardButton(View view) {
        Intent showLeaderboardActivity = new Intent(this, LeaderboardActivity.class);
        startActivity(showLeaderboardActivity);
    }

    public void settingsButton(View view) {
        Intent showSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(showSettingsActivity);
    }

    @Override
    public void onBackPressed() {
        new SignOut().execute();
    }

    public void signOut() throws CloudException {
        CloudUser.getcurrentUser().logOut(new CloudUserCallback(){
            @Override
            public void done(CloudUser user, CloudException e) throws CloudException {
                if ( user == null ) {
                    Intent showWelcomeActivity = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(showWelcomeActivity);
                }
                if ( e != null ) {
                    e.getLocalizedMessage();
                }
            }
        });
    }

    private class SignOut extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                signOut();
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

}
