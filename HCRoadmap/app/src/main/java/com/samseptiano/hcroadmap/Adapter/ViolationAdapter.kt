package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Violation
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.career_history_item.view.tvTanggal
import kotlinx.android.synthetic.main.data_violation_item.view.*


class ViolationAdapter(private val violationList: List<Violation>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager) : RecyclerView.Adapter<ViolationHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViolationHolder {
        return ViolationHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.data_violation_item, viewGroup, false))
    }

    override fun getItemCount(): Int = violationList.size

    override fun onBindViewHolder(holder: ViolationHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(violationList[position],ctx,activity,fm,position)
            }
        }
    }
}

class ViolationHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvTanggal = view.tvTanggal
    private val tvBentukPelanggaran = view.tvBentukPelanggaran
    private val tvJenisPelanggaran = view.tvJenisPelanggaran
    private val tvDeskripsi = view.tvDeskripsiPelanggaran

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(violation: Violation,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int) {
        if(violation.empvioldate!=null) {
            tvTanggal.text = dateConverter.convertToLocalDate(violation.empvioldate!!.split(" ")[0])
        }
        tvBentukPelanggaran.text = violation.violancename
        tvDeskripsi.text = violation.empviolnote
        tvJenisPelanggaran.text = violation.warninglevel
    }

}