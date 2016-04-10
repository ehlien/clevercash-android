package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;

public class GameUpdateActivity extends AppCompatActivity {
    private TextView playersAnsweredNTV;
    private TextView gotWrongNTV;
    private TextView gotRightNTV;
    private TextView questionEarningsNTV;
    private TextView yourDividendNTV;
    private TextView youEarnedNTV;
    private TextView timer5TV;

    private CloudQuery qaQuery = new CloudQuery("QuestionsAndAnswers");
    private CloudQuery sQuery = new CloudQuery("Scores");
    private CloudUser currentUser = CloudUser.getcurrentUser();

    int pA;
    int gW;
    int gR;
    double qE;
    double yD;
    double yE;
    String questionID;
    String rightAnswer;

    private Bundle bundle;
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

            timer5TV.setText("NEXT QUESTION IN " + time);
            handler.postDelayed(this, 1000);
            String[] test = {"0:00"};
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
        setContentView(R.layout.activity_game_update);

        Log.i("-------UPDATE ACTIVITY:", "1 CREATE");

        InitCloudboost.initClient();

        playersAnsweredNTV = (TextView) findViewById(R.id.playersAnsweredNTV);
        gotWrongNTV = (TextView) findViewById(R.id.gotWrongNTV);
        gotRightNTV = (TextView) findViewById(R.id.gotRightNTV);
        questionEarningsNTV = (TextView) findViewById(R.id.questionEarningsNTV);
        yourDividendNTV = (TextView) findViewById(R.id.yourDividendNTV);
        youEarnedNTV = (TextView) findViewById(R.id.youEarnedNTV);
        timer5TV = (TextView) findViewById(R.id.timer5TV);

        playersAnsweredNTV.setText("");
        gotWrongNTV.setText("");
        gotRightNTV.setText("");
        questionEarningsNTV.setText("$0.00");
        yourDividendNTV.setText("$0.00");
        youEarnedNTV.setText("$0.00");

        Intent receive = getIntent();
        questionID = receive.getStringExtra("qID");
        rightAnswer = receive.getStringExtra("rA");

        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);
        new Score().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("-------UPDATE ACTIVITY:", "2 START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("-------UPDATE ACTIVITY:", "3 RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("-------UPDATE ACTIVITY:", "4 PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("-------UPDATE ACTIVITY:", "5 STOP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("-------UPDATE ACTIVITY:", "6 RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("-------UPDATE ACTIVITY:", "7 DESTROY");
        handler.removeCallbacks(runnable);
        Intent showGameLoaderActivity = new Intent(getApplicationContext(), GameLoaderActivity.class);
        startActivity(showGameLoaderActivity);
    }

    @Override
    public void onBackPressed() {
        // Back Button Disabled
    }

    public void showScore() throws CloudException {
        Log.i("-------QUESTION ID", "# " + questionID);
        qaQuery.equalTo("id", questionID);
        qaQuery.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("-------FIND SCORE QUERY", "SUCCESS");
                    pA = Integer.valueOf(object.get("answered").toString());
                    gR = Integer.valueOf(object.get("right").toString());
                    gW = Integer.valueOf(object.get("wrong").toString());
                    qE = Double.valueOf(object.get("earnings").toString());
                    yD = Double.valueOf(object.get("dividend").toString());
                }
                if (e != null) {
                    Log.i("-------FIND SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
        sQuery.equalTo("username", currentUser);
        sQuery.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("-------FIND SCORE QUERY", "SUCCESS");
                    yE = Double.valueOf(object.get("earnings").toString());
                }
                if (e != null) {
                    Log.i("-------FIND SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private class Score extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                showScore();
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            playersAnsweredNTV.setText(String.valueOf(pA));
            gotRightNTV.setText(String.valueOf(gR));
            gotWrongNTV.setText(String.valueOf(gW));
            questionEarningsNTV.setText(String.format(Locale.CANADA, "$%.2f", (float)qE));
            if (Integer.parseInt(rightAnswer) == 1) {
                yourDividendNTV.setText(String.format(Locale.CANADA, "$%.2f", (float) yD));
            } else {
                yourDividendNTV.setText(String.format(Locale.CANADA, "$%.2f", (float) 0.00));
            }
            youEarnedNTV.setText(String.format(Locale.CANADA, "$%.2f", (float)yE));
        }
    }
}
