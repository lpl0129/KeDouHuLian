package com.qttx.kedouhulian.ui.reward.task

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.TaskConfigItem
import com.qttx.kedouhulian.ui.common.SettingActivity
import com.qttx.kedouhulian.ui.dialog.PublishDialog
import com.qttx.kedouhulian.ui.reward.task.adapter.TaskAdapter
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskViewModel
import com.qttx.kedouhulian.utils.getUserLocation
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.ContainerActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullGridView
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.utils.extension.loadImage
import com.stay.toolslibrary.widget.VerticalTextview
import kotlinx.android.synthetic.main.reward_fragment_task_search.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/11 0011
 * 抢单大厅
 */
class TaskGrapFragment : BaseFragment(), IListViewProvider {

    val listModel: TaskViewModel  by viewModel()

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
    val params = mutableMapOf<String, String>()
    private lateinit var adapter: TaskAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override fun ptrRequestListener(isRefresh: Boolean) {

        val city = getUserLocation()
        city?.let {
            params["province"] = it.provinceId.toString()
            params["city"] = it.cityId.toString()
            params["district"] = it.districtId.toString()
        }
        listModel.getListData(this, isRefresh,params)
    }

    private var notifyTv: VerticalTextview?=null

    private var gridlistView: NestFullGridView?=null

    override fun getLayoutId(): Int = R.layout.reward_fragment_task_search

    override fun processLogic() {

        val boundle = arguments

        if (boundle == null) {

            val view = layoutInflater.inflate(R.layout.reward_header_task_grap, null)
            notifyTv=view.findViewById(R.id.notifyTv)
            gridlistView=view.findViewById(R.id.gridlistView)
            notifyTv?.setTextStillTime(3000)//设置停留时长间隔
            notifyTv?.setAnimTime(400)//设置进入和退出的时间间隔
            floatBt.setOnClickListener {
                val dialog = PublishDialog(appContext)
                        .setOffsetXAndY(0, 0)
                        .setOnSelectListener(OnSelectListener { position, text ->
                            if (position == 0) {
                                val intent = Intent(appContext, TaskPublishActivity::class.java)
                                startActivityForResult(intent, 200)
                            } else if (position == 1) {
                                val intent = Intent(appContext, MyTaskActivity::class.java)
                                startActivityForResult(intent, 200)
                            } else if (position == 2) {
                                SettingActivity.show(appContext, "task")
                            }
                        }
                        )
                XPopup.Builder(appContext)
                        .hasShadowBg(false)
                        .atView(floatBt)
                        .asCustom(dialog)
                        .show()

            }
            gridlistView?.setAdapter(object : NestFullViewAdapter<TaskConfigItem>(R.layout.reward_grid_item_task_type, listModel.typeList) {
                override fun onBind(pos: Int, t: TaskConfigItem?, holder: NestFullViewHolder?) {
                    t?.let {
                        val image = holder!!.getView<ImageView>(R.id.typeIV)

                        val tv = holder!!.getView<TextView>(R.id.typeTitleTv)
                        image.loadImage(it.image)
                        tv.text = it.type_name;
                    }
                }
            }
            )
            gridlistView?.setOnItemClickListener(object : NestFullGridView.OnGirdItemClickListener {
                override fun onLongItemClick(parent: NestFullGridView?, view: View?, position: Int) {
                }

                override fun onItemClick(parent: NestFullGridView?, view: View?, position: Int) {
                    val bound = Bundle()
                    bound.putInt("cate", listModel.typeList[position].id)
                    bound.putString("title", listModel.typeList[position].type_name)
                    ContainerActivity.startContainerActivity(appContext, TaskGrapFragment::class.java.canonicalName, bound)
                }
            }
            )
            adapter.addHeaderView(view)
            floatBt.visibility = View.VISIBLE
            listModel.getData(this)
        }
        boundle?.let {

            val cate = boundle.getInt("cate", -1)
            if (cate == -1) {
                //文字搜索
                searchLl.visibility = View.VISIBLE

                topBackIv1.setOnClickListener {
                    activity?.finish()
                }
                searchTv.setOnClickListener {

                    val text = searchEt.text.toString()
                    if (text.isNotEmpty()) {
                        params["search"] = text
                        ptrlayout.autoRefresh()
                    }
                }

            } else {
                //关键字搜索
                titleFl.visibility = View.VISIBLE
                topBackIv.setOnClickListener {
                    activity?.let {
                        it.finish()
                    }
                }
                val titletext = boundle.getString("title")
                titleTv.text = titletext
                params["cid"] = cate.toString()
            }
        }
        ptrlayout.autoRefresh()
    }

    override fun liveDataListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        adapter = TaskAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(appContext, TaskHomeDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }
        }
        listModel.listManagerLiveData.toObservableList(this, this)


        listModel.configLiveData.toObservable(this)
        {
            gridlistView?.updateUI()
            listModel.configLiveData.value?.data?.let {
                val list = mutableListOf<String>()
                it.broadcastlist.forEach {
                    list.add(it.title)
                }
                notifyTv?.setTextList(list)
                notifyTv?.startAutoScroll()
                listModel.typeList.addAll(it.list)
                gridlistView?.updateUI()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        notifyTv?.stopAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        notifyTv?.startAutoScroll()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 400) {
            ptrRequestListener(true)
        }
    }
}