package com.ehlien.clevercash;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;
import io.cloudboost.CloudUserCallback;

public class LeaderboardActivity extends AppCompatActivity {
    private ListView rankLV;
    private ListView nameLV;
    private ListView earnedLV;
    private ArrayAdapter<String> rankArrayAdapter;
    private ArrayAdapter<String> nameArrayAdapter;
    private ArrayAdapter<String> earnedArrayAdapter;
    private CloudQuery sQuery = new CloudQuery("Scores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        InitCloudboost.initClient();

        rankLV = (ListView) findViewById(R.id.rankLV);
        nameLV = (ListView) findViewById(R.id.playerNameLV);
        earnedLV = (ListView) findViewById(R.id.playersEarnedLV);
        rankArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        nameArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        earnedArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        new Leaderboard().execute();
    }

    public void showLeaderboard(CloudQuery query) throws CloudException {
        query.find(new CloudObjectArrayCallback() {
            @Override
            public void done(CloudObject[] objects, CloudException e) throws CloudException {
                if (objects != null) {
                    for (int i = 0 ; i < objects.length ; i++) {
                        rankArrayAdapter.add(String.valueOf(i+1));
                        nameArrayAdapter.add(objects[i].getString("user"));
                        earnedArrayAdapter.add("$" + objects[i].get("earnings").toString());
                    }
                }
                if (e != null) {
                    Log.i("Leaderboard", "ERROR: " + e.getLocalizedMessage());
                }
            }
        });
    }

    private class Leaderboard extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                showLeaderboard(sQuery);
            } catch (CloudException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            rankLV.setAdapter(rankArrayAdapter);
            nameLV.setAdapter(nameArrayAdapter);
            earnedLV.setAdapter(earnedArrayAdapter);
        }
    }
}
