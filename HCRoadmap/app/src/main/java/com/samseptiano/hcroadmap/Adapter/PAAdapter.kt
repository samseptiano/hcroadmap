package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.PA
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.PerformanceFragment
import com.samseptiano.hcroadmap.R
import kotlinx.android.synthetic.main.award_item.view.tvTahun
import kotlinx.android.synthetic.main.pa_item.view.*


class PAAdapter(private val paList: List<PA>, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager, private val fg: PerformanceFragment) : RecyclerView.Adapter<PAHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): PAHolder {
        return PAHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.pa_item, viewGroup, false))
    }

    override fun getItemCount(): Int = paList.size

    override fun onBindViewHolder(holder: PAHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(paList[position],ctx,activity,fm,position)
            }
        }
    }
}

class PAHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvtahun = view.tvTahun
        var tvPA = view.tvPA

    fun bindEmpList(pa: PA,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
       tvtahun.text = pa.tahun.toString()
        tvPA.text = pa.pascore.toString()
    }

}