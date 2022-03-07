package cn.weli.mediaplayer.constant;

public class SongsConstant {

    /**
     * 所有音乐
     */
    public static final int TYPE_ALL = 1;

    /**
     * 收藏音乐
     */
    public static final int TYPE_FAV = 2;

    /**
     * 最近音乐
     */
    public static final int TYPE_REC = 3;

    /**
     * 既是最近音乐，也是收藏音乐
     */
    public static final int TYPE_FAV_AND_REC = 5;

    /**
     * 表的名字
     */
    public static final String TableName = "SongData";

    /**
     * 表的字段名
     */
    public static final String KEY_Id = "id";
    public static final String KEY_MediaId = "media_id";
    public static final String KEY_Singer = "singer";
    public static final String KEY_SongName = "song_name";
    public static final String KEY_Path = "path";
    public static final String KEY_Duration = "duration";
    public static final String KEY_Size = "size";
    public static final String KEY_AlbumId = "albumId";
    public static final String KEY_Type = "type";
    public static final String KEY_Status = "status";
    public static final String KEY_UpdateTime = "updateTime";
    public static final String KEY_FavUpdateTime= "fav_update_time";
    public static final String KEY_IsFavorite= "isFavorite";
    public static final String KEY_isRecMusic= "isRecMusic";


    /**
     * 媒体广播Action
     */
    public static final String ACTION_PLAY_MUSIC = "cn.weli.play_music";
    public static final String ACTION_PAUSE_MUSIC = "cn.weli.pause_music";
    public static final String ACTION_STOP_MUSIC = "cn.weli.stop_music";
    public static final String ACTION_PLAY_NEXT_MUSIC = "cn.weli.play_next_music";
    public static final String ACTION_PLAY_LAST_MUSIC = "cn.weli.play_last_music";

    public static final String ACTION_UPDATE_ALL_MUSIC = "cn.weli.update_all_music";
    public static final String ACTION_UPDATE_FAV_MUSIC = "cn.weli.update_fav_music";
    public static final String ACTION_UPDATE_REC_MUSIC = "cn.weli.update_rec_music";

    /**
     * notification的请求code
     */
    public static final int REQUEST_CODE_PLAY = 1;
    public static final int REQUEST_CODE_PAUSE =2;
    public static final int REQUEST_CODE_NEXT = 3;
    public static final int REQUEST_CODE_LAST = 4;
    public static final int REQUEST_CODE_START_ACTIVITY = 0;

    /**
     * notification channel
     */
    public static final String mCommonId = "common";
    public static final String mMusicId = "music";
    public static final String mCommonChannel = "common_channel";
    public static final String mMusicChannel = "music_channel";

    /**
     * 音乐的状态 0-stop,1-play,2-pause
     */
    public static final int MUSIC_STATUS_STOP = 0;
    public static final int MUSIC_STATUS_PLAY = 1;
    public static final int MUSIC_STATUS_PAUSE = 2;

    /**
     * 是否收藏0-未收藏，1-收藏
     */
    public static final int NOT_FAV = 0;
    public static final int IS_FAV = 1;

    public static final int NOT_REC = 0;
    public static final int IS_REC = 1;
}
