package cn.weli.mediaplayer.module.mine.module.recMusic.view;

import java.util.List;

import cn.weli.mediaplayer.base.IBaseView;
import cn.weli.mediaplayer.bean.SongData;

public interface IRecMusicView extends IBaseView {

    void setAdapter(List<SongData> list);

    void setData(List<SongData> list);
}
