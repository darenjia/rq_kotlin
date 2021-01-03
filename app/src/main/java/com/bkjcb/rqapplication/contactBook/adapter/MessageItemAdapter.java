package com.bkjcb.rqapplication.contactBook.adapter;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.contactBook.model.ChatMessage;
import com.bkjcb.rqapplication.contactBook.model.Unit;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.carbs.android.avatarimageview.library.AvatarImageView;

public class MessageItemAdapter extends BaseQuickAdapter<ChatMessage, BaseViewHolder> {
    public MessageItemAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatMessage item) {
        AvatarImageView iv = (AvatarImageView) helper.getView(R.id.item_avatar);
        iv.setTextAndColor(item.getReceiverName().substring(0, 1), Utils.getColor(mContext, helper.getLayoutPosition()));
        helper.setText(R.id.item_name, item.getReceiverName())
                .setText(R.id.item_content, getPreViewText(item))
                .setText(R.id.item_time, Utils.dateFormat(item.getTimestamp()));
    }

    private String getPreViewText(ChatMessage message) {
        String text = null;
        switch (message.getContentType()) {
            case "text":
                text = message.getContent();
                break;
            case "file":
                text ="[ 文件 ]";
                break;
            case "image":
                text ="[ 图片 ]";
                break;
            case "voice":
                text ="[ 语音 ]";
                break;
            case "contact":
                text ="[ 联系人 ]";
                break;
            case "LINK":
                text ="[ 链接 ]";
                break;
            default:
                text = "";
        }
        return text;
    }
}
