package cn.weli.mediaplayer.module.mine.presenter;

import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.module.find.ui.FindFragment;
import cn.weli.mediaplayer.module.mine.module.allMusic.ui.AllMusicFragment;
import cn.weli.mediaplayer.module.mine.module.favMusic.ui.FavMusicFragment;
import cn.weli.mediaplayer.module.mine.module.recMusic.ui.RecMusicFragment;
import cn.weli.mediaplayer.module.mine.ui.MineFragment;
import cn.weli.mediaplayer.module.mine.view.IMineView;

public class MinePresenter implements IPresenter {

    private IMineView mView;

    public MinePresenter(IMineView view){
        mView = view;
    }

//    /**
//     * 初始化
//     */
//    public void initFragment() {
//        AllMusicFragment allMusicFragment = new AllMusicFragment();
//        FavMusicFragment favMusicFragment = new FavMusicFragment();
//        RecMusicFragment recMusicFragment = new RecMusicFragment();
//        List<Fragment> list = new ArrayList<>();
//
//        list.add(allMusicFragment);
//        list.add(favMusicFragment);
//        list.add(recMusicFragment);
//
//        mView.setAdapter(list);
//    }

    /**
     * 添加fragment
     */
//    private List<Fragment> addFragment() {
//
//        return list;
//    }

    @Override
    public void clear() {

    }

//    private OnItemCliclistenner mOnItemCliclistenner;
//    public interface OnItemCliclistenner{
//        void onClickItem();
//    }
//    public void setOnItemCliclistenner(OnItemCliclistenner mOnItemCliclistenner){
//        this.mOnItemCliclistenner = mOnItemCliclistenner;
//    }
}
