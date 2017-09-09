package com.luis_santiago.flagquizapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.luis_santiago.flagquizapp.ui.SettingsActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private TextView mMinutesTextview;
    private TextView mSecondsTextView;
    private Toolbar mToolbar;
    private Button mStart;
    private Button mStop;
    private Button mReset;
    private boolean isStarted = false;
    private int minutesToStart = Keys.MINUTE_TO_START;
    private long currentSecond = 60000;
    private NotificationCompat.Builder notificaionBuilder;
    private Bitmap icon;
    private NotificationManager notificationManager;
    private int currentNotificationID = 0;
    private boolean isFirstTimePressed = false;

    private CountDownTimer countDownTimer2 = new CountDownTimer(currentSecond, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            //We rest our currentSecond so we can take track on how many seconds have passed on our UI
            currentSecond -=1000;
            mSecondsTextView.setText(String.valueOf(currentSecond / 1000));

        }
        @Override
        public void onFinish() {
            //When the 60 sec have passed we have to rest the current minutes
            Log.e(TAG, "CURRENT MINUTES: "+minutesToStart);
            Log.e(TAG, "CURRENT SECONDS: "+currentSecond);
            if(minutesToStart == 0 || minutesToStart<0 && currentSecond == 1000){
                Log.e(TAG, "Im inside the condition");
                setUpNotification();
                resetMinutes();

            }
            else{
                minutesToStart-=1;
                mSecondsTextView.setText(String.valueOf(currentSecond));
                //We restart the seconds so we dont have negative values
                currentSecond = 60000;
                mMinutesTextview.setText(String.valueOf(minutesToStart));
                //We start our onTick()
                start();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mReset.setOnClickListener(this);
        setSupportActionBar(mToolbar);
        icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.ic_launcher_app);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
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
        mMinutesTextview = (TextView) findViewById(R.id.minutes);
        mSecondsTextView = (TextView) findViewById(R.id.seconds);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mStart = (Button) findViewById(R.id.start);
        mStop = (Button) findViewById(R.id.stop);
        mReset = (Button) findViewById(R.id.reset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start:{
                // call the start method
                if(!isStarted ){
                    startTiming();
                }
                break;
            }
            case R.id.stop:{
                //call the stop method
                stop();
                break;
            }

            case R.id.reset:{
                //call the reset method
                resetMinutes();
                break;
            }
        }
    }

    private void stop(){
        countDownTimer2.cancel();
        isStarted = false;
    }

    private void resetMinutes() {
        countDownTimer2.cancel();
        currentSecond = 60000;
        minutesToStart = Keys.MINUTE_TO_START;
        mSecondsTextView.setText("00");
        mMinutesTextview.setText(String.valueOf(minutesToStart));
        isStarted = false;
        isFirstTimePressed = false;
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

    private void startTiming(){
        Log.e(TAG, "Before:"+minutesToStart);
        if(!isFirstTimePressed){
            minutesToStart-=1;
            Log.e("Main activity", "I'm resting");
            isFirstTimePressed = true;
        }
        Log.e(TAG, "NOW:"+minutesToStart);
        if(minutesToStart==1){
            mMinutesTextview.setText("0");
        }
        mMinutesTextview.setText(String.valueOf(minutesToStart));
        isStarted = true;
        countDownTimer2.start();
        Keys.UPDATE_PERMISSON = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Keys.UPDATE_PERMISSON){
            minutesToStart = Keys.MINUTE_TO_START;
            mMinutesTextview.setText(String.valueOf(minutesToStart));
            mSecondsTextView.setText("00");
            isFirstTimePressed = false;
        }
    }
}
