package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.AwardFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.award_item.view.*
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor


class AwardAdapter(private val awardList: List<Award>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: AwardFragment) : RecyclerView.Adapter<AwardHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): AwardHolder {
        return AwardHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.award_item, viewGroup, false))
    }

    override fun getItemCount(): Int = awardList.size

    override fun onBindViewHolder(holder: AwardHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(awardList[position],ctx,activity,fm,position)
            }
        }
    }
}

class AwardHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTahun = view.tvTahun
    private val tvAwardDesc = view.tvAwardDesc
    private val tvAward = view.tvAward

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(award: Award,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
        if(award.emphondate!=null) {
            tvTahun.text = dateConverter.convertToLocalDate(award.emphondate.split(" ")[0]).split("/")[0]+" -"
        }
        tvAward.text = award.honorname
        tvAwardDesc.text = award.honordescription
    }

}