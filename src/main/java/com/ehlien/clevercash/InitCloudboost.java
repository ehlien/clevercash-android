package com.ehlien.clevercash;

import android.app.Application;
import io.cloudboost.*;

public class InitCloudboost extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initClient();
    }

    public static void initClient() {
        CloudApp.init("lemidcqnshoo", "19f068e8-062d-494b-8724-2775ee485273");
    }
}
