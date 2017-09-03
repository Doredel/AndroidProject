package com.example.edelsteindo.androidproject.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dor-New on 15/07/2017.
 */

public class SqlModel extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    SqlModel(Context context) {
        super(context, "database.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PostSql.onCreate(db);
        UserPostSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PostSql.onUpgrade(db, oldVersion, newVersion);
        UserPostSql.onUpgrade(db,oldVersion,newVersion);
    }

}