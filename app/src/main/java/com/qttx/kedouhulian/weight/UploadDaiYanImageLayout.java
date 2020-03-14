package com.qttx.kedouhulian.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.bean.ImageBean;
import com.stay.toolslibrary.library.nestfulllistview.NestFullGridView;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter;
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder;
import com.stay.toolslibrary.utils.EmptyUtils;
import com.stay.toolslibrary.utils.extension.Glide_ExtensionKt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyuru on 2018/9/19.
 * <p>
 * 单项图片上传
 */

public class UploadDaiYanImageLayout extends LinearLayout {


    private NestFullGridView grideForScrollView;

    private ArrayList<ImageBean> imageList = new ArrayList<>();

    /**
     * 如果多个上传控件同时存在.区别位置
     */
    private int parentPosition;

    private ImageAdapter adapter;


    /**
     * 限制上传数量
     */
    private int limitSize = 9;

    private onUploadManager uploadManager;


    public UploadDaiYanImageLayout(Context context) {
        this(context, null, 0);
    }

    public UploadDaiYanImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadDaiYanImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.upload_image_layout, this, true);
        grideForScrollView = (NestFullGridView) findViewById(R.id.gridview);
    }


    public class ImageAdapter extends NestFullViewAdapter<ImageBean> {


        public ImageAdapter(List<ImageBean> mDatas) {
            super(R.layout.upload_grid_list_item, mDatas);
        }

        @Override
        public void onBind(final int position, final ImageBean item, NestFullViewHolder holder) {
            final ImageView icon_delete = holder.getView(R.id.icon_delete);
            final ImageView icon = holder.getView(R.id.icon);


            if (item.isAdd()) {
                //设置为上传图片时,用glide加载,否定,size=size-1时,赋值不成功
                Glide_ExtensionKt.loadImage(icon, "", R.drawable.toolslib_upload_image_icon);
                icon_delete.setVisibility(View.INVISIBLE);
            } else {//显示上传操作按钮
                Glide_ExtensionKt.loadImage(icon, item.getShowImage(), R.drawable.default_image_360_360);
                icon_delete.setVisibility(View.VISIBLE);
            }
            icon_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uploadManager != null) {
                        //可以加入删除的服务器图片元素的记录
                        imageList.remove(position);
                        grideForScrollView.updateUI();
                        uploadManager.onDelete(parentPosition, position);
                    }
                }
            });

            icon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uploadManager != null && item.isAdd()) {
                        uploadManager.onSelect(parentPosition, limitSize - (imageList.size() - 1));
                    }
                }
            });
        }

    }

    public void setImageList(List<String> pathlist) {
        setImageList("", pathlist);
    }

    public void setImageList(String httpurl, List<String> pathlist) {
        List<ImageBean> list = new ArrayList<>();
        if (EmptyUtils.Companion.isNotEmpty(pathlist)) {
            for (String s : pathlist) {
                ImageBean bean = new ImageBean();
                bean.setImg(s);
                bean.setFromService(true);
                bean.setShowImage(httpurl + s);
                list.add(bean);
            }
        }
        this.imageList.clear();
        this.imageList.addAll(list);
        initAdapter();
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    /**
     * 添加本地图片
     */
    public void addLocalImage(List<String> pathlist) {

        if (EmptyUtils.Companion.isNotEmpty(pathlist)) {
            /**
             * 移除掉占位图
             */
            imageList.remove(imageList.size() - 1);
            for (String s : pathlist) {
                ImageBean bean = new ImageBean();
                bean.setLocal(s);
                bean.setShowImage(s);
                bean.setFromService(false);
                imageList.add(bean);
            }
            initAdapter();
        }

    }

    /**
     * 初始化导入原有的图片
     */
    private void initAdapter() {
        if (imageList.size() < limitSize) {
            ImageBean bean = new ImageBean();
            bean.setAdd(true);
            imageList.add(bean);
        }
        adapter = null;
        adapter = new ImageAdapter(imageList);
        grideForScrollView.setAdapter(adapter);

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

    /**
     * 外部获取数据时剔除掉ADD占位图
     *
     * @return
     */
    public ArrayList<ImageBean> getImageList() {
        if (EmptyUtils.Companion.isNotEmpty(imageList)) {
            ArrayList<ImageBean> list = new ArrayList();
            for (ImageBean bean : imageList) {
                if (!bean.isAdd()) {
                    list.add(bean);
                }
            }

            return list;
        } else {
            return imageList;
        }
    }


    public interface onUploadManager {

        void onDelete(int parentPos, int childrenPos);

        void onSelect(int parentPos, int limitsize);
    }
}
