package cn.weli.mediaplayer.module.find.view;

import java.util.List;

import cn.weli.mediaplayer.base.IBaseView;
import cn.weli.mediaplayer.bean.NewData;

public interface IFindView extends IBaseView {

    void setAdapter(List<NewData> list);

    void appendList(List<NewData> list);

}
