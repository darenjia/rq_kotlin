package com.bkjcb.rqapplication.base.loadsir;

import com.bkjcb.rqapplication.base.loadsir.callback.EmptyCallback;
import com.bkjcb.rqapplication.base.loadsir.callback.ErrorCallback;
import com.bkjcb.rqapplication.base.loadsir.callback.LoadingCallback;
import com.bkjcb.rqapplication.base.model.SimpleHttpResult;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

/**
 * Created by DengShuai on 2020/7/10.
 * Description :
 */
public class LoadsirUtil {
    public static void initLoadsir() {
        LoadSir.beginBuilder()
                .addCallback(new LoadingCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new ErrorCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .commit();
    }

    public static LoadService register(Object target, Callback.OnReloadListener onReloadListener) {
        return  LoadSir.getDefault().register(target, onReloadListener, new Convertor<SimpleHttpResult>() {
            @Override
            public Class<? extends Callback> map(SimpleHttpResult result) {
                if (result.pushState == 200) {
                    if (result.getDatas() == null) {
                        return EmptyCallback.class;
                    } else {
                        return SuccessCallback.class;
                    }
                }
                return ErrorCallback.class;
            }
        });
    }

    public static LoadService registerNormal(Object target, Callback.OnReloadListener onReloadListener) {
        return LoadSir.getDefault().register(target, onReloadListener, new Convertor<Boolean>() {
            @Override
            public Class<? extends Callback> map(Boolean result) {
                return result ? SuccessCallback.class : EmptyCallback.class;
            }
        });
    }

}
