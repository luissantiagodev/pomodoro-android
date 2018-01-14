package com.luis_santiago.pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;

    private FloatingActionButton mReset;

    private FloatingActionButton mStart;

    private FloatingActionButton mStop;

    private NotificationCompat.Builder notificaionBuilder;

    private Bitmap icon;

    private NotificationManager notificationManager;

    private int currentNotificationID = 0;

    private AdView mAdview;

    private ArcProgress mCircularProgressBar;

    private CountDownTimer mCounterTimer;

    private TextView mTimeTextView;

    private Long generalMilisecondsProgress;

    private boolean isCounting;

    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setSupportActionBar(mToolbar);

        icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher_app);

        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        MobileAds.initialize(this, "ca-app-pub-5461480863776866~9743955901");

        
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdview.loadAd(adRequest);

        mStart.setOnClickListener(this);
        mReset.setOnClickListener(this);
        mStop.setOnClickListener(this);

        /**
         * 20 minutes is equal to 1,200,000 miliseconds by multiplying 60 seg * 1000 milliseconds
         * */
        setUpToolbar();
        setMaxPref();
    }


    private void setMaxPref(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Long valueFromSettings = convertMinutesToMiliseconds(Long.valueOf(preferences.getString("time_progress" , "20")));
        mCircularProgressBar.setMax((int) (valueFromSettings / 1000));
    }

    private void setUpTimer(Long resOfMinutes){
        //Get the current value from the preference settings
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Long valueFromSettings = convertMinutesToMiliseconds(Long.valueOf(preferences.getString("time_progress" , "20")));

        if(resOfMinutes !=null){
            valueFromSettings = resOfMinutes;
        }

        mCounterTimer = new CountDownTimer(valueFromSettings , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                isCounting = true;
                int processMade = (int) (millisUntilFinished / 1000);
                int minutes = processMade / 60;
                int seconds = processMade % 60;
                generalMilisecondsProgress = millisUntilFinished;
                mTimeTextView.setText(String.format("%02d", minutes) + ":" + String.format("%02d" , seconds));
                mCircularProgressBar.setProgress(processMade);
            }

            @Override
            public void onFinish() {
                setUpNotification();
                isCounting = false;
            }
        }.start();
    }


    private Long convertMinutesToMiliseconds(Long minutes){
        return minutes * 1000 * 60;
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
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            }
        }
        return true;
    }

    private void initComponents(){
        mAdview = (AdView) findViewById(R.id.adView);
        mStart = (FloatingActionButton) findViewById(R.id.play);
        mStop = (FloatingActionButton) findViewById(R.id.pause);
        mReset = (FloatingActionButton) findViewById(R.id.reset);
        mCircularProgressBar = (ArcProgress) findViewById(R.id.circular_progress);
        mTimeTextView = (TextView) findViewById(R.id.time_text_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pause : {
                mCounterTimer.cancel();
                isCounting = false;
                break;
            }

            case R.id.play : {
                if (generalMilisecondsProgress != null && !isCounting) {
                    mCounterTimer.cancel();
                    setUpTimer(generalMilisecondsProgress);
                }else if (firstTime){
                    setUpTimer(null);
                    firstTime = false;
                }
                break;
            }

            case R.id.reset : {
                mCounterTimer.cancel();
                setUpTimer(null);
                setMaxPref();
                break;
            }
        }
    }


    private void setUpNotification(){
        notificaionBuilder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_launcher_app)
                .setLargeIcon(icon)
                .setContentTitle("Hora de descansar!")
                .setContentText("Has estado trabajando duro, es hora de tomar un descanso!");
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

    private void setUpToolbar(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
