package cn.weli.mediaplayer.module.mine.module.allMusic.model;

import java.util.List;

import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.db.DbCommand;
import cn.weli.mediaplayer.db.SongDataDbHelper;

public class AllMusicModel {


    public AllMusicModel() {
    }

    /**
     * 获取所有音乐
     *
     * @return
     */
    public void querySongs() {
        new DbCommand<List<SongData>>() {
            @Override
            protected List<SongData> doInBackground() {
                List<SongData> allSongs = SongDataDbHelper.getInstance().getAllSongs();
                return allSongs;
            }

            @Override
            protected void onPostExecute(List<SongData> result) {
                super.onPostExecute(result);
                mOnQueryEndListener.queryAllSongs(result);
            }
        }.execute();
    }

    /**
     * 获取所有音乐
     *
     * @return
     */
    public void querySongById(final int id) {
        new DbCommand<SongData>() {
            @Override
            protected SongData doInBackground() {
                SongData song = SongDataDbHelper.getInstance().getDataById(id);
                return song;
            }

            @Override
            protected void onPostExecute(SongData result) {
                super.onPostExecute(result);
//                mOnQueryEndListener.queryAllSongById(result);
            }
        }.execute();
    }


    /**
     * 更新音乐状态
     *
     * @param id
     * @param status
     */
    public void upSongDbStatus(final int id, final int status) {

        new DbCommand<Void>() {
            @Override
            protected Void doInBackground() {
                SongDataDbHelper.getInstance().updateStatusById(id, status);
                return null;
            }

        }.execute();
    }

    /**
     * 更新音乐类型
     *
     * @param id
     * @param type
     */
    public void upSongDbTypeById(final int id, final int type) {
        new DbCommand<Void>() {
            @Override
            protected Void doInBackground() {
//                SongDataDbHelper.getInstance().updateTypeypeById(id, type);
                return null;
            }

        }.execute();
    }

    /**
     * 通过id进行收藏
     */
    public void setDbFavById(final int id, final int isFav) {
        new DbCommand<Void>() {
            @Override
            protected Void doInBackground() {
                SongDataDbHelper.getInstance().setFavOrNotById(id, isFav);
                return null;
            }

        }.execute();
    }


    private OnQueryEndListener mOnQueryEndListener;

    public interface OnQueryEndListener {
        void queryAllSongs(List<SongData> list);
//        void queryAllSongById(SongData songData);
    }

    public void setOnQueryEndListener(OnQueryEndListener mOnQueryEndListener) {
        this.mOnQueryEndListener = mOnQueryEndListener;
    }

}
