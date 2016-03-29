package com.ehlien.clevercash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import io.cloudboost.*;

public class SignInActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private EditText usernameET;
    private EditText passwordET;

    private ProgressDialog progress;
    private Handler handler = new Handler();

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        InitCloudboost.initClient();

        relativeLayout = (RelativeLayout) findViewById(R.id.sRelativeLayout);
        usernameET = (EditText) findViewById(R.id.usernameIET);
        passwordET = (EditText) findViewById(R.id.passwordIET);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progress.dismiss();
    }

    public void signInButton(View view) {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();

        Log.i("SignInInfo", username);
        Log.i("SignInInfo", password);

        new SignIn().execute();
    }

    public void forgotPasswordButton(View view) {
        Intent showForgotPasswordActivity = new Intent(this, ForgotPasswordActivity.class);
        startActivity(showForgotPasswordActivity);
    }

    public void signingIn() {
        progress = new ProgressDialog(getApplicationContext());
        progress.setMessage("Signing In ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        progress.show();
        final Thread thread = new Thread() {
            @Override
            public void run() {
                SignInActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        thread.start();
    }

    public void signInUser() throws CloudException {
        CloudUser user = new CloudUser();
        user.setUserName(username);
        user.setPassword(password);
        user.logIn(new CloudUserCallback() {
            @Override
            public void done(CloudUser object, CloudException e) throws CloudException {
                if (object != null) {
                    // Successful
                    Log.i("SingedIn", CloudUser.getcurrentUser().getId());
                    Intent showHomeActivity = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(showHomeActivity);
                }
                if (e != null) {
                    // Error
                    progress.dismiss();
                    Snackbar.make(relativeLayout, "Sign in failed. Please Try again...", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private class SignIn extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                signInUser();
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
