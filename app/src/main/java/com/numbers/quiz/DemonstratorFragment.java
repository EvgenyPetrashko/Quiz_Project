package com.numbers.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DemonstratorFragment extends Fragment {
    private View demonstrator;
    private Question question;

    public DemonstratorFragment(Question question){
        this.question = question;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        demonstrator = inflater.inflate(R.layout.quiz_demonstrator_fragment, container, false);
        System.out.println("Before: " + demonstrator.findViewById(R.id.time_title).getZ());
        demonstrator.findViewById(R.id.time_title).setZ(2);
        System.out.println("Before: " + demonstrator.findViewById(R.id.time_title).getZ());
        demonstrator.findViewById(R.id.card_question_title).setZ(2);
        init();
        return demonstrator;
    }

    private void init(){
        TextView title = demonstrator.findViewById(R.id.question_title);
        title.setText(question.question);

        switch (question.answers.length){
            case 0:
                demonstrator.findViewById(R.id.variant1).setVisibility(View.GONE);
                demonstrator.findViewById(R.id.variant2).setVisibility(View.GONE);
                demonstrator.findViewById(R.id.variant3).setVisibility(View.GONE);
                demonstrator.findViewById(R.id.variant4).setVisibility(View.GONE);
                break;
            case 2:
                demonstrator.findViewById(R.id.variant3).setVisibility(View.GONE);
                demonstrator.findViewById(R.id.variant4).setVisibility(View.GONE);
                ((TextView)demonstrator.findViewById(R.id.answer_text_1)).setText(question.answers[0]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_2)).setText(question.answers[1]);
                break;
            case 3:
                demonstrator.findViewById(R.id.variant4).setVisibility(View.GONE);
                ((TextView)demonstrator.findViewById(R.id.answer_text_1)).setText(question.answers[0]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_2)).setText(question.answers[1]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_3)).setText(question.answers[2]);
                break;
            case 4:
                ((TextView)demonstrator.findViewById(R.id.answer_text_1)).setText(question.answers[0]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_2)).setText(question.answers[1]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_3)).setText(question.answers[2]);
                ((TextView)demonstrator.findViewById(R.id.answer_text_4)).setText(question.answers[3]);
                break;
        }
    }
}
