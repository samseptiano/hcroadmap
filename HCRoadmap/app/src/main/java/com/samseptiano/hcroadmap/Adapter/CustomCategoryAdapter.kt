package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.CustomCategory
import com.samseptiano.hcroadmap.DataClass.LDPSetting
import com.samseptiano.hcroadmap.Fragment.CustomCategoryDetailFragment
import com.samseptiano.hcroadmap.Fragment.CustomCategoryFragment
import com.samseptiano.hcroadmap.Fragment.LDPMasterSettingFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentProfileFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.CustomCategoryViewModel
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.custom_category_item.view.*
import kotlinx.android.synthetic.main.ldp_setting_item.view.*
import kotlinx.android.synthetic.main.ldp_setting_item.view.btnDelete


class CustomCategoryAdapter(private val customCategoryList: List<CustomCategory>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: CustomCategoryFragment) : RecyclerView.Adapter<CustomCategoryHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CustomCategoryHolder {
        return CustomCategoryHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.custom_category_item, viewGroup, false))
    }

    override fun getItemCount(): Int = customCategoryList.size

    override fun onBindViewHolder(holder: CustomCategoryHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(customCategoryList[position],ctx,activity,fm,position,fg)
            }
        }
    }
}

class CustomCategoryHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNomor = view.tvNomor
    private val tvProfileCategory = view.tvProfileCategory
    private val tvCategoryType = view.tvCategoryType
    private val btnView = view.btnView
    private val btnDelete = view.btnDelete

    fun bindEmpList(CustomCategory: CustomCategory,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int, fg:CustomCategoryFragment) {
        tvNomor.text = (pos+1).toString()+". "
        tvProfileCategory.text = CustomCategory.attribute_title
        tvCategoryType.text = CustomCategory.flag_upload
        if(CustomCategory.flag_nik_pk.equals("Y")){
            tvCategoryType.text = "Informasi"
        }
        else{
            tvCategoryType.text = "List"
        }


        btnView.setOnClickListener {


            val customCategoryDetailFragment =
                CustomCategoryDetailFragment()

            val args = Bundle()
            args.putString("TABLE_NAME", CustomCategory.attribute_title)
            customCategoryDetailFragment.arguments=args
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.myFragment, customCategoryDetailFragment)
            transaction?.addToBackStack("CustomCategoryDetailFragment")
            transaction?.commit()

        }

        btnDelete.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var customCategory = CustomCategory()
            customCategory.table_name = CustomCategory.table_name.toString()
            customCategory.flag_nik_pk = CustomCategory.flag_nik_pk.toString()
            customCategory.flag_automated_table=CustomCategory.flag_automated_table.toString()
            customCategory.flag_upload= CustomCategory.flag_upload.toString()
            customCategory.attribute_title=CustomCategory.attribute_title.toString()
            customCategory.attribute_id=CustomCategory.attribute_id.toString()


            val customCategoryViewModel = CustomCategoryViewModel( customCategory,dao.getNIK(),dao.getToken(), ctx!!)

            customCategoryViewModel.deleteCustomCategory
                .observe(fg, object : Observer<String?> {
                    override fun onChanged(currencyPojos: String?) {
                        fg.loadAwardViewModel()
                    }
                })
        }

    }


}