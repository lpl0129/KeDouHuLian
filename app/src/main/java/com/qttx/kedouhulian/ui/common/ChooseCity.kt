package com.qttx.kedouhulian.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.RegionsBean
import com.qttx.kedouhulian.room.DataBase
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.widget.RecycleViewDivider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.common_activity_chosecity.*
import kotlinx.android.synthetic.main.user_list_item_chose_city.*
import java.util.*


/**
 * Created by huangyuru on 2017/2/17 0019.
 */
class ChooseCity : BaseActivity() {

    private var context: Context? = null
    private var id = 0
    private val mCityInfos = ArrayList<RegionsBean>()
    private lateinit var adapter: RecyclerAdapter<RegionsBean>
    private val CityName = ""
    private var level = 1//当前城市级别 1/省,2/市,3/区;
    private var allLevel: Int = 0//一共级别.3/三级到区,2,两级到市,1,一级到省

    private var info: RegionsBean? = null//item选择的城市信息;

    /**
     * 是否强制遵守allLevel,
     * false:当前页便可以返回
     */
    private var forceLevel = true

    override fun getLayoutId(): Int {
        return R.layout.common_activity_chosecity
    }


    protected fun findViewById() {
        context = this@ChooseCity

        val mLayoutManager = LinearLayoutManager(this)
        //垂直方向
        mLayoutManager.orientation = OrientationHelper.VERTICAL
        //给RecyclerView设置布局管理器
        recyclerView.layoutManager = mLayoutManager
        recyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL))
        adapter = object : RecyclerAdapter<RegionsBean>(mCityInfos) {
            override fun RecyclerViewHolder.bindData(item: RegionsBean, position: Int) {
                tv_city.text=item.name

            }

            override fun getLayoutIdByType(viewType: Int) = R.layout.user_list_item_chose_city

        }
        adapter.setListener {
            onItemClickListener { regionsBean, i, view ->
                val intent = Intent()
                info = regionsBean
                if (allLevel == regionsBean.level)
                //如果总共级别等于当前级别,返回,不在继续请求
                {
                    intent.putExtra(level.toString() + "", info)
                    setResult(400, intent)
                    finish()
                } else {
                    intent.setClass(this@ChooseCity, ChooseCity::class.java)
                    intent.putExtra("id", regionsBean.id)
                    intent.putExtra("level", allLevel)
                    intent.putExtra("thislevel", level)
                    intent.putExtra("forceLevel", forceLevel)
                    startActivityForResult(intent, 100)
                }
            }
        }
        recyclerView.adapter = adapter
    }


    override fun processLogic(savedInstanceState: Bundle?) {
        allLevel = intent.getIntExtra("level", 0)
        level = intent.getIntExtra("thislevel", 0) + 1
        id = intent.getIntExtra("id", 0)
        if (allLevel == 0) {
            allLevel = 3//默认三级选择,为方便可以不穿值为0,强制赋值为3,级别选择到区
        }
        if (level == 1) {
            setTopTitle("选择省份")
        } else if (level == 2) {
            setTopTitle("选择市")
        } else if (level == 3) {
            setTopTitle("选择区域")
        } else {
            setTopTitle("选择街道")
        }
        findViewById()
        initData()
        forceLevel = intent.getBooleanExtra("forceLevel", true)
        if (!forceLevel) {
//            top_right.setVisibility(View.VISIBLE);
//            top_right.setText("确认");
//            top_right.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent();
//                    setResult(400, intent);
//                    finish();
//                }
//            });
        }
    }

    private fun initData() {
        getCityData()
    }

    private fun getCityData() {
        Observable.create<List<RegionsBean>> {
            it.onNext(
                DataBase.getInstance(context)
                    .cityDao()
                    .getCityByPid(id)
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mCityInfos.clear()
                mCityInfos.addAll(it)
                adapter.notifyDataSetChanged()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 400) {
            data?.putExtra(level.toString() + "", info)
            setResult(400, data)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun liveDataListener() {

    }

    companion object {
        val REQUEST_CODE = 0x001
    }
}
