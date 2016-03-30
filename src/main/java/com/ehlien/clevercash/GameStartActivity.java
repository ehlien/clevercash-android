package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cloudboost.*;

public class GameStartActivity extends AppCompatActivity {
    private TextView questionNTV;
    private TextView questionTTV;
    private EditText answerET;
    private TextView answeringTV;
    private TextView earningsTV;
    private TextView dividendTV;
    private TextView earnedTV;
    private TextView timer20TV;

    private CloudQuery qaQuery = new CloudQuery("QuestionsAndAnswers");
    private CloudObject qaObject = new CloudObject("QuestionsAndAnswers");
    private CloudQuery sQuery = new CloudQuery("Scores");
    private CloudObject sObject = new CloudObject("Scores");
    private CloudUser currentUser = CloudUser.getcurrentUser();

    private int questionN;
    private String questionT;
    private String answer;
    private int answered = 0;
    private int rightAnswer = 0;
    private int wrongAnswer = 0;
    private int answering;
    private double earnings;
    private double dividend;
    private double earned;
    private String questionID;

    private int [] right;
    private int [] wrong;
    private int numberOfRight = 0;
    private int numberOfWrong = 0;

    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar today = Calendar.getInstance();
            Calendar endOfYear = Calendar.getInstance();
            endOfYear.setTime(new Date(0));
            endOfYear.set(Calendar.DAY_OF_MONTH, 31);
            endOfYear.set(Calendar.MONTH, 11);
            endOfYear.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

            double t = endOfYear.getTimeInMillis() - today.getTimeInMillis();

            timer20TV.setText(new SimpleDateFormat("0:ss", Locale.US).format(t));
            handler.postDelayed(this, 1000);
            String[] test = {"0:35", "0:05"};
            for (String time : test) {
                if (timer20TV.getText().equals(time)) {
                    if (answerET.getText().toString().isEmpty()) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "WRONG!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        answered++;
                        wrongAnswer++;
                        Log.i("----------WRONG:", "#" + wrongAnswer);
                        new Score().execute();
                    }

                    Intent showGameUpdateActivity = new Intent(getApplicationContext(), GameUpdateActivity.class);
                    showGameUpdateActivity.putExtra("pA", answering);
                    showGameUpdateActivity.putExtra("qE", earnings);
                    startActivity(showGameUpdateActivity);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        Log.i("--------START ACTIVITY:", "1 CREATE");

        InitCloudboost.initClient();

        questionNTV = (TextView) findViewById(R.id.questionNTV);
        questionTTV = (TextView) findViewById(R.id.questionTTV);
        answerET = (EditText) findViewById(R.id.answerET);
        answeringTV = (TextView) findViewById(R.id.playersAnsweringTV);
        earningsTV = (TextView) findViewById(R.id.questionEarningsTV);
        dividendTV = (TextView) findViewById(R.id.possibleDividendTV);
        earnedTV = (TextView) findViewById(R.id.yourEarningsTV);
        timer20TV = (TextView) findViewById(R.id.timer20TV);

        answering = 874;
        earnings = ((double) answering / 100);
        dividend = (earnings / answering);
        earned = 10.25;

        questionTTV.setText("");
        answeringTV.setText(String.valueOf(answering));
        earningsTV.setText("$" + earnings);
        dividendTV.setText("$" + dividend);
        earnedTV.setText("$" + earned);

        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);

        new QueryID().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("--------START ACTIVITY:", "2 START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("--------START ACTIVITY:", "3 RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("--------START ACTIVITY:", "4 PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("--------START ACTIVITY:", "5 STOP");
        if (answered != 0) {
            new Answered().execute();
        }
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("--------START ACTIVITY:", "6 RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("--------START ACTIVITY:", "7 DESTROY");
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onBackPressed() {
        // Disabled Back Button
    }

    public void answerButton(View view) {
        answerET = (EditText) findViewById(R.id.answerET);
        answerET.setEnabled(false);
        answerET.setClickable(false);

        if (answer.equalsIgnoreCase(answerET.getText().toString())) {
            Toast toast = Toast.makeText(getApplicationContext(), "CORRECT!", Toast.LENGTH_SHORT);
            toast.show();
            rightAnswer++;
            Log.i("----------RIGHT:", "#" + rightAnswer);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "WRONG!", Toast.LENGTH_SHORT);
            toast.show();
            wrongAnswer++;
            Log.i("----------WRONG:", "#" + wrongAnswer);
        }
        answered++;
        new Score().execute();
    }

    public void createScore(CloudObject object) throws CloudException {
        object.set("username", currentUser);
        object.set("right", rightAnswer);
        object.set("wrong", wrongAnswer);
        object.set("tempRight", rightAnswer);
        object.set("tempWrong", wrongAnswer);
        object.set("payouts", earned);
        object.set("earnings", earned);
        object.save(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("-----CREATE SCORE QUERY", "SUCCESS");
                    setAnswers(sQuery);
                }
                if (e != null) {
                    Log.i("-----CREATE SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void updateScore(CloudObject object, int rightAnswer, int wrongAnswer, double payouts, double earnings) throws CloudException {
        object.set("right", rightAnswer + this.rightAnswer);
        object.set("wrong", wrongAnswer + this.wrongAnswer);
        object.set("tempRight", rightAnswer);
        object.set("tempWrong", wrongAnswer);
        object.set("payouts", payouts + this.earned);
        object.set("earnings", earnings + this.earned);
        object.save(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    // Success
                    Log.i("-------NEW SCORE OBJECT", "SUCCESS");
                    setAnswers(sQuery);
                }
                if (e != null) {
                    // Error
                    Log.i("----SCORE OBJECT ERROR:", e.getLocalizedMessage());
                }
            }
        });
    }

    public void findScore(CloudQuery query) throws CloudException {
        query.equalTo("username", currentUser);
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("-------FIND SCORE QUERY", "SUCCESS");

                    updateScore(object,
                            object.getInteger("right"),
                            object.getInteger("wrong"),
                            Double.valueOf(object.get("payouts").toString()),
                            Double.valueOf(object.get("earnings").toString()));
                } else {
                    createScore(sObject);
                }
                if (e != null && object == null) {
                    Log.i("-------FIND SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void answered(CloudObject object) throws CloudException {
        Log.i("------UPDATE ANS OBJECT", "A: " + answered);
        Log.i("------UPDATE ANS OBJECT", "R: " + rightAnswer);
        Log.i("------UPDATE ANS OBJECT", "W: " + wrongAnswer);

        int answered = numberOfRight + numberOfWrong;
        double earnings = answered / 100.00;
        double dividend = earnings / answered;

        object.set("earnings", earnings);
        object.set("dividend", dividend);
        object.set("answered", answered);
        object.set("right", numberOfRight);
        object.set("wrong", numberOfWrong);

        object.save(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    // Success
                    Log.i("------UPDATE ANS OBJECT", "SUCCESS");
                }
                if (e != null) {
                    // Error
                    Log.i("-----UPDATE ANS ERROR: ", e.getLocalizedMessage());
                }
            }
        });
    }

    public void findAnswered(CloudQuery query) throws CloudException {
        query.findById(questionID, new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("---------FIND ANS QUERY", "SUCCESS");
                    answered(object);
                }
                if (e != null) {
                    Log.i("---------FIND ANS QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void setAnswers(final CloudQuery query) throws CloudException {
        query.setLimit(10000);
        query.find(new CloudObjectArrayCallback() {
            @Override
            public void done(CloudObject[] objects, CloudException e) throws CloudException {
                if (objects != null) {
                    // Success
                    Log.i("QUERY:", "SUCCESS" + objects.length);

                    right = new int[objects.length];
                    wrong = new int[objects.length];

                    for (int i = 0; i < objects.length; i++) {
                        right[i] = objects[i].getInteger("tempRight");
                        wrong[i] = objects[i].getInteger("tempWrong");

                        numberOfRight += right[i];
                        numberOfWrong += wrong[i];
                    }
                }
                if (e != null) {
                    // Error
                    Log.i("QUERY:", "ERROR " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void queryID(CloudQuery query) throws CloudException {
        query.equalTo("answered", null);
        query.orderByDesc("createdAt");
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    // Success
                    questionID = object.getId();
                    questionT = object.get("question").toString();
                    answer = object.get("answer").toString();

                    Log.i("------------------ID", "SUCCESS");
                }
                if (e != null) {
                    // Error
                    Log.i("-------------QUERY:", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private class QueryID extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                queryID(qaQuery);
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            questionTTV.setText(questionT);
        }
    }

    private class Score extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                findScore(sQuery);
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }
    }

    private class Answered extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                findAnswered(qaQuery);
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