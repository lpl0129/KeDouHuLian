package com.qttx.kedouhulian.ui.reward.buunty.adapter

import android.view.View
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.BountyBean
import com.stay.toolslibrary.base.RecyclerAdapter
import com.stay.toolslibrary.base.RecyclerViewHolder
import com.stay.toolslibrary.utils.SpanUtils
import kotlinx.android.synthetic.main.reward_list_item_my_grap_bounty.*
import java.lang.StringBuilder
import java.util.*

/**
 * @author huangyr
 * @date 2019/4/18 0018
 */
class MyGrapBountyAdapter constructor(result: MutableList<BountyBean>) : RecyclerAdapter<BountyBean>(result) {


    override fun getLayoutIdByType(viewType: Int) = R.layout.reward_list_item_my_grap_bounty

    override fun RecyclerViewHolder.bindData(item: BountyBean, position: Int) {
        with(item)
        {
            val builder = StringBuilder()
            builder.append("[$type_name] ")
            SpanUtils.with(titleTv)
                    .append(builder)
                    .setForegroundColor(mContext.resources.getColor(R.color.tagColor))
                    .append(title)
                    .create()

            val cal = Calendar.getInstance()
            cal.time = Date(create_time * 1000)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            timeMonth.text = "/ $month 月"
            timeDay.text = day.toString()
            if (seller_score.isNotEmpty())
            {
                socreTv.visibility=View.VISIBLE
                socreTv.text = "工作完成后将会获得$seller_score 个蝌蚪币"
            }else
            {
                socreTv.visibility=View.GONE
            }

            priceTv.text = "$price"
            priceUnitTv.text = "/$unit"
            numberAcountTv.text = total_num.toString()
            locationTv.text=area_name
            publishUserNameTv.text = nickname
            applyAcountTv.visibility = View.GONE
            refuseTv.visibility = View.GONE
            agreeTv.visibility = View.GONE
            agreeStatusTv.visibility = View.GONE
            appealLl.visibility=View.GONE
            addOnItemChildClickListener(applyAcountTv, refuseTv, agreeTv, position = position)
//            状态：-2=已解雇,-1=解雇中（agree为1时，显示拒绝和确认按钮）,0=已报名,1=已雇佣,2=已结算
//            agree	string	是否同意被解雇:-1=已拒绝,0=无,1=待确认,2=已同意
            when (order_status) {
                0 -> {
                    statusTv.text = "已报名"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                }
                1 -> {
                    statusTv.text = "进行中"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.primaryColor))
                    applyAcountTv.visibility=View.VISIBLE
                    //已申请
                    if(is_apply_finish==1)
                    {
                        applyAcountTv.text="已申请结算"
                        applyAcountTv.isEnabled=false
                    }else
                    {
                        applyAcountTv.text="申请结算"
                        applyAcountTv.isEnabled=true
                    }

                }
                2 -> {
                    statusTv.text = "已完成"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.deepColor))
                }
                -1 -> {
                    statusTv.text = "解雇中"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                    when (agree) {
                        1 -> {
                            agreeTv.visibility=View.VISIBLE
                            refuseTv.visibility = View.VISIBLE
                        }
                        -1 -> {
                            agreeStatusTv.visibility=View.VISIBLE
                            agreeStatusTv.text="已拒绝"
                        }
                        -2 -> {
                            agreeStatusTv.text="申诉中"
                            appealLl.visibility=View.VISIBLE
//                            agreeStatusTv.visibility=View.VISIBLE
//                            agreeStatusTv.text="已同意"
                        }
                    }
                }
                -2 -> {
                    statusTv.text = "已解雇"
                    statusTv.setTextColor(mContext.resources.getColor(R.color.tagColor))
                }

            }


        }
    }
}