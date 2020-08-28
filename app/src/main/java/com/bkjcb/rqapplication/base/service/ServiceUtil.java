package com.bkjcb.rqapplication.base.service;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.bkjcb.rqapplication.base.MyApplication;

import java.util.List;

/**
 * Created by DengShuai on 2017/6/7.
 */

public class ServiceUtil {

    /**
     * 校验某个服务是否还存在
     */
    public static boolean isServiceRunning(String serviceName) {
        // 校验服务是否还存在
        ActivityManager am = (ActivityManager) MyApplication.getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : services) {
            // 得到所有正在运行的服务的名称
            String name = info.service.getClassName();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static  void startService(int jobID,Class<?> cls,Context context){
        JobInfo.Builder builder = new JobInfo.Builder(jobID, new ComponentName(context,cls));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setMinimumLatency(0).setOverrideDeadline(5000)
                .setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            //jobScheduler.cancel(1);
            jobScheduler.schedule(builder.build());
        }
    }
}
