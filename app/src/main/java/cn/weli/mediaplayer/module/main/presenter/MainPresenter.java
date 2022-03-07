package cn.weli.mediaplayer.module.main.presenter;


import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.module.find.ui.FindFragment;
import cn.weli.mediaplayer.module.main.model.MainModel;
import cn.weli.mediaplayer.module.main.view.IMainView;
import cn.weli.mediaplayer.module.mine.ui.MineFragment;

public class MainPresenter implements IPresenter {

    private IMainView mView;
    private MainModel mMainModel;

    public MainPresenter(IMainView view) {
        mView = view;
        mMainModel = new MainModel();
    }

    /**
     * 添加fragment
     */
    private List<Fragment> addFragment() {
        List<Fragment> list = new ArrayList<>();
        MineFragment mineFragment = new MineFragment();
        list.add(mineFragment);
        list.add(new FindFragment());
        return list;
    }

    /**
     * 添加Fragment
     */
    public void initFragment(){
        mView.setAdapter(addFragment());
    }

    /**
     * 数据库置为停止
     */
    public void stopPlay(){
        mMainModel.stopMusicById();
    }

    @Override
    public void clear() {

    }
}
