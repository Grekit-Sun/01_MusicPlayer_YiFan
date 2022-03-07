package cn.weli.mediaplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;

import static cn.weli.mediaplayer.YfApplication.appContext;

public class SongDataDbHelper {

    /**
     * 数据库的名字
     */
    private static final String DB_NAME = "yfdb";

    private static YfDbHelper dbhelper = null;
    private static SQLiteDatabase db = null;

    /**
     * 单例
     */
    private static SongDataDbHelper mSongDataDbHelper;

    private SongDataDbHelper(Context context) {
        if (dbhelper == null) {
            dbhelper = new YfDbHelper(context, DB_NAME, null, 1);
        }
        if (db == null) {
            //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
            db = dbhelper.getWritableDatabase();
        }
    }

    /**
     * 提供外部调用方法
     */
    public static SongDataDbHelper getInstance() {
        if (mSongDataDbHelper == null) {
            synchronized (SongDataDbHelper.class) {
                if (mSongDataDbHelper == null) {
                    mSongDataDbHelper = new SongDataDbHelper(appContext);
                }
            }
        }
        return mSongDataDbHelper;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        // mDbHelper.close();
        mSongDataDbHelper = null;
        db.close();
    }

    /**
     * 向数据库插入音乐
     *
     * @param songData
     */
    public void insert(SongData songData) {   //1是全部音乐，2是收藏音乐、3是最近播放

        if (countAllByPath(songData.path) == 0) {    //没有此音乐，才插入

            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            values.put(SongsConstant.KEY_MediaId, songData.songMediaId);
            values.put(SongsConstant.KEY_Singer, songData.singer);
            values.put(SongsConstant.KEY_SongName, songData.songName);
            values.put(SongsConstant.KEY_Path, songData.path);
            values.put(SongsConstant.KEY_Duration, songData.duration);
            values.put(SongsConstant.KEY_Size, songData.size);
            values.put(SongsConstant.KEY_AlbumId, songData.albumId);
//            values.put(SongsConstant.KEY_Type, SongsConstant.TYPE_ALL);
            values.put(SongsConstant.KEY_Status, songData.status);
            values.put(SongsConstant.KEY_UpdateTime, songData.updateTime);
            values.put(SongsConstant.KEY_IsFavorite, songData.isFavorite);
            values.put(SongsConstant.KEY_isRecMusic, songData.isRecMusic);
            values.put(SongsConstant.KEY_FavUpdateTime, songData.favUpdateTime);

            db.insert(SongsConstant.TableName, null, values);
        }

    }


    /**
     * 获取全部数据
     *
     * @return
     */
    public List<SongData> getAllSongs() {
        List<SongData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + SongsConstant.TableName + " order by id ASC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {     //判断是否有数据
            if (cursor.moveToFirst()) {
                do {
                    SongData data = new SongData();
                    data.id = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Id));
                    data.songMediaId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_MediaId));
                    data.singer = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Singer));
                    data.songName = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_SongName));
                    data.path = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Path));
                    data.duration = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Duration));
                    data.size = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_Size));
                    data.albumId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_AlbumId));
//                    data.type = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Type));
                    data.status = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Status));
                    data.updateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_UpdateTime));
                    data.isFavorite = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_IsFavorite));
                    data.isRecMusic = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_isRecMusic));
                    data.favUpdateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_FavUpdateTime));
                    list.add(data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 通过 id更新状态
     *
     * @param id
     */
    public void updateStatusById(int id, int status) {
        String sql = "UPDATE " + SongsConstant.TableName + " set " + SongsConstant.KEY_Status + " = "
                + status + ", " + SongsConstant.KEY_UpdateTime + " = " + System.currentTimeMillis()
                + ", " + SongsConstant.KEY_isRecMusic + " = " + SongsConstant.IS_REC
                + " where " + SongsConstant.KEY_Id + " = " + id;
        db.execSQL(sql);
    }

    /**
     * 通过id更新类型
     *
     * @param id
     * @param type
     */
    public void updateTypeypeById(int id, int type) {
        String sql;
        if (type == SongsConstant.TYPE_FAV || type == SongsConstant.TYPE_FAV_AND_REC) {
            sql = "UPDATE " + SongsConstant.TableName + " set "
                    + SongsConstant.KEY_FavUpdateTime + " = " + System.currentTimeMillis() + ", "
                    + SongsConstant.KEY_IsFavorite + " = " + SongsConstant.IS_FAV + ", "
                    + SongsConstant.KEY_isRecMusic + " = " + SongsConstant.IS_REC
                    + " where " + SongsConstant.KEY_Id + " = " + id;
        } else {
            sql = "UPDATE " + SongsConstant.TableName + " set "
                    + SongsConstant.KEY_Type + " = " + type
                    + " where " + SongsConstant.KEY_Id + " = " + id;
        }

        db.execSQL(sql);
    }

    /**
     * 通过id置为收藏
     * @param id
     */
    public void setFavOrNotById(int id,int isFav){
        String sql = "UPDATE "+ SongsConstant.TableName + " set " + SongsConstant.KEY_IsFavorite + " = "
                +isFav + " where " + SongsConstant.KEY_Id + " = " + id;
        db.execSQL(sql);
    }


    /**
     * 通过类型查询
     *
     * @param type
     * @return
     */
    public List<SongData> loadByType(int type) {
        List<SongData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + SongsConstant.TableName + " where " + SongsConstant.KEY_Type + " = " + type;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {     //判断是否有数据
            if (cursor.moveToFirst()) {
                do {
                    SongData data = new SongData();
                    data.id = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Id));
                    data.songMediaId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_MediaId));
                    data.singer = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Singer));
                    data.songName = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_SongName));
                    data.path = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Path));
                    data.duration = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Duration));
                    data.size = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_Size));
                    data.albumId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_AlbumId));
//                    data.type = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Type));
                    data.status = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Status));
                    data.updateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_UpdateTime));
                    data.isFavorite = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_IsFavorite));
                    data.isRecMusic = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_isRecMusic));
                    data.favUpdateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_FavUpdateTime));
                    list.add(data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }


    /**
     * 通过ID查询
     *
     * @param id
     * @return
     */
    public SongData getDataById(int id) {
        String sql = "SELECT * FROM " + SongsConstant.TableName + " where " + SongsConstant.KEY_Id + " = " + id;
        Cursor cursor = db.rawQuery(sql, null);
        SongData data = new SongData();
        if (cursor != null) {     //判断是否有数据
            if (cursor.moveToFirst()) {
                do {
                    data.id = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Id));
                    data.songMediaId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_MediaId));
                    data.singer = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Singer));
                    data.songName = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_SongName));
                    data.path = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Path));
                    data.duration = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Duration));
                    data.size = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_Size));
                    data.albumId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_AlbumId));
//                    data.type = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Type));
                    data.status = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Status));
                    data.updateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_UpdateTime));
                    data.isFavorite = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_IsFavorite));
                    data.isRecMusic = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_isRecMusic));
                    data.favUpdateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_FavUpdateTime));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return data;
    }


    /**
     * 查询最近播放（时间降序）
     *
     * @return
     */
    public List<SongData> loadRecSong() {
        List<SongData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + SongsConstant.TableName + " where " + SongsConstant.KEY_isRecMusic
                + " = " + SongsConstant.IS_REC +  " order by " + SongsConstant.KEY_UpdateTime + " DESC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {     //判断是否有数据
            if (cursor.moveToFirst()) {
                do {
                    SongData data = new SongData();
                    data.id = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Id));
                    data.songMediaId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_MediaId));
                    data.singer = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Singer));
                    data.songName = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_SongName));
                    data.path = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Path));
                    data.duration = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Duration));
                    data.size = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_Size));
                    data.albumId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_AlbumId));
//                    data.type = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Type));
                    data.status = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Status));
                    data.updateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_UpdateTime));
                    data.isFavorite = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_IsFavorite));
                    data.isRecMusic = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_isRecMusic));
                    data.favUpdateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_FavUpdateTime));
                    list.add(data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }


    /**
     * 查询收藏音乐（时间降序）
     *
     * @return
     */
    public List<SongData> loadFavSong() {
        List<SongData> list = new ArrayList<>();
        String sql = "SELECT * FROM " + SongsConstant.TableName + " where " + SongsConstant.KEY_IsFavorite
                + " = " + SongsConstant.IS_FAV + " order by " + SongsConstant.KEY_FavUpdateTime + " DESC";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {     //判断是否有数据
            if (cursor.moveToFirst()) {
                do {
                    SongData data = new SongData();
                    data.id = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Id));
                    data.songMediaId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_MediaId));
                    data.singer = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Singer));
                    data.songName = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_SongName));
                    data.path = cursor.getString(cursor.getColumnIndex(SongsConstant.KEY_Path));
                    data.duration = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Duration));
                    data.size = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_Size));
                    data.albumId = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_AlbumId));
//                    data.type = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Type));
                    data.status = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_Status));
                    data.updateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_UpdateTime));
                    data.isFavorite = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_IsFavorite));
                    data.isRecMusic = cursor.getInt(cursor.getColumnIndex(SongsConstant.KEY_isRecMusic));
                    data.favUpdateTime = cursor.getLong(cursor.getColumnIndex(SongsConstant.KEY_FavUpdateTime));
                    list.add(data);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 通过path查询是有已有此音乐
     *
     * @return
     */
    public int countAllByPath(String path) {
        String sql = "SELECT * FROM " + SongsConstant.TableName + " where " + SongsConstant.KEY_Path + " = " + "'" + path + "'";
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
