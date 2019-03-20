package com.example.flagfind;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, TaskCompleted {

    private Switch btn_timer;
    private TextView lb_timer_status;
    private Toolbar toolbar;
    private HashMap<String, Object> retMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setSupportActionBar(toolbar);
        btn_timer.setOnCheckedChangeListener(this);
        new FetchData(MainActivity.this).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_advanced_level:
                createIntent(AdvancedLevelActivity.class);
                return true;
            case R.id.action_guess_hints:
                createIntent(GuessHintsActivity.class);
                return true;
            case R.id.action_guess_flag:
                createIntent(GuessFlagActivity.class);
                return true;
            case R.id.action_guess_country:
                createIntent(GuessCountryActivity.class);
                return true;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (btn_timer.isChecked()) {
            lb_timer_status.setText(R.string.switch_timer_on);
        } else {
            lb_timer_status.setText(R.string.switch_timer_off);
        }
    }

    @Override
    public void onTaskComplete(HashMap<String, Object> result) {
        retMap = result;
    }

    public void openGuessCountry(View view) {
        createIntent(GuessCountryActivity.class);
    }

    public void openGuessFlag(View view) {
        createIntent(GuessFlagActivity.class);
    }

    public void openGuessHints(View view) {
        createIntent(GuessHintsActivity.class);
    }

    public void openAdvancedLevel(View view) {
        createIntent(AdvancedLevelActivity.class);
    }


    //    Navigating to Activity by creating an intent.
    public void createIntent(Class<?> createActivityClass) {
        Intent intent = new Intent(this, createActivityClass);
        intent.putExtra("Counties", retMap);
        intent.putExtra("Timer", btn_timer.isChecked());
        startActivity(intent);
    }

    //    Initializing XML to Activity elements
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        btn_timer = findViewById(R.id.btn_timer);
        lb_timer_status = findViewById(R.id.lb_timer_status);

    }
}
