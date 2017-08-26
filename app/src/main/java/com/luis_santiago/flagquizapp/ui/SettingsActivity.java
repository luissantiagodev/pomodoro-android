package com.luis_santiago.flagquizapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.luis_santiago.flagquizapp.Keys;
import com.luis_santiago.flagquizapp.R;

public class SettingsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Switch mSwitch;
    private SeekBar mSeekbar;
    private TextView mTextView;


    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Keys.MINUTE_TO_START = progress;
            Log.e("Setting", "Minutes are being changed" + Keys.MINUTE_TO_START);
            mTextView.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
        loadToolbar();
        mSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSeekbar.setProgress(Keys.MINUTE_TO_START);
        mTextView.setText(String.valueOf(Keys.MINUTE_TO_START));
    }

    private void initComponents(){
       mSwitch = (Switch) findViewById(R.id.switch_settings);
        mSeekbar = (SeekBar) findViewById(R.id.seekbar);
        mTextView = (TextView) findViewById(R.id.text_seek);
    }

    private void loadToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.settings);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
