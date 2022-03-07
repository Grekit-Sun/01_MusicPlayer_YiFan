package cn.weli.mediaplayer.module.splash.presenter;

import android.content.Context;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.db.DbCommand;
import cn.weli.mediaplayer.module.splash.model.SplashModel;
import cn.weli.mediaplayer.module.splash.view.ISplashView;

public class SplashPresenter implements IPresenter {

    private ISplashView mView;
    private SplashModel mSplashModel;


    public SplashPresenter(ISplashView view) {
        mView = view;
        mSplashModel = new SplashModel();
        mView.startToMainNow();
    }


    /**
     * 获取本地音乐并且插入(异步)
     */
    public void initMusic(final Context context) {

        new DbCommand<Void>() {
            @Override
            protected Void doInBackground() {
                mSplashModel.insertAllMusic();       //获取本地音乐并且插入数据库
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
            }
        }.execute();

    }

    @Override
    public void clear() {

    }
}
