package com.bkjcb.rqapplication.base.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.suke.widget.SwitchButton;

/**
 * Created by DengShuai on 2019/11/1.
 * Description :
 */
public class QestionCheckView {
    Context context;
    View view;
    private SwitchButton switchButton;
    private EditText remark;

    public QestionCheckView(Context context, View.OnClickListener listener, boolean isChecked) {
        this.context = context;
        view = View.inflate(context, R.layout.question_window_view, null);
        TextView submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(listener);
        TextView statusText = view.findViewById(R.id.status_text);
        switchButton = view.findViewById(R.id.switch_status);
        remark = view.findViewById(R.id.question_remark);
        switchButton.setChecked(isChecked);
        statusText.setText(getText(isChecked));
        statusText.setTextColor(getTextColor(isChecked));
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                statusText.setText(getText(isChecked));
                statusText.setTextColor(getTextColor(isChecked));
            }
        });
    }

    private String getText(boolean isChecked) {
        if (isChecked) {
            return "有隐患";
        } else {
            return "无隐患";
        }
    }

    private int getTextColor(boolean isChecked) {
        if (isChecked) {
            return context.getResources().getColor(R.color.colorAccent);
        } else {
            return context.getResources().getColor(R.color.colorBasil);
        }
    }

    public View build() {
        return view;
    }

    public String getRemark() {
        return remark.getText().toString();
    }

    public boolean getStatus() {
        return switchButton.isChecked();
    }

    public void setStatus(boolean isChecked) {
        if (switchButton.isChecked() != isChecked) {
            switchButton.toggle(true);
        }
    }
}
