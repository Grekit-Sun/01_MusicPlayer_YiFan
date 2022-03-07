package cn.weli.mediaplayer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.lang.reflect.Constructor;

import cn.weli.mediaplayer.helper.handler.WeakHandler;

public abstract class BaseActivity<T extends IPresenter, K> extends AppCompatActivity {

    private boolean needSetOrientation = true;
    protected T mPresenter;
    protected WeakHandler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定布局
        setContentView(getLayout());
        initPresenter();
        setOrientation();
    }


    protected <T> T $(int res) {
        return (T) findViewById(res);
    }

    protected <T> T $(int res, View parent) {
        return (T) parent.findViewById(res);
    }

    protected abstract int getLayout();

    /**
     * 返回View层的接口类.
     */
    protected abstract Class<K> getViewClass();

    /**
     * 返回逻辑处理的具体类型.
     */
    protected abstract Class<T> getPresenterClass();

    /**
     * 初始化Presenter
     */
    protected void initPresenter() {
        try {
            Constructor constructor = getPresenterClass().getConstructor(getViewClass());
            mPresenter = (T) constructor.newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setOrientation() {
        if (needSetOrientation) {
//            DensityUtil.getInstance().setDefault(this);
        }

    }

    /**
     * 延时处理
     * @param runnable
     * @param delayMillis
     */
    public void handleEventDelay(Runnable runnable, long delayMillis) {
        if (mHandler == null) {
            mHandler = new WeakHandler();
        }
        mHandler.postDelayed(runnable, delayMillis);
    }


    /**
     * 弹出toast.
     *
     * @param toastInfo toast内容
     */
    public void showToast(String toastInfo) {

    }

    /**
     * 关闭当前页面
     */
    public void finishActivity() {
        this.finish();
    }


}
