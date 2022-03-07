package cn.weli.mediaplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import cn.weli.mediaplayer.constant.SongsConstant;


public class YfDbHelper extends SQLiteOpenHelper {


    //歌曲信息表
    public static final String CREATE_TABLE = "create table " + SongsConstant.TableName
            + "( "
            + SongsConstant.KEY_Id + " integer primary key autoincrement, "
            + SongsConstant.KEY_MediaId + " Long , " + SongsConstant.KEY_Singer + " text, " + SongsConstant.KEY_SongName + " text, "
            + SongsConstant.KEY_Path + " text, " + SongsConstant.KEY_Duration + " integer, "
            + SongsConstant.KEY_Size + " Long, " + SongsConstant.KEY_AlbumId + " Long, "
            + SongsConstant.KEY_Status + " integer, "
            + SongsConstant.KEY_UpdateTime + " Long, " + SongsConstant.KEY_FavUpdateTime + " Long, "
            + SongsConstant.KEY_IsFavorite + " integer, " + SongsConstant.KEY_isRecMusic + " integer "
            + ")";

    public YfDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
