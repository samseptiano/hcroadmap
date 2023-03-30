package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.DataClass.Inovasi
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.AwardFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.InovasiFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.award_item.view.*
import kotlinx.android.synthetic.main.award_item.view.tvTahun
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.innovation_item.view.*


class InovasiAdapter(private val awardList: List<Inovasi>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: InovasiFragment) : RecyclerView.Adapter<InovasiHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): InovasiHolder {
        return InovasiHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.innovation_item, viewGroup, false))
    }

    override fun getItemCount(): Int = awardList.size

    override fun onBindViewHolder(holder: InovasiHolder, position: Int) {
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

class InovasiHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTahun = view.tvTahun
    private val tvAwardDesc = view.tvInnovationDesc
    private val tvAward = view.tvInnovation

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(award: Inovasi,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
            tvTahun.text = award.tahun+" -"
            tvAward.text = award.judul
        tvAwardDesc.text = award.deskripsi
    }

}