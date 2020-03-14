package com.stay.toolslibrary.library.picture;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.stay.basiclib.R;

import java.util.ArrayList;
import java.util.List;


public class ImageBrowserAdapter extends PagerAdapter {

    private List<String> mPhotos = new ArrayList<String>();

    private Context context;

    private LayoutInflater inflater;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    public ImageBrowserAdapter(List<String> photos, Context context) {
        if (photos != null) {
            mPhotos = photos;
        }
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // if (mPhotos.size() > 1) {
        // return Integer.MAX_VALUE;
        // }
        return mPhotos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {


        View imageLayout = inflater.inflate(R.layout.image_brose_item, container, false);
        assert imageLayout != null;

        final PhotoView photoView = (PhotoView) imageLayout.findViewById(R.id.image_brose_item_iv);


        photoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                ((Activity) context).finish();
            }
        });
        final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.image_brose_item_loading);
        spinner.setVisibility(View.VISIBLE);
        RequestOptions options = new RequestOptions();

//        GlideUtils.loadImage(photoView,mPhotos.get(position));

        String url=mPhotos.get(position);
        if (!TextUtils.isEmpty(baseUrl))
        {
            url=baseUrl+url;
        }
        Glide.with(context)
                .load(url)
                .apply(options.fitCenter().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        spinner.setVisibility(View.GONE);
                        photoView.setImageResource(R.drawable.default_image_360_360);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        spinner.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);
        container.addView(imageLayout, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        return imageLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
