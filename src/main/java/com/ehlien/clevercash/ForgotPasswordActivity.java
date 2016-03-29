package com.ehlien.clevercash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.cloudboost.CloudException;
import io.cloudboost.CloudStringCallback;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class ForgotPasswordActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private EditText useEmailET;
    private TextView resetPasswordTV;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        relativeLayout = (RelativeLayout) findViewById(R.id.fpRelativeLayout);
        useEmailET = (EditText) findViewById(R.id.useEmailET);
        resetPasswordTV = (TextView) findViewById(R.id.resetPasswordTV);

        InitCloudboost.initClient();

        resetPasswordTV.setClickable(false);
        resetPasswordTV.setEnabled(false);
        resetPasswordTV.setBackgroundResource(R.color.common_plus_signin_btn_text_light_disabled);

        useEmailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                resetPasswordTV.setClickable(false);
                resetPasswordTV.setEnabled(false);
                resetPasswordTV.setBackgroundResource(R.color.common_plus_signin_btn_text_light_disabled);
                } else {
                resetPasswordTV.setClickable(true);
                resetPasswordTV.setEnabled(true);
                resetPasswordTV.setBackgroundResource(R.color.colorSkyBlue);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void resetPasswordButton(View view) {
        email = useEmailET.getText().toString();
        new ResetPassword().execute();
    }

    public void resetPassword() throws CloudException {
        CloudUser.resetPassword(email, new CloudStringCallback(){
            @Override
            public void done(String m, CloudException e){
                if (m != null) {
                    //reset password email sent
                    Log.i("--------RESET PASS MSG:", "SUCCESS: " + m);

                    Snackbar.make(relativeLayout, "An email has been sent to " + email + " with a link to reset your password.", Snackbar.LENGTH_LONG).show();

                    try {
                        Thread.sleep(2000);
                        finish();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
                if (e != null) {
                    // Error
                    Log.i("------RESET PASS ERROR:", e.getLocalizedMessage());
                    Snackbar.make(relativeLayout, "Email not found. Please try again.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private class ResetPassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                resetPassword();
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
