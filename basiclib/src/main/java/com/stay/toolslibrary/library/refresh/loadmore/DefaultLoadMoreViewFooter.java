/*
Copyright 2015 Chanven

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.stay.toolslibrary.library.refresh.loadmore;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.stay.basiclib.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * default load more view
 */
public class DefaultLoadMoreViewFooter implements ILoadMoreViewFactory {

    private LoadMoreHelper loadMoreView=null;

    @Override
    public ILoadMoreView madeLoadMoreView() {
        loadMoreView = new LoadMoreHelper();
        return loadMoreView;
    }
    public ILoadMoreView getILoadMoreView() {
        return loadMoreView;
    }
    private class LoadMoreHelper implements ILoadMoreView {

        protected View footerView;
        protected TextView footerTv;
        protected AVLoadingIndicatorView footerBar;

        protected OnClickListener onClickRefreshListener;

        @Override
        public void init(FootViewAdder footViewHolder, OnClickListener onClickRefreshListener) {
            footerView = footViewHolder.addFootView(R.layout.base_loadmore_default_footer);
            footerTv = (TextView) footerView.findViewById(R.id.loadmore_default_footer_tv);
            footerBar = (AVLoadingIndicatorView) footerView.findViewById(R.id.loadmore_default_footer_progressbar);
            this.onClickRefreshListener = onClickRefreshListener;
            showNormal();
        }

        @Override
        public void showNormal() {
            setFooterVisibility(true);
            footerTv.setText("点击加载更多");
            footerTv.setVisibility(View.VISIBLE);
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(onClickRefreshListener);

        }

        @Override
        public void showLoading() {
            setFooterVisibility(true);
            footerTv.setText("正在加载中...");
            footerTv.setVisibility(View.GONE);
            footerBar.setVisibility(View.VISIBLE);
            footerView.setOnClickListener(null);
        }

        @Override
        public void showFail(Exception exception) {
            setFooterVisibility(true);
            footerTv.setText("加载失败，点击重试");
            footerTv.setVisibility(View.VISIBLE);
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(onClickRefreshListener);
        }

        @Override
        public void showNomore() {
            setFooterVisibility(true);
            footerTv.setText("没有更多了");
            footerTv.setVisibility(View.VISIBLE);
            footerBar.setVisibility(View.GONE);
            footerView.setOnClickListener(null);
        }

        @Override
        public void setFooterVisibility(boolean isVisible) {
            footerView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

        @Override
        public AVLoadingIndicatorView getProgess() {
            return footerBar;
        }
    }

}
