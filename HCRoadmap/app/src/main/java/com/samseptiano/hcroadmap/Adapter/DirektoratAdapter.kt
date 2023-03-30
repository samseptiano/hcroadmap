package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.DataClass.UsermanagementdirItem
import com.samseptiano.hcroadmap.DataClass.UsermanagementorgItem
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.AwardFragment
import com.samseptiano.hcroadmap.Fragment.UserManagementFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import kotlinx.android.synthetic.main.award_item.view.*
import kotlinx.android.synthetic.main.career_history_item.view.*
import kotlinx.android.synthetic.main.career_history_item.view.tvDirektorat
import kotlinx.android.synthetic.main.direktorat_item.view.*


class DirektoratAdapter(private val direktoratList: List<Any>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, var type:String) : RecyclerView.Adapter<DirektoratHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): DirektoratHolder {
        return DirektoratHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.direktorat_item, viewGroup, false))
    }

    override fun getItemCount(): Int = direktoratList.size

    override fun onBindViewHolder(holder: DirektoratHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(direktoratList[position],ctx,activity,fm,position,type)
            }
        }
    }
}

class DirektoratHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvDirektorat = view.tvDirektorat
    private val tvOrg = view.tvOrg
    private val tvOrgTitle = view.tvOrgTitle

//    private val tvNomor = view.tvNomor


    fun bindEmpList(award: Any,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int,type: String) {
//        tvNomor.text = (pos+1).toString()+". "

        if(type.equals("ORG")){
            var aaa = award as UsermanagementorgItem
            tvDirektorat.text = aaa.dirname
            tvOrg.text = award.orgname
        }
        else if(type.equals("DIR")){
            var aaa = award as UsermanagementdirItem
            tvDirektorat.text = aaa.dirname
            tvOrg.visibility=View.GONE
            tvOrgTitle.visibility = View.GONE
        }

    }

}