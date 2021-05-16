package com.numbers.quiz.helper;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int from, int to);
    void onItemDismiss(int position);
}
