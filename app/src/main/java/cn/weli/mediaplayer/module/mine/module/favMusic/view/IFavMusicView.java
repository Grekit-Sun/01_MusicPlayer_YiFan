package cn.weli.mediaplayer.module.mine.module.favMusic.view;

import java.util.List;

import cn.weli.mediaplayer.base.IBaseView;
import cn.weli.mediaplayer.bean.SongData;

public interface IFavMusicView extends IBaseView {

    void setAdapter(List<SongData> list);

    void setData(List<SongData> list);
}
