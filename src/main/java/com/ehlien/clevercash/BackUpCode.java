package com.ehlien.clevercash;

import android.util.Log;

import java.util.Random;

import io.cloudboost.CloudException;
import io.cloudboost.CloudObject;
import io.cloudboost.CloudObjectArrayCallback;
import io.cloudboost.CloudObjectCallback;
import io.cloudboost.CloudQuery;
import io.cloudboost.CloudUser;

public class BackUpCode {
    private Random random = new Random();
    private String[] objectIDArray;
    private int randomID;
    private String questionT;
    private String answer;

    // BY RANDOM
    public void queryID(final CloudQuery query) throws CloudException {
        query.setLimit(10000);
        query.find(new CloudObjectArrayCallback() {
            @Override
            public void done(CloudObject[] id, CloudException e) throws CloudException {
                if (id != null) {
                    // Success
                    objectIDArray = new String[id.length];
                    randomID = random.nextInt(objectIDArray.length);

                    Log.i("LENGTH", "#" + objectIDArray.length);
                    for (int i = 0; i < id.length; i++) {
                        objectIDArray[i] = id[i].getId();
                        //Log.i("OBJECT:", "ID: " + objectIDArray[i]);
                    }

                    query.findById(objectIDArray[randomID], new CloudObjectCallback() {
                        @Override
                        public void done(CloudObject object, CloudException e) throws CloudException {
                            if (object != null) {
                                // Success
                                questionT = object.get("question").toString();
                                answer = object.get("answer").toString();

                                Log.i("Q", "Q: " + questionT);
                                Log.i("A", "A: " + answer);
                            }
                            if (e != null) {
                                // Error
                                Log.i("QUERY:", "ERROR");
                            }
                        }
                    });

                }
                if (e != null) {
                    // Error
                    Log.i("QUERY:", "ERROR " + e.getLocalizedMessage());
                }
            }
        });
    }

}
