package com.example.divin.workmanager.Presenter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.example.divin.workmanager.Model.BlurViewModel;

public class SelectImagePresenter {
    private BlurViewModel mViewModel;
    private Context context;


    public SelectImagePresenter() {
    }

    public void setDefaultContext(Context context){
        if (this.context == null){
            this.context = context;
            mViewModel = ViewModelProviders.of((FragmentActivity) context).get(BlurViewModel.class);
        }
    }
}
