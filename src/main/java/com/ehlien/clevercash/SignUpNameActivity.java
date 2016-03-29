package com.ehlien.clevercash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpNameActivity extends AppCompatActivity {
    private EditText firstnameET;
    private EditText lastnameET;
    private String firstname;
    private String lastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_name);

        firstnameET = (EditText) findViewById(R.id.firstnameET);
        lastnameET = (EditText) findViewById(R.id.lastnameET);
    }

    public void nextToUsernameButton(View view) {
        firstname = this.firstnameET.getText().toString();
        lastname = this.lastnameET.getText().toString();

        Log.i("SIGN UP:", firstname);
        Log.i("SIGN UP:", lastname);

        if ( firstname.isEmpty() && lastname.isEmpty() ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, you have to tell me your first and last name if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent showSignUpUsernameActivity = new Intent(this, SignUpUsernameActivity.class);
            showSignUpUsernameActivity.putExtra("firstname", firstname);
            showSignUpUsernameActivity.putExtra("lastname", lastname);
            startActivity(showSignUpUsernameActivity);
        }
    }
}
