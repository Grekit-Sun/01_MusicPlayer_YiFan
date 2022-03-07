package cn.weli.mediaplayer.module.mine.module.recMusic.presenter;

import android.util.Log;

import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.mine.module.allMusic.model.AllMusicModel;
import cn.weli.mediaplayer.module.mine.module.recMusic.model.RecMusicModel;
import cn.weli.mediaplayer.module.mine.module.recMusic.view.IRecMusicView;
import cn.weli.mediaplayer.utils.SongUtil;

import static android.support.constraint.Constraints.TAG;

public class RecMusicPresenter implements IPresenter, CallbackListener {
    private IRecMusicView mView;
    private RecMusicModel mRecMusicModel;
    private List<SongData> list;
    private boolean hasSetListener = false;
    private MediaManager mMediaManager;

    public RecMusicPresenter(IRecMusicView view) {
        mView = view;
        mRecMusicModel = new RecMusicModel();
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);    //注册回调
    }

    /**
     * 获取最近音乐
     */
    public void obtainRecSongs() {

        mRecMusicModel.queryRecSongs();

        /**
         * 设置数据库操作监听
         */
        if (!hasSetListener) {
            mRecMusicModel.setOnQueryEndListener(new RecMusicModel.OnQueryEndListener() {
                @Override
                public void queryRecSongs(List<SongData> list) {
                    setList(list);
                    mView.setAdapter(list);
                }
            });
            hasSetListener = true;
        }
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

    public void setList(List<SongData> list) {
        this.list = list;
    }

    @Override
    public void clear() {

    }

    @Override
    public void play(int oldId, int nowId) {
        obtainRecSongs();
//        mAllMusicModel.upSongDbStatus(nowId, SongsConstant.MUSIC_STATUS_PLAY);
//        //更新新位置
//        int nowPosition = -1;
//        for (SongData songData : list) {
//            if (songData.id == nowId) {
//                nowPosition = list.indexOf(songData);
//                songData.status = SongsConstant.MUSIC_STATUS_PLAY;
//                list.set(nowPosition, songData);
//                Log.e(TAG, "play------------back: ---------------nowPosition" + nowPosition);
//                break;
//            }
//        }
//        if (oldId < 0) {
////            mView.setData(list);
////            return;
//        } else if (oldId == nowId) {
////            mView.setData(list);
////            return;
//        } else {
////            mAllMusicModel.upSongDbStatus(oldId, SongsConstant.MUSIC_STATUS_STOP);
//            int oldPosition = -1;
//            for (SongData songData : list) {
//                if (songData.id == oldId) {
//                    oldPosition = list.indexOf(songData);
//                    songData.status = SongsConstant.MUSIC_STATUS_STOP;
//                    list.set(oldPosition, songData);
//                    Log.e(TAG, "play------------back: ---------------oldPosition" + oldPosition);
//                    break;
//                }
//            }
////            mView.setData(list);
//        }


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
