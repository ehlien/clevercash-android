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

    // GET QUESTIONS IDS BY RANROM
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

    private int[] right;
    private int[] wrong;
    private int numberOfRight = 0;
    private int numberOfWrong = 0;

    // GET RIGHT AND WRONG ANSWERS FROM ALL USERS
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
                        right[i] = objects[i].getInteger("right");
                        wrong[i] = objects[i].getInteger("wrong");

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
/*
    // ---------------------------------------------------------------------------------------------

        public void answered(CloudObject object) throws CloudException {
            // QUESTION AND ANSWER TABLE
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

*/
}
