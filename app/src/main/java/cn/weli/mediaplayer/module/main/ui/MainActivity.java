package cn.weli.mediaplayer.module.main.ui;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.adapter.MyViewPageAdapter;
import cn.weli.mediaplayer.base.BaseActivity;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.detail.ui.MusicDetailActivity;
import cn.weli.mediaplayer.module.main.presenter.MainPresenter;
import cn.weli.mediaplayer.module.main.view.IMainView;
import cn.weli.mediaplayer.utils.SongUtil;
import cn.weli.mediaplayer.utils.ThreadPoolUtil;

public class MainActivity extends BaseActivity<MainPresenter, IMainView> implements IMainView, CallbackListener {

    private ViewPager mViewPager;
    private TextView mNameTxt;
    private TextView mAuthor;
    private ImageView mImgIcon;
    private TextView mMyTxt;
    private TextView mFindTxt;
    private ProgressBar mProgress;
    private LinearLayout mEnterDetail;
    private LinearLayout mProgressContainer;
    private LinearLayout mBottomContainer;
    private ImageView mMusicStatusImg;
    private MediaManager mMediaManager;
    private boolean hasInitThread = false;

    private Handler sHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                mProgress.setProgress((Integer) msg.obj);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        setViewPagerChangeListener();
        onClickListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        if (mMediaManager.mMediaPlayer.isPlaying()) {     //正在播放
            setData(SongsConstant.MUSIC_STATUS_PLAY);
        } else if (mMediaManager.isPause()) {
            setData(SongsConstant.MUSIC_STATUS_PAUSE);
        }
        mPresenter.stopPlay();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);

        mNameTxt = $(R.id.name_txt);
        mAuthor = $(R.id.author_txt);
        mImgIcon = $(R.id.icon_img);
        mProgress = $(R.id.progress);
        mMyTxt = $(R.id.my_txt);
        mFindTxt = $(R.id.find_txt);
        mMusicStatusImg = $(R.id.music_status);
        mEnterDetail = $(R.id.container_enterDetail);
        mProgressContainer = $(R.id.progress_container);
        mBottomContainer = $(R.id.bottom_container);
        mViewPager = $(R.id.main_vp);
        mPresenter.initFragment();  //初始化fragment

        mMyTxt.setTextColor(Color.RED);
        mFindTxt.setTextColor(Color.BLACK);

        if (mMediaManager.mMediaPlayer.isPlaying()) {
            mProgressContainer.setVisibility(View.VISIBLE);
            mBottomContainer.setVisibility(View.VISIBLE);
        } else {
            mProgressContainer.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
        }

    }


    /**
     * viewpager的监听
     */
    private void setViewPagerChangeListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {

                    if (mMediaManager.mMediaPlayer.isPlaying()) {
                        mProgressContainer.setVisibility(View.VISIBLE);
                        mBottomContainer.setVisibility(View.VISIBLE);
                    } else {
                        mProgressContainer.setVisibility(View.GONE);
                        mBottomContainer.setVisibility(View.GONE);
                    }

                    mMyTxt.setTextColor(Color.RED);
                    mFindTxt.setTextColor(Color.BLACK);

                } else if (position == 1) {
                    if (mMediaManager.mMediaPlayer.isPlaying()) {
                        mProgressContainer.setVisibility(View.VISIBLE);
                        mBottomContainer.setVisibility(View.VISIBLE);
                    } else {
                        mProgressContainer.setVisibility(View.GONE);
                        mBottomContainer.setVisibility(View.GONE);
                    }

                    mMyTxt.setTextColor(Color.BLACK);
                    mFindTxt.setTextColor(Color.RED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 点击监听
     */
    private void onClickListener() {

        mMyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });

        mFindTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });

        //音乐状态点击事件
        mMusicStatusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SongUtil.isPlaySongData.status == SongsConstant.MUSIC_STATUS_PLAY) {    //正在播放
                    mMediaManager.pause();
                } else if (SongUtil.isPlaySongData.status == SongsConstant.MUSIC_STATUS_PAUSE) {    //暂停
                    mMediaManager.play(SongUtil.isPlaySongData);
                }
            }
        });

        //进入音乐详情
        mEnterDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MusicDetailActivity.class);
                startActivity(intent);  //进入音乐详情
            }
        });
    }

    /**
     * 设置适配器
     *
     * @param list
     */
    @Override
    public void setAdapter(List<Fragment> list) {
        //构造适配器
        MyViewPageAdapter adapter = new MyViewPageAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaHelper.saveIsPlaySongId(SongUtil.isPlaySongData.id);

    }

    @Override
    public void play(int oldId, int nowId) {
        setData(SongsConstant.MUSIC_STATUS_PLAY);
    }

    @Override
    public void pause(int id) {
        setData(SongsConstant.MUSIC_STATUS_PAUSE);
    }

    @Override
    public void stop(int id) {
        setData(SongsConstant.MUSIC_STATUS_STOP);
    }


    /**
     * 设置数据
     */
    public void setData(int status) {

        mProgress.setMax(SongUtil.isPlaySongData.duration);

        mNameTxt.setText(SongUtil.isPlaySongData.songName);
        mAuthor.setText(SongUtil.isPlaySongData.singer);
        mImgIcon.setImageBitmap(MediaHelper.getSongAlbumBitmap(
                SongUtil.isPlaySongData.songMediaId, SongUtil.isPlaySongData.albumId));
        if (status == SongsConstant.MUSIC_STATUS_STOP) {
            mMusicStatusImg.setImageResource(R.drawable.waitplay);
        } else if (status == SongsConstant.MUSIC_STATUS_PLAY) {
            mMusicStatusImg.setImageResource(R.drawable.isplay);
        } else if (status == SongsConstant.MUSIC_STATUS_PAUSE) {
            mMusicStatusImg.setImageResource(R.drawable.waitplay);
        }

        if (mMediaManager.mMediaPlayer.isPlaying() || mMediaManager.isPause()) {
            mProgressContainer.setVisibility(View.VISIBLE);
            mBottomContainer.setVisibility(View.VISIBLE);
        } else {
            mProgressContainer.setVisibility(View.GONE);
            mBottomContainer.setVisibility(View.GONE);
        }

        initProgressThread();
    }

    /**
     * 初始化进度线程
     */
    private void initProgressThread() {
        if (!hasInitThread) {
            ThreadPoolUtil.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        int currentPosition = mMediaManager.mMediaPlayer.getCurrentPosition();
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = 0x123;
                        message.obj = currentPosition;
                        sHandler.sendMessage(message);
                    }
                }
            });
            hasInitThread = true;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected Class<IMainView> getViewClass() {
        return IMainView.class;
    }

    @Override
    protected Class<MainPresenter> getPresenterClass() {
        return MainPresenter.class;
    }

}
