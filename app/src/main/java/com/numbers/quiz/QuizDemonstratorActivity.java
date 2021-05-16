package com.numbers.quiz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuizDemonstratorActivity extends AppCompatActivity {
    private ArrayList<Question> questions = new ArrayList<>();
    int current_pointer = 0;
    private DBHelper dbHelper = new DBHelper(this);
    private int quiz_id;
    private ImageButton prev;
    private ImageButton next;
    private int[] balls = new int[4];
    private ImageView ball1;
    private ImageView ball2;
    private ImageView ball3;
    private ImageView ball4;
    private Animation anim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_demonstrator_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent inp_intent = getIntent();
        quiz_id = inp_intent.getIntExtra("quiz_id", 1);
        MakeArray();
        prev = findViewById(R.id.prev_question);
        prev.setVisibility(View.GONE);
        next = findViewById(R.id.next_question);
        if (questions.size() == 1) next.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new DemonstratorFragment(questions.get(0))).commit();
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_pointer--;
                if (current_pointer == 0) prev.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new DemonstratorFragment(questions.get(current_pointer))).commit();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_pointer++;
                if (current_pointer == questions.size() - 1) next.setVisibility(View.GONE);
                prev.setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new DemonstratorFragment(questions.get(current_pointer))).commit();
            }
        });

        ball1 = findViewById(R.id.ball1);
        ball1.setY(-100);
        ball1.setZ(1.5f);
        ball2 = findViewById(R.id.ball2);
        ball2.setY(-100);
        ball2.setZ(1.5f);
        ball3 = findViewById(R.id.ball3);
        ball3.setY(-100);
        ball3.setZ(1.5f);
        ball4 = findViewById(R.id.ball4);
        ball4.setY(-100);
        ball4.setZ(1.5f);
        StartAnimation();
    }

    private void MakeArray(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Info.QUESTION_ID,
                Info.QUESTION_QUIZ_ID,
                Info.QUESTION_TYPE,
                Info.QUESTION_QUESTION
        };
        String selection = Info.QUESTION_QUIZ_ID + " = " + quiz_id;

        Cursor cursor = db.query(
                "QUESTION",   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext()){
            int question_id = cursor.getInt(0);
            int temp_question_type = cursor.getInt(2);
            String temp_question_string = cursor.getString(3);
            String[] projection2 = {
                    Info.ANSWER_ID,
                    Info.ANSWER_QUESTION_ID,
                    Info.ANSWER_ANSWER
            };
            String selection2 = Info.ANSWER_QUESTION_ID + " = " + question_id;

            Cursor cursor2 = db.query(
                    "ANSWERS",   // The table to query
                    projection2,             // The array of columns to return (pass null to get all)
                    selection2,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            ArrayList<String> answer_array = new ArrayList<>();
            while (cursor2.moveToNext()){
                answer_array.add(cursor2.getString(2));
            }
            cursor2.close();
            String[] answers = new String[0];
            switch (answer_array.size()){
                case 0:
                    answers = new String[]{};
                    break;
                case 2:
                    answers = new String[]{answer_array.get(0), answer_array.get(1)};
                    break;
                case 3:
                    answers = new String[]{answer_array.get(0), answer_array.get(1), answer_array.get(2)};
                    break;
                case 4:
                    answers = new String[]{answer_array.get(0), answer_array.get(1), answer_array.get(2), answer_array.get(3)};
                    break;
            }
            questions.add(new Question(temp_question_type, temp_question_string, answers));

        }
        cursor.close();
    }

    private void StartAnimation(){
        int next = (int) (Math.random()*4);
        while (!checkFairness(next)){
            next = (int) (Math.random()*4);
        }
        switch (next){
            case 0:
                anim = AnimationUtils.loadAnimation(QuizDemonstratorActivity.this, R.anim.falling_up_motion_sector_1);
                ball1.setVisibility(View.VISIBLE);
                ball1.startAnimation(anim);
                ball1.setBackgroundResource(getRandomBack());
                balls[0]++;
                current_ball = ball1;
                break;
            case 1:
                anim = AnimationUtils.loadAnimation(QuizDemonstratorActivity.this, R.anim.falling_up_motion_sector_2);
                ball2.setVisibility(View.VISIBLE);
                ball2.startAnimation(anim);
                balls[1]++;
                ball2.setBackgroundResource(getRandomBack());
                current_ball = ball2;
                break;
            case 2:
                anim = AnimationUtils.loadAnimation(QuizDemonstratorActivity.this, R.anim.falling_up_motion_sector_3);
                ball3.setVisibility(View.VISIBLE);
                ball3.startAnimation(anim);
                balls[2]++;
                ball3.setBackgroundResource(getRandomBack());
                current_ball = ball3;
                break;
            case 3:
                anim = AnimationUtils.loadAnimation(QuizDemonstratorActivity.this, R.anim.falling_up_motion_sector_4);
                ball4.setVisibility(View.VISIBLE);
                ball4.startAnimation(anim);
                balls[3]++;
                ball4.setBackgroundResource(getRandomBack());
                current_ball = ball4;
                break;
        }
        anim.setAnimationListener(animationListener);
    }

    private int getRandomBack(){
        int res_id = 0;
        switch ((int)(Math.random()*5)){
            case 0:
                res_id = R.drawable.red_ball_back;
                break;
            case 1:
                res_id = R.drawable.blue_ball_back;
                break;
            case 2:
                res_id = R.drawable.purple_ball_back;
                break;
            case 3:
                res_id = R.drawable.green_ball_back;
                break;
            case 4:
                res_id = R.drawable.yellow_ball_back;
                break;
        }
        return res_id;
    }

    private ImageView current_ball;

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            current_ball.clearAnimation();
            current_ball.setVisibility(View.INVISIBLE);
            StartAnimation();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private boolean checkFairness(int i){
        return balls[i] == findMin();
    }

    private int findMin(){
        int min = 10000000;
        for (int i = 0; i <4 ; i++) {
            if (balls[i] <= min) min = balls[i];
        }
        return min;
    }


}
