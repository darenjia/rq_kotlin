package com.bkjcb.rqapplication.base.util;

import com.bkjcb.rqapplication.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by DengShuai on 2019/12/19.
 * Description :
 */
public class GlideUtil {
    public static RequestOptions getRequestOption() {
        return new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.error_image).placeholder(R.drawable.vector_drawable_picture_loding);
    }
}
