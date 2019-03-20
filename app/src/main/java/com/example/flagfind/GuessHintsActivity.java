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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class GuessHintsActivity extends AppCompatActivity {

    private TextView lb_timer_count;
    private TextView num_timer_count;
    private ImageView img_hint_flag;
    private TextView lb_correct_answer;
    private LinearLayout lLayout;
    private EditText edit_flag_char;
    private TextView lb_answer_status;
    private Button btn_hints_submit;
    private String answer;

    private ArrayList<Integer> indexCount = new ArrayList<>();
    private DynamicViews dynamicViews;
    private Context context;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 11000; //10sec
    private boolean timerRunning;

    private int generateNumber;
    private Object correctAnsCode;
    private Object correctAns;
    private char[] answerInChar;
    private String[] answerToString;
    private int count;
    private boolean isLong = false;
    private String currAns = "";
    private ArrayList<Integer> integersaaa = new ArrayList<>();
    private ArrayList<Integer> cctegersaaa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_hints);

        init();

        dynamicViews = new DynamicViews(context);
        Intent intent = getIntent();
        HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("Counties");
        boolean timer = intent.getBooleanExtra("Timer", false);

        if (!timer) {
            lb_timer_count.setVisibility(View.INVISIBLE);
            num_timer_count.setVisibility(View.INVISIBLE);
        } else {
            startStop();
        }

        generateNumber = generateNumbers();
        Resources resources = getResources();
        try {
            correctAnsCode = hashMap.keySet().toArray()[generateNumber];
        } catch (NullPointerException ex) {
            finish();
            startActivity(intent);
        }

        String firstKey = correctAnsCode.toString().toLowerCase();
        correctAns = hashMap.get(correctAnsCode);

        if (firstKey.equals("do")) {
            firstKey = "dor";
        }

        int resID = resources.getIdentifier(firstKey, "drawable", getPackageName());
        Drawable drawable = resources.getDrawable(resID);

        img_hint_flag.setImageDrawable(drawable);

        answer = correctAns.toString();
        answerToString = answer.split("");
        answerInChar = answer.toCharArray();

        if (answerInChar.length == 32) {
            isLong = true;
        }

        for (int i = 0; i < answerInChar.length; i++) {
            if (!Character.toString(answerInChar[i]).equals(" ")) {
                integersaaa.add(i);
            }
            lLayout.addView(dynamicViews.createEditText(getApplicationContext(), answerInChar[i], i));
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
                lb_answer_status.setTextColor(Color.RED);
                lb_answer_status.setText(R.string.lb_wrong);
                btn_hints_submit.setText(R.string.lb_next);
                btn_hints_submit.setTypeface(btn_hints_submit.getTypeface(), Typeface.BOLD);
                edit_flag_char.setEnabled(false);
                lb_correct_answer.setText(answer);
                btn_hints_submit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
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

    private void init() {
        lb_timer_count = findViewById(R.id.lb_timer_count_hint);
        num_timer_count = findViewById(R.id.num_timer_count_hint);
        img_hint_flag = findViewById(R.id.img_hint_flag);
        lb_correct_answer = findViewById(R.id.lb_correct_answer);
        lLayout = findViewById(R.id.lLayout);
        edit_flag_char = findViewById(R.id.edit_flag_char);
        lb_answer_status = findViewById(R.id.lb_answer_status);
        btn_hints_submit = findViewById(R.id.btn_hints_submit);

    }

    public void validateCharacter(View view) {
        if (edit_flag_char.getText().toString().equals("")) {
            new InputEmptyFragment().show(getSupportFragmentManager(), "inputemptydialog");
        }
        lb_answer_status.setText("");
        String typedLetter = edit_flag_char.getText().toString().toUpperCase();

        if (isMatchFound(indexCount, typedLetter)) {
            count++;
            lb_answer_status.setTextColor(Color.YELLOW);
            lb_answer_status.setText(R.string.lb_incorrect_character);
        }

        int pCount = lLayout.getChildCount();
        View v = null;
        for (int i = 0; i < pCount; i++) {
            v = lLayout.getChildAt(i);
            for (int j = 0; j < indexCount.size(); j++) {
                if (indexCount.get(j) == i) {
                    TextView txtv = v.findViewById(i);
                    txtv.setText(typedLetter);
                    int h = ((isLong) ? 9 : 14);
                    txtv.setTextSize(h);
                    cctegersaaa.add(i);
                    for (Iterator<Integer> iterator = indexCount.iterator(); iterator.hasNext(); ) {
                        int removeEle = iterator.next();
                        if (i == removeEle) {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        edit_flag_char.setFocusableInTouchMode(true);
        edit_flag_char.setFocusable(true);
        if (cctegersaaa.size() == integersaaa.size()) {

            edit_flag_char.setEnabled(false);
            lb_answer_status.setTextColor(Color.BLUE);
            lb_answer_status.setText(R.string.lb_correct);
            btn_hints_submit.setTypeface(btn_hints_submit.getTypeface(), Typeface.BOLD);
            btn_hints_submit.setText(R.string.lb_next);
            btn_hints_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            });
        }
        if (count == 3) {
            lb_answer_status.setTextColor(Color.RED);
            lb_answer_status.setText(R.string.lb_wrong);
            btn_hints_submit.setTypeface(btn_hints_submit.getTypeface(), Typeface.BOLD);
            btn_hints_submit.setText(R.string.lb_next);
            edit_flag_char.setEnabled(false);
            lb_answer_status.setTextColor(Color.BLUE);
            lb_correct_answer.setText(answer);
            btn_hints_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            });
        } else {
            edit_flag_char.getText().clear();
        }

    }

    private boolean isMatchFound(ArrayList<Integer> indexCount, String typedLetter) {
        for (int i = 0; i < answerToString.length; i++) {
            if (answerToString[i].toUpperCase().equals(typedLetter)) {
                indexCount.add(i - 1);
            }
        }
        return indexCount.isEmpty();
    }

}
