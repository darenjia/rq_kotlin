package com.bkjcb.rqapplication.fragment;

import android.support.v4.app.Fragment;

import io.reactivex.disposables.Disposable;

/**
 * Created by DengShuai on 2019/2/19.
 * Description :
 */
public class BaseFragment extends Fragment {
    protected Disposable disposable;
    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
