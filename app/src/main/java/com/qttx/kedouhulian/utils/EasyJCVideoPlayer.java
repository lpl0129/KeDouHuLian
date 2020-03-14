package com.qttx.kedouhulian.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.qttx.kedouhulian.R;

import cn.jzvd.JzvdStd;


public class EasyJCVideoPlayer extends JzvdStd {

    public LinearLayout vide_bottom_ll;
    public TextView post_video_play_time;
    public TextView post_video_play_size;

    public EasyJCVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyJCVideoPlayer(Context context) {
        super(context);
    }

    @Override
    public void init(Context context) {
        widthRatio = 16;
        heightRatio = 9;
        super.init(context);
        vide_bottom_ll = (LinearLayout) findViewById(R.id.vide_bottom_ll);
        post_video_play_time = (TextView) findViewById(R.id.post_video_play_time);
        post_video_play_size = (TextView) findViewById(R.id.post_video_play_size);
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        vide_bottom_ll.setVisibility(View.GONE);
        if (listener!=null)
        {
            listener.onPaly();
        }
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        vide_bottom_ll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateError() {
        super.onStateError();
        vide_bottom_ll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        vide_bottom_ll.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_player_wight_layout;
    }


    public void setListener(onPlayListener listener) {
        this.listener = listener;
    }

    private onPlayListener listener;
    public  interface onPlayListener
    {
        public  void onPaly();
    }
}
