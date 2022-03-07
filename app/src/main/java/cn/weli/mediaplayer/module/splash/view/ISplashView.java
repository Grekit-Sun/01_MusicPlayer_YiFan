package cn.weli.mediaplayer.module.splash.view;

import cn.weli.mediaplayer.base.IBaseView;

public interface ISplashView extends IBaseView {

    /**
     * 跳转主页的最大延迟
     */
    int FLAG_MAIN_DELAY = 2000;

    /**
     * 即时跳转主页
     */
    void startToMainNow();
}
