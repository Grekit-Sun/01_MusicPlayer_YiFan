package cn.weli.mediaplayer.module.mine.module.recMusic.model;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.DbCommand;
import cn.weli.mediaplayer.db.SongDataDbHelper;
import cn.weli.mediaplayer.module.mine.module.favMusic.model.FavMusicModel;

public class RecMusicModel {

    /**
     * 查询所有最近音乐
     */
    public void queryRecSongs(){
        new DbCommand<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                List<SongData> allSongs = SongDataDbHelper.getInstance().loadRecSong();
                return allSongs;
            }

            @Override
            protected void onPostExecute(List<SongData> result) {
                super.onPostExecute(result);
                mOnQueryEndListener.queryRecSongs(result);
            }
        }.execute();
    }


    private OnQueryEndListener mOnQueryEndListener;

    public interface OnQueryEndListener {
        void queryRecSongs(List<SongData> list);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }
}
