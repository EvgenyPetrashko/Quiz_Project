package com.numbers.quiz;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConstructorFragment extends Fragment {
    private View constructor;

    private TextView time_title;
    private int timer;
    private int type;
    private int length;
    private boolean is_saved;
    private String question;
    private String[] answers;
    OnSaveQuizClickListener save_quiz_interface;
    public ConstructorFragment(int timer, int type, int length, ConstructorActivity activity){
        this.timer = timer;
        this.type = type;
        this.length = length;
        is_saved = false;
        save_quiz_interface = activity;
    }

    public ConstructorFragment(int timer, int type, String question, String[] answers, ConstructorActivity activity){
        this.timer = timer;
        this.type = type;
        this.is_saved = true;
        this.question = question;
        this.answers = answers;
        this.length = answers.length;
        save_quiz_interface = activity;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        constructor = inflater.inflate(R.layout.quiz_constructor_fragment, container, false);
        init();
        return constructor;
    }

    private void init(){
        time_title = constructor.findViewById(R.id.time_title);
        String time_string = "";
        switch (timer){
            case 0:
                time_string = "00:15";
                break;
            case 1:
                time_string = "00:30";
                break;
            case 2:
                time_string = "00:45";
                break;
            case 3:
                time_string = "00:59";
                break;
        }
        if (time_string.equals("")) time_title.setVisibility(View.INVISIBLE);
        else time_title.setText(time_string);

        switch (length){
            case 0:
                constructor.findViewById(R.id.variant1).setVisibility(View.GONE);
                constructor.findViewById(R.id.variant2).setVisibility(View.GONE);
                constructor.findViewById(R.id.variant3).setVisibility(View.GONE);
                constructor.findViewById(R.id.variant4).setVisibility(View.GONE);
                break;
            case 2:
                constructor.findViewById(R.id.variant3).setVisibility(View.GONE);
                constructor.findViewById(R.id.variant4).setVisibility(View.GONE);
                break;
            case 3:
                constructor.findViewById(R.id.variant4).setVisibility(View.GONE);
                break;
        }

        if (is_saved){
            Button save_quiz = constructor.findViewById(R.id.save_quiz_button);
            save_quiz.setVisibility(View.VISIBLE);
            save_quiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Should Work", Toast.LENGTH_SHORT).show();
                    save_quiz_interface.onSaveQuizClick(v);
                }
            });
            EditText qTitle = constructor.findViewById(R.id.question_title);
            qTitle.setText(question);
            qTitle.setEnabled(false);

            EditText ed1 = constructor.findViewById(R.id.answer_text_1);
            EditText ed2 = constructor.findViewById(R.id.answer_text_2);
            EditText ed3 = constructor.findViewById(R.id.answer_text_3);
            EditText ed4 = constructor.findViewById(R.id.answer_text_4);
            switch (answers.length){
                case 2:
                    ed1.setText(answers[0]);
                    ed1.setEnabled(false);
                    ed2.setText(answers[1]);
                    ed2.setEnabled(false);
                    break;
                case 3:
                    ed1.setText(answers[0]);
                    ed1.setEnabled(false);
                    ed2.setText(answers[1]);
                    ed2.setEnabled(false);
                    ed3.setText(answers[2]);
                    ed3.setEnabled(false);
                    break;
                case 4:
                    ed1.setText(answers[0]);
                    ed1.setEnabled(false);
                    ed2.setText(answers[1]);
                    ed2.setEnabled(false);
                    ed3.setText(answers[2]);
                    ed3.setEnabled(false);
                    ed4.setText(answers[3]);
                    ed4.setEnabled(false);
                    break;
            }

        }else{
            constructor.findViewById(R.id.save_quiz_button).setVisibility(View.INVISIBLE);
        }
    }
}
