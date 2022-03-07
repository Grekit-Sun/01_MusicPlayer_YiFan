package cn.weli.mediaplayer.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SongsDao {

    /**
     * 获取全部数据
     * @return
     */
    @Query("SELECT * FROM song_table")
    List<SongsEntity> getAll();


    /**
     * 通过id查询
     * @return
     */
    @Query("SELECT * FROM song_table where id IN (:id)")
    List<SongsEntity> loadAllById(int[] id);

    /**
     * 通过类型查询
     * @param type
     * @return
     */
    @Query("SELECT * FROM song_table where type IN (:type)")
    List<SongsEntity> loadByType(int type);

    /**
     * 查询最近播放（时间降序）
     * @return
     */
    @Query("SELECT * FROM song_table where type = 3 order by update_time DESC")
    List<SongsEntity> loadRecSong();

    /**
     * 查询收藏音乐（时间降序）
     * @return
     */
    @Query("SELECT * FROM song_table where type = 2 order by update_time DESC")
    List<SongsEntity> loadFavSong();
}
