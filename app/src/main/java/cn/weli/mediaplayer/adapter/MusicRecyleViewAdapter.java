package cn.weli.mediaplayer.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.bean.SongData;
import cn.weli.mediaplayer.constant.SongsConstant;
import cn.weli.mediaplayer.utils.SongUtil;

import static android.support.constraint.Constraints.TAG;

public class MusicRecyleViewAdapter extends RecyclerView.Adapter {

    private List<SongData> list;
    private int oldId;
    private int initType;

    public MusicRecyleViewAdapter(List<SongData> list, int initType) {
        this.list = list;
        this.initType = initType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        return new MyBaseViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SongData songData = list.get(position);
        if(initType == SongsConstant.TYPE_ALL){
        ((MyBaseViewHolder) holder).mCountMusic.setText(songData.id + "");
        }else {
            ((MyBaseViewHolder) holder).mCountMusic.setText(" ");
        }
        ((MyBaseViewHolder) holder).mSongName.setText(songData.songName);
        ((MyBaseViewHolder) holder).mSongAuthor.setText(songData.singer);
        if (songData.status == SongsConstant.MUSIC_STATUS_PLAY) {     //播放
            isPlayBackground(holder);
            oldId = songData.id;
        } else if (songData.status == SongsConstant.MUSIC_STATUS_PAUSE) {      //暂停
            isPauseBackGround(holder);
            oldId = songData.id;
        } else if (songData.status == SongsConstant.MUSIC_STATUS_STOP) {       //停止
            notPlayBackground(holder);
        }

        ((MyBaseViewHolder) holder).mSongContainer.setOnClickListener(new View.OnClickListener() {  //点击事件
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(songData, position);
                Log.e(TAG, "OnClick: ---------------postion"+position);
            }
        });

        ((MyBaseViewHolder) holder).mSongContainer.setOnLongClickListener(new View.OnLongClickListener() {    //长按事件
            @Override
            public boolean onLongClick(View view) {
                if (songData.isFavorite == SongsConstant.NOT_FAV) {       //未收藏，则长按收藏
                    mOnItemClickListener.onItemLongClick(songData);
                } else {     //已收藏提示
                    mOnItemClickListener.hasFav(songData);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    protected int getLayout() {
        return R.layout.recy_music_layout;
    }

    protected class MyBaseViewHolder extends RecyclerView.ViewHolder {
        protected TextView mCountMusic;
        protected TextView mSongName;
        protected TextView mSongAuthor;
        protected ImageView mIsPlayImg;
        protected LinearLayout mSongContainer;

        protected MyBaseViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        protected void initView(View itemView) {
            mCountMusic = $(R.id.recy_count_music, itemView);
            mSongName = $(R.id.recy_music_name, itemView);
            mSongAuthor = $(R.id.recy_music_author, itemView);
            mIsPlayImg = $(R.id.is_play_img, itemView);
            mSongContainer = $(R.id.rec_recy_container, itemView);
        }
    }

    /**
     * 设置音乐清单
     */
    public void setSongList(List<SongData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    protected <T> T $(int res, View parent) {
        return (T) parent.findViewById(res);
    }

    /**
     * 暂停音乐的背景
     *
     * @param holder
     */
    private void isPauseBackGround(RecyclerView.ViewHolder holder) {
        ((MyBaseViewHolder) holder).mCountMusic.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mSongName.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mSongAuthor.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mIsPlayImg.setImageResource(R.drawable.waitplay);
    }

    /**
     * 播放音乐的背景
     *
     * @param holder
     */
    private void isPlayBackground(RecyclerView.ViewHolder holder) {
        ((MyBaseViewHolder) holder).mCountMusic.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mSongName.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mSongAuthor.setTextColor(Color.RED);
        ((MyBaseViewHolder) holder).mIsPlayImg.setImageResource(R.drawable.isplay);
    }

    /**
     * 待播放音乐的背景
     *
     * @param holder
     */
    private void notPlayBackground(RecyclerView.ViewHolder holder) {
        ((MyBaseViewHolder) holder).mCountMusic.setTextColor(Color.BLACK);
        ((MyBaseViewHolder) holder).mSongName.setTextColor(Color.BLACK);
        ((MyBaseViewHolder) holder).mSongAuthor.setTextColor(Color.BLACK);
        ((MyBaseViewHolder) holder).mIsPlayImg.setImageResource(R.drawable.waitplay);
    }


    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SongData songData, int position);

        void onItemLongClick(SongData songData);

        void hasFav(SongData songData);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
