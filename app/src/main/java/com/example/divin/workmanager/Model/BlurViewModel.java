package com.example.divin.workmanager.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

import static com.example.divin.workmanager.Data.Constants.IMAGE_MANIPULATION_WORK_NAME;
import static com.example.divin.workmanager.Data.Constants.KEY_IMAGE_URI;
import static com.example.divin.workmanager.Data.Constants.TAG_OUTPUT;


public class BlurViewModel extends ViewModel{

    private Uri mImageUri;
    private WorkManager workManager;

    private LiveData<List<WorkStatus>> mSavedWorkStatus;

    public BlurViewModel() {
        workManager = WorkManager.getInstance();
        mSavedWorkStatus = workManager.getStatusesByTagLiveData(TAG_OUTPUT);
    }

    public void applyBlur(int blurLevel) {
        WorkContinuation continuation = workManager
                .beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(CleanupWorker.class));

        OneTimeWorkRequest blurRequest = new OneTimeWorkRequest.Builder(BlurWorker.class)
                .setInputData(createInputDataForUri())
                .build();
        OneTimeWorkRequest save = new OneTimeWorkRequest.Builder(SaveImageToFileWorker.class)
                .addTag(TAG_OUTPUT)
                .build();

        continuation = continuation.then(blurRequest).then(save);
        continuation.enqueue();
    }

    private Data createInputDataForUri() {
        Data.Builder builder = new Data.Builder();
        if (mImageUri != null) {
            builder.putString(KEY_IMAGE_URI, mImageUri.toString());
        }
        return builder.build();
    }

    private Uri uriOrNull(String uriString) {
        if (!TextUtils.isEmpty(uriString)) {
            return Uri.parse(uriString);
        }
        return null;
    }

    public void setImageUri(String uri) {
        mImageUri = uriOrNull(uri);
    }

    public LiveData<List<WorkStatus>> getOutputStatus() { return mSavedWorkStatus; }
    public Uri getImageUri() {
        return mImageUri;
    }

}
