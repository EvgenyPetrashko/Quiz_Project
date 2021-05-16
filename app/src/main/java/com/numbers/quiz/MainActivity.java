package com.numbers.quiz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private int[] balls = new int[4];
    private ImageView ball1;
    private ImageView ball2;
    private ImageView ball3;
    private ImageView ball4;
    private Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Button q_const = findViewById(R.id.quiz_constructor);
        q_const.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        new AlertDialog.Builder(MainActivity.this, R.style.MyDialog).setSingleChoiceItems(new String[]{"15s", "30s", "45s", "60s", "Without timer"}, 4, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which2) {
                                Intent intent = new Intent(getBaseContext(), ConstructorActivity.class);
                                intent.putExtra("timer", which2);
                                startActivity(intent);
                            }
                        }).setTitle("Choose the time bounds for each question").show();
            }
        });
        Button q_list = findViewById(R.id.quiz_list);
        q_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreatedQuizzesActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.title_block).setZ(1.00f);
        findViewById(R.id.button_block).setZ(1.00f);

        ball1 = findViewById(R.id.ball1);
        ball1.setY(-100);
        ball2 = findViewById(R.id.ball2);
        ball2.setY(-100);
        ball3 = findViewById(R.id.ball3);
        ball3.setY(-100);
        ball4 = findViewById(R.id.ball4);
        ball4.setY(-100);
        StartAnimation();
    }

    private void StartAnimation(){
        int next = (int) (Math.random()*4);
        while (!checkFairness(next)){
            next = (int) (Math.random()*4);
        }
        switch (next){
            case 0:
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.falling_up_motion_sector_1);
                ball1.setVisibility(View.VISIBLE);
                ball1.startAnimation(anim);
                ball1.setBackgroundResource(getRandomBack());
                balls[0]++;
                current_ball = ball1;
                break;
            case 1:
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.falling_up_motion_sector_2);
                ball2.setVisibility(View.VISIBLE);
                ball2.startAnimation(anim);
                balls[1]++;
                ball2.setBackgroundResource(getRandomBack());
                current_ball = ball2;
                break;
            case 2:
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.falling_up_motion_sector_3);
                ball3.setVisibility(View.VISIBLE);
                ball3.startAnimation(anim);
                balls[2]++;
                ball3.setBackgroundResource(getRandomBack());
                current_ball = ball3;
                break;
            case 3:
                anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.falling_up_motion_sector_4);
                ball4.setVisibility(View.VISIBLE);
                ball4.startAnimation(anim);
                balls[3]++;
                ball4.setBackgroundResource(getRandomBack());
                current_ball = ball4;
                break;
        }
        anim.setAnimationListener(animationListener);
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

}