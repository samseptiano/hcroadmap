package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.LDP
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.learning_kmmp_item.view.*
import kotlinx.android.synthetic.main.learning_training_item.view.tvTahun


class LDPAdapter(private val trainingList: List<LDP>, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager) : RecyclerView.Adapter<LDPHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): LDPHolder {
        return LDPHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.learning_kmmp_item, viewGroup, false))
    }

    override fun getItemCount(): Int = trainingList.size

    override fun onBindViewHolder(holder: LDPHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(trainingList[position],ctx,activity,fm, position)
            }
        }
    }
}

class LDPHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNomor = view.tvNomor

    private val tvTahun = view.tvTahun
    private val tvNIlai = view.tvNilai
    private val tvProgram = view.tvProgram
    private val tvJudul = view.tvJudul
    private val tvprogress = view.tvProgress

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(training: LDP,ctx: Context,activity:Activity,fm:FragmentManager, pos:Int) {
        tvNomor.text = (pos+1).toString()+". "
        tvTahun.text = training.kmmPYEAR
        tvNIlai.text = training.kmmPRESULT
        tvJudul.text = training.kmmPTITLE
        tvprogress.text = training.kmmPPROGRESS
    }

}