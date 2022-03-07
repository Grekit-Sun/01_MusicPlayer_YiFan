package cn.weli.mediaplayer.module.detail.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.base.BaseActivity;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.helper.MediaHelper;
import cn.weli.mediaplayer.manager.CallbackListener;
import cn.weli.mediaplayer.manager.MediaManager;
import cn.weli.mediaplayer.module.detail.presenter.MusicDetailPresenter;
import cn.weli.mediaplayer.module.detail.view.IMusicDetailView;
import cn.weli.mediaplayer.utils.ImageUtil;
import cn.weli.mediaplayer.utils.SongUtil;
import cn.weli.mediaplayer.utils.ThreadPoolUtil;


public class MusicDetailActivity extends BaseActivity<MusicDetailPresenter, IMusicDetailView> implements IMusicDetailView, CallbackListener {
    private ImageView mBackImg;
    private ImageView mDetailImg;
    private Animation animation;
    private TextView mSongName;
    private TextView mSinger;

    private ImageView mFavImg;
    private ImageView mLastImg;
    private ImageView mPlayImg;
    private ImageView mPauseImg;
    private ImageView mNextImg;
    private ProgressBar mProgress;
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
        //初始化
        initMediaManager();
        initData();
        onClickListener();
        //初始化动画
        initAnim();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setData();
    }

    protected void initView() {

        mBackImg = $(R.id.detail_back);
        mDetailImg = $(R.id.detail_img);
        mSongName = $(R.id.detail_song_name);
        mSinger = $(R.id.detail_singer);
        mProgress = $(R.id.detail_progress);

        mFavImg = $(R.id.detail_img_fav);
        mLastImg = $(R.id.detail_lastsong);
        mPlayImg = $(R.id.detail_status_play);
        mPauseImg = $(R.id.detail_status_pause);
        mNextImg = $(R.id.detail_nextsong);
    }

    private void initData() {
        setData();
    }


    /**
     * 更新信息
     */
    private void initMediaManager() {
        mMediaManager = MediaManager.getInstance();
        mMediaManager.registCallbackListener(this);
    }


    /**
     * 动画初始化
     */
    private void initAnim() {
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        LinearInterpolator interpolator = new LinearInterpolator();     //匀速插值器
        animation.setInterpolator(interpolator);
        if (animation != null) {
            mDetailImg.startAnimation(animation);
        }
    }

    /**
     * 点击事件
     */
    private void onClickListener() {
        //返回
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //点击添加至收藏并且红心变实心
        mFavImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.dealFavItemClick();
            }
        });

        mLastImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaManager.last();
            }
        });

        mPlayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaManager.play(SongUtil.isPlaySongData);
            }
        });

        mPauseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaManager.pause();
            }
        });

        mNextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaManager.next();
            }
        });


    }

    /**
     * 设置正在播放音乐的数据
     */
    private void setData() {

        if (mMediaManager.mMediaPlayer.isPlaying()) {
            //状态图片
            mPlayImg.setVisibility(View.GONE);
            mPauseImg.setVisibility(View.VISIBLE);
        } else {
            //状态图片
            mPlayImg.setVisibility(View.VISIBLE);
            mPauseImg.setVisibility(View.GONE);
        }

        mProgress.setMax(SongUtil.isPlaySongData.duration);

        if (SongUtil.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            mFavImg.setImageResource(R.drawable.hasfav);
        } else if (SongUtil.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            mFavImg.setImageResource(R.drawable.wait_fav);
        }
        mSongName.setText(SongUtil.isPlaySongData.songName);
        mSinger.setText(SongUtil.isPlaySongData.singer);
        mDetailImg.setImageBitmap(ImageUtil.makeRoundCorner(MediaHelper.getSongAlbumBitmap(
                 SongUtil.isPlaySongData.songMediaId, SongUtil.isPlaySongData.albumId)));
        //初始化进度线程
        initProgressThread();
    }

    /**
     * 设置红心状态
     */
    @Override
    public void setIsOrNotFav() {
        if (SongUtil.isPlaySongData.isFavorite == SongsConstant.IS_FAV) {
            mFavImg.setImageResource(R.drawable.hasfav);
        } else if (SongUtil.isPlaySongData.isFavorite == SongsConstant.NOT_FAV) {
            mFavImg.setImageResource(R.drawable.wait_fav);
        }
    }

    /**
     * 播放回调
     *
     * @param oldId
     * @param nowId
     */
    @Override
    public void play(int oldId, int nowId) {


        if (animation != null) {
            mDetailImg.startAnimation(animation);
        }

        setData();

    }

    @Override
    public void pause(int id) {

        if (animation != null) {
            mDetailImg.clearAnimation();
        }
        setData();
    }

    @Override
    public void stop(int id) {

        if (animation != null) {
            mDetailImg.clearAnimation();
        }
        setData();
    }

    /**
     * 获取当前音乐精度线程
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayout() {
        return R.layout.music_detail_layout;
    }

    @Override
    protected Class<IMusicDetailView> getViewClass() {
        return IMusicDetailView.class;
    }

    @Override
    protected Class<MusicDetailPresenter> getPresenterClass() {
        return MusicDetailPresenter.class;
    }
}
