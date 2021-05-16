package com.numbers.quiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "quiz_db.db";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"QUIZ\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"date\"\tTEXT NOT NULL,\n" +
                "\t\"def_time\"\tINTEGER,\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");");
        db.execSQL("CREATE TABLE \"QUESTION\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"quiz_id\"\tINTEGER NOT NULL,\n" +
                "\t\"q_type\"\tINTEGER NOT NULL,\n" +
                "\t\"question\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"id\"),\n" +
                "\tFOREIGN KEY(\"quiz_id\") REFERENCES \"QUIZ\"(\"id\")\n" +
                ");");
        db.execSQL("CREATE TABLE \"ANSWERS\" (\n" +
                "\t\"id\"\tINTEGER,\n" +
                "\t\"q_id\"\tINTEGER NOT NULL,\n" +
                "\t\"answer\"\tTEXT,\n" +
                "\tFOREIGN KEY(\"q_id\") REFERENCES \"QUESTION\"(\"id\"),\n" +
                "\tPRIMARY KEY(\"id\")\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
