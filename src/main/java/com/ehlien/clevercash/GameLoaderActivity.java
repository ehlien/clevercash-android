package com.ehlien.clevercash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class GameLoaderActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private int time = 0;
    private Handler handler = new Handler();

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i( "----------START:", "LOAD");

        final Thread thread =  new Thread() {
            @Override
            public void run() {
                while (time < 100) {
                    try {
                        Thread.sleep(10);
                        time++;
                        progress.setProgress(time);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Intent showGameStartActivity = new Intent(getApplicationContext(), GameStartActivity.class);
                startActivity(showGameStartActivity);
            }
        };
        thread.start();
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
        progress.dismiss();
    }
}
