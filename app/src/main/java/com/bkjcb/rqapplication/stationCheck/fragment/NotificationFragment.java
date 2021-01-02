package com.bkjcb.rqapplication.stationCheck.fragment;

import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.base.fragment.BaseSimpleFragment;
import com.bkjcb.rqapplication.stationCheck.model.ModifyNotificationModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;

/**
 * Created by DengShuai on 2020/12/31.
 * Description :
 */
public class NotificationFragment extends BaseSimpleFragment {


    @BindView(R.id.info_zi)
    TextView mInfoZi;
    @BindView(R.id.info_condition1)
    CheckBox mInfoCondition1;
    @BindView(R.id.info_condition2)
    CheckBox mInfoCondition2;
    @BindView(R.id.info_content)
    TextView mInfoContent;
    @BindView(R.id.name)
    TextView mInfoName;
    @BindView(R.id.info_des)
    TextView mInfoDes;
    @BindView(R.id.content_view)
    LinearLayout contentLayout;
    ModifyNotificationModel model;

    public void setModifyNotificationModel(ModifyNotificationModel model) {
        this.model = model;
    }

    public static NotificationFragment newInstance(ModifyNotificationModel model) {
        NotificationFragment fragment = new NotificationFragment();
        fragment.setModifyNotificationModel(model);
        return fragment;
    }

    @Override
    public void setResId() {
        resId = R.layout.fragment_notification_detail;
    }

    @Override
    protected void initView() {
        if (model == null) {
            showError();
        } else {
            mInfoZi.setText(buildZI());
            mInfoDes.setText(buildDes());
            mInfoCondition2.setText(buildCondition2());
            mInfoContent.setText(model.content);
            mInfoName.setText(buildUnit());
            mInfoCondition1.setEnabled(false);
            mInfoCondition2.setEnabled(false);
            if (model.type == 1) {
                mInfoCondition1.setChecked(true);
            } else {
                mInfoCondition2.setChecked(true);
            }
        }
    }

    @Override
    protected void initData() {

    }


    public boolean buildPDF() {
        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(contentLayout.getWidth(), contentLayout.getHeight(), 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        View content = contentLayout;
        content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);
        // write the document content
        try {
            document.writeTo(getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // close the document
        document.close();
        return true;
    }

    private OutputStream getOutputStream() throws IOException {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"责令整改通知书_" + model.guid + ".pdf");
        return new FileOutputStream(file);
    }

    private String buildZI() {
        return "沪建管责改字【 " + model.zi + " 】号";
    }

    private String buildUnit() {
        return "被检单位: " + model.name;
    }

    private String buildDes() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t\t经查，你（单位）于 ")
                .append(model.time)
                .append("在")
                .append(model.address)
                .append("从事了")
                .append(model.des)
                .append("的行为，违反了")
                .append(model.provision)
                .append("第")
                .append(model.provision1)
                .append("条第")
                .append(model.provision2)
                .append("款第")
                .append(model.provision3)
                .append("项的规定，依据")
                .append(model.provisionName)
                .append("第")
                .append(model.provision11)
                .append("条第")
                .append(model.provision12)
                .append("款第")
                .append(model.provision13)
                .append("项的规定，本机关现责令你（单位）：");
        return builder.toString();
    }

    private String buildCondition2() {
        if (model.type == 1) {
            return "于_________前改正上述行为。";
        }
        return "于" + model.typeTime + "前改正上述行为。";
    }
}
