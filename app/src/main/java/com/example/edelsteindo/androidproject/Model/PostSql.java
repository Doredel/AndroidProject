package com.example.edelsteindo.androidproject.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dor-New on 16/07/2017.
 */


public class PostSql {
    private static final String POST_TABLE = "posts";

    private static final String POST_ID = "id";
    private static final String POST_ACTIVE = "active";
    private static final String POST_DESCRIPTION = "description";
    private static final String POST_LIKES = "likes";
    private static final String POST_USER = "user";
    private static final String POST_PICURL = "pic_url";


    static List<Post> getAllPosts(SQLiteDatabase db) {
        Cursor cursor = db.query(POST_TABLE, null, null, null, null, null, null);

        List<Post> list = new LinkedList<Post>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_ID);
            int userIndex = cursor.getColumnIndex(POST_USER);
            int descriptionIndex = cursor.getColumnIndex(POST_DESCRIPTION);
            int likesIndex = cursor.getColumnIndex(POST_LIKES);
            int activeIndex = cursor.getColumnIndex(POST_ACTIVE);
            int picUrlIndex = cursor.getColumnIndex(POST_PICURL);

            do {

                Post pst = new Post();
                pst.setId(cursor.getString(idIndex));
                pst.setUser(cursor.getString(userIndex));
                pst.setActive((cursor.getInt(activeIndex) == 1));
                pst.setDescription(cursor.getString(descriptionIndex));
                pst.setNumOfLikes(cursor.getInt(likesIndex));
                pst.setPostPicUrl(cursor.getString(picUrlIndex));

                list.add(pst);

            } while (cursor.moveToNext());
        }

        return list;
    }

    static boolean addPost(SQLiteDatabase db, Post pst) {
        ContentValues values = new ContentValues();

        values.put(POST_ID, pst.getId());
        values.put(POST_USER, pst.getUser());
        values.put(POST_ACTIVE, pst.isActive());
        values.put(POST_DESCRIPTION, pst.getDescription());
        values.put(POST_LIKES,pst.getNumOfLikes());
        values.put(POST_PICURL,pst.getPostPicUrl());

        long res = db.insert(POST_TABLE, null, values);

        return (res != -1);
    }

    static void removePost(SQLiteDatabase db, String id) {
        db.execSQL("delete from "+POST_TABLE+" where "+id+"="+"'"+id+"'");
    }

    static Post getPost(SQLiteDatabase db, String pstId) {
        String[] col = {POST_ID,POST_USER,POST_ACTIVE,POST_LIKES,POST_DESCRIPTION,POST_PICURL};

        Cursor cursor = db.query(POST_TABLE , col, POST_ID+"=?", new String[] {pstId}, null, null, null);

        Post pst = null;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_ID);
            int userIndex = cursor.getColumnIndex(POST_USER);
            int descriptionIndex = cursor.getColumnIndex(POST_DESCRIPTION);
            int likesIndex = cursor.getColumnIndex(POST_LIKES);
            int activeIndex = cursor.getColumnIndex(POST_ACTIVE);
            int picUrlIndex = cursor.getColumnIndex(POST_PICURL);



            pst = new Post();
            pst.setId(cursor.getString(idIndex));
            pst.setUser(cursor.getString(userIndex));
            pst.setActive((cursor.getInt(activeIndex) == 1));
            pst.setDescription(cursor.getString(descriptionIndex));
            pst.setNumOfLikes(cursor.getInt(likesIndex));
            pst.setPostPicUrl(cursor.getString(picUrlIndex));


        }
        return pst;
    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + POST_TABLE +
                " (" +
                POST_ID + " TEXT PRIMARY KEY, " +
                POST_ACTIVE + " BOOLEAN, " +
                POST_LIKES + " NUMBER, "+
                POST_DESCRIPTION + " TEXT, " +
                POST_USER + " TEXT, " +
                POST_PICURL  + " TEXT " +
                ");");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE + ";");
        onCreate(db);
    }

    private static String calendarToString(Calendar cal){
        return cal.get(cal.YEAR)+"-"+cal.get(cal.MONTH)+"-"+cal.get(cal.DAY_OF_MONTH)+"-"+cal.get(cal.HOUR_OF_DAY)+"-"+cal.get(cal.MINUTE)+"-"+cal.get(cal.SECOND);
    }

    private static Calendar stringToCalendar(String str){
        String[] fields = str.split("-");
        return new GregorianCalendar(Integer.parseInt(fields[0]),Integer.parseInt(fields[1]),Integer.parseInt(fields[2]),Integer.parseInt(fields[3]),Integer.parseInt(fields[4]),Integer.parseInt(fields[5]));
    }
}

