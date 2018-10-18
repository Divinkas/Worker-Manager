package com.example.divin.workmanager.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.divin.workmanager.Data.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SaveImageToFileWorker extends Worker {

    private static final String TAG = SaveImageToFileWorker.class.getSimpleName();

    private static final String TITLE = "Blurred Image";
    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault());

    public SaveImageToFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Context applicationContext = getApplicationContext();

        ContentResolver resolver = applicationContext.getContentResolver();
        try {
            String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);

            Bitmap bitmap = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri)));
            String imageUrl = MediaStore.Images.Media.insertImage(
                    resolver, bitmap, TITLE, DATE_FORMATTER.format(new Date()));
            if (TextUtils.isEmpty(imageUrl)) {
                return Worker.Result.FAILURE;
            }

            assert resourceUri != null;
            setOutputData(new Data.Builder().putString(
                    Constants.KEY_IMAGE_URI, resourceUri).build());

            return Worker.Result.SUCCESS;
        } catch (Exception exception) {
            return Worker.Result.FAILURE;
        }
    }
}
