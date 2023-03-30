package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Module
import com.samseptiano.hcroadmap.Fragment.HomeFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import kotlinx.android.synthetic.main.dashboard_icon.view.*


class DBIconAdapter(private val moduleList: List<Module>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg:HomeFragment) : RecyclerView.Adapter<DBIconHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): DBIconHolder {
        return DBIconHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.dashboard_icon, viewGroup, false))
    }

    override fun getItemCount(): Int = moduleList.size

    override fun onBindViewHolder(holder: DBIconHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(moduleList[position],ctx,activity,fm,position, fg)
            }
        }
    }
}

class DBIconHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imgIcon = view.imgbtnIcon
    private val tviconDesc = view.tviconDesc

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(module: Module,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int, fg: HomeFragment) {
        module.icon?.toInt()?.let { imgIcon.setImageResource(it) }
        tviconDesc.text = module.moduledesc

        imgIcon.setOnClickListener {
            module.modulecode?.let { it1 -> fg.action(it1) }
        }
    }

}