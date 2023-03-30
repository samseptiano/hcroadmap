package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.OtherArchievement
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.PerformanceFragment
import com.samseptiano.hcroadmap.R
import kotlinx.android.synthetic.main.award_item.view.tvTahun
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.other_archievement_item.view.*


class OtherArchievementAdapter(private val otherArchievementList: List<OtherArchievement>, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager, private val fg: PerformanceFragment) : RecyclerView.Adapter<OtherArchievementHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): OtherArchievementHolder {
        return OtherArchievementHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.other_archievement_item, viewGroup, false))
    }

    override fun getItemCount(): Int = otherArchievementList.size

    override fun onBindViewHolder(holder: OtherArchievementHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(otherArchievementList[position],ctx,activity,fm,position)
            }
        }
    }
}

class OtherArchievementHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvtahun = view.tvTahun
    private val tvStatus = view.tvStatus
    private val tvNomor = view.tvNomor
    private val tvArchievement = view.tvArchievement

    fun bindEmpList(otherArchievement: OtherArchievement,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
        tvNomor.text = (pos+1).toString()+". "
        tvtahun.text = otherArchievement.otheRYEAR
        tvStatus.text = otherArchievement.otheRSTATUS
        tvArchievement.text = otherArchievement.otheRTITLE

    }

}