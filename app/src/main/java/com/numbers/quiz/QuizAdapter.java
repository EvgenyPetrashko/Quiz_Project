package com.numbers.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.numbers.quiz.helper.OnStartDragListener;

import java.util.ArrayList;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizHolder> {
    private ArrayList<Integer> arrayList;
    private Context context;
    //private final OnStartDragListener dragListener;
    public QuizAdapter(ArrayList<Integer> arrayList/*, OnStartDragListener inp*/, Context context){
        this.arrayList = arrayList;
        this.context = context;
        /*this.dragListener = inp;*/
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuizHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final QuizHolder holder, final int position) {
        holder.title.setText(Integer.toString(arrayList.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuizDemonstratorActivity.class);
                intent.putExtra("quiz_id", arrayList.get(position));
                context.startActivity(intent);
            }
        });
        holder.buttons.setVisibility(View.GONE);
        holder.expand_collapse.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if (holder.hidden){
                    holder.buttons.setVisibility(View.VISIBLE);
                    holder.expand_collapse.setImageDrawable(context.getDrawable(R.drawable.arrow_up));
                }else{
                    holder.buttons.setVisibility(View.GONE);
                    holder.expand_collapse.setImageDrawable(context.getDrawable(R.drawable.arrow_down));
                }
                holder.hidden = !holder.hidden;
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class QuizHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageButton expand_collapse;
        RelativeLayout buttons;
        Button share;
        Button delete;
        boolean hidden = true;
        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.quiz_title);
            expand_collapse = itemView.findViewById(R.id.expand_collapse_button);
            buttons = itemView.findViewById(R.id.rl2);
            share = itemView.findViewById(R.id.quiz_share_button);
            delete = itemView.findViewById(R.id.quiz_delete_button);
        }
    }
}
