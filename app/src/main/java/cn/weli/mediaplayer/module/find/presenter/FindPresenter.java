package cn.weli.mediaplayer.module.find.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

import java.util.List;

import cn.weli.mediaplayer.base.IPresenter;
import cn.weli.mediaplayer.bean.NewData;
import cn.weli.mediaplayer.bean.NewsRes;
import cn.weli.mediaplayer.module.find.view.IFindView;
import cn.weli.mediaplayer.utils.NetUtil;
import cn.weli.mediaplayer.utils.ThreadPoolUtil;

import static cn.weli.mediaplayer.YfApplication.appContext;

public class FindPresenter implements IPresenter {

    private IFindView mView;

    private List<NewData> list;

    public FindPresenter(IFindView view) {
        mView = view;
    }

    /**
     * 获取网络数据
     */
    public void accessData() {
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<NewData> newData = initPartData();
                if (list == null) {
                    setList(newData);
                    return;
                }

                if(newData == null){return;}

                if (list.get(0).thumbnail_pic_s.equals(newData.get(0).thumbnail_pic_s)) {     //新闻相同
                } else {
                    setList(newData);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 加载数据
     */
    public void appendData() {
        ThreadPoolUtil.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                List<NewData> newData = initPartData();
                if (list == null) {
                    return;
                }
                if (list.get(0).thumbnail_pic_s.equals(newData.get(0).thumbnail_pic_s)) {     //新闻相同
                } else {
                    appendList(newData);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 拼接
     * @param slist
     */
    public void appendList(List<NewData> slist) {
        for (NewData nd : slist) {
            list.add(nd);
        }
        mView.appendList(list);
    }

    public void setList(List<NewData> list) {
        this.list = list;
        mView.setAdapter(list);
    }


    /**
     * 获取最新新闻
     *
     * @return
     */
    public List<NewData> initPartData() {
        String URL = "http://toutiao-ali.juheapi.com/toutiao/index";
        NewsRes newsRes = new NewsRes();
        ConnectivityManager cwjManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetWorkInfo = cwjManager.getActiveNetworkInfo();
        if (mNetWorkInfo != null && mNetWorkInfo.isConnected()) {
            //有网
            String resp = NetUtil.get(URL);
            Gson gson = new Gson();
            newsRes = gson.fromJson(resp, NewsRes.class);
            return newsRes.result.data;
        }
        return null;
    }

    @Override
    public void clear() {

    }
}
