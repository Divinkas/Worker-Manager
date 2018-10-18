package com.example.divin.workmanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.divin.workmanager.Data.Constants;
import com.example.divin.workmanager.Model.BlurViewModel;

import androidx.work.WorkStatus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BlurActivity extends AppCompatActivity {


    private BlurViewModel mViewModel;

    @BindView(R.id.image_view)
    public ImageView mImageView;

    @BindView(R.id.progress_bar)
    public ProgressBar mProgressBar;

    @BindView(R.id.go_button)
    public Button mGoButton;

    @BindView(R.id.see_file_button)
    public Button mOutputButton;

    @BindView(R.id.radio_blur_group)
    public RadioGroup radioGroup;

    @BindView(R.id.cancel_button)
    public Button mCancelButton;

    @OnClick(R.id.go_button)
    public void apply(){
        mViewModel.applyBlur(getBlurLevel());
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        ButterKnife.bind(this);

        mViewModel = ViewModelProviders.of(this).get(BlurViewModel.class);

        Intent intent = getIntent();
        String imageUriExtra = intent.getStringExtra(Constants.KEY_IMAGE_URI);
        mViewModel.setImageUri(imageUriExtra);
        if (mViewModel.getImageUri() != null) {
            Glide.with(this).load(mViewModel.getImageUri()).into(mImageView);
        }

        mViewModel.getOutputStatus().observe(this, listOfWorkStatuses -> {
            if (listOfWorkStatuses == null || listOfWorkStatuses.isEmpty()) { return; }

            WorkStatus workStatus = listOfWorkStatuses.get(0);

            if (!workStatus.getState().isFinished()) { showWorkInProgress(); }
            else { showWorkFinished(); }
        });

    }


    private void showWorkInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mCancelButton.setVisibility(View.VISIBLE);
        mGoButton.setVisibility(View.GONE);
        mOutputButton.setVisibility(View.GONE);
    }


    private void showWorkFinished() {
        mProgressBar.setVisibility(View.GONE);
        mCancelButton.setVisibility(View.GONE);
        mGoButton.setVisibility(View.VISIBLE);
    }

    private int getBlurLevel() {
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_blur_lv_1:
                return 1;
            case R.id.radio_blur_lv_2:
                return 2;
            case R.id.radio_blur_lv_3:
                return 3;
        }
        return 1;
    }
}
