package cn.weli.mediaplayer.module.detail.presenter;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.module.detail.view.IMusicDetailView;
import cn.weli.mediaplayer.module.mine.module.allMusic.model.AllMusicModel;
import cn.weli.mediaplayer.utils.SongUtil;

public class MusicDetailPresenter implements IPresenter {

    private IMusicDetailView mView;
    private AllMusicModel mAllMusicModel;

    public MusicDetailPresenter(IMusicDetailView view) {
        mView = view;
        mAllMusicModel = new AllMusicModel();
    }


    public void dealFavItemClick() {
        if (SongUtil.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            SongUtil.isPlaySongData.isFavorite = SongsConstant.NOT_FAV;
            //更新数据库
            mAllMusicModel.setDbFavById(SongUtil.isPlaySongData.id,SongsConstant.NOT_FAV);

        } else if (SongUtil.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            SongUtil.isPlaySongData.isFavorite = SongsConstant.IS_FAV;
            //更新数据库
            mAllMusicModel.setDbFavById(SongUtil.isPlaySongData.id,SongsConstant.IS_FAV);
        }
        mView.setIsOrNotFav();
    }

    @Override
    public void clear() {

    }
}
