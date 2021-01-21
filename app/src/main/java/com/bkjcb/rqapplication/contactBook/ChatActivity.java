package com.bkjcb.rqapplication.contactBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.SimpleBaseActivity;
import com.bkjcb.rqapplication.contactBook.adapter.MessageItemAdapter;
import com.bkjcb.rqapplication.contactBook.database.ContactDataUtil;
import com.bkjcb.rqapplication.contactBook.model.ChatMessage;
import com.bkjcb.rqapplication.contactBook.model.User;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/2/18.
 * Description :
 */
public class ChatActivity extends SimpleBaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

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
        adapter.setOnItemLongClickListener(this);
    }

    private void refreshData() {
        List<ChatMessage> messages = ChatMessage.queryMessage();
        if (messages.size() == 0) {
            adapter.setEmptyView(R.layout.empty_textview);
        } else {
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
        IMActivity.ToActivity(ChatActivity.this, getUserByID((ChatMessage) adapter.getItem(position)));
    }

    private User getUserByID(ChatMessage chatMessage) {
        return ContactDataUtil.queryUser(chatMessage.getReceiver());
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        ChatMessage chatMessage = (ChatMessage) adapter.getItem(position);
        ChatMessage.remove(chatMessage.getReceiver());
        adapter.remove(position);
        return true;
    }
}
