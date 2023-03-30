package com.samseptiano.hcroadmap.Fragment

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinner
import com.samseptiano.hcroadmap.Adapter.CustomCategoryAdapter
import com.samseptiano.hcroadmap.Adapter.LDPSettingAdapter
import com.samseptiano.hcroadmap.DataClass.CustomCategory
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.DataClass.LDPSetting
import com.samseptiano.hcroadmap.MainActivity

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.CustomCategoryViewModel
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import kotlinx.android.synthetic.main.fragment_custom_category.view.*
import kotlinx.android.synthetic.main.fragment_ldpmaster_setting.view.*


class CustomCategoryFragment : Fragment() {

    lateinit var rootView:View
    private var customCategoryList: MutableList<CustomCategory>? = null
    lateinit var customCategoryAdapter: CustomCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_custom_category, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Custom Category Setting")

        rootView.btnAddCustomCategory.setOnClickListener {
            showAddCategoryDialog()
        }

        loadAwardViewModel()
        return rootView
    }

    fun loadAwardViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        val customCategoryViewModel = CustomCategoryViewModel( null,dao.getNIK(),dao.getToken(), context!!)

        customCategoryViewModel.customCategory
            .observe(this, object : Observer<List<CustomCategory?>?> {
                override fun onChanged(currencyPojos: List<CustomCategory?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            customCategoryList = mutableListOf()
                            customCategoryList!!.addAll(currencyPojos as MutableList<CustomCategory>)
                            insertToRV()
                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        var ldpSettingAdapter = customCategoryList?.let {
            CustomCategoryAdapter(
                it, context,activity,
                this.fragmentManager!!,this
            )
        }!!

        rootView.rvCustomCategory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ldpSettingAdapter
        }


    }

    private fun showAddCategoryDialog(){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_custom_category_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val edtNamaKategori = dialog.findViewById(R.id.edtNamaKategori) as EditText
        val spinnerTipeKategory = dialog.findViewById(R.id.spinnerTipeKategory) as SmartMaterialSpinner<Any>

        var aaa = mutableListOf<String>()
        aaa.add("LIST")
        aaa.add("INFORMASI")
        spinnerTipeKategory!!.setItem(aaa!! as List<Any>)

        spinnerTipeKategory!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, aaa!![position], Toast.LENGTH_SHORT).show()
                spinnerTipeKategory.setFloatingLabelText(aaa!![position])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var customCategory = CustomCategory()
            customCategory.table_name = edtNamaKategori.text.toString()
            if(spinnerTipeKategory.floatingLabelText.equals("INFORMASI")){
            customCategory.flag_nik_pk = "Y"
            }
            else{
                customCategory.flag_nik_pk = "N"

            }
            customCategory.flag_automated_table="Y"
            customCategory.flag_upload="Y"
            customCategory.attribute_title="TALENT_"+edtNamaKategori.text.toString().toUpperCase()+"_CUSTOM"


            val customCategoryViewModel = CustomCategoryViewModel( customCategory,dao.getNIK(),dao.getToken(), context!!)

            customCategoryViewModel.insertCustomCategory
                .observe(this, object : Observer<String?> {
                    override fun onChanged(currencyPojos: String?) {
                        dialog.dismiss()
                        loadAwardViewModel()
                    }
                })

        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }

}
