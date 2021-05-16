package com.numbers.quiz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.numbers.quiz.helper.OnStartDragListener;

import java.util.ArrayList;

public class CreatedQuizzesActivity extends AppCompatActivity {
    private DBHelper dbHelper = new DBHelper(this);
    private int screen_width;
    private int screen_height;
    private int[] balls = new int[4];
    private ImageView ball1;
    private ImageView ball2;
    private ImageView ball3;
    private ImageView ball4;
    private Animation anim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_list_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RecyclerView rv = findViewById(R.id.quiz_container);
        rv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rv.setAdapter(new QuizAdapter(ShowAll(), this));
        rv.setZ(1.00f);
        ((SimpleItemAnimator)rv.getItemAnimator()).setSupportsChangeAnimations(false);
        rv.setHasFixedSize(true);
        getDimensions();

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

    private ArrayList<Integer> ShowAll(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                Info.QUIZ_ID,
                Info.QUIZ_DATE,
                Info.QUIZ_DEFAULT_TIME
        };

        String selection = Info.QUIZ_ID + " > 0";

        Cursor cursor = db.query(
                "QUIZ",   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        ArrayList<Integer> temp = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            temp.add(id);
        }
        cursor.close();
        return temp;
    }

    private void StartAnimation(){
        int next = (int) (Math.random()*4);
        while (!checkFairness(next)){
            next = (int) (Math.random()*4);
        }
        switch (next){
            case 0:
                anim = AnimationUtils.loadAnimation(CreatedQuizzesActivity.this, R.anim.falling_up_motion_sector_1);
                ball1.setVisibility(View.VISIBLE);
                ball1.startAnimation(anim);
                ball1.setBackgroundResource(getRandomBack());
                balls[0]++;
                current_ball = ball1;
                break;
            case 1:
                anim = AnimationUtils.loadAnimation(CreatedQuizzesActivity.this, R.anim.falling_up_motion_sector_2);
                ball2.setVisibility(View.VISIBLE);
                ball2.startAnimation(anim);
                balls[1]++;
                ball2.setBackgroundResource(getRandomBack());
                current_ball = ball2;
                break;
            case 2:
                anim = AnimationUtils.loadAnimation(CreatedQuizzesActivity.this, R.anim.falling_up_motion_sector_3);
                ball3.setVisibility(View.VISIBLE);
                ball3.startAnimation(anim);
                balls[2]++;
                ball3.setBackgroundResource(getRandomBack());
                current_ball = ball3;
                break;
            case 3:
                anim = AnimationUtils.loadAnimation(CreatedQuizzesActivity.this, R.anim.falling_up_motion_sector_4);
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

    private void getDimensions(){
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screen_height = metrics.heightPixels;
        screen_width = metrics.widthPixels;
    }

}
