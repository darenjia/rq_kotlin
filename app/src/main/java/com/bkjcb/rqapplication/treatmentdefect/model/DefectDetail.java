package com.bkjcb.rqapplication.treatmentdefect.model;

import java.util.List;

/**
 * Created by DengShuai on 2020/6/18.
 * Description :
 */
public class DefectDetail {

    /**
     * userId : 111222333
     * type : 1
     * mtfId : 444555666
     * disposalTime : 2020-06-12 16:31:49
     * opinions : 3天内到现场处置查看
     * disposalResult : 已经停止供气
     * feedbackRemark : 其他
     * other : 好好学习天天向上
     * jzReasons : 测试退回原因
     * jzImg : 街镇上传图片地址
     */

    private String userId;
    private String type;
    private String mtfId;
    private String disposalTime;
    private String opinions;
    private String disposalResult;
    private String feedbackRemark;
    private String other;
    private String jzReasons;
    private String jzImg;
    private List<String> filePaths;
    private String remotePath;

    public List<String> getFilePaths() {
        return filePaths;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMtfId() {
        return mtfId;
    }

    public void setMtfId(String mtfId) {
        this.mtfId = mtfId;
    }

    public String getDisposalTime() {
        return disposalTime;
    }

    public void setDisposalTime(String disposalTime) {
        this.disposalTime = disposalTime;
    }

    public String getOpinions() {
        return opinions;
    }

    public void setOpinions(String opinions) {
        this.opinions = opinions;
    }

    public String getDisposalResult() {
        return disposalResult;
    }

    public void setDisposalResult(String disposalResult) {
        this.disposalResult = disposalResult;
    }

    public String getFeedbackRemark() {
        return feedbackRemark;
    }

    public void setFeedbackRemark(String feedbackRemark) {
        this.feedbackRemark = feedbackRemark;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getJzReasons() {
        return jzReasons;
    }

    public void setJzReasons(String jzReasons) {
        this.jzReasons = jzReasons;
    }

    public String getJzImg() {
        return jzImg;
    }

    public void setJzImg(String jzImg) {
        this.jzImg = jzImg;
    }
}
