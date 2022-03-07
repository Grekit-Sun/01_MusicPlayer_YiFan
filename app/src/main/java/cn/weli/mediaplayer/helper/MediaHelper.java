package cn.weli.mediaplayer.helper;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.utils.SongUtil;

import static cn.weli.mediaplayer.YfApplication.appContext;

public class MediaHelper {

    public static final String TAG = "position";

    /**
     * 获取本地音乐并插入数据库
     */
    public void insertIntoDb() {
        List<SongData> list = obtainLoalSongs();
        SongDataDbHelper dbHelper = SongDataDbHelper.getInstance();
        for (SongData data : list) {
            dbHelper.insert(data);
        }
    }

    /**
     * 获取音乐专辑图片
     *
     * @param songMeidaId
     * @param albumId
     * @return 音乐专辑图片
     */
    public static Bitmap getSongAlbumBitmap(long songMeidaId, long albumId) {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Bitmap bm = null;
        // 专辑id和歌曲id小于0说明没有专辑、歌曲，并抛出异常
        if (albumId < 0 && songMeidaId < 0) {
            throw new IllegalArgumentException(
                    "Must specify an album or a song id");
        }
        try {
            if (albumId < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songMeidaId + "/albumart");
                ParcelFileDescriptor pfd = null;

                pfd = appContext.getContentResolver().openFileDescriptor(uri, "r");

                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);
                ParcelFileDescriptor pfd = appContext.getContentResolver()
                        .openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);


                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //如果获取的bitmap为空，则返回一个默认的bitmap
        if (bm == null) {
            Resources resources = appContext.getResources();
            Drawable drawable = resources.getDrawable(R.drawable.icon_music);
            //Drawable 转 Bitmap
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            bm = bitmapDrawable.getBitmap();

        }
        return Bitmap.createScaledBitmap(bm, 150, 150, true);
    }


    /**
     * 自定义RemoteView
     *
     * @param songData
     * @return
     */
    public static RemoteViews initNotifyView(SongData songData) {
        String packageName = appContext.getPackageName();
        RemoteViews remoteViews = new RemoteViews(packageName, R.layout.notification_layout);

        remoteViews.setImageViewBitmap(R.id.music_img, getSongAlbumBitmap(songData.songMediaId, songData.albumId));
        remoteViews.setTextViewText(R.id.noti_music_name, songData.songName);
        remoteViews.setTextViewText(R.id.noti_music_nauthor, songData.singer);

        if (!MediaManager.getInstance().mMediaPlayer.isPlaying()) {        //音乐停止
            remoteViews.setViewVisibility(R.id.img_status_play, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.img_status_pause, View.GONE);
        } else if (MediaManager.getInstance().mMediaPlayer.isPlaying()) {          //音乐播放
            remoteViews.setViewVisibility(R.id.img_status_play, View.GONE);
            remoteViews.setViewVisibility(R.id.img_status_pause, View.VISIBLE);
        } else if (MediaManager.getInstance().isPause()) {          //音乐暂停
            remoteViews.setViewVisibility(R.id.img_status_play, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.img_status_pause, View.GONE);
        }

        //播放
        Intent play = new Intent(SongsConstant.ACTION_PLAY_MUSIC);
        PendingIntent intent_play = PendingIntent.getBroadcast(appContext, SongsConstant.REQUEST_CODE_PLAY, play, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_status_play, intent_play);

        //暂停
        Intent pause = new Intent(SongsConstant.ACTION_PAUSE_MUSIC);    //暂停
        PendingIntent intent_pause = PendingIntent.getBroadcast(appContext, SongsConstant.REQUEST_CODE_PAUSE, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_status_pause, intent_pause);

        //上一首
        Intent last = new Intent(SongsConstant.ACTION_PLAY_LAST_MUSIC);    //播放上一首
        PendingIntent intent_prv = PendingIntent.getBroadcast(appContext, SongsConstant.REQUEST_CODE_LAST, last, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_lastsong, intent_prv);

        //下一首
        Intent next = new Intent(SongsConstant.ACTION_PLAY_NEXT_MUSIC);    //播放下一首
        PendingIntent intent_next = PendingIntent.getBroadcast(appContext, SongsConstant.REQUEST_CODE_NEXT, next, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.img_nextsong, intent_next);

        return remoteViews;
    }


    /**
     * 获取本地音乐
     *
     * @return 本地音乐
     */
    private List<SongData> obtainLoalSongs() {
        List<SongData> list = new ArrayList<SongData>();
        // 媒体库查询语句
        Cursor cursor = appContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    SongData song = new SongData();
                    song.songMediaId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                    song.songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    song.albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                    if (song.songName == null) {
                        continue;
                    }

                    if (song.size > 1000 * 800) {
                        // 注释部分是切割标题，分离出歌曲名和歌手 （本地媒体库读取的歌曲信息不规范）
                        if (song.songName.contains("-")) {
                            String[] str = song.songName.split("-");
                            song.singer = str[0];
                            song.songName = str[1];
                        }
                        list.add(song);
                    }
                } while (cursor.moveToNext());
            }
            // 释放资源
            cursor.close();
        }
        return list;
    }

    /**
     * 存储上一首歌的id
     *
     * @param id
     */
    public static void saveIsPlaySongId(int id) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("music_sp", appContext.MODE_PRIVATE).edit();
        editor.putInt("isPlaySongId", id);
        editor.commit();

    }

    /**
     * 读取正在播放Id
     *
     * @return
     */
    public static Integer readIsPlaySongId() {
        SharedPreferences read = appContext.getSharedPreferences("music_sp", appContext.MODE_PRIVATE);
        return read.getInt("isPlaySongId", -1);
    }

    /**
     * 存储上一首歌的type
     *
     * @param type
     */
    public static void saveIsPlaySongType(int type) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("music_sp", appContext.MODE_PRIVATE).edit();
        editor.putInt("isPlaySongType", type);
        editor.commit();

    }

    /**
     * 读取正在播放type
     *
     * @return
     */
    public static Integer readIsPlaySongType() {
        SharedPreferences read = appContext.getSharedPreferences("music_sp", appContext.MODE_PRIVATE);
        return read.getInt("isPlaySongType", -1);
    }

}
