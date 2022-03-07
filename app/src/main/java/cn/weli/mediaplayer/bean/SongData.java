package cn.weli.mediaplayer.bean;

import android.arch.persistence.room.ColumnInfo;

/**
 * 音乐的Bean类
 */
public class SongData {

    /**
     * 数据库中的id
    */
    public int id;
    /**
     * song id
     */
    public long songMediaId;

    /**
     * 歌手
     */
    public String singer;
    /**
     * 歌曲名
     */
    public String songName;
    /**
     * 歌曲的地址
     */
    public String path;
    /**
     * 歌曲长度
     */
    public int duration;
    /**
     * 歌曲的大小
     */
    public long size;

    /**
     * 音乐图片
     */
    public long albumId;

    /**
     * 音乐类型
     * 1-全部音乐，2-收藏音乐、3-最近播放、5-既是收藏也是最近
     */
//    public Integer type;

    /**
     * 音乐的状态 0-stop,1-play,2-pause
     */
    public int status;

    /**
     * 音乐的更新时间
     */
    public long updateTime;

    /**
     * 是否为收藏0-未收藏，1-收藏
     */
    public int isFavorite;

    /**
     * 是否为最近0-不是最近，1-最近
     */
    public int isRecMusic;

    /**
     * 收藏时间
     */
    public long favUpdateTime;

}
