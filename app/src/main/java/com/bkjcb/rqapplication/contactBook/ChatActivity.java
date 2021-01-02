package com.bkjcb.rqapplication.contactBook;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.base.util.Utils;
import com.bkjcb.rqapplication.contactBook.adapter.MessageItemAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.fragment.ContactFirstFragment;
import com.bkjcb.rqapplication.contactBook.model.ChatMessage;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hss01248.dialog.StyledDialog;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import cn.carbs.android.avatarimageview.library.AvatarImageView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ChatActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.check_list)
    RecyclerView mList;
    private MessageItemAdapter adapter;

    @Override
    protected int setLayoutID() {
        return R.layout.activity_main_chat;
    }

    @Override
    protected void initView() {
        initTopbar("对话列表");
    }

    @Override
    protected void initData() {
        mList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageItemAdapter(R.layout.item_chat_message_view);
        mList.setAdapter(adapter);
        adapter.bindToRecyclerView(mList);
        adapter.setOnItemClickListener(this);
    }

    private void refreshData(){
      List<ChatMessage> messages =  ChatMessage.queryMessage();
      if (messages.size()==0){
          adapter.setEmptyView(R.layout.empty_textview);
      }else {
          adapter.setNewData(messages);
      }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    public static void ToActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        IMActivity.ToActivity(ChatActivity.this,getUserByID((ChatMessage) adapter.getItem(position)));
    }

    private User getUserByID(ChatMessage chatMessage){
        return ContactDataUtil.queryUser(chatMessage.getReceiver());
    }
}
