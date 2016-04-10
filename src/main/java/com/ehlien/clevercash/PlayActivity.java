package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectCallback;

public class PlayActivity extends AppCompatActivity {
    private TextView gameStartTV;
    private TextView liveTimerTV;

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

            liveTimerTV.setText(new SimpleDateFormat("0:ss", Locale.CANADA).format(t));
            handler.postDelayed(this, 1000);
            String[] test = {"0:25", "0:24", "0:23", "0:22", "0:21",
                             "0:20", "0:19", "0:18", "0:17", "0:16",
                             "0:15", "0:14", "0:13", "0:12", "0:11",
                             "0:10", "0:09", "0:08", "0:07", "0:06",
                             "0:05", "0:04", "0:03", "0:02", "0:01"};
            for (int i = 0; i < test.length; i++) {
                if (liveTimerTV.getText().toString().equals(test[i])) {
                    Log.i("----------TEST:", "TIME: " + liveTimerTV.getText().toString() + " EQUALS: " + test[i]);
                    gameStartTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast toast = Toast.makeText(getApplicationContext(), "PLS WAIT!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } else {
                    gameStartTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent showGameLoaderActivity = new Intent(getApplicationContext(), GameLoaderActivity.class);
                            startActivity(showGameLoaderActivity);
                        }
                    });
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Log.i("---------PLAY ACTIVITY:", "1 CREATE");

        InitCloudboost.initClient();

        gameStartTV = (TextView) findViewById(R.id.gameStartTV);
        liveTimerTV = (TextView) findViewById(R.id.liveTimerTV);

        handler = new Handler(getMainLooper());
        handler.postDelayed(runnable, 10);

        //new Time().execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("---------PLAY ACTIVITY:", "2 START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("---------PLAY ACTIVITY:", "3 RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("---------PLAY ACTIVITY:", "4 PAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("---------PLAY ACTIVITY:", "5 STOP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("---------PLAY ACTIVITY:", "6 RESTART");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("---------PLAY ACTIVITY:", "7 DESTROY");
        handler.removeCallbacks(runnable);
    }

    public void settingsButton(View view) {
        Intent showSettingsActivity = new Intent(this, SettingsActivity.class);
        startActivity(showSettingsActivity);
    }

    public void time() throws CloudException{
        CloudObject table = new CloudObject("Time");

        Calendar startOfYear = Calendar.getInstance();
        startOfYear.setTime(new Date(0)); /* reset */
        startOfYear.set(Calendar.DAY_OF_MONTH, 31);
        startOfYear.set(Calendar.MONTH, 0);
        startOfYear.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

        Calendar endOfYear = Calendar.getInstance();
        endOfYear.setTime(new Date(0)); /* reset */
        endOfYear.set(Calendar.DAY_OF_MONTH, 31);
        endOfYear.set(Calendar.MONTH, 11);
        endOfYear.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));

        table.set("Time", startOfYear);
        table.setExpires(endOfYear);
        table.save(new CloudObjectCallback() {
            @Override
            public void done(CloudObject object, CloudException e) throws CloudException {
                if (object != null) {
                    System.out.println( object );
                }
                if (e != null) {
                    System.out.println( e.getLocalizedMessage() );
                }
            }
        });
    }

    private class Time extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                time();
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
