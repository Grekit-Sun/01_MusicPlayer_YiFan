package cn.weli.mediaplayer.module.mine.module.allMusic.view;

import java.util.List;

import cn.weli.mediaplayer.base.IBaseView;
import cn.weli.mediaplayer.bean.SongData;

public interface IAllMusicView extends IBaseView {

    /**
     * 所有音乐清单
     * @param list
     */
    void initPlayList(List<SongData> list);

    void setData(List<SongData> list);
}
