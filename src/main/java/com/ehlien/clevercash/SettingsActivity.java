package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.cloudboost.CloudException;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        InitCloudboost.initClient();
    }

    public void signOutButton(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Signing Out...", Toast.LENGTH_SHORT);
        toast.show();
        new SignOut().execute();
    }

    public void signOutUser() throws CloudException {
        CloudUser.getcurrentUser().logOut(new CloudUserCallback() {
            @Override
            public void done(CloudUser user, CloudException e) throws CloudException {
                if (e != null) {
                    // Error
                    Log.i("SO ERROR:", e.getMessage());
                } else {
                    // Successful
                    Log.i("SIGNED OUT:", CloudUser.getcurrentUser().toString());
                    Intent showWelcomeActivity = new Intent(getApplicationContext(), WelcomeActivity.class);
                    startActivity(showWelcomeActivity);
                }
            }
        });
    }

    private class SignOut extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                signOutUser();
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
