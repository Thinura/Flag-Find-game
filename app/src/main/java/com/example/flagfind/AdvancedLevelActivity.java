package com.example.flagfind;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class AdvancedLevelActivity extends AppCompatActivity {

    private TextView lb_timer_count_advanced, num_timer_count_advanced, lb_score_advanced,
            lb_correct_answer_0, lb_correct_answer_1, lb_correct_answer_2, lb_answer_status;
    private EditText edit_advanced_0, edit_advanced_1, edit_advanced_2;
    private ImageView imgvAdv_0, imgvAdv_1, imgvAdv_2;
    private ImageView[] imgvAdv_ = {imgvAdv_0, imgvAdv_1, imgvAdv_2};
    private TextView[] lb_correct_answer_ = {lb_correct_answer_0, lb_correct_answer_1, lb_correct_answer_2};
    private EditText[] edit_advanced_ = {edit_advanced_0, edit_advanced_1, edit_advanced_2};
    private Button btn_advanced_submit;

    private ArrayList<Integer> correctFlagIndex = new ArrayList<>();
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000; //10sec
    private boolean timerRunning;
    private int[] generateNumbers;
    private Object[] correctAnsCode = new Object[3];
    private final Object[] correctAns = new Object[3];
    private final Drawable[] drawable = new Drawable[3];
    private String[] firstKey = new String[3];
    private final int[] resID = new int[3];
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_level);
        init();
        Intent intent = getIntent();
        HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("Counties");
        boolean timer = intent.getBooleanExtra("Timer", false);
        if (!timer) {
            lb_timer_count_advanced.setVisibility(View.INVISIBLE);
            num_timer_count_advanced.setVisibility(View.INVISIBLE);
        } else {
            startStop();
        }

        //Set the image to imageView
        generateNumbers = generateNumbers();
        Resources resources = getResources();
        for (int i = 0; i < generateNumbers.length; i++) {
            try {
                correctAnsCode[i] = Objects.requireNonNull(hashMap.keySet().toArray())[generateNumbers[i]];
            } catch (NullPointerException ex) {
                finish();
                startActivity(intent);
            }
            firstKey[i] = correctAnsCode[i].toString().toLowerCase();
            correctAns[i] = hashMap.get(correctAnsCode[i]);
            if (firstKey[i].equals("do")) {
                firstKey[i] = "dor";
            }
            resID[i] = resources.getIdentifier(firstKey[i], "drawable", getPackageName());
            drawable[i] = resources.getDrawable(resID[i]);
            imgvAdv_[i].setImageDrawable(drawable[i]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void startStop() {
        if (timerRunning) {
            stopTimer();
        } else {

            startTimer();
        }
    }

    private void startTimer() {
        count = 1;
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliseconds = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                if (count == 3) {
                    stopTimer();
                    lb_answer_status.setText(R.string.lb_wrong);
                    btn_advanced_submit.setText(R.string.lb_next);
                    btn_advanced_submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);

                        }
                    });
                } else {
                    timerRunning = true;
                    count++;
                    countDownTimer.start();
                }
            }
        }.start();
        timerRunning = true;
    }

    public static int[] generateNumbers() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < 257; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] num = new int[3];
        for (int i = 0; i < num.length; i++) {
            num[i] = list.get(i);
        }

        return num;
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
    }

    private void updateTimer() {
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;
        int timeLeft = 0;
        timeLeft += seconds;
        num_timer_count_advanced.setText(String.valueOf(timeLeft));
    }

    private void init() {

        lb_timer_count_advanced = findViewById(R.id.lb_timer_count_advanced);
        num_timer_count_advanced = findViewById(R.id.num_timer_count_advanced);
        lb_score_advanced = findViewById(R.id.lb_score_advanced);

        imgvAdv_0 = findViewById(R.id.imgvAdv_0);
        imgvAdv_[0] = imgvAdv_0;
        lb_correct_answer_0 = findViewById(R.id.lb_correct_answer_0);
        lb_correct_answer_[0] = lb_correct_answer_0;
        edit_advanced_0 = findViewById(R.id.edit_advanced_0);
        edit_advanced_[0] = edit_advanced_0;

        imgvAdv_1 = findViewById(R.id.imgvAdv_1);
        imgvAdv_[1] = imgvAdv_1;
        lb_correct_answer_1 = findViewById(R.id.lb_correct_answer_1);
        lb_correct_answer_[1] = lb_correct_answer_1;
        edit_advanced_1 = findViewById(R.id.edit_advanced_1);
        edit_advanced_[1] = edit_advanced_1;

        imgvAdv_2 = findViewById(R.id.imgvAdv_2);
        imgvAdv_[2] = imgvAdv_2;
        lb_correct_answer_2 = findViewById(R.id.lb_correct_answer_2);
        lb_correct_answer_[2] = lb_correct_answer_2;
        edit_advanced_2 = findViewById(R.id.edit_advanced_2);
        edit_advanced_[2] = edit_advanced_2;

        lb_answer_status = findViewById(R.id.lb_answer_status);
        btn_advanced_submit = findViewById(R.id.btn_advanced_submit);

    }

    public void validateInput(View view) {

        if (!inputIsEmpty()) {
            new InputEmptyFragment().show(getSupportFragmentManager(),"inputemptydialog");
        }
        count++;

        int j = generateNumbers.length;
        if (count == 3) {

            if (timerRunning) {
                countDownTimer.cancel();
            }

            if (!correctFlagIndex.isEmpty()) {
                for (int i = 0; i < correctFlagIndex.size(); i++) {
                    edit_advanced_[correctFlagIndex.get(i)].setTextColor(Color.RED);
                    edit_advanced_[correctFlagIndex.get(i)].setEnabled(false);
                    lb_correct_answer_[correctFlagIndex.get(i)].setText(correctAns[correctFlagIndex.get(i)].toString());
                    lb_correct_answer_[correctFlagIndex.get(i)].setTextColor(Color.BLUE);
                }
            } else {
                for (int i = 0; i < generateNumbers.length; i++) {
                    edit_advanced_[i].setTextColor(Color.RED);
                    edit_advanced_[i].setEnabled(false);
                    lb_correct_answer_[i].setText(correctAns[i].toString());
                    lb_correct_answer_[i].setTextColor(Color.BLUE);
                }
            }
            lb_answer_status.setTextColor(Color.RED);
            lb_answer_status.setText(R.string.lb_wrong);
            btn_advanced_submit.setTypeface(btn_advanced_submit.getTypeface(), Typeface.BOLD);
            btn_advanced_submit.setText(R.string.lb_next);
            btn_advanced_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            });
        } else {
            int score = 0;
            for (int i = 0; i < generateNumbers.length; i++) {
                if (edit_advanced_[i].getText().toString().toUpperCase().equals(correctAns[i].toString().toUpperCase())) {
                    for (Iterator<Integer> iterator = correctFlagIndex.iterator(); iterator.hasNext(); ) {
                        int removeEle = iterator.next();
                        if (i == removeEle) {
                            iterator.remove();
                        }
                    }
                    score++;
                    lb_score_advanced.setText(String.valueOf(score));
                    startStop();
                    edit_advanced_[i].setEnabled(false);
                    edit_advanced_[i].setTextColor(Color.GREEN);
                    lb_answer_status.setTextColor(Color.BLUE);
                    lb_answer_status.setText(R.string.lb_correct);
                    btn_advanced_submit.setTypeface(btn_advanced_submit.getTypeface(), Typeface.BOLD);
                    btn_advanced_submit.setText(R.string.lb_next);
                    btn_advanced_submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);

                        }
                    });
                } else {

                    correctFlagIndex.add(i);
                    correctFlagIndex = removeDuplicates(correctFlagIndex);
                    edit_advanced_[i].getText().clear();
                    if (i < j) {
                        j = i;
                        edit_advanced_[j].requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edit_advanced_[j], InputMethodManager.SHOW_IMPLICIT);
                        edit_advanced_[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                return actionId == EditorInfo.IME_ACTION_DONE;
                            }
                        });
                    }
                }
            }
        }
    }

    private boolean inputIsEmpty() {
        int emptyCount = 0;
        for (int i = 0; i < 3; i++) {
            if (edit_advanced_[i].getText().toString().equals("")) {
                emptyCount++;
            }
        }
        return emptyCount != 3;
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

}
