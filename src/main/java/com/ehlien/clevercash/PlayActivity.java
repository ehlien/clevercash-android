package com.ehlien.clevercash;

import android.content.Intent;
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
            String[] test = {"0:36", "0:35", "0:34", "0:33", "0:32", "0:31", "0:30",
                    "0:06", "0:05", "0:04", "0:03", "0:02", "0:01", "0:00"};
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
}
