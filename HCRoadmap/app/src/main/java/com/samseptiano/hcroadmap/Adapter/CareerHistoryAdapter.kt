package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.career_history_item.view.*


class CareerHistoryAdapter(private val careerHistoryList: List<CareerHistory>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager) : RecyclerView.Adapter<CareerHistoryHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CareerHistoryHolder {
        return CareerHistoryHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.career_history_item, viewGroup, false))
    }

    override fun getItemCount(): Int = careerHistoryList.size

    override fun onBindViewHolder(holder: CareerHistoryHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(careerHistoryList[position],ctx,activity,fm,position)
            }
        }
    }
}

class CareerHistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTanggal = view.tvTanggal
    private val tvGolongan = view.tvGolongan
    private val tvJabatan = view.tvJabatan
    private val tvDirektorat = view.tvDirektorat
    private val tvBranch = view.tvBranch
    private val tvNomor = view.tvNomor

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(careerHistory: CareerHistory,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
        if(careerHistory.skeffective!=null) {
            tvTanggal.text = dateConverter.convertToLocalDate(careerHistory.skeffective!!.split(" ")[0])
        }
        tvDirektorat.text = careerHistory.orgname
        tvGolongan.text = "GOLONGAN: "+careerHistory.joblevel
        tvJabatan.text = careerHistory.jobttlname
        tvNomor.text = (pos+1).toString()+". "
    }

}