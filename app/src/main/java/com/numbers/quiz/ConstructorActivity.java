package com.numbers.quiz;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class ConstructorActivity extends AppCompatActivity implements OnSaveQuizClickListener {
    ArrayList<Question> question_array = new ArrayList<>();
    int default_timer;
    int pointer = -1;
    Button next;
    Button prev;
    ImageButton save;
    ImageButton delete;
    DBHelper dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constructor_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent input = getIntent();
        default_timer = input.getIntExtra("timer", 4);
        init();
        addQuestion();
        dbHelper = new DBHelper(this);
    }

    private void init(){
        next = findViewById(R.id.next_question_const);
        prev = findViewById(R.id.prev_question_const);
        delete = findViewById(R.id.delete_question);
        save = findViewById(R.id.save_question);

        prev.setVisibility(View.GONE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save.getVisibility() != View.VISIBLE){
                    if (pointer >= question_array.size()-1){
                        addQuestion();
                    }else{
                        pointer++;
                        Question q = question_array.get(pointer);
                        prev.setVisibility(View.VISIBLE);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConstructorFragment(default_timer, q.type, q.question, q.answers, ConstructorActivity.this)).commit();
                    }
                }else Toast.makeText(ConstructorActivity.this, "Please save the question", Toast.LENGTH_SHORT).show();

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save.getVisibility() != View.VISIBLE){
                    pointer--;
                    if (pointer==0) prev.setVisibility(View.GONE);
                    Question q = question_array.get(pointer);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConstructorFragment(default_timer, q.type, q.question, q.answers, ConstructorActivity.this)).commit();
                }else Toast.makeText(ConstructorActivity.this, "Please save the question", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ConstructorActivity.this, R.style.MyDialog).setTitle("Do you really want to delete this question?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (question_array.size() == 1){
                                    Intent intent = new Intent(ConstructorActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    Question q;
                                    if (pointer == question_array.size()-1){
                                        q = question_array.get(pointer-1);
                                        question_array.remove(pointer);
                                        pointer--;
                                    }else {
                                        q = question_array.get(pointer + 1);
                                        question_array.remove(pointer);
                                    }
                                    if (pointer == 0) prev.setVisibility(View.GONE);
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConstructorFragment(default_timer, q.type, q.question, q.answers, ConstructorActivity.this)).commit();
                                    save.setVisibility(View.INVISIBLE);
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                }).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ConstructorActivity.this, R.style.MyDialog).setTitle("Do you want to save this question?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Question current_question = question_array.get(pointer);
                                current_question.question = ((EditText)findViewById(R.id.question_title)).getText().toString();
                                EditText ed1 = findViewById(R.id.answer_text_1);
                                EditText ed2 = findViewById(R.id.answer_text_2);
                                EditText ed3 = findViewById(R.id.answer_text_3);
                                EditText ed4 = findViewById(R.id.answer_text_4);
                                ((EditText)findViewById(R.id.question_title)).setEnabled(false);
                                switch (current_question.answers.length){
                                    case 2:
                                        current_question.answers = new String[]{ed1.getText().toString(), ed2.getText().toString()};
                                        ((EditText)findViewById(R.id.answer_text_1)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_2)).setEnabled(false);
                                        break;
                                    case 3:
                                        current_question.answers = new String[]{ed1.getText().toString(), ed2.getText().toString(), ed3.getText().toString()};
                                        ((EditText)findViewById(R.id.answer_text_1)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_2)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_3)).setEnabled(false);
                                        break;
                                    case 4:
                                        current_question.answers = new String[]{ed1.getText().toString(), ed2.getText().toString(), ed3.getText().toString(), ed4.getText().toString()};
                                        ((EditText)findViewById(R.id.answer_text_1)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_2)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_3)).setEnabled(false);
                                        ((EditText)findViewById(R.id.answer_text_4)).setEnabled(false);
                                        break;
                                }
                                question_array.set(pointer, current_question);
                                save.setVisibility(View.INVISIBLE);
                                Toast.makeText(ConstructorActivity.this, "Question was saved", Toast.LENGTH_SHORT).show();
                                Button quiz_save = findViewById(R.id.save_quiz_button);
                                quiz_save.setVisibility(View.VISIBLE);
                                quiz_save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        onSaveQuizClick(v);
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    private void addQuestion(){
        new AlertDialog.Builder(ConstructorActivity.this, R.style.MyDialog).setTitle("Choose the type of the question")
                .setSingleChoiceItems(new String[]{"Write by yourself", "Single-Choice", "Multiple-Choice"}, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        if (which != 0){
                            new AlertDialog.Builder(ConstructorActivity.this, R.style.MyDialog).setTitle("How much variants?")
                                    .setSingleChoiceItems(new String[]{"2", "3", "4"}, 2, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog2, int which2) {
                                            pointer++;
                                            String[] temp_array = new String[which2+2];
                                            question_array.add(new Question(which, "", temp_array));
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConstructorFragment(default_timer, which, which2+2, ConstructorActivity.this)).commit();
                                            dialog.dismiss();
                                            dialog2.dismiss();
                                            if (pointer != 0) prev.setVisibility(View.VISIBLE);
                                            save.setVisibility(View.VISIBLE);
                                        }
                                    }).show();
                        }else{
                            pointer++;
                            question_array.add(new Question(which, "", new String[]{}));
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConstructorFragment(default_timer, which, 0, ConstructorActivity.this)).commit();
                            dialog.dismiss();
                            if (pointer != 0) prev.setVisibility(View.VISIBLE);
                            save.setVisibility(View.VISIBLE);
                        }
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                if (question_array.size() == 0){
                    Intent intent = new Intent(ConstructorActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }).show();
    }

    @Override
    public void onSaveQuizClick(View v) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        String hours;
        String minutes;
        if (calendar.get(Calendar.MINUTE) < 10){
            minutes = "0" + calendar.get(Calendar.MINUTE);
        }else{
            minutes = Integer.toString(calendar.get(Calendar.MINUTE));
        }
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10){
            hours = "0" + calendar.get(Calendar.HOUR_OF_DAY);
        }else{
            hours = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        }

        ContentValues value1 = new ContentValues();
        value1.put(Info.QUIZ_DATE, hours + "." + minutes);
        value1.put(Info.QUIZ_DEFAULT_TIME, default_timer);
        long quiz_id = db.insert("QUIZ", null, value1);

        for (int i = 0; i < question_array.size(); i++) {
            ContentValues value2 = new ContentValues();
            Question temp = question_array.get(i);
            value2.put(Info.QUESTION_QUIZ_ID, quiz_id);
            value2.put(Info.QUESTION_TYPE, temp.type);
            value2.put(Info.QUESTION_QUESTION, temp.question);
            long question_id = db.insert("QUESTION", null, value2);
            for (int j = 0; j < temp.answers.length ; j++) {
                ContentValues value3 = new ContentValues();
                value3.put(Info.ANSWER_QUESTION_ID, question_id);
                value3.put(Info.ANSWER_ANSWER, temp.answers[j]);
                db.insert("ANSWERS", null, value3);
            }

        }
        Toast.makeText(ConstructorActivity.this, "Quiz was saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ConstructorActivity.this, MainActivity.class);
        startActivity(intent);
    }



}
