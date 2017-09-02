package com.example.edelsteindo.androidproject.Model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import com.example.edelsteindo.androidproject.MyApplication;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Dor-New on 15/07/2017.
 */

public class Model {
    public final static Model instace = new Model();

    private MemModel modelMem;
    private SqlModel modelSql;
    private FirebaseModel modelFirebase;

    public Model() {
        modelMem = new MemModel();
        modelSql = new SqlModel(MyApplication.getMyContext());
        modelFirebase = new FirebaseModel();
    }

    public void addPost(Post post) {
        this.modelFirebase.updatePost(post);
        PostSql.addPost(modelSql.getWritableDatabase(),post);
    }

    public void removePost(Post post) {
        this.modelFirebase.removePost(post);
        PostSql.removePost(modelSql.getWritableDatabase(),post.getId());
    }

    public void updatePost(Post post) {
        this.modelFirebase.updatePost(post);
        PostSql.editPost(modelSql.getWritableDatabase(),post);
    }

    public interface GetPostCallback{
        void onComplete(Post post);
        void onCancel();
    }
    public void getPost(String stId, final GetPostCallback callback) {
        //return PostSql.getPost(modelSql.getReadableDatabase(),stId);
        modelFirebase.getPost(stId, new FirebaseModel.GetPostCallback() {
            @Override
            public void onComplete(Post post) {

                callback.onComplete(post);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }


    public interface GetAllPostsAndObserveCallback {
        void onComplete(List<Post> list);
        void onCancel();
    }
    public void getAllPostsAndObserve(final GetAllPostsAndObserveCallback callback){
        //1. get local lastUpdateTade
        SharedPreferences pref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        final float lastUpdateDate = pref.getFloat("PostsLastUpdateDate",0);
        Log.d("TAG","lastUpdateDate: " + lastUpdateDate);

        modelFirebase.getAllPostsAndObserve(lastUpdateDate, new FirebaseModel.GetAllPostsAndObserveCallback() {
            @Override
            public void onComplete(List<Post> list)
            {
                float newLastUpdateDate = lastUpdateDate;
                Log.d("TAG", "FB detch:" + list.size());
                for (Post post: list) {

                    if(post.isActive()) {
                        //3. update the local db
                        boolean res = PostSql.editPost(modelSql.getWritableDatabase(), post);
                        if (!res) {
                            PostSql.addPost(modelSql.getWritableDatabase(), post);
                        }

                    }
                    else{
                        PostSql.removePost(modelSql.getWritableDatabase(),post.getId());
                    }

                    //4. update the lastUpdateTade
                    if (newLastUpdateDate < (float) post.getLastUpdateDate()) {
                        newLastUpdateDate = (float) post.getLastUpdateDate();
                    }

                }
                SharedPreferences.Editor prefEd = MyApplication.getMyContext().getSharedPreferences("TAG",
                        Context.MODE_PRIVATE).edit();
                prefEd.putFloat("PostsLastUpdateDate", newLastUpdateDate);
                prefEd.commit();
                Log.d("TAG","PostsLastUpdateDate: " + newLastUpdateDate);


                //5. read from local db
                List<Post> data = PostSql.getAllPosts(modelSql.getReadableDatabase());

                //6. return list of post
                callback.onComplete(data);
            }

            @Override
            public void onCancel() {
                callback.onCancel();
            }
        });

    }


    //---------------------------------------------------------------------------------------

    public interface SaveImageListener {
        void complete(String url);

        void fail();
    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                String fileName = URLUtil.guessFileName(url, null, null);
                saveImageToFile(imageBmp, fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });

    }

    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }
    public void getImage(final String url, final GetImageListener listener) {
        //check if image exsist localy
        final String fileName = URLUtil.guessFileName(url, null, null);
        FileModel.loadImageFromFileAsynch(fileName, new FileModel.LoadImageFromFileAsynch() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (bitmap != null){
                    //Log.d("TAG","getImage from local success " + fileName);
                    listener.onSuccess(bitmap);
                }else {
                    modelFirebase.getImage(url, new GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
                            //Log.d("TAG","getImage from FB success " + fileName);
                            saveImageToFile(image,fileName);
                            listener.onSuccess(image);
                        }

                        @Override
                        public void onFail() {
                            listener.onFail();
                        }
                    });

                }
            }
        });

    }

    ////// SAVE FILES TO LOCAL STORAGE ///////

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir, imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            addPicureToGallery(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Bitmap loadImageFromFile(String imageFileName) {
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir, imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }  catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private void addPicureToGallery(File imageFile) {
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyApplication.getMyContext().sendBroadcast(mediaScanIntent);
    }
}
