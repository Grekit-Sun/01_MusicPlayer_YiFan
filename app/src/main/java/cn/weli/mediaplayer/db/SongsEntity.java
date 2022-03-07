package cn.weli.mediaplayer.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "song_table")
public class SongsEntity {

    /**
     * 数据库中的id
     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public Integer id;

    /**
     * song id
     */
    @ColumnInfo(name = "media_id")
    public Long songMediaId;

    /**
     * 歌手
     */
    @ColumnInfo(name = "singer")
    public String singer;

    /**
     * 歌曲名
     */
    @ColumnInfo(name = "song_name")
    public String songName;

    /**
     * 歌曲的地址
     */
    @ColumnInfo(name = "song_path")
    public String path;

    /**
     * 歌曲长度
     */
    @ColumnInfo(name = "duration")
    public int duration;

    /**
     * 歌曲的大小
     */
    @ColumnInfo(name = "size")
    public long size;

    /**
     * 音乐图片
     */
    @ColumnInfo(name = "albumId")
    public long albumId;

    /**
     * 音乐类型
     * 1-全部音乐，2-收藏音乐、3-最近播放
     */
    @ColumnInfo(name = "type")
    public int type;

    /**
     * 音乐的状态 0-stop,1-play,2-pause
     */
    @ColumnInfo(name = "status")
    public int status;

    /**
     * 音乐的更新时间
     */
    @ColumnInfo(name = "update_time")
    public Long updateTime;

    /**
     * 是否为收藏0-未收藏，1-收藏
     */
    @ColumnInfo(name = "is_favorite")
    public Integer isFavorite;

    /**
     * 收藏时间
     */
    @ColumnInfo(name = "fav_update_time")
    public Long favUpdateTime;
}
