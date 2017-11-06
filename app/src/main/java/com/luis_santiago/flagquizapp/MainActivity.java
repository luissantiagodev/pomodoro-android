package com.luis_santiago.flagquizapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Button mReset;
    private Button mStart;
    private Button mStop;
    private NotificationCompat.Builder notificaionBuilder;
    private Bitmap icon;
    private NotificationManager notificationManager;
    private int currentNotificationID = 0;
    //This is for ads
    private InterstitialAd interstitialAd;
    private AdView mAdview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


        setSupportActionBar(mToolbar);

        icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher_app);

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        /*interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5461480863776866/3346084113");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("FA73653EA402CC30D55A5140976DA6C4")
                .build();
        interstitialAd.loadAd(adRequest);
        mAdview.loadAd(adRequest);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:{

                break;
            }
        }
        return true;
    }

    private void initComponents(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mAdview = (AdView) findViewById(R.id.adView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:{

                break;
            }
            case R.id.stop:{
                //call the stop method

                break;
            }

            case R.id.reset:{
                //call the reset method

                break;
            }
        }
    }


    private void setUpNotification(){
        notificaionBuilder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_launcher_app)
                .setLargeIcon(icon)
                .setContentTitle("Time to relax!")
                .setContentText("You've been working very hard, it's time for a break!");
        sendNotification();
    }

    private void sendNotification(){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notificaionBuilder.setContentIntent(contentIntent);
        Notification notification = notificaionBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        // In order to send notification we have to create a unique id for each one
        currentNotificationID++;

        int notificationId = currentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;
        // setting our notification to just be send!
        notificationManager.notify(notificationId, notification);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mAdview != null) {
            mAdview.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdview!= null) {
            mAdview.destroy();
        }
        super.onDestroy();
    }
}
