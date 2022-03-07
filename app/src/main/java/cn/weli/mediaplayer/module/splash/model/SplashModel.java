package cn.weli.mediaplayer.module.splash.model;

import android.content.Context;

import cn.weli.mediaplayer.helper.MediaHelper;

public class SplashModel {

    /**
     * 将所有音乐插入数据库
     */
    public void insertAllMusic(){
        MediaHelper mediaHelper = new MediaHelper();
        mediaHelper.insertIntoDb();  //获取本地音乐并且插入数据库
    }

}
