package com.luis_santiago.flagquizapp.ui;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.luis_santiago.flagquizapp.Keys;
import com.luis_santiago.flagquizapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPrefrences extends Fragment {

    private SeekBar mSeekbar;
    private TextView mTextView;
    private View view;

    public FragmentPrefrences() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fragment_prefrences, container, false);
        init();
        mSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        mSeekbar.setProgress(Keys.MINUTE_TO_START);
        mTextView.setText(String.valueOf(Keys.MINUTE_TO_START));
        return view;
    }

    private void init(){
        mSeekbar = (SeekBar) view.findViewById(R.id.seekbar);
        mTextView = (TextView) view.findViewById(R.id.text_seek);
    }

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Keys.MINUTE_TO_START = progress;
            Keys.UPDATE_PERMISSON = true;
            Log.e("Setting", "Minutes are being changed" + Keys.MINUTE_TO_START);
            mTextView.setText(String.valueOf(progress));
        }
        //Not needed for now
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}
