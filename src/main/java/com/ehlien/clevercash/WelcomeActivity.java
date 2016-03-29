package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import io.cloudboost.*;

public class WelcomeActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;

    private static final String URL = "http://www.ehlien.com/QA.json";
    private static final String Q = "Question";
    private static final String A = "Answer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        InitCloudboost.initClient();

        relativeLayout = (RelativeLayout) findViewById(R.id.wRelativeLayout);

        if (CloudUser.getcurrentUser() != null) {
            Intent showHomeActivity = new Intent(this, HomeActivity.class);
            startActivity(showHomeActivity);
        }
        //Snackbar.make(relativeLayout, "Sign in failed. Please Try again...", Snackbar.LENGTH_LONG).show();
        //new AddToTable().execute();
    }

    public void signInButton(View view) {
        Intent showSignInActivity = new Intent(this, SignInActivity.class);
        startActivity(showSignInActivity);
    }

    public void signUpNameButton(View view) {
        Intent showSignUpNameActivity = new Intent(this, SignUpNameActivity.class);
        startActivity(showSignUpNameActivity);
    }

    public void addToTable() throws CloudException, IOException, ParseException, JSONException {
        CloudObject table = new CloudObject("QuestionsAndAnswers");

        ArrayList<String> question = new ArrayList<String>();
        ArrayList<String> answer = new ArrayList<String>();

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = jsonParser.getJSONFromUrl(URL);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            question.add(jsonObject.getString(Q));
            answer.add(jsonObject.getString(A));

            table.set("question", question.get(i) + "?");
            table.set("answer", answer.get(i));

            table.save(new CloudObjectCallback() {
                @Override
                public void done(CloudObject x, CloudException e) {
                    if (x != null) {
                        // CloudObject
                        Log.i("TABLE SUCCESS:", x.toString());
                    }
                    if (e != null) {
                        // Error
                        Log.i("TABLE ERROR:", e.getMessage());
                    }
                }
            });
        }
    }

    private class AddToTable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                addToTable();
            } catch (CloudException | IOException | ParseException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}
