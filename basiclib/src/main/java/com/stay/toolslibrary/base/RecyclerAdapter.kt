package com.stay.toolslibrary.base

import android.animation.Animator
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.IntDef
import android.support.annotation.IntRange
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout

import com.stay.basiclib.R
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.ALPHAIN
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.EMPTY_VIEW
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.FOOTER_VIEW
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.HEADER_VIEW
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.LOAD_MORE_VIEW
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.SCALEIN
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.SLIDEIN_BOTTOM
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.SLIDEIN_LEFT
import com.stay.toolslibrary.base.RecyclerAdapter.Companion.SLIDEIN_RIGHT
import com.stay.toolslibrary.widget.animation.animation.*
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * RecyView 公共适配器
 * Created by huangyuru on 2016/9/12.
 */
abstract class RecyclerAdapter<T>(var mItems: MutableList<T>) : RecyclerView.Adapter<RecyclerViewHolder>() {

    companion object {
        const val HEADER_VIEW = 0x00000111
        const val LOAD_MORE_VIEW = 0x00000222
        const val FOOTER_VIEW = 0x00000333
        //空数据时的有两种显示区别,错误和空数据
        const val EMPTY_VIEW = 0x00000444
        const val ERROR_VIEW = 0x00000555
        const val INIT_VIEW = 0x00000666
        //Animation
        /**
         * Use with [.openLoadAnimation]
         */
        const val ALPHAIN = 0x00000001
        /**
         * Use with [.openLoadAnimation]
         */
        const val SCALEIN = 0x00000002
        /**
         * Use with [.openLoadAnimation]
         */
        const val SLIDEIN_BOTTOM = 0x00000003
        /**
         * Use with [.openLoadAnimation]
         */
        const val SLIDEIN_LEFT = 0x00000004
        /**
         * Use with [.openLoadAnimation]
         */
        const val SLIDEIN_RIGHT = 0x00000005
    }


    protected lateinit var mLayoutInflater: LayoutInflater

    protected lateinit var mContext: Context

    /**
     * 当数据为空时,
     * isError=true,显示ERROR_VIEW
     * isError=false,显示EMPTY_VIEW
     */
    public var isError: Boolean = false

    /**
     * 空数据时是否允许空界面出现header
     * false:覆盖header.true 不覆盖header
     */
    private var mHeadAndEmptyEnable: Boolean = false

    /**
     * 空数据时是否允许空界面出现footer
     */
    private var mFootAndEmptyEnable: Boolean = false

    private var mEmptyView: View? = null
    private var mErrorView: View? = null

    private var mInitView: View? = null

    private var mFooterLayout: LinearLayout? = null
    private var mHeaderLayout: LinearLayout? = null
    private var mLoadMoreView: View? = null


    @IntDef(ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT)
    annotation class AnimationType

    private var mFirstOnlyEnable = true
    private var mOpenAnimationEnable = false
    private val mInterpolator = LinearInterpolator()
    private var mDuration = 300
    private var mLastPosition = -1

    private var mCustomAnimation: BaseAnimation? = null
    private var mSelectAnimation: BaseAnimation = AlphaInAnimation()

    /**
     * * Get the data of list
     */
    fun getItems(): MutableList<T> {
        return mItems
    }

    fun getItemSize(): Int {
        return mItems.size
    }

    fun getItem(@IntRange(from = 0) position: Int): T? {
        return if (position >= 0 && position < mItems.size)
            mItems[position]
        else
            null
    }

    /**
     * add one new data in to certain location
     *
     * @param position
     */
    fun addItem(@IntRange(from = 0) position: Int, data: T) {
        mItems.add(position, data)
        if (getItemSize() == 1) {
            notifyDataSetChanged()
        } else {
            notifyItemInserted(position + getHeaderLayoutCount())
        }
    }

    /**
     * add one new data
     */
    fun addItem(data: T) {
        mItems.add(data)
        notifyItemInserted(getItemSize() + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /**
     * remove the item associated with the specified position of adapter
     *
     * @param position
     */
    fun remove(@IntRange(from = 0) position: Int) {
        mItems.removeAt(position)
        val internalPosition = position + getHeaderLayoutCount()
        notifyItemRemoved(internalPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(internalPosition, mItems.size - internalPosition)
    }

    /**
     * change data
     */
    fun setData(@IntRange(from = 0) index: Int, data: T) {
        mItems.set(index, data)
        notifyItemChanged(index + getHeaderLayoutCount())
    }

    /**
     * add new data in to certain location
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    fun addData(@IntRange(from = 0) position: Int, newData: Collection<T>) {
        mItems.addAll(position, newData)
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * add new data to the end of mData
     *
     * @param newData the new data collection
     */
    fun addData(newData: Collection<T>) {
        mItems.addAll(newData)
        notifyItemRangeInserted(mItems.size - newData.size + getHeaderLayoutCount(), newData.size)
        compatibilityDataSizeChanged(newData.size)
    }

    /**
     * use data to replace all item in mData. this method is different [.setNewData],
     * it doesn't change the mData reference
     *
     * @param data data collection
     */
    fun replaceData(data: Collection<T>) {
        // 不是同一个引用才清空列表
        if (data !== mItems) {
            mItems.clear()
            mItems.addAll(data)
        }
        notifyDataSetChanged()
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    private fun compatibilityDataSizeChanged(size: Int) {
        val dataSize = getItemSize()
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    /**
     * if show empty view will be return 1 or not will be return 0
     *
     * @return
     */
    fun isEmpty(): Boolean {
        return mItems.isEmpty()
    }

    /**
     * 重写该方法，根据viewType设置item的layout
     *
     * @param viewType 通过重写getItemViewType（）设置，默认item是0
     * @return
     */
    protected abstract fun getLayoutIdByType(viewType: Int): Int

    /**
     * 重写该方法进行item数据项视图的数据绑定
     *
     * @param holder   通过holder获得item中的子View，进行数据绑定
     * @param position 该item的position
     * @param item     映射到该item的数据
     */
    protected abstract fun RecyclerViewHolder.bindData(item: T, position: Int)


    open fun initEmptyView(parent: ViewGroup): View {
        return mLayoutInflater.inflate(setEmptyView(), parent, false)
    }

    open fun initErroryView(parent: ViewGroup): View {
        return mLayoutInflater.inflate(setErrorView(), parent, false)
    }

    open fun initInitView(parent: ViewGroup): View {
        return mLayoutInflater.inflate(R.layout.base_include_view_init, parent, false)
    }

    /***
     * 重写可以控制空数据界面的值
     *
     * @param holder
     */
    open protected fun bindEmptyData(holder: RecyclerViewHolder) {

    }

    open fun bindHeaderData(holder: RecyclerViewHolder) {
        mListener?.mbindHeaderDataAction?.let {
            it(holder)
        }
    }

    open fun bindFooterData(holder: RecyclerViewHolder) {

    }

    open protected fun bindErrorData(holder: RecyclerViewHolder) {
        bindErrorListener?.mOnBindErrorAction?.let {
            it(holder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        mLayoutInflater = LayoutInflater.from(mContext)

        val holder: RecyclerViewHolder
        when (viewType) {
            HEADER_VIEW -> holder = RecyclerViewHolder(mHeaderLayout!!)
            FOOTER_VIEW -> holder = RecyclerViewHolder(mFooterLayout!!)
            ERROR_VIEW -> {
                if (mErrorView == null) {
                    mErrorView = initErroryView(parent)
                }
                holder = RecyclerViewHolder(mErrorView!!)
            }
            INIT_VIEW -> {
                if (mInitView == null) {
                    mInitView = initInitView(parent)
                }
                holder = RecyclerViewHolder(mInitView!!)
            }
            EMPTY_VIEW -> {
                if (mEmptyView == null) {
                    mEmptyView = initEmptyView(parent);
                }
                holder = RecyclerViewHolder(mEmptyView!!)
            }
            LOAD_MORE_VIEW ->
                holder = RecyclerViewHolder(mLoadMoreView!!)

            else -> holder = RecyclerViewHolder(
                    mLayoutInflater.inflate(getLayoutIdByType(viewType), parent, false))
        }

        return holder

    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val viewType = holder.itemViewType
        when (viewType) {
            HEADER_VIEW -> bindHeaderData(holder)
            FOOTER_VIEW -> bindFooterData(holder)
            LOAD_MORE_VIEW -> {

            }
            EMPTY_VIEW -> bindEmptyData(holder)
            ERROR_VIEW -> bindErrorData(holder)
            INIT_VIEW -> {

            }
            else -> {
                val realposition = position - getHeaderLayoutCount()
                val item = mItems[realposition]
                mListener?.let {
                    it.mOnItemClickAction?.let { action ->
                        holder.itemView.setOnClickListener { view ->
                            action(item, realposition, view)
                        }
                    }
                    it.mOnItemLongClickAction?.let { action ->
                        holder.itemView.setOnLongClickListener { view ->
                            action(item, realposition, view)
                            true
                        }
                    }
                    Unit
                }
                holder.bindData(item, realposition)
            }
        }
    }

    override fun getItemCount(): Int {
        var count: Int
        if (isEmpty()) {
            count = 1
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++
            }
            if (mFootAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++
            }
        } else {
            count = getHeaderLayoutCount() + mItems.size + getFooterLayoutCount() + getLoadMoreViewCount()
        }
        return count
    }


    override fun getItemViewType(position: Int): Int {
        if (isEmpty()) {
            val header = mHeadAndEmptyEnable && getHeaderLayoutCount() != 0
            when (position) {
                0 -> return if (header) {
                    HEADER_VIEW
                } else {
                    getEmptyViewType()
                }
                1 -> return if (header) {
                    getEmptyViewType()
                } else {
                    FOOTER_VIEW
                }
                2 -> return FOOTER_VIEW
                else -> return getEmptyViewType()
            }
        }
        val numHeaders = getHeaderLayoutCount()
        if (position < numHeaders) {
            return HEADER_VIEW
        } else {
            var adjPosition = position - numHeaders
            val adapterCount = mItems.size
            return if (adjPosition < adapterCount) {
                getDefItemViewType(adjPosition)
            } else {
                adjPosition -= adapterCount
                val numFooters = getFooterLayoutCount()
                if (adjPosition < numFooters) {
                    FOOTER_VIEW
                } else {
                    LOAD_MORE_VIEW
                }
            }
        }
    }


    protected fun getDefItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerViewHolder) {
        super.onViewAttachedToWindow(holder)
        val type = holder.getItemViewType()
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }


    protected fun setFullSpan(holder: RecyclerView.ViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params = holder
                    .itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private fun addAnimation(holder: RecyclerView.ViewHolder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || holder.layoutPosition > mLastPosition) {
                var animation: BaseAnimation? = null
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation
                } else {
                    animation = mSelectAnimation
                }
                for (anim in animation!!.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.layoutPosition)
                }
                mLastPosition = holder.layoutPosition
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected fun startAnim(anim: Animator, index: Int) {
        anim.setDuration(mDuration.toLong()).start()
        anim.interpolator = mInterpolator
    }


    /**
     * Set the view animation type.
     *
     * @param animationType One of [.ALPHAIN], [.SCALEIN], [.SLIDEIN_BOTTOM],
     * [.SLIDEIN_LEFT], [.SLIDEIN_RIGHT].
     */
    fun openLoadAnimation(@RecyclerAdapter.AnimationType animationType: Int) {
        this.mOpenAnimationEnable = true
        mCustomAnimation = null
        when (animationType) {
            ALPHAIN -> mSelectAnimation = AlphaInAnimation()
            SCALEIN -> mSelectAnimation = ScaleInAnimation()
            SLIDEIN_BOTTOM -> mSelectAnimation = SlideInBottomAnimation()
            SLIDEIN_LEFT -> mSelectAnimation = SlideInLeftAnimation()
            SLIDEIN_RIGHT -> mSelectAnimation = SlideInRightAnimation()
            else -> {
            }
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param animation ObjectAnimator
     */
    fun openLoadAnimation(animation: BaseAnimation) {
        this.mOpenAnimationEnable = true
        this.mCustomAnimation = animation
    }

    /**
     * To open the animation when loading
     */
    fun openLoadAnimation() {
        this.mOpenAnimationEnable = true
    }

    /**
     * To close the animation when loading
     */
    fun closeLoadAnimation() {
        this.mOpenAnimationEnable = false
    }

    /**
     * [.addAnimation]
     *
     * @param firstOnly true just show anim when first loading false show anim when load the data every time
     */
    fun isFirstOnly(firstOnly: Boolean) {
        this.mFirstOnlyEnable = firstOnly
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val gridManager = recyclerView.layoutManager
        if (gridManager is GridLayoutManager) {
            gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
                        return 1
                    }
                    return if (mSpanSizeLookup == null) {
                        if (isFixedViewType(type)) gridManager.spanCount else 1
                    } else {
                        if (isFixedViewType(type))
                            gridManager.spanCount
                        else
                            mSpanSizeLookup!!.getSpanSize(gridManager,
                                    position - getHeaderLayoutCount())
                    }
                }


            }
        }
    }

    protected fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == ERROR_VIEW || type == INIT_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW

    }

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    private var headerViewAsFlow: Boolean = false

    private var footerViewAsFlow: Boolean = false

    fun setHeaderViewAsFlow(headerViewAsFlow: Boolean) {
        this.headerViewAsFlow = headerViewAsFlow
    }

    fun isHeaderViewAsFlow(): Boolean {
        return headerViewAsFlow
    }

    fun setFooterViewAsFlow(footerViewAsFlow: Boolean) {
        this.footerViewAsFlow = footerViewAsFlow
    }

    fun isFooterViewAsFlow(): Boolean {
        return footerViewAsFlow
    }

    private var mSpanSizeLookup: SpanSizeLookup? = null

    interface SpanSizeLookup {
        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
    }

    /**
     * @param spanSizeLookup instance to be used to query number of spans occupied by each item
     */
    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup
    }

    /**
     * 是否是空数据初始化状态
     * 如果想显示 空数据 或者错误界面
     *
     * isInitEmpty 必须为false
     */
     var isInitEmpty = true

    private fun getEmptyViewType(): Int {
        return when {
            isInitEmpty -> INIT_VIEW
            isError -> ERROR_VIEW
            else -> EMPTY_VIEW
        }
    }



    /**
     * 错误界面不允许重写
     *
     * @param parent
     * @return
     */
    private fun setErrorView(): Int {
        return R.layout.base_include_view_error
    }

    /**
     * 重写 可改变空数据界面
     *
     * @param parent
     * @return
     */
    fun setEmptyView(): Int {
        return R.layout.base_include_view_empty
    }


    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    fun getHeaderLayoutCount(): Int {

        mHeaderLayout?.let {
            if (it.childCount != 0) {
                return 1
            }
        }
        return 0
    }

    /**
     * if addFooterView will be return 1, if not will be return 0
     */
    fun getFooterLayoutCount(): Int {

        mFooterLayout?.let {
            if (it.childCount != 0) {
                return 1
            }
        }
        return 0
    }

    private fun getLoadMoreViewCount(): Int {
        return if (mLoadMoreView == null) 0 else 1
    }

    /**
     * Return root layout of header
     */

    fun getHeaderLayout(): LinearLayout? {
        return mHeaderLayout
    }

    /**
     * Return root layout of footer
     */
    fun getFooterLayout(): LinearLayout? {
        return mFooterLayout
    }

    /**
     * Call before [RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty false will not show headView if the data is empty true will show emptyView and headView
     */
    fun setHeaderAndEmpty(isHeadAndEmpty: Boolean) {
        setHeaderFooterEmpty(isHeadAndEmpty, false)
    }

    /**
     * set emptyView show if adapter is empty and want to show headview and footview
     * Call before [RecyclerView.setAdapter]
     *
     * @param isHeadAndEmpty
     * @param isFootAndEmpty
     */
    fun setHeaderFooterEmpty(isHeadAndEmpty: Boolean, isFootAndEmpty: Boolean) {
        mHeadAndEmptyEnable = isHeadAndEmpty
        mFootAndEmptyEnable = isFootAndEmpty
    }

    /**
     * Append header to the rear of the mHeaderLayout.
     *
     * @param header
     */
    fun addHeaderView(header: View): Int {
        return addHeaderView(header, -1)
    }

    /**
     * Add header view to mHeaderLayout and set header view position in mHeaderLayout.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     *
     * @param header
     * @param index  the position in mHeaderLayout of this header.
     * When index = -1 or index >= child count in mHeaderLayout,
     * the effect of this method is the same as that of [.addHeaderView].
     */
    fun addHeaderView(header: View, index: Int): Int {
        return addHeaderView(header, index, LinearLayout.VERTICAL)
    }

    /**
     * @param header
     * @param index
     * @param orientation
     */
    fun addHeaderView(header: View, index: Int, orientation: Int): Int {
        var index = index
        if (mHeaderLayout == null) {
            mHeaderLayout = LinearLayout(header.context)
        }
        mHeaderLayout?.let {
            if (orientation == LinearLayout.VERTICAL) {
                it.orientation = LinearLayout.VERTICAL
                it.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            } else {
                it.orientation = LinearLayout.HORIZONTAL
                it.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            }

            val childCount = it.childCount

            if (index < 0 || index > childCount) {
                index = childCount
            }
            it.addView(header, index)
            if (it.childCount == 1) {
                val position = getHeaderViewPosition()
                if (position != -1) {
                    notifyItemInserted(position)
                }
            }
        }

        return index
    }

    fun setHeaderView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {

        if (mHeaderLayout == null || mHeaderLayout!!.getChildCount() <= index) {
            return addHeaderView(header, index, orientation)
        } else {
            mHeaderLayout!!.removeViewAt(index)
            mHeaderLayout!!.addView(header, index)
            return index
        }
    }

    /**
     * Append footer to the rear of the mFooterLayout.
     *
     * @param footer
     */
    fun addFooterView(footer: View): Int {
        return addFooterView(footer, -1, LinearLayout.VERTICAL)
    }

    fun addFooterView(footer: View, index: Int): Int {
        return addFooterView(footer, index, LinearLayout.VERTICAL)
    }

    /**
     * Add footer view to mFooterLayout and set footer view position in mFooterLayout.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of [.addFooterView].
     *
     * @param footer
     * @param index  the position in mFooterLayout of this footer.
     * When index = -1 or index >= child count in mFooterLayout,
     * the effect of this method is the same as that of [.addFooterView].
     */
    fun addFooterView(footer: View, index: Int, orientation: Int): Int {
        var index = index


        if (mFooterLayout == null) {
            mFooterLayout = LinearLayout(footer.context)

        }
        mFooterLayout?.let {
            it.orientation = orientation
            if (orientation == LinearLayout.VERTICAL) {
                it.layoutParams = RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT)
            } else {
                it.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
            val childCount = it.childCount
            if (index < 0 || index > childCount) {
                index = childCount
            }
            it.addView(footer, index)
            if (it.childCount == 1) {
                val position = getFooterViewPosition()
                if (position != -1) {
                    notifyItemInserted(position)
                }
            }
        }
        return index
    }


    fun setFooterView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        if (mFooterLayout == null || mFooterLayout!!.childCount <= index) {
            return addFooterView(header, index, orientation)
        } else {
            mFooterLayout!!.removeViewAt(index)
            mFooterLayout!!.addView(header, index)
            return index
        }
    }


    /**
     * remove header view from mHeaderLayout.
     * When the child count of mHeaderLayout is 0, mHeaderLayout will be set to null.
     *
     * @param header
     */
    fun removeHeaderView(header: View) {
        if (getHeaderLayoutCount() == 0) return

        mHeaderLayout?.removeView(header)
        if (mHeaderLayout?.getChildCount() == 0) {
            val position = getHeaderViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove footer view from mFooterLayout,
     * When the child count of mFooterLayout is 0, mFooterLayout will be set to null.
     *
     * @param footer
     */
    fun removeFooterView(footer: View) {
        if (getFooterLayoutCount() == 0) return

        mFooterLayout?.removeView(footer)
        if (mFooterLayout?.getChildCount() == 0) {
            val position = getFooterViewPosition()
            if (position != -1) {
                notifyItemRemoved(position)
            }
        }
    }

    /**
     * remove all header view from mHeaderLayout and set null to mHeaderLayout
     */
    fun removeAllHeaderView() {
        if (getHeaderLayoutCount() == 0) return

        mHeaderLayout?.removeAllViews()
        val position = getHeaderViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    fun removeAllFooterView() {
        if (getFooterLayoutCount() == 0) return
        mFooterLayout?.removeAllViews()
        val position = getFooterViewPosition()
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    private fun getHeaderViewPosition(): Int {
        //Return to header view notify position
        if (isEmpty()) {
            if (mHeadAndEmptyEnable) {
                return 0
            }
        } else {
            return 0
        }
        return -1
    }

    private fun getFooterViewPosition(): Int {
        //Return to footer view notify position
        if (isEmpty()) {
            var position = 1
            if (mHeadAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++
            }
            if (mFootAndEmptyEnable) {
                return position
            }
        } else {
            return getHeaderLayoutCount() + mItems.size
        }
        return -1
    }

    // add a footer to the adapter
    fun setLoadMoreView(loadMore: View) {
        if (mLoadMoreView == null) {
            mLoadMoreView = loadMore
            // animate
            notifyItemInserted(itemCount - 1)
        }
    }

    // remove a footer from the adapter
    fun removeLoadMoreView() {
        mLoadMoreView?.let {
            notifyItemRemoved(itemCount - 1)
            mLoadMoreView = null
        }
    }

    private var mListener: ListenerBuilder? = null

    fun setListener(listenerBuilder: ListenerBuilder.() -> Unit) {
        mListener = ListenerBuilder().also(listenerBuilder)
    }

    inner class ListenerBuilder {
        internal var mOnItemLongClickAction: ((T, Int, View) -> Unit)? = null
        internal var mOnItemClickAction: ((T, Int, View) -> Unit)? = null

        internal var mOnItemChildClickAction: ((T, Int, View) -> Unit)? = null
        internal var mOnItemChildLongClickAction: ((T, Int, View) -> Unit)? = null
        fun onItemLongClickListener(action: (T, Int, View) -> Unit) {
            mOnItemLongClickAction = action
        }

        fun onItemClickListener(action: (T, Int, View) -> Unit) {
            mOnItemClickAction = action
        }

        fun onItemChildClickListener(action: (T, Int, View) -> Unit) {
            mOnItemChildClickAction = action
        }

        fun onItemChildLongClickListener(action: (T, Int, View) -> Unit) {
            mOnItemChildLongClickAction = action
        }
        internal var mbindHeaderDataAction: (RecyclerViewHolder.() -> Unit)? = null

        fun onbindHeaderData(action: RecyclerViewHolder.() -> Unit) {
            mbindHeaderDataAction = action
        }
    }

    private var bindErrorListener: BindErrorDataListner? = null

    fun setErrorBindListener(listenerBuilder: BindErrorDataListner.() -> Unit) {
        bindErrorListener = BindErrorDataListner().also(listenerBuilder)
    }

    inner class BindErrorDataListner {
        internal var mOnBindErrorAction: (RecyclerViewHolder.() -> Unit)? = null

        fun mOnBindErrorListener(action: RecyclerViewHolder.() -> Unit) {
            mOnBindErrorAction = action
        }
    }

    protected fun RecyclerViewHolder.addOnItemChildClickListener(@IdRes vararg viewIds: Int, position: Int) {

        val t: T? = getItem(position)
        t?.let { item ->
            viewIds.forEach { id ->
                this.findViewById<View>(id)
                        .setOnClickListener { childview ->
                            mListener?.mOnItemChildClickAction?.let { it(item, position, childview) }
                            true
                        }
            }
        }
    }

    protected fun RecyclerViewHolder.addOnItemChildClickListener( vararg views: View, position: Int) {

        val t: T? = getItem(position)
        t?.let { item ->
            views.forEach {
                it.setOnClickListener { childview ->
                    mListener?.mOnItemChildClickAction?.let { it(item, position, childview) }
                    true
                }
            }
        }
    }

    protected fun RecyclerViewHolder.addOnItemChildLongClickListener( vararg viewIds: Int, position: Int) {

        val t: T? = getItem(position)
        t?.let { item ->
            viewIds.forEach { id ->
                this.findViewById<View>(id)
                        .setOnLongClickListener { childview ->
                            mListener?.mOnItemChildLongClickAction?.let { it(item, position, childview) }
                            true
                        }
            }
        }
    }


}
