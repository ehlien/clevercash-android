package com.ehlien.clevercash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpUsernameActivity extends AppCompatActivity {
    private EditText usernameET;

    private String firstname;
    private String lastname;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_username);

        usernameET = (EditText) findViewById(R.id.usernameSET);

        Intent receive = getIntent();

        firstname = receive.getStringExtra("firstname");
        lastname = receive.getStringExtra("lastname");
    }

    public void nextToEmailButton(View view) {
        username = usernameET.getText().toString();

        Log.i("SIGN UP:", username);

        if ( username.isEmpty() ) {
            Toast toast = Toast.makeText(getApplicationContext(), "Sorry, you have to choose a username if you want to continue...", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent showSignUpEmailActivity = new Intent(this, SignUpEmailActivity.class);
            showSignUpEmailActivity.putExtra("firstname", firstname);
            showSignUpEmailActivity.putExtra("lastname", lastname);
            showSignUpEmailActivity.putExtra("username", username);
            startActivity(showSignUpEmailActivity);
        }
    }
}
