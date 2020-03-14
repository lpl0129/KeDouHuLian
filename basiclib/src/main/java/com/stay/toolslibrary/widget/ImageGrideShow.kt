package com.stay.toolslibrary.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout

import com.stay.basiclib.R
import com.stay.toolslibrary.library.nestfulllistview.NestFullGridView
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder
import com.stay.toolslibrary.library.picture.ImageBrowserActivity
import com.stay.toolslibrary.utils.EmptyUtils
import com.stay.toolslibrary.utils.extension.loadRountCenterImage

import java.util.ArrayList

/**
 * Created by huangyuru on 2016/12/16.
 */

class ImageGrideShow @JvmOverloads constructor(private val contxt: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(contxt, attrs, defStyleAttr) {

    private val grideForScrollView: NestFullGridView
    private var adapter: ImageGrideShow.ImageItemAdapter? = null
    private val imageList = ArrayList<String>()

    private var id: String? = null
    private var position: Int = 0

    private var listener: OnTouchBlankPositionListener? = null

    fun setId(id: String, position: Int) {
        this.position = position
        this.id = id
    }

    init {
        val inflater = contxt
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.common_weight_imagegride, this, true)
        grideForScrollView = findViewById<View>(R.id.gride_listview) as NestFullGridView

    }


    /**
     * @param list 数据集合
     */
    fun setImageList(list: List<String>?) {
        if (EmptyUtils.isEmpty(list)) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
        imageList.clear()
        if (list != null) {
            imageList.addAll(list)
        }
        if (adapter == null) {
            adapter = ImageItemAdapter(imageList)
            grideForScrollView.setAdapter(adapter)
        } else {
            grideForScrollView.updateUI()
        }
        grideForScrollView.setOnClickListener { listener?.onTouchBlank() }
        grideForScrollView.setOnItemClickListener(object : NestFullGridView.OnGirdItemClickListener {
            override fun onItemClick(parent: NestFullGridView, view: View, position: Int) {
                ImageBrowserActivity.showActivity(contxt, imageList, position)
            }

            override fun onLongItemClick(parent: NestFullGridView, view: View, position: Int) {}
        })
    }

    fun setListener(listener: OnTouchBlankPositionListener) {
        this.listener = listener
    }

    interface OnTouchBlankPositionListener {
        fun onTouchBlank()
    }

    inner class ImageItemAdapter(mDatas: List<String>) : NestFullViewAdapter<String>(R.layout.common_list_item_weight_imagegride, mDatas) {

        override fun onBind(pos: Int, s: String, holder: NestFullViewHolder) {
            val view: ImageView = holder.getView(R.id.iv_photo)
            view.loadRountCenterImage(s)
        }
    }
}
