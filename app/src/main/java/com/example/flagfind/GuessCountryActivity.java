package com.example.flagfind;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GuessCountryActivity extends AppCompatActivity {
    private Spinner sp_country_answers;
    private ImageView img_country_flag;
    private TextView lb_correct_answer;
    private TextView lb_answer_status;
    private Button btn_country_submit;
    private TextView lb_timer_count;
    private TextView num_timer_count;

    private int generateNumber;
    private Object correctAnsCode;
    private Object correctAns;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 11000; //10sec
    private boolean timerRunning;
    private String stringInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_country);

        init();
        Intent intent = getIntent();

//      Getting extra data that was parse from MainActivity.
        HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("Counties");
        boolean timer = intent.getBooleanExtra("Timer", false);
        checkTimer(timer);

        generateNumber = generateNumbers();
        Resources resources = getResources();
        try {
            correctAnsCode = hashMap.keySet().toArray()[generateNumber];
        } catch (NullPointerException ex) {
            finish();
            startActivity(intent);
        }

        String countryKey = correctAnsCode.toString().toLowerCase();
        correctAns = hashMap.get(correctAnsCode);
        if (countryKey.equals("do")) {
            countryKey = "dor";
        }


        int resID = resources.getIdentifier(countryKey, "drawable", getPackageName());
        Drawable drawable = resources.getDrawable(resID);
        stringInst = correctAns.toString();
        img_country_flag.setContentDescription(stringInst);
        img_country_flag.setImageDrawable(drawable);

        TreeMap<String, Object> treeMap = new TreeMap<>(hashMap);
        ArrayList<String> strings = new ArrayList<>();
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            strings.add(entry.getValue().toString());
        }
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_country_answers.setAdapter(stringArrayAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(stringInst,correctAns.toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Read the state of item position
        stringInst = savedInstanceState.getString(stringInst);
    }
    private void init() {
        sp_country_answers = findViewById(R.id.sp_country_answers);
        img_country_flag = findViewById(R.id.img_country_flag);
        lb_correct_answer = findViewById(R.id.lb_correct_answer);
        lb_answer_status = findViewById(R.id.lb_answer_status);
        btn_country_submit = findViewById(R.id.btn_country_submit);
        lb_timer_count = findViewById(R.id.lb_timer_count_hint);
        num_timer_count = findViewById(R.id.num_timer_count);
    }

    public void getSelectedCountry(View v) {
        if (timerRunning)
            stopTimer();
        String selected = sp_country_answers.getSelectedItem().toString();
        btn_country_submit.setTypeface(btn_country_submit.getTypeface(), Typeface.BOLD);
        btn_country_submit.setText(R.string.lb_next);
        btn_country_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        sp_country_answers.setEnabled(false);
        sp_country_answers.setClickable(false);

        if (selected.equals(correctAns)) {
            lb_answer_status.setTextColor(Color.GREEN);
            lb_answer_status.setText(R.string.lb_correct);
        } else {
            lb_correct_answer.setTextColor(Color.BLUE);
            lb_correct_answer.setText(correctAns.toString());
            lb_answer_status.setTextColor(Color.RED);
            lb_answer_status.setText(R.string.lb_wrong);
        }
    }

    public static int generateNumbers() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i < 257; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        return list.get(0);
    }

    private void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                getSelectedCountry(findViewById(android.R.id.content));
            }
        }.start();
        timerRunning = true;
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        String timeLeft = "";
        timeLeft += seconds;
        num_timer_count.setText(timeLeft);
    }

    //  Checking whether time is switched.
    private void checkTimer(boolean timer) {
        if (!timer) {
            lb_timer_count.setVisibility(View.INVISIBLE);
            num_timer_count.setVisibility(View.INVISIBLE);
        } else {
            startStop();
        }
    }
}
