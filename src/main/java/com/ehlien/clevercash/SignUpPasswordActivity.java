package com.ehlien.clevercash;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.cloudboost.*;

public class SignUpPasswordActivity extends AppCompatActivity {
    private EditText passwordET;
    private EditText repasswordET;
    private TextView x;

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String number;
    private String password;
    private String repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_password);

        InitCloudboost.initClient();

        passwordET = (EditText) findViewById(R.id.passwordSET);
        repasswordET = (EditText) findViewById(R.id.repasswordSET);

        Intent receive = getIntent();

        firstname = receive.getStringExtra("firstname");
        lastname = receive.getStringExtra("lastname");
        username = receive.getStringExtra("username");
        email = receive.getStringExtra("email");
        number = receive.getStringExtra("number");

        x = (TextView) findViewById(R.id.emptySTV);
        x.setText("firstname: " + firstname + " lastname:  " + lastname + " username:  " + username + " email:  " + email + " phone:  " + number);
    }

    public void signUpButton(View view) {
        password = passwordET.getText().toString();
        repassword = repasswordET.getText().toString();

        Log.i("SIGN UP:", "FIRSTNAME: " + firstname);
        Log.i("SIGN UP:", "LASTNAME: " + lastname);
        Log.i("SIGN UP:", "USERNAME: " + username);
        Log.i("SIGN UP:", "EMAIL: " + email);
        Log.i("SIGN UP:", "NUMBER: " + number);
        Log.i("SIGN UP:", "PASSWORD: " + password);
        Log.i("SIGN UP:", "REPASSWORD: " + repassword);

        if (password.isEmpty() && repassword.isEmpty()) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, you have to choose a password if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else if (!password.contentEquals(repassword)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, your password must match if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else if (password.length() <= 6) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, your password must be more than 7 characters if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else if (password.matches(".+[A-Z].+") && password.matches(".+[a-z].+")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, your password must have an uppercase and lowercase letter if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else if (password.matches(".+[0-9].+")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, your password must have a digit if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            new SignUp().execute();
        }
    }

    public void signUpUser() throws CloudException {
        CloudUser newUser = new CloudUser();
        newUser.setUserName(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.set("firstname", firstname);
        newUser.set("lastname", lastname);
        if (!number.isEmpty()) {
            newUser.set("phone", Double.parseDouble(number));
        }
        newUser.signUp(new CloudUserCallback() {
            @Override
            public void done(CloudUser user, CloudException e) throws CloudException {
                if (e != null) {
                    // Error
                    Log.i("SIGN UP ERROR:", e.getMessage());
                    // Toast toast = Toast.makeText(getApplicationContext(), "The username or email you have entered have already been used to sign up. Please try again.", Toast.LENGTH_SHORT);
                    // toast.show();
                }
                if (user != null) {
                    // Successful
                    Log.i("SIGN UP:", "Account Created!" + CloudUser.getcurrentUser().toString());

                    CloudObject userInfo = new CloudObject("UserInfo");
                    userInfo.set("firstname", firstname);
                    userInfo.set("lastname", lastname);
                    userInfo.set("username", username);
                    userInfo.set("email", email);
                    if (!number.isEmpty()) {
                        userInfo.set("phone", Double.parseDouble(number));
                    }
                    userInfo.save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject x, CloudException e) {
                            if (x != null) {
                                // Success
                            }
                            if (e != null) {
                                // Error
                                Log.i("TABLE ERROR:", e.getMessage());
                            }
                        }
                    });

                    Intent showLogInActivity = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(showLogInActivity);
                }
            }
        });
    }

    private class SignUp extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                signUpUser();
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
