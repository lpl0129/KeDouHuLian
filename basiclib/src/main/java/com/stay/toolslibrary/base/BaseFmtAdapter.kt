package com.stay.toolslibrary.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

class BaseFmtAdapter(fm: FragmentManager, private var mFmts: List<BaseFragment>, private var mTitles: List<String>) : FragmentStatePagerAdapter(fm) {

    fun setList(fmts: List<BaseFragment>, titles: List<String>) {
        this.mFmts = fmts
        this.mTitles = titles
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return mFmts[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mTitles[position]
    }

    override fun getCount(): Int {
        return mFmts.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }
}
