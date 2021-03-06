package com.example.edelsteindo.androidproject.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


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
    private static final String POST_DATE = "date";
    private static final String POST_LAST_UPDATE = "lastUpdateDate";


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
            int dateIndex = cursor.getColumnIndex(POST_DATE);
            int lastUpdateDateIndex  = cursor.getColumnIndex(POST_LAST_UPDATE);


            do {

                Post pst = new Post();

                pst.setId(cursor.getString(idIndex));
                pst.setUser(cursor.getString(userIndex));
                pst.setActive((cursor.getInt(activeIndex) == 1));
                pst.setDescription(cursor.getString(descriptionIndex));
                pst.setNumOfLikes(cursor.getInt(likesIndex));
                pst.setPostPicUrl(cursor.getString(picUrlIndex));
                pst.setLastUpdateDate(cursor.getDouble(lastUpdateDateIndex));
                pst.setLikedUsers(UserPostSql.getAllUsers(db,pst.getId()));
                pst.setTimeMs(cursor.getLong(dateIndex));

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
        values.put(POST_LAST_UPDATE,pst.getLastUpdateDate());
        UserPostSql.updateAllUsers(db,pst);
        values.put(POST_DATE,pst.getTimeMs());

        long res = db.insert(POST_TABLE, null, values);

        return (res != -1);
    }

    static void removePost(SQLiteDatabase db, String id) {
        UserPostSql.removePost(db,id);
        db.execSQL("delete from "+POST_TABLE+" where "+POST_ID+"="+"'"+id+"';");
    }

    static Post getPost(SQLiteDatabase db, String pstId) {
        String[] col = {POST_ID,POST_USER,POST_ACTIVE,POST_LIKES,POST_DESCRIPTION,POST_PICURL,POST_LAST_UPDATE,POST_DATE};

        Cursor cursor = db.query(POST_TABLE , col, POST_ID+"=?", new String[] {pstId}, null, null, null);

        Post pst = null;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(POST_ID);
            int userIndex = cursor.getColumnIndex(POST_USER);
            int descriptionIndex = cursor.getColumnIndex(POST_DESCRIPTION);
            int likesIndex = cursor.getColumnIndex(POST_LIKES);
            int activeIndex = cursor.getColumnIndex(POST_ACTIVE);
            int picUrlIndex = cursor.getColumnIndex(POST_PICURL);
            int lastUpdateDateIndex  = cursor.getColumnIndex(POST_LAST_UPDATE);
            int dateIndex = cursor.getColumnIndex(POST_DATE);


            pst = new Post();
            pst.setId(cursor.getString(idIndex));
            pst.setUser(cursor.getString(userIndex));
            pst.setActive((cursor.getInt(activeIndex) == 1));
            pst.setDescription(cursor.getString(descriptionIndex));
            pst.setNumOfLikes(cursor.getInt(likesIndex));
            pst.setPostPicUrl(cursor.getString(picUrlIndex));
            pst.setLastUpdateDate(cursor.getDouble(lastUpdateDateIndex));
            pst.setLikedUsers(UserPostSql.getAllUsers(db,pst.getId()));
            pst.setTimeMs(cursor.getLong(dateIndex));
        }
        return pst;
    }

    static boolean editPost(SQLiteDatabase db, Post pst) {
        ContentValues values = new ContentValues();

        values.put(POST_ID, pst.getId());
        values.put(POST_USER, pst.getUser());
        values.put(POST_ACTIVE, pst.isActive());
        values.put(POST_DESCRIPTION, pst.getDescription());
        values.put(POST_LIKES,pst.getNumOfLikes());
        values.put(POST_PICURL,pst.getPostPicUrl());
        values.put(POST_LAST_UPDATE,pst.getLastUpdateDate());
        UserPostSql.updateAllUsers(db,pst);
        values.put(POST_DATE,pst.getTimeMs());

        long res = db.update(POST_TABLE,values,POST_ID+"="+pst.getId(), null);
        return (res != 0);
    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + POST_TABLE +
                " (" +
                POST_ID + " TEXT PRIMARY KEY, " +
                POST_ACTIVE + " BOOLEAN, " +
                POST_LIKES + " NUMBER, "+
                POST_DESCRIPTION + " TEXT, " +
                POST_USER + " TEXT, " +
                POST_PICURL  + " TEXT, " +
                POST_DATE + " INTEGER ,"+
                POST_LAST_UPDATE  + " NUMBER " +
                ");");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE + ";");
        onCreate(db);
    }


}

