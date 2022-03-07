package cn.weli.mediaplayer.module.find.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adapter.NewsRecyleViewAdapter;
import cn.weli.mediaplayer.base.BaseFragment;
import cn.weli.mediaplayer.bean.NewData;
import cn.weli.mediaplayer.module.find.presenter.FindPresenter;
import cn.weli.mediaplayer.module.find.view.IFindView;

public class FindFragment extends BaseFragment<FindPresenter, IFindView> implements IFindView {

    private View mFragmentView;
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;
    private boolean isFragmentInit = false;
    private NewsRecyleViewAdapter adapter;
    private List<NewData> list;
    private TextView mLoadTxt;
    private LinearLayout mNewsContainer;

    public static final int SET_ADAPTER = 0X123;
    public static final int APPEND_DATA = 0X124;
    public static final int NOT_HAVE_NET = 0X125;
    public static final int OBTAIN_NEWS = 0X126;
    public static final int NOT_OBTAIN_NEWS = 0X127;

    private Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_ADAPTER:
                    adapter = new NewsRecyleViewAdapter(getContext(), list);
                    mRecyclerView.setAdapter(adapter);
                    break;
                case APPEND_DATA:
                    adapter.setList(list);
                    break;
                case NOT_HAVE_NET:
                    mLoadTxt.setText("无网络连接...");
                    break;
                case OBTAIN_NEWS:
                    mLoadTxt.setVisibility(View.GONE);
                    mNewsContainer.setVisibility(View.VISIBLE);
                    break;
                case NOT_OBTAIN_NEWS:
                    mLoadTxt.setText("未获取到新闻...");
                    break;
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.find_layout, container, false);
            ButterKnife.bind(this, mFragmentView);
            initView();
        } else {
            if (mFragmentView.getParent() != null) {
                ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
            }
        }
        return mFragmentView;
    }

    @Override
    protected Class<FindPresenter> getPresenterClass() {
        return FindPresenter.class;
    }

    @Override
    protected Class<IFindView> getViewClass() {
        return IFindView.class;
    }

    private void initView() {

        mLoadTxt = mFragmentView.findViewById(R.id.find_load_txt);
        mRecyclerView = mFragmentView.findViewById(R.id.recy_all_news);
        mRefreshLayout = mFragmentView.findViewById(R.id.refreshLayout);
        mNewsContainer = mFragmentView.findViewById(R.id.news_container);

        //设置recyleView保持固定的大小，这样可以优化RecylerView的性能
        mRecyclerView.setHasFixedSize(true);
        //线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置recyleView滚动方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //为recyleView设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        isFragmentInit = true;
        initListener();
    }

    private void initListener() {
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPresenter.accessData();
                mRefreshLayout.finishRefresh();
            }
        });

        //上啦加载
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.appendData();
                mRefreshLayout.finishLoadmore();
            }
        });

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isFragmentInit) {
            if (isVisibleToUser) {
                mPresenter.accessData();
            }
        }
    }

    @Override
    public void setAdapter(List<NewData> list) {
        if (list == null) {
            sHandler.sendEmptyMessage(NOT_HAVE_NET);
        } else if (list.size() > 0) {
            sHandler.sendEmptyMessage(OBTAIN_NEWS);
        } else {
            sHandler.sendEmptyMessage(NOT_OBTAIN_NEWS);
        }


        this.list = list;
        sHandler.sendEmptyMessage(SET_ADAPTER);
    }

    @Override
    public void appendList(List<NewData> list) {
        this.list = list;
        sHandler.sendEmptyMessage(APPEND_DATA);
    }
}
