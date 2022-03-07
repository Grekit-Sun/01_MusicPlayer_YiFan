package cn.weli.mediaplayer.module.mine.module.favMusic.presenter;

import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.mine.module.allMusic.model.AllMusicModel;
import cn.weli.mediaplayer.module.mine.module.favMusic.model.FavMusicModel;
import cn.weli.mediaplayer.module.mine.module.favMusic.view.IFavMusicView;
import cn.weli.mediaplayer.utils.SongUtil;

public class FavMusicPresenter implements IPresenter, CallbackListener {

    private List<SongData> list;
    private IFavMusicView mView;
    private FavMusicModel mFavMusicModel;
    private boolean hasSetListener = false;
    private MediaManager mMediaManager;

    public FavMusicPresenter(IFavMusicView view) {
        mView = view;
        mFavMusicModel = new FavMusicModel();
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);    //注册回调
    }

    /**
     * 传入list
     *
     * @param list
     */
    public void setList(List<SongData> list) {
        this.list = list;
    }

    /**
     * 获取最新的list
     *
     * @return
     */
    public void obtainHasDealSongList() {
        obtainFavSongs();
    }

    /**
     * 获取收藏音乐
     */
    public void obtainFavSongs() {

        mFavMusicModel.queryFavSongs();

        /**
         * 设置数据库操作监听
         */
        if (!hasSetListener) {
            mFavMusicModel.setOnQueryEndListener(new FavMusicModel.OnQueryEndListener() {
                @Override
                public void queryFavSongs(List<SongData> list) {
                    setList(list);
                    mView.setAdapter(list);
                }
            });
            hasSetListener = true;
        }
    }

    @Override
    public void clear() {

    }


    /**
     * 处理点击事件
     *
     * @param songData
     */
    public void dealItemClick(SongData songData, int position) {

        SongUtil.isPlayList = list;     //正在播放的list

        if (songData.status == SongsConstant.MUSIC_STATUS_STOP) {     //音乐停止->播放
            mMediaManager.play(songData);
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PLAY) {       //音乐播放->暂停
            mMediaManager.pause();
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PAUSE) {      //音乐暂停->播放
            mMediaManager.play(songData);
        }

    }

    @Override
    public void play(int oldId, int nowId) {
        obtainFavSongs();
    }

    @Override
    public void pause(int id) {
        int position = -1;
        for (SongData songData : list) {
            if (songData.id == id) {
                position = list.indexOf(songData);
                songData.status = SongsConstant.MUSIC_STATUS_PAUSE;
                list.set(position, songData);
                break;
            }
        }
        mView.setData(list);
    }

    @Override
    public void stop(int id) {
        int position = -1;
        for (SongData songData : list) {
            if (songData.id == id) {
                position = list.indexOf(songData);
                songData.status = SongsConstant.MUSIC_STATUS_STOP;
                list.set(position, songData);
                break;
            }
        }
        mView.setData(list);
    }
}
