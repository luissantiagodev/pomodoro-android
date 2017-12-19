package com.luis_santiago.flagquizapp;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Luis Fernando Santiago Ruiz on 12/17/17.
 */

public class Adds {

    public static AdRequest getAddInstance(Context context) {
        InterstitialAd ourInstance = new InterstitialAd(context);
        ourInstance.setAdUnitId("ca-app-pub-5461480863776866/3346084113");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("FA73653EA402CC30D55A5140976DA6C4")
                .build();
        ourInstance.loadAd(adRequest);
        return adRequest;
    }

    private Adds() {
    }
}
