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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import io.cloudboost.*;

public class GameStartActivity extends AppCompatActivity {
    private TextView questionTTV;
    private EditText answerET;
    private TextView wordsTV;
    private TextView earnedTV;
    private TextView timer20TV;

    private CloudQuery qaQuery = new CloudQuery("QuestionsAndAnswers");
    private CloudQuery sQuery = new CloudQuery("Scores");
    private CloudObject sObject = new CloudObject("Scores");
    private CloudUser currentUser = CloudUser.getcurrentUser();

    static String questionID;
    private String questionT;
    private String answer;
    private int words = 0;
    private int answered = 0;
    private int rightAnswer = 0;
    private int wrongAnswer = 0;
    private double earnings = 0;
    private double earned = 0;

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
            String[] test = {"0:20"};
            for (String time : test) {
                if (timer20TV.getText().equals(time)) {
                    answerCheck();

                    Intent showGameSavingActivity = new Intent(getApplicationContext(), GameSavingActivity.class);
                    showGameSavingActivity.putExtra("qID", questionID);
                    showGameSavingActivity.putExtra("rA", String.valueOf(rightAnswer));
                    startActivity(showGameSavingActivity);
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

        questionTTV = (TextView) findViewById(R.id.questionTTV);
        answerET = (EditText) findViewById(R.id.answerET);
        wordsTV = (TextView) findViewById(R.id.numberOfWordsTV);
        earnedTV = (TextView) findViewById(R.id.earningsTV);
        timer20TV = (TextView) findViewById(R.id.timer20TV);

        questionTTV.setText("");
        wordsTV.setText("");
        earnedTV.setText("");

        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);

        new Question().execute();
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
        answerET.setEnabled(false);
        answerET.setClickable(false);
        answerCheck();
    }

    public void answerCheck() {
        if (answered == 0) {
            answered++;

            if (answer.equalsIgnoreCase(answerET.getText().toString())) {
                rightAnswer++;
                Toast.makeText(getApplicationContext(), "RIGHT!", Toast.LENGTH_SHORT).show();
            } else {
                wrongAnswer++;
                Toast.makeText(getApplicationContext(), "WRONG!", Toast.LENGTH_SHORT).show();
            }
            Log.i("----------ANSWERED:", "= " + answered);
            Log.i("----------RIGHT:", "= " + rightAnswer);
            Log.i("----------WRONG:", "= " + wrongAnswer);

            new Score().execute();
        }
    }

    public void createScore(CloudObject object) throws CloudException {
        object.set("username", currentUser);
        object.set("user", SignInActivity.username);
        object.set("right", rightAnswer);
        object.set("wrong", wrongAnswer);
        object.set("tempRight", rightAnswer);
        object.set("tempWrong", wrongAnswer);
        /*if (rightAnswer == 1) {
            object.set("payouts", earned);
            object.set("earnings", earned);
        }*/
        //if (wrongAnswer == 1) {
            object.set("payouts", 0.00);
            object.set("earnings", 0.00);
        //}
        object.save(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    Log.i("----------SCORE", "NEW USER SCORE CREATED");
                    //setAnswers(sQuery);
                }
                if (e != null) {
                    Log.i("----------SCORE", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void updateScore(CloudObject object, int rightAnswer, int wrongAnswer, double payouts, double earnings) throws CloudException {
        object.set("right", rightAnswer + this.rightAnswer);
        object.set("wrong", wrongAnswer + this.wrongAnswer);
        object.set("tempRight", this.rightAnswer);
        object.set("tempWrong", this.wrongAnswer);
        /*if (rightAnswer == 1) {
            object.set("payouts", payouts + this.earned);
            object.set("earnings", earnings + this.earned);
        }*/
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

    public void findScore(CloudQuery query) throws CloudException {
        query.equalTo("username", currentUser);
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object == null) {
                    Log.i("-------FIND SCORE QUERY", "SUCCESS");
                    createScore(sObject);
                } else if (object != null) {
                    updateScore(object,
                            object.getInteger("right"),
                            object.getInteger("wrong"),
                            Double.valueOf(object.get("payouts").toString()),
                            Double.valueOf(object.get("earnings").toString()));
                } else {
                    Log.i("-------FIND SCORE QUERY", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void showYourEarnings(CloudQuery query) throws CloudException {
        query.equalTo("username", currentUser);
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object == null) {
                    Log.i("-------FIND USER SCORE", "SUCCESS");
                    earnings = 0.00;
                } else if (object != null) {
                    earnings = Double.valueOf( object.get("earnings").toString() );
                } else {
                    Log.i("-------FIND USER SCORE", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public static int countWords(String s){
        int wordCount = 0;
        boolean word = false;
        int endOfLine = s.length() - 1;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    public void showQuestion(CloudQuery query) throws CloudException {
        query.equalTo("answered", null);
        query.orderByDesc("createdAt");
        query.findOne(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    // Success
                    Log.i("------------------ID", "SUCCESS");
                    questionID = object.getId();
                    questionT = object.get("question").toString();
                    answer = object.get("answer").toString();
                    words = countWords(answer);
                    showYourEarnings(sQuery);
                }
                if (e != null) {
                    // Error
                    Log.i("-------------QUERY:", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private class Question extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                showQuestion(qaQuery);
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            questionTTV.setText(questionT);
            wordsTV.setText(String.valueOf(words));
            earnedTV.setText(String.format(Locale.CANADA, "$%.2f", (float)earned));
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

}