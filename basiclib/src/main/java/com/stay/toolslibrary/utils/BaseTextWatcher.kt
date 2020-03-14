package com.stay.toolslibrary.utils

import android.text.Editable
import android.text.TextWatcher


/**
 * @author huangyr
 * @date 2019/5/16
 */
class BaseTextWatcher : TextWatcher {

    private var hasMax: Boolean = false


    private var maxNumber = -1.0

    private var digits = 2

    fun setDigits(d: Int): BaseTextWatcher {
        digits = d
        return this
    }

    fun setMax(d: Double): BaseTextWatcher {
        hasMax = true
        maxNumber = d
        return this
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {

        val text = s.toString()
        val len = s.toString().length

        if (text.startsWith(".")) {
            //起始为. 插入0成为0.
            s.insert(0, "0")
        } else if (len >= 2 && text.startsWith("0") && !text.contains(".")) {
            //如果起始是0但是没输入 点  则剔除掉0
            s.replace(0, 1, "")
        } else if (text.contains(".")) {
            if (s.length - 1 - s.toString().indexOf(".") > digits) {
                s.replace(s.length - 1, s.length, "")
            }
        }
        if (text.isNotEmpty() && hasMax && !text.startsWith(".")) {
            val price = java.lang.Double.parseDouble(text)
            if (price > maxNumber) {
                s.clear()
                maxNumber.toString().toCharArray().forEach {
                    s.append(it)
                }
            }
        }


    }
}