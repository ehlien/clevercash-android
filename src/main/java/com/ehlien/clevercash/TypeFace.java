package com.ehlien.clevercash;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class TypeFace extends Application {
    static Typeface futureLT;
    static Typeface futureLTBold;
    static Typeface futureLTBoldOblique;
    static Typeface futureLTBook;
    static Typeface futureLTBookOblique;
    static Typeface futureLTCondensed;
    static Typeface futureLTCondensedBold;
    static Typeface futureLTCondensedBoldOblique;
    static Typeface futureLTCondensedExtraBold;
    static Typeface futureLTCondensedLight;
    static Typeface futureLTCondensedLightOblique;
    static Typeface futureLTCondensedOblique;
    static Typeface futureLTCondensedExtraBoldOblique;
    static Typeface futureLTExtraBold;
    static Typeface futureLTExtraBoldOblique;
    static Typeface futureLTHeavy;
    static Typeface futureLTHeavyOblique;
    static Typeface futureLTLight;
    static Typeface futureLTLightOblique;
    static Typeface futureLTOblique;

    public static void FuturaLT(Context context) {
        futureLT = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT.ttf");
        futureLTBold = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Bold.ttf");
        futureLTBoldOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-BoldOblique.ttf");
        futureLTBook = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Book.ttf");
        futureLTBookOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-BookOblique.ttf");
        futureLTCondensed = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Condensed.ttf");
        futureLTCondensedBold = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedBold.ttf");
        futureLTCondensedBoldOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedBoldOblique.ttf");
        futureLTCondensedExtraBold = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedExtraBold.ttf");
        futureLTCondensedLight = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedLight.ttf");
        futureLTCondensedLightOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedLightObl.ttf");
        futureLTCondensedOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondensedOblique.ttf");
        futureLTCondensedExtraBoldOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-CondExtraBoldObl.ttf");
        futureLTExtraBold = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-ExtraBold.ttf");
        futureLTExtraBoldOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-ExtraBoldOblique.ttf");
        futureLTHeavy = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Heavy.ttf");
        futureLTHeavyOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-HeavyOblique.ttf");
        futureLTLight = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Light.ttf");
        futureLTLightOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-LightOblique.ttf");
        futureLTOblique = Typeface.createFromAsset(context.getAssets(), "FuturaLT/FuturaLT-Oblique.ttf");
    }
}
