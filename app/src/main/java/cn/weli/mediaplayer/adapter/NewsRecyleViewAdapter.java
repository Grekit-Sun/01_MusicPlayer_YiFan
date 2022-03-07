package cn.weli.mediaplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.weli.mediaplayer.R;
import cn.weli.mediaplayer.bean.NewData;
import cn.weli.mediaplayer.module.WebViewActivity;


public class NewsRecyleViewAdapter extends RecyclerView.Adapter {
    RecyclerView.ViewHolder holder;
    private Context context;
    private List<NewData> list;

    public NewsRecyleViewAdapter(Context context, List<NewData> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_glance_layout, parent, false);
        return new MyNewsViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        this.holder = holder;
        final NewData data = list.get(position);
        ((MyNewsViewHolder) holder).mNewsTitleTxt.setText(data.title);
        //如果已经有照片了并且则不请求
        if (data.bitmap != null) {
            ((MyNewsViewHolder) holder).mNewsImg.setImageBitmap(data.bitmap);
        } else {
                Glide.with(context).load(data.thumbnail_pic_s).into(((MyNewsViewHolder) holder).mNewsImg);
        }
        //点击跳入webview
        ((MyNewsViewHolder) holder).mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", data.url);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    public void setList(List<NewData> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyNewsViewHolder extends RecyclerView.ViewHolder {

        public ImageView mNewsImg;
        public TextView mNewsTitleTxt;

        public LinearLayout mContainer;

        public MyNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }
        private void initView(View itemView) {
            mNewsImg = itemView.findViewById(R.id.news_img);
            mNewsTitleTxt = itemView.findViewById(R.id.news_title);
            mContainer = itemView.findViewById(R.id.news_container);
        }


    }

}
