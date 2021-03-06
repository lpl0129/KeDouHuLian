package com.qttx.kedouhulian.ui.reward.buunty

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.common.SettingActivity
import com.qttx.kedouhulian.ui.dialog.BountyPacketDialog
import com.qttx.kedouhulian.ui.reward.buunty.adapter.BountyAdapter
import com.qttx.kedouhulian.ui.reward.buunty.viewModel.BountyViewModel
import com.qttx.kedouhulian.utils.getUserLocation
import com.stay.toolslibrary.base.BaseFragment
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.library.refresh.PtrFrameLayout
import com.stay.toolslibrary.net.IListViewProvider
import com.stay.toolslibrary.widget.RecycleViewDivider
import kotlinx.android.synthetic.main.reward_fragment_task_search.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author huangyr
 * @date 2019/4/11 0011
 * 赏金猎人列表
 */
class BountyGrapFragment : BaseFragment(), IListViewProvider {

    val listModel: BountyViewModel  by viewModel()

    val params = mutableMapOf<String, String>()
    private lateinit var adapter: BountyAdapter

    private lateinit var layoutManger: LinearLayoutManager

    override fun getLayoutId(): Int = R.layout.reward_fragment_task_search

    override fun initBeforeListener() {
        layoutManger = LinearLayoutManager(context)
        layoutManger.orientation = OrientationHelper.VERTICAL
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, R.color.bgColor))
        adapter = BountyAdapter(listModel.list)
        adapter.setListener {
            onItemClickListener { any, i, view ->
                val intent = Intent(appContext, BountyDetailActivity::class.java)
                intent.putExtra("id", any.id.toString())
                startActivityForResult(intent, 200)
            }
        }
    }

    override fun liveDataListener() {
        listModel.listManagerLiveData.toObservableList(this, this)
    }

    override fun processLogic() {
        val boundle = arguments
        if (boundle == null) {
            floatBt.setOnClickListener {
                val dialog = BountyPacketDialog(appContext)
                        .setOffsetXAndY(0, 0)
                        .setOnSelectListener(OnSelectListener { position, text ->
                            if (position == 0) {
                                val intent = Intent(appContext, BountyPublishActivity::class.java)
                                startActivityForResult(intent, 200)
                            } else if (position == 1) {
                                val intent = Intent(appContext, MyBountyActivity::class.java)
                                startActivityForResult(intent, 200)
                            }else if (position ==2) {
                                SettingActivity.show(appContext,"bounty")
                            }
                        }
                        )
                XPopup.Builder(appContext)
                        .hasShadowBg(false)
                        .atView(floatBt)
                        .asCustom(dialog)
                        .show()

            }
            floatBt.visibility = View.VISIBLE
        }
        boundle?.let {
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
        }
        ptrlayout.autoRefresh()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == 400) {
            ptrRequestListener(true)
        }
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

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return layoutManger
    }

    override fun getRecyclerAdapter(): RecyclerAdapter<*> {
        return adapter
    }

    override val ptrProvider: PtrFrameLayout
        get() = ptrlayout
    override val recyclerViewProvider: RecyclerView
        get() = recyclerView
}