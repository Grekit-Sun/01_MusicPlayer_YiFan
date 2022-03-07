package cn.weli.mediaplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.BroadcastHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.utils.SongUtil;


public class MediaService extends Service {

    private MediaManager mMediaManager;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        initAndRegisterMediaCallback();

        //适配8.0service
        adaptService();
        //根据广播信号做出相应的处理
        dealBroadcastReceiver();
    }

    private void initAndRegisterMediaCallback() {
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(new CallbackListener() {
            @Override
            public void play(int oldId, int nowId) {

            }

            @Override
            public void pause(int id) {

            }

            @Override
            public void stop(int id) {

            }
        });
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * 广播接收，并做处理（SongList确定）
     */
    private void dealBroadcastReceiver() {
         mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (SongsConstant.ACTION_PLAY_MUSIC.equals(intent.getAction())) {     //播放
                    mMediaManager.play(SongUtil.isPlaySongData);
                } else if (SongsConstant.ACTION_PAUSE_MUSIC.equals(intent.getAction())) {      //暂停
                    mMediaManager.pause();
                } else if (SongsConstant.ACTION_PLAY_LAST_MUSIC.equals(intent.getAction())) {      //上一首
                    mMediaManager.last();
                } else if (SongsConstant.ACTION_PLAY_NEXT_MUSIC.equals(intent.getAction())) {      //下一首
                    mMediaManager.next();
                } else if (SongsConstant.ACTION_STOP_MUSIC.equals(intent.getAction())) {       //停止
                    mMediaManager.stop();
                }

            }
        };

        //注册广播
        BroadcastHelper.registReceiver(this, mBroadcastReceiver, SongsConstant.ACTION_PLAY_MUSIC, SongsConstant.ACTION_PAUSE_MUSIC,
                SongsConstant.ACTION_STOP_MUSIC, SongsConstant.ACTION_PLAY_LAST_MUSIC, SongsConstant.ACTION_PLAY_NEXT_MUSIC);

    }

    /**
     * 适配8.0service
     */
    private void adaptService() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(SongsConstant.mMusicId, SongsConstant.mCommonChannel, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            Notification notification = new Notification.Builder(getApplicationContext(), SongsConstant.mCommonId).build();
            startForeground(1, notification);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
