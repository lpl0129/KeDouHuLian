package com.qttx.kedouhulian.ui.user

import android.os.Bundle
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.user.viewModel.UserInfoViewModel
import com.qttx.kedouhulian.utils.Utils.Companion.formatmillTime
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.livedata.bindScheduler
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.reward_task_activity_detail.*
import kotlinx.android.synthetic.main.user_activity_bonus.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

/**
 * @author huangyr
 * @date 2019/5/18 0018
 */
class UserBonusActivity : BaseActivity() {

    private val viewModel: UserInfoViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.user_activity_bonus
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("分红")
        viewModel.getBonusInfo(this)
    }

    override fun liveDataListener() {
        viewModel.bonusInfoLiveData.toObservable(this)
        {
            it.data?.let {
                incomeTv.text = "累计获得分红：${it.money}元"
                timeTv.text = "每天 ${it.starttime}-${it.endtime}"
//                /state 0.没分红,1,正常分红,2,已经领过2
                val nowTime = System.currentTimeMillis()

                if (nowTime < it.start_strtotime * 1000) {
//                    未开始(今天或者明天)
                    val left = it.start_strtotime * 1000 - nowTime
                    beginTimeCount(left)
                    getBonusTv.text = "立即领取"
                    getBonusTv.isEnabled = false
                    getBonusTv.setBackgroundResource(R.drawable.spite_15_bk)

                } else {
                    //正在进行,进入倒计时
                    //判断是否已领取
                    if (it.fh_money > 0) {
                        getBonusTv.text = "今日已领"
                        getBonusTv.isEnabled = false
                        getBonusTv.setBackgroundResource(R.drawable.spite_15_bk)
                    } else {
                        getBonusTv.text = "立即领取"
                        getBonusTv.isEnabled = true
                        getBonusTv.setBackgroundResource(R.drawable.primary_15_bk)

                    }
                    val left = it.end_strtotime * 1000 - nowTime
                    beginTimeCount(left)
                }
            }

        }
        getBonusTv.setOnClickListener {
            viewModel.getBonus(this)
        }
        viewModel.getbonusInfoLiveData.toObservable(this)
        {
            showToast("领取成功")
            TipDialog.newInstance("领取${it.data?.fh_money}元")
                    .show(supportFragmentManager)
            viewModel.getBonusInfo(this)
        }
    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getBonusInfo(this)
    }

    var disposable: Disposable? = null

    private fun beginTimeCount(endTime: Long) {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        Observable.interval(1, TimeUnit.SECONDS)
                .bindScheduler()
                .subscribe(object : Observer<Long> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: Long) {
                        val now = endTime - t * 1000

                        if (now <= 0) {
                            viewModel.getBonusInfo(this@UserBonusActivity)
                            disposable?.let {
                                if (!it.isDisposed) {
                                    it.dispose()
                                }
                            }
                        } else {

                            val time = formatmillTime(now)
                            val times = time.split(":")
                            hTv.text = times[0]
                            mTv.text = times[1]
                            sTv.text = times[2]
                        }
                    }

                    override fun onError(e: Throwable) {
                    }
                }
                )
    }

    override fun onDestroy() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        super.onDestroy()

    }
}