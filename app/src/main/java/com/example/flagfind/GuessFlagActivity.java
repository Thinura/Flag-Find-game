package com.example.flagfind;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GuessFlagActivity extends AppCompatActivity {

    private TextView num_timer_count_flag, lb_timer_count, lb_guess_flag, lb_answer_status;
    private Button btn_flag_submit;
    private ImageView imgv_0, imgv_1, imgv_2;
    private ImageView[] imgv_ = {imgv_0, imgv_1, imgv_2};

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 11000; //10sec
    private boolean timerRunning;

    private int[] generateNumbers;
    private Object[] correctAnsCode = new Object[3];
    private final Object[] correctAns = new Object[3];
    private final Drawable[] drawable = new Drawable[3];
    private String[] firstKey = new String[3];
    private final int[] resID = new int[3];
    private int answerNum;
    private int indexAns;
    private String selectedImage = "";
    private int selectedPre = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_flag);
        init();
        Intent intent = getIntent();
        HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("Counties");
        boolean timer = intent.getBooleanExtra("Timer", false);
        if (!timer) {
            lb_timer_count.setVisibility(View.INVISIBLE);
            num_timer_count_flag.setVisibility(View.INVISIBLE);
        } else {
            startStop();
        }

        // Set the image to imageView
        generateNumbers = generateNumbers();
        answerNum = RandomizeArray(generateNumbers);
        indexAns = find(generateNumbers, answerNum);
        lb_guess_flag.setText(hashMap.get(Objects.requireNonNull(hashMap.keySet().toArray())[answerNum]).toString());
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
            imgv_[i].setImageDrawable(drawable[i]);
        }
    }

    //    Getting selected Image
    public void getSelectedImage(View v) {
        v.getContext().getResources();
        String[] id_ = v.getResources().getResourceName(v.getId()).split("/");
        selectedImage = id_[1];

        String[] selectedImageName = selectedImage.split("_");

        // Setting the border to selected image
        for (int i = 0; i < 3; i++) {
            if (selectedPre != i) {
                if (Integer.parseInt(selectedImageName[1]) == i) {
                    imgv_[Integer.parseInt(selectedImageName[1])].setBackgroundColor(R.drawable.image_border);
                    selectedPre = i;
                } else {
                    imgv_[i].setBackgroundColor(Color.WHITE);
                }
            } else {
                imgv_[selectedPre].setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void validateAnswer(View v) {
        if (selectedImage.equals("")) {
            new InputEmptyFragment().show(getSupportFragmentManager(), "inputemptydialog");
        } else {
            if (timerRunning)
                stopTimer();
            btn_flag_submit.setTypeface(btn_flag_submit.getTypeface(), Typeface.BOLD);
            btn_flag_submit.setText(R.string.lb_next);

            btn_flag_submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
            for (int i = 0; i < 3; i++) {

                imgv_[i].setEnabled(false);
                imgv_[i].setClickable(false);
            }
            String[] selectedImageName = selectedImage.split("_");
            if (Integer.parseInt(selectedImageName[1]) == indexAns) {
                lb_answer_status.setTextColor(Color.BLUE);
                lb_answer_status.setText(R.string.lb_correct);

            } else {
                lb_answer_status.setTextColor(Color.RED);

                lb_answer_status.setText(R.string.lb_wrong);
            }
        }

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
                validateAnswer(findViewById(android.R.id.content));
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
        num_timer_count_flag.setText(timeLeft);

    }

    private void init() {
        num_timer_count_flag = findViewById(R.id.num_timer_count_flag);
        lb_timer_count = findViewById(R.id.lb_timer_count_hint);
        lb_guess_flag = findViewById(R.id.lb_guess_flag);
        lb_answer_status = findViewById(R.id.lb_answer_status);
        btn_flag_submit = findViewById(R.id.btn_flag_submit);
        imgv_0 = findViewById(R.id.imgv_0);
        imgv_1 = findViewById(R.id.imgv_1);
        imgv_2 = findViewById(R.id.imgv_2);
        imgv_[0] = imgv_0;
        imgv_[1] = imgv_1;
        imgv_[2] = imgv_2;
    }

    //    Generating a random number from the array
    public static int RandomizeArray(int[] array) {
        Random rgen = new Random();
        for (int i = 0; i < array.length; i++) {
            int randomPosition = rgen.nextInt(array.length);
            int temp = array[i];
            array[i] = array[randomPosition];
            array[randomPosition] = temp;
        }

        return array[usingThreadLocalClass()];
    }

    static int usingThreadLocalClass() {
        return ThreadLocalRandom.current().nextInt(0, 3);
    }

    public static int find(int[] a, int target) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == target)
                return i;

        return -1;
    }
}
