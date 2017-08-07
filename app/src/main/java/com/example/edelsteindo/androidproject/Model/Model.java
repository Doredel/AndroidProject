package com.example.edelsteindo.androidproject.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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

        this.modelFirebase.addPost(post);
        PostSql.addPost(modelSql.getWritableDatabase(),post);
        //this.modelMem.addPost(post);
    }

    public void removePost(String id) {
        PostSql.removePost(modelSql.getWritableDatabase(),id);
        //this.modelMem.removePost(id);
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
        //return PostSql.getAllPosts(modelSql.getReadableDatabase());
        modelFirebase.getAllPostsAndObserve(new FirebaseModel.GetAllPostsAndObserveCallback() {
            @Override
            public void onComplete(List<Post> list) {
                callback.onComplete(list);
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
                    listener.onSuccess(bitmap);
                }else {
                    modelFirebase.getImage(url, new GetImageListener() {
                        @Override
                        public void onSuccess(Bitmap image) {
                            String fileName = URLUtil.guessFileName(url, null, null);
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
