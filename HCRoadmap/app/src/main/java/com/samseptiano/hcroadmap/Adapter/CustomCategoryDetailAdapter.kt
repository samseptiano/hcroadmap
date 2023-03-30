package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.DataClass.CustomCategory
import com.samseptiano.hcroadmap.DataClass.CustomCategoryDetail
import com.samseptiano.hcroadmap.DataClass.LDPSetting
import com.samseptiano.hcroadmap.Fragment.CustomCategoryDetailFragment
import com.samseptiano.hcroadmap.Fragment.CustomCategoryFragment
import com.samseptiano.hcroadmap.Fragment.LDPMasterSettingFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.CustomCategoryViewModel
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.custom_category_detail_item.view.*
import kotlinx.android.synthetic.main.custom_category_item.view.*
import kotlinx.android.synthetic.main.ldp_setting_item.view.*
import kotlinx.android.synthetic.main.ldp_setting_item.view.btnDelete


class CustomCategoryDetailAdapter(private val customCategoryList: List<CustomCategoryDetail>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: CustomCategoryDetailFragment) : RecyclerView.Adapter<CustomCategoryDetailHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): CustomCategoryDetailHolder {
        return CustomCategoryDetailHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.custom_category_detail_item, viewGroup, false))
    }

    override fun getItemCount(): Int = customCategoryList.size

    override fun onBindViewHolder(holder: CustomCategoryDetailHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(customCategoryList[position],ctx,activity,fm,position,fg)
            }
        }
    }
}

class CustomCategoryDetailHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNomor = view.tvNomor
    private val tvDataType = view.tvData
    private val tvCategoryDetail = view.tvCategoryDetail
    private val btnEdit = view.findViewById<Button>(R.id.btnEdit)
    private val btnDelete = view.btnDelete

    fun bindEmpList(CustomCategory: CustomCategoryDetail,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int, fg:CustomCategoryDetailFragment) {
        tvNomor.text = (pos+1).toString()+". "
        tvDataType.text = CustomCategory.columNTYPE
        tvCategoryDetail.text = CustomCategory.columNNAME

        btnEdit.setOnClickListener {
            fg.showAddCategoryDialog(CustomCategory)
        }

        btnDelete.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var customCategory = CustomCategoryDetail()
            customCategory.tablENAME = CustomCategory.tablENAME.toString().replace(" ","_").toUpperCase()
            customCategory.columNNAME = CustomCategory.columNNAME.toString()
            customCategory.columNTYPE = CustomCategory.columNTYPE
            customCategory.datatype = ""

            val customCategoryViewModel = CustomCategoryViewModel( customCategory,dao.getNIK(),dao.getToken(), ctx!!)

            customCategoryViewModel.deleteCustomCategoryDetail
                .observe(fg, object : Observer<String?> {
                    override fun onChanged(currencyPojos: String?) {
                        fg.loadAwardViewModel()
                    }
                })
        }

    }




}