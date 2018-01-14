package com.luis_santiago.pomodoro;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    public static void startThisActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SettingsFragment fragmentPrefrences = new SettingsFragment();
        setUpFragment(fragmentPrefrences);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void setUpFragment(android.app.Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame,fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LinearLayout root;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent().getParent();
        } else{
            root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        }

        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.action_app_bar, root, false);
        bar.setTitle("Ajustes");
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        root.addView(bar, 0);
    }

}
