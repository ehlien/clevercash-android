package com.ehlien.clevercash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpEmailActivity extends AppCompatActivity {
    private EditText emailET;
    private EditText numberET;

    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);

        emailET = (EditText) findViewById(R.id.emailET);
        numberET = (EditText) findViewById(R.id.numberET);

        Intent receive = getIntent();

        firstname = receive.getStringExtra("firstname");
        lastname = receive.getStringExtra("lastname");
        username = receive.getStringExtra("username");
    }

    public void nextToPasswordButton(View view) {
        email = emailET.getText().toString();
        number = numberET.getText().toString();

        Log.i("SIGN UP:", email);
        Log.i("SIGN UP:", number);

        if ( email.isEmpty() ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, you have to choose an email if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent showSignUpPasswordActivity = new Intent(this, SignUpPasswordActivity.class);
            showSignUpPasswordActivity.putExtra("firstname", firstname);
            showSignUpPasswordActivity.putExtra("lastname", lastname);
            showSignUpPasswordActivity.putExtra("username", username);
            showSignUpPasswordActivity.putExtra("email", email);
            showSignUpPasswordActivity.putExtra("number", number);
            startActivity(showSignUpPasswordActivity);
        }
    }
}
