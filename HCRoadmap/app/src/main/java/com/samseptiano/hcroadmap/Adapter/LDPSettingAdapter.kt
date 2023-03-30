package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.LDPSetting
import com.samseptiano.hcroadmap.Fragment.LDPMasterSettingFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.ldp_setting_item.view.*


class LDPSettingAdapter(private val ldpSettingList: List<LDPSetting>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: LDPMasterSettingFragment) : RecyclerView.Adapter<LDPSettingHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): LDPSettingHolder {
        return LDPSettingHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.ldp_setting_item, viewGroup, false))
    }

    override fun getItemCount(): Int = ldpSettingList.size

    override fun onBindViewHolder(holder: LDPSettingHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(ldpSettingList[position],ctx,activity,fm,position,fg)
            }
        }
    }
}

class LDPSettingHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNomor = view.tvNomor
    private val tvCompany = view.tvCompanyName
    private val tvLDPName = view.tvLDPName
    private val btnEdit = view.btnEdit
    private val btnDelete = view.btnDelete

    fun bindEmpList(ldpSetting: LDPSetting,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int, fg:LDPMasterSettingFragment) {
        tvNomor.text = (pos+1).toString()+". "
        tvCompany.text = ldpSetting.compname
        tvLDPName.text = ldpSetting.ldp_title

        btnEdit.setOnClickListener {
            fg.showEditDialog(ldpSetting)
        }

        btnDelete.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var ldpSettings = LDPSetting()
            ldpSettings.compgrpid = ldpSetting.compgrpid
            ldpSettings.compid=ldpSetting.compid
            ldpSettings.compname = ldpSetting.compname
            ldpSettings.ldp_id=ldpSetting.ldp_id
            ldpSettings.ldp_title=ldpSetting.ldp_title
            ldpSettings.upduser=dao.getusername()

            val ldpSettingViewModel = LDPSettingViewModel(ldpSettings,dao.getCompGrpId(),dao.getToken(), ctx!!)

            ldpSettingViewModel.deleteLdpSetting
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        //loadAwardViewModel()
                       fg.loadAwardViewModel()
                    }
                })

        }

    }


}