package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cloudboost.*;

public class GameSavingActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private CloudQuery qaQuery = new CloudQuery("QuestionsAndAnswers");
    private CloudQuery sQuery = new CloudQuery("Scores");

    private String questionID;
    private String rightAnswer;

    private int[] right;
    private int[] wrong;
    private int numberOfRight = 0;
    private int numberOfWrong = 0;

    private int answered;
    private double earnings;
    private double dividend;


    private Handler handler;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar today = Calendar.getInstance();
            Calendar endOfYear = Calendar.getInstance();
            endOfYear.setTime(new Date(0)); /* reset */
            endOfYear.set(Calendar.DAY_OF_MONTH, 31);
            endOfYear.set(Calendar.MONTH, 11);
            endOfYear.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

            double t = endOfYear.getTimeInMillis() - today.getTimeInMillis();
            String time = new SimpleDateFormat("0:ss", Locale.US).format(t);

            handler.postDelayed(this, 1000);
            String[] test = {"0:10"};
            for (int i = 0; i < test.length; i++) {
                if (time.equals(test[i])) {
                    finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_saving);

        progressBar = (ProgressBar) findViewById(R.id.savingPB);

        Intent receive = getIntent();
        questionID = receive.getStringExtra("qID");
        rightAnswer = receive.getStringExtra("rA");

        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);

        new Answered().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Intent showGameUpdateActivity = new Intent(getApplicationContext(), GameUpdateActivity.class);
        showGameUpdateActivity.putExtra("qID", questionID);
        showGameUpdateActivity.putExtra("rA", rightAnswer);
        startActivity(showGameUpdateActivity);
    }

    public void setAnswers(final CloudQuery query) throws CloudException {
        query.find(new CloudObjectArrayCallback() {
            @Override
            public void done(CloudObject[] objects, CloudException e) throws CloudException {
                if (objects != null) {
                    // Success
                    Log.i("QUERY:", "SUCCESS L = " + objects.length);

                    right = new int[objects.length];
                    wrong = new int[objects.length];

                    for (int i = 0; i < objects.length; i++) {
                        right[i] = objects[i].getInteger("tempRight");
                        wrong[i] = objects[i].getInteger("tempWrong");

                        numberOfRight += right[i];
                        numberOfWrong += wrong[i];
                    }

                    Log.i("---------TEMP R", "RIGHT L: " + right.length);
                    Log.i("---------TEMP W", "WRONG L: " + wrong.length);
                    Log.i("---------TEMP R", "RIGHT = " + numberOfRight);
                    Log.i("---------TEMP W", "WRONG = " + numberOfWrong);
                }
                if (e != null) {
                    // Error
                    Log.i("QUERY:", "ERROR " + e.getLocalizedMessage());
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
                    answered = numberOfRight + numberOfWrong;
                    earnings = answered / 100.00;
                    if (numberOfRight == 0) {
                        dividend = 0.00;
                    } else {
                        dividend = earnings / numberOfRight;
                    }

                    object.set("answered", answered);
                    object.set("right", numberOfRight);
                    object.set("wrong", numberOfWrong);
                    object.set("earnings", earnings);
                    object.set("dividend", dividend);
                    object.save(new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject object, CloudException e) throws CloudException {
                            if (object != null) {
                                // Success
                                Log.i("------UPDATE ANS OBJECT", "SUCCESS");
                                findScore(sQuery);
                            }
                            if (e != null) {
                                // Error
                                Log.i("-----UPDATE ANS ERROR: ", e.getLocalizedMessage());
                            }
                        }
                    });
                }
                if (e != null) {
                    Log.i("---------FIND ANS QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void addEarningsToScoreTable(CloudObject object, double payouts, double earnings) throws CloudException {
        Log.i("----------RIGHT", "ANS = " + rightAnswer);
        if (Integer.parseInt( rightAnswer ) == 1) {
            object.set("payouts", payouts + this.dividend);
            object.set("earnings", earnings + this.dividend);
            object.save(new CloudObjectCallback() {
                @Override
                public void done(CloudObject object, CloudException e) throws CloudException {
                    if (object != null) {
                        // Success
                        Log.i("----------SCORE", "USER SCORE UPDATED");
                        //setAnswers(sQuery);
                    }
                    if (e != null) {
                        // Error
                        Log.i("----------SCORE:", "ERROR: " + e.getLocalizedMessage());
                    }
                }
            });
        }
    }

    public void findScore(CloudQuery query) throws CloudException {
        query.equalTo("username", CloudUser.getcurrentUser());
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    addEarningsToScoreTable(object,
                            Double.valueOf(object.get("payouts").toString()),
                            Double.valueOf(object.get("earnings").toString()));
                }
                if (e != null) {
                    Log.i("-------FIND SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void resetScores(CloudQuery query) throws CloudException {
        query.find(new CloudObjectArrayCallback() {
            @Override
            public void done(CloudObject[] objects, CloudException e) throws CloudException {
                if (objects != null) {
                    // Success
                    Log.i("QUERY:", "SUCCESS L = " + objects.length);

                    for (int i = 0 ; i < objects.length ; i++) {
                        objects[i].set("tempRight", 0);
                        objects[i].set("tempWrong", 0);
                        objects[i].save(new CloudObjectCallback() {
                            @Override
                            public void done(CloudObject object, CloudException e) throws CloudException {
                                if (object != null) {
                                    Log.i("-------TEMP ANS CHANGE", "SUCCESS");
                                }
                                if (e != null) {
                                    Log.i("-------TEMP ANS CHANGE", "ERROR: " + e.getLocalizedMessage());
                                }
                            }
                        });
                    }

                }
                if (e != null) {
                    // Error
                    Log.i("QUERY:", "ERROR " + e.getLocalizedMessage());
                }
            }
        });
    }

    private class Answered extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                setAnswers(sQuery);
                findAnswered(qaQuery);
                resetScores(sQuery);
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
