package com.numbers.quiz;

public class Question {
    int type;
    String question;
    String[] answers;
    public Question(int type, String question, String[] answers){
        this.type = type;
        this.question = question;
        this.answers = answers;
    }
}
