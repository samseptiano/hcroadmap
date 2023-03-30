package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Training
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import kotlinx.android.synthetic.main.learning_training_item.view.*


class TrainingAdapter(private val trainingList: List<Training>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager) : RecyclerView.Adapter<TrainingHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): TrainingHolder {
        return TrainingHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.learning_training_item, viewGroup, false))
    }

    override fun getItemCount(): Int = trainingList.size

    override fun onBindViewHolder(holder: TrainingHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(trainingList[position],ctx,activity,fm, position)
            }
        }
    }
}

class TrainingHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNomor = view.tvNo

    private val tvTahun = view.tvTahun
    private val tvTopik = view.tvTopik
    private val tvHasil = view.tvHasil

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(training: Training,ctx: Context,activity:Activity,fm:FragmentManager, pos:Int) {
        tvNomor.text = (pos+1).toString()+". "
        tvTahun.text = training.trainingdate?.split(" ")?.get(0) ?: ""
        tvHasil.text = training.hasiltraining
        tvTopik.text = training.trainingname
    }

}