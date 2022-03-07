package cn.weli.mediaplayer.module.mine.module.favMusic.model;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.DbCommand;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.module.mine.module.allMusic.model.AllMusicModel;

public class FavMusicModel {

    /**
     * 查询所有收藏音乐
     */
    public void queryFavSongs(){
        new DbCommand<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                List<SongData> allSongs = SongDataDbHelper.getInstance().loadFavSong();
                return allSongs;
            }

            @Override
            protected void onPostExecute(List<SongData> result) {
                super.onPostExecute(result);
                mOnQueryEndListener.queryFavSongs(result);
            }
        }.execute();
    }



    private OnQueryEndListener mOnQueryEndListener;

    public interface OnQueryEndListener {
        void queryFavSongs(List<SongData> list);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }
}
