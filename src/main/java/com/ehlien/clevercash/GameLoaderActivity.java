package com.ehlien.clevercash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;

public class GameLoaderActivity extends AppCompatActivity {
    private CloudQuery sQuery = new CloudQuery("Scores");
    private ProgressDialog progress;
    private int time = 0;

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
            String[] test = {"0:50"};
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
        setContentView(R.layout.activity_question_loader);
        Log.i( "----------CREATE:", "LOAD");


        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setIndeterminate(false);
        progress.setMax(100);
        progress.setProgress(time);

        progress.show();
        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);

        new ResetTemp().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i( "----------START:", "LOAD");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i( "----------RESUME:", "LOAD");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i( "----------PAUSE:", "LOAD");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i( "----------STOP:", "LOAD");
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i( "----------RESTART:", "LOAD");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i( "----------DESTROY:", "LOAD");
        handler.removeCallbacks(runnable);
        progress.dismiss();
        Intent showGameStartActivity = new Intent(getApplicationContext(), GameStartActivity.class);
        startActivity(showGameStartActivity);
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

    private class ResetTemp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
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
