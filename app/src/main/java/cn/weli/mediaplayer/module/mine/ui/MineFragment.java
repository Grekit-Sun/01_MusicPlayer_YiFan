package cn.weli.mediaplayer.module.mine.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adapter.MyViewPageAdapter;
import cn.weli.mediaplayer.base.BaseFragment;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.mine.module.allMusic.ui.AllMusicFragment;
import cn.weli.mediaplayer.module.mine.module.favMusic.ui.FavMusicFragment;
import cn.weli.mediaplayer.module.mine.module.recMusic.ui.RecMusicFragment;
import cn.weli.mediaplayer.module.mine.presenter.MinePresenter;
import cn.weli.mediaplayer.module.mine.view.IMineView;
import cn.weli.mediaplayer.utils.SongUtil;

public class MineFragment extends BaseFragment<MinePresenter, IMineView> implements IMineView, CallbackListener {

    private View mFragmentView;
    private AllMusicFragment mAllMusicFragment;
    private FavMusicFragment mFavMusicFragment;
    private RecMusicFragment mRecMusicFragment;
    private ViewPager mViewPager;
    private ImageView mImage;
    private RelativeLayout mAllMusicContainer;
    private RelativeLayout mFavMusicContainer;
    private RelativeLayout mRecMusicContainer;

    private TextView mAllMusicTxt;
    private TextView mFavMusicTxt;
    private TextView mRecMusicTxt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.mine_layout, container, false);
            ButterKnife.bind(this, mFragmentView);
            initView();
            initFragment();
            initListener();
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

        mViewPager = mFragmentView.findViewById(R.id.music_vp);
        mAllMusicContainer = mFragmentView.findViewById(R.id.all_music_container);
        mFavMusicContainer = mFragmentView.findViewById(R.id.favorite_music_container);
        mRecMusicContainer = mFragmentView.findViewById(R.id.rec_music_container);
        mImage = mFragmentView.findViewById(R.id.image_album);
        mAllMusicTxt = mFragmentView.findViewById(R.id.all_music_txt);
        mFavMusicTxt = mFragmentView.findViewById(R.id.fav_music_txt);
        mRecMusicTxt = mFragmentView.findViewById(R.id.rec_music_txt);

        mAllMusicTxt.setTextColor(Color.RED);
        mFavMusicTxt.setTextColor(Color.BLACK);
        mRecMusicTxt.setTextColor(Color.BLACK);
    }

    private void initFragment() {
        mAllMusicFragment = new AllMusicFragment();
        mFavMusicFragment = new FavMusicFragment();
        mRecMusicFragment = new RecMusicFragment();
        List<Fragment> list = new ArrayList<>();

        list.add(mAllMusicFragment);
        list.add(mFavMusicFragment);
        list.add(mRecMusicFragment);

        //构造适配器
        MyViewPageAdapter adapter = new MyViewPageAdapter(getActivity().getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);

        //注册监听
        registerListener();

        //设置照片
        if (MediaManager.getInstance().mMediaPlayer.isPlaying()) {     //正在播放
            mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(SongUtil.isPlaySongData.songMediaId
                    , SongUtil.isPlaySongData.albumId));
        } else if (MediaManager.getInstance().isPause()) {
            mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(SongUtil.isPlaySongData.songMediaId
                    , SongUtil.isPlaySongData.albumId));
        }

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        mAllMusicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        mFavMusicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);

            }
        });
        mRecMusicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);

            }
        });


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mAllMusicTxt.setTextColor(Color.RED);
                    mFavMusicTxt.setTextColor(Color.BLACK);
                    mRecMusicTxt.setTextColor(Color.BLACK);
                } else if (position == 1) {
                    mAllMusicTxt.setTextColor(Color.BLACK);
                    mFavMusicTxt.setTextColor(Color.RED);
                    mRecMusicTxt.setTextColor(Color.BLACK);
                } else if (position == 2) {
                    mAllMusicTxt.setTextColor(Color.BLACK);
                    mFavMusicTxt.setTextColor(Color.BLACK);
                    mRecMusicTxt.setTextColor(Color.RED);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void registerListener() {
        MediaManager.getInstance().registCallbackListener(this);
    }

    @Override
    public void play(int oldId, int nowId) {
        mImage.setImageBitmap(MediaHelper.getSongAlbumBitmap(SongUtil.isPlaySongData.songMediaId, SongUtil.isPlaySongData.albumId));
    }

    @Override
    public void pause(int id) {

    }

    @Override
    public void stop(int id) {

    }

    @Override
    protected Class<MinePresenter> getPresenterClass() {
        return MinePresenter.class;
    }

    @Override
    protected Class<IMineView> getViewClass() {
        return IMineView.class;
    }
}
