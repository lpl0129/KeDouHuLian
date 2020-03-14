package com.stay.toolslibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stay.basiclib.R;
import com.stay.toolslibrary.library.nestfulllistview.NestFullGridView;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder;
import com.stay.toolslibrary.utils.extension.Glide_ExtensionKt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyuru on 2016/10/12.
 */

public class UploadImageLayout extends LinearLayout {


    private NestFullGridView grideForScrollView;

    private List<String> imageList = new ArrayList<>();


    /**
     * 如果多个上传控件同时存在.区别位置
     */
    private int parentPosition;

    private ImageAdapter adapter;


    /**
     * 限制上传数量
     */
    private int limitSize = 6;

    private onUploadManager uploadManager;


    public UploadImageLayout(Context context) {
        this(context, null, 0);
    }

    public UploadImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.upload_image_layout, this, true);
        grideForScrollView = (NestFullGridView) findViewById(R.id.gridview);
    }


    public class ImageAdapter extends NestFullViewAdapter<String> {


        public ImageAdapter(List<String> mDatas) {
            super(R.layout.upload_grid_list_item, mDatas);
        }


        @Override
        public void onBind(final int position, final String item, NestFullViewHolder holder) {
            final ImageView icon_delete = holder.getView(R.id.icon_delete);
            final ImageView icon = holder.getView(R.id.icon);

            if ("add".equals(item)) {


                //设置为上传图片时,用glide加载,否定,size=size-1时,赋值不成功
                Glide_ExtensionKt.loadImage(icon, item, R.drawable.toolslib_upload_image_icon);
                icon_delete.setVisibility(View.INVISIBLE);
            } else {//显示上传操作按钮
                Glide_ExtensionKt.loadImage(icon, item, R.drawable.default_image_360_360);
                icon_delete.setVisibility(View.VISIBLE);
            }
            icon_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uploadManager != null) {
                        imageList.remove(position);
                        if (!imageList.contains("add")) {
                            imageList.add("add");
                        }
                        grideForScrollView.updateUI();
                        uploadManager.onDelete(parentPosition, position);
                    }
                }
            });

            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uploadManager != null && "add".equals(item)) {
                        uploadManager.onSelect(parentPosition, limitSize - (imageList.size() - 1));
                    }
                }
            });
        }

    }

    public void setImageList(List<String> imageList) {
        if (imageList != null) {
            this.imageList.clear();
            this.imageList.addAll(imageList);
        }
        initAdapter();
    }

    public List<String> getImageList() {

        List<String> list = new ArrayList<>();
        for (String s : imageList) {
            if (!"add".equals(s)) {
                list.add(s);
            }
        }
        return list;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    public void addImageList(List<String> list) {
        if (imageList.contains("add")) {
            //移除出占位图
            imageList.remove("add");
        }
        if (list != null) {
            imageList.addAll(list);
        }
        initAdapter();
    }

    private void initAdapter() {
        if (imageList.size() < limitSize) {
            imageList.add("add");
        }
        if (adapter == null) {
            adapter = new ImageAdapter(imageList);
            grideForScrollView.setAdapter(adapter);
        } else {
            grideForScrollView.updateUI();
        }


    }

    public onUploadManager getUploadMange() {
        return uploadManager;
    }

    public void setUploadManager(onUploadManager uploadMange) {
        this.uploadManager = uploadMange;
    }

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public interface onUploadManager {

        void onDelete(int parentPos, int childrenPos);

        void onSelect(int parentPos, int limitsize);
    }
}
