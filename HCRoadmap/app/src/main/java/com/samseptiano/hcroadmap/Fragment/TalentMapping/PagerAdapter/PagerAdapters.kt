package com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter


class PagerAdapters(private val myContext: Context?, fm: FragmentManager, internal var totalTabs: Int) : FragmentStatePagerAdapter(fm) {

    private var mFragmentList= mutableListOf<Fragment>()
    private var mFragmentTitleList= mutableListOf<String>()

//    fun PagerAdapters(manager: FragmentManager?) {
////        super(manager)
//        mFragmentList = mutableListOf()
//        mFragmentTitleList = mutableListOf()
//    }


    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(
        fragment: Fragment,
        title: String
    ) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    fun removeAdapter(fragment: Fragment,title: String){
        mFragmentList.remove(fragment)
        mFragmentTitleList.remove(title)
    }

}