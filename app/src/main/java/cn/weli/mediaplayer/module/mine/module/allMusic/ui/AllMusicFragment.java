package cn.weli.mediaplayer.module.mine.module.allMusic.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adapter.MusicRecyleViewAdapter;
import cn.weli.mediaplayer.base.BaseFragment;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.module.mine.module.allMusic.presenter.AllMusicPresenter;
import cn.weli.mediaplayer.module.mine.module.allMusic.view.IAllMusicView;

public class AllMusicFragment extends BaseFragment<AllMusicPresenter, IAllMusicView> implements IAllMusicView {

    private View mFragmentView;
    private RecyclerView mRecyclerView;
    private MusicRecyleViewAdapter adapter;
    private boolean isFragmentInit = false;
    private TextView mLoadTxt;

    @Override
    protected Class<AllMusicPresenter> getPresenterClass() {
        return AllMusicPresenter.class;
    }

    @Override
    protected Class<IAllMusicView> getViewClass() {
        return IAllMusicView.class;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.music_layout, container, false);
            ButterKnife.bind(this, mFragmentView);
            initView();
        } else {
            if (mFragmentView.getParent() != null) {
                ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
            }
        }
        return mFragmentView;
    }

    /**
     * 初始化
     */
    private void initView() {

        mRecyclerView = mFragmentView.findViewById(R.id.recy_music_list);
        mLoadTxt = mFragmentView.findViewById(R.id.load_txt);
        //设置recyleView保持固定的大小，这样可以优化RecylerView的性能
        mRecyclerView.setHasFixedSize(true);
        //线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置recyleView滚动方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        //为recyleView设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter.obtainAllSongs();

        isFragmentInit = true;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {    //用户可见
//            if (isFragmentInit) {
//                mPresenter.obtainAllSongs();
//            }
        }
    }

    /**
     * 所有音乐清单，并设置设配器
     *
     * @param list
     */
    @Override
    public void initPlayList(List<SongData> list) {

        if(list.size() > 0){
            mLoadTxt.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else {
            mLoadTxt.setText("暂无本地音乐...");
        }

        adapter = new MusicRecyleViewAdapter(list, SongsConstant.TYPE_ALL);
        mRecyclerView.setAdapter(adapter);

        //点击监听
        adapter.setOnItemClickListener(new MusicRecyleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SongData songData, int position) {
                mPresenter.dealItemClick(songData, position);
            }

            @Override
            public void onItemLongClick(SongData songData) {
                weatherBeFav(songData);
            }

            @Override
            public void hasFav(SongData songData) {
                showToast(songData.songName + " 已经收藏！");
            }
        });

    }

    @Override
    public void setData(List<SongData> list) {
        adapter.setSongList(list);
    }

    /**
     * 是否收藏
     */
    public void weatherBeFav(final SongData songData) {
        //弹出dialog提示是否添加为收藏
        //1. 创建AlertDialog.Builder 对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                //2.调用AlertDialog.Builder setTitle或setCustomTitle， setIcon
                .setTitle("是否将《" + songData.songName + "》" + "添加为收藏？")
                .setIcon(R.drawable.icon_music)
                //设置确定按钮
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.dealItemLongClick(songData);
                    }
                });

        //设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
}
