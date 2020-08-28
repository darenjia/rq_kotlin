package com.bkjcb.rqapplication.base.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.bkjcb.rqapplication.R;
import com.bkjcb.rqapplication.actionRegister.ActionRegisterActivity;
import com.bkjcb.rqapplication.base.MyApplication;
import com.bkjcb.rqapplication.base.util.Utils;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Notification.DEFAULT_ALL;

/**
 * Created by DengShuai on 2019/3/13.
 * Description :
 */
public class MessageService extends JobService {
    private Disposable disposable;
    private int notifyId = 0;
    private String serverURI = "tcp://192.168.8.206:1883";
    private String clientId = "1";
    private MqttAndroidClient mqttAndroidClient;
    String subscribeTopic = "gasEventMQ";
    private MqttConnectOptions connectOptions;
    //    private Connection connection;

    @Override
    public boolean onStartJob(JobParameters params) {
        Logger.i("开启服务" + this.toString());
        initMQClient();
        startListen();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Logger.i("停止服务");
        if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {
            try {
                mqttAndroidClient.disconnect();
                mqttAndroidClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.i("销毁服务");
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static final String CHANNEL_ID_LOCATION = "com.bkjcb.myapplication";

    public void initChannel() {

    }

    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MyApplication.getContext().getPackageName(),
                    "会话消息",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }
        init();
    }

    private void init() {

    }

    private void startListen() {
        if (disposable == null) {
            disposable = createObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            showNotification(s);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Logger.w("消息服务出现错误:" + throwable.getMessage());
                        }
                    });
        }

    }

    private void showNotification(String s) {
        Intent intent = getSpecialIntent();
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //创建返回栈
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            //添加Activity到返回栈
            stackBuilder.addParentStack(ActionRegisterActivity.class);
            //添加Intent到栈顶
            stackBuilder.addNextIntent(intent);
            pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID_LOCATION)
                    .setContentText(s)
                    .setContentTitle("新任务提醒")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(Utils.getBitmapByRid(R.drawable.app_icon))
                    .setNumber(1)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(DEFAULT_ALL)
                    .setFullScreenIntent(pendingIntent, true)
                    .setTicker("你收到一条新待办任务！请注意查看");

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(++notifyId, builder.build());

        } else {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(getApplicationContext())
                    .setContentText(s)
                    .setContentTitle("新任务提醒")
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentIntent(pendingIntent)
                    .setLargeIcon(Utils.getBitmapByRid(R.drawable.app_icon))
                    .setNumber(1)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(DEFAULT_ALL)
                    .setTicker("你收到一条新待办任务！请注意查看");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(++notifyId, builder.build());
        }
    }

    private Observable<String> createObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {

                try {
                    mqttAndroidClient.connect(connectOptions, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Logger.w("连接成功");
                            DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                            disconnectedBufferOptions.setBufferEnabled(true);
                            disconnectedBufferOptions.setBufferSize(100);
                            disconnectedBufferOptions.setPersistBuffer(false);
                            disconnectedBufferOptions.setDeleteOldestMessages(false);
                            mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                            subscribeTopic(emitter);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable e) {
                            e.printStackTrace();
                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();

                }
                Logger.w("消息服务开启成功");
                //Toast.makeText(MessageService.this, "消息服务开启成功！", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private Intent getSpecialIntent() {
        return new Intent(this, ActionRegisterActivity.class);
    }

    private void initMQClient(){
        connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setUserName("admin");
        connectOptions.setPassword("admin".toCharArray());
        connectOptions.setAutomaticReconnect(true);
        connectOptions.setCleanSession(true);
        mqttAndroidClient = new MqttAndroidClient(MessageService.this, serverURI, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Logger.w("connectionLost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Logger.w("messageArrived");
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Logger.w("deliveryComplete");
            }
        });
    }
    private void subscribeTopic(ObservableEmitter<String> emitter){
        try {
            mqttAndroidClient.subscribe(subscribeTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    Logger.w(message.toString());
                    emitter.onNext(message.toString());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
