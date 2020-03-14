package com.qttx.kedouhulian.ui.reward.task

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.ViewTreeObserver
import android.view.WindowManager

import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.ui.reward.task.viewModel.TaskCommentViewModel
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.utils.KeyboardUtils
import kotlinx.android.synthetic.main.reward_activity_publish_comment.*
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * Created by huangyuru on 2018/12/10.
 */

class TaskCommentActivity : BaseActivity() {


    private var context: Context? = null
    lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener
    private var usableHeightPrevious: Int = 0


    private var b_id:String =""
    private var id: String = ""
    private var keyBordShow = true
    private var isBounty = false
    private val viewModel: TaskCommentViewModel by viewModel()

    protected fun setListener() {
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            simpleInputRoot.getWindowVisibleDisplayFrame(r)
            val usableHeightNow = r.bottom - 0
            if (usableHeightNow != usableHeightPrevious) {
                val usableHeightSansKeyboard = simpleInputRoot.getRootView()
                        .getHeight()
                val heightDifference = usableHeightSansKeyboard - usableHeightNow
                if (heightDifference > usableHeightSansKeyboard / 5) {
                    onKeyShow(heightDifference)
                } else {
                    // keyboard probably just became hidden
                    onKeyHide()
                }
                // mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow
            }
        }
        simpleInputRoot.getViewTreeObserver().addOnGlobalLayoutListener(listener)

        simpleInputEditor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    fun onKeyShow(heigth: Int) {
        val params = simpleInputSelectorContainer.getLayoutParams()
        if (params.height != heigth) {
            params.height = heigth
            simpleInputSelectorContainer.setLayoutParams(params)
        }
        keyBordShow = true
    }

    fun onKeyHide() {
        keyBordShow = false
        if (usableHeightPrevious != -0) {
            finish()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.reward_activity_publish_comment
    }


    override fun finish() {
        KeyboardUtils.hideSoftInput(context!!, simpleInputEditor)
        super.finish()
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        context = this@TaskCommentActivity
        setListener()
        isBounty = intent.getBooleanExtra("bounty", false)
        id = intent.getStringExtra("id")
        if (intent.hasExtra("b_id"))
        {
            b_id = intent.getStringExtra("b_id")
        }

        if (intent.hasExtra("name"))
        {
            val name = intent.getStringExtra("name")
            simpleInputEditor.hint = "回复$name:"
        }







        simpleInputBkg.setOnClickListener {
            KeyboardUtils.hideSoftInput(context!!, simpleInputEditor)
            finish()
        }
        sendTv.setOnClickListener {
            val content = simpleInputEditor.getText().toString()
            if (TextUtils.isEmpty(content)) {
                showToast("请输入回复内容")
            } else {
                if (isBounty) {
                    if (b_id.isEmpty())
                    {
                        viewModel.submitConent_bounty(this, content, id)
                    }else
                    {
                        viewModel.evaluateBounty(this, content, id,b_id)
                    }

                } else {
                    if (b_id.isEmpty())
                    {
                        viewModel.submitConent(this, content, id)
                    }else
                    {
                        viewModel.evaluate(this, content, id,b_id)
                    }

                }
            }
        }

        cancle_tv.setOnClickListener {
            finish()
        }
    }

    override fun liveDataListener() {
        viewModel.commitLiveData.toObservable(this)
        {
            showToast("回复成功")
            setResult(400)
            finish()
        }
    }

    companion object {
        fun show(context: Context, bid: String) {
            val intent = Intent(context, TaskCommentActivity::class.java)
            intent.putExtra("id", bid)
            context.startActivity(intent)
        }
    }
}
