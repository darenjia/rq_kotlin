package com.bkjcb.rqapplication.fragment;

import android.support.v4.app.Fragment;

import com.bkjcb.rqapplication.Constants;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DengShuai on 2019/2/19.
 * Description :
 */
public class BaseFragment extends Fragment {
    protected Retrofit retrofit;
    protected Disposable disposable;

    protected void initRetrofit() {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    protected void initRetrofit(String url) {
        retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
