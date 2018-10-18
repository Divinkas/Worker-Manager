package com.example.divin.workmanager.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.divin.workmanager.Data.Constants;
import com.example.divin.workmanager.Workers.WorkerUtils;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BlurWorker extends Worker {

    public BlurWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        String resourceUri = getInputData().getString(Constants.KEY_IMAGE_URI);

        try {
            if (TextUtils.isEmpty(resourceUri)) {
                throw new IllegalArgumentException("Invalid input uri");
            }

            ContentResolver resolver = applicationContext.getContentResolver();
            Bitmap picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)));
            Uri outputUri = WorkerUtils.writeBitmapToFile(applicationContext, picture);
            WorkerUtils.makeStatusNotification("Output is " + outputUri.toString(), applicationContext);

            setOutputData(new Data.Builder().putString(
                    Constants.KEY_IMAGE_URI, outputUri.toString()).build());

            return Worker.Result.SUCCESS;
        } catch (Throwable throwable) {
            Log.e("exception throwable", "Error applying blur", throwable);
            return Worker.Result.FAILURE;
        }

    }
}
