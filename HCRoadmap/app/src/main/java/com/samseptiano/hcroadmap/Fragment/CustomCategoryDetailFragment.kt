package com.samseptiano.hcroadmap.Fragment

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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
import com.samseptiano.hcroadmap.Adapter.CustomCategoryAdapter
import com.samseptiano.hcroadmap.Adapter.CustomCategoryDetailAdapter
import com.samseptiano.hcroadmap.DataClass.CustomCategory
import com.samseptiano.hcroadmap.DataClass.CustomCategoryDetail

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.CustomCategoryViewModel
import kotlinx.android.synthetic.main.fragment_custom_category.view.*
import kotlinx.android.synthetic.main.fragment_custom_category_detail.view.*


class CustomCategoryDetailFragment : Fragment() {

    lateinit var rootView:View
    var tableName=""
    private var customCategoryDetailList: MutableList<CustomCategoryDetail>? = null
    lateinit var customCategoryDetailAdapter: CustomCategoryDetailAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tableName = arguments?.getString("TABLE_NAME").toString()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_custom_category_detail, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(tableName)


        rootView.btnAddCustomCategoryDetail.setOnClickListener {
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

        var aa = CustomCategoryDetail()
        aa.tablENAME = tableName

        val customCategoryViewModel = CustomCategoryViewModel( aa,dao.getNIK(),dao.getToken(), context!!)

        customCategoryViewModel.customCategoryDetail
            .observe(this, object : Observer<List<CustomCategoryDetail?>?> {
                override fun onChanged(currencyPojos: List<CustomCategoryDetail?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            customCategoryDetailList = mutableListOf()
                            customCategoryDetailList!!.addAll(currencyPojos as MutableList<CustomCategoryDetail>)
                            insertToRV()
                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        var customCategoryDetailAdapter = customCategoryDetailList?.let {
            CustomCategoryDetailAdapter(
                it, context,activity,
                this.fragmentManager!!,this
            )
        }!!

        rootView.rvCustomCategoryDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = customCategoryDetailAdapter
        }


    }


     fun showAddCategoryDialog(customCategoryDetail: CustomCategoryDetail?=null){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_custom_category_detail_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val edtNamaKategoriDetail = dialog.findViewById(R.id.edtNamaKategoriDetail) as EditText
        val spinnerDatatype = dialog.findViewById(R.id.spinnerDataType) as SmartMaterialSpinner<Any>

        var aaa = mutableListOf<String>()
        aaa.add("FILE")
        aaa.add("GAMBAR")
        aaa.add("TEXT")
        spinnerDatatype!!.setItem(aaa!! as List<Any>)

        if(customCategoryDetail!=null){
            edtNamaKategoriDetail.isEnabled = false
            edtNamaKategoriDetail.setText(customCategoryDetail.columNNAME)
            Handler().postDelayed(
                {

                    for (i in 0 until (aaa?.size ?: 0)) {
                        if (customCategoryDetail.columNTYPE.equals(aaa?.get(i))) {
//                                spinnerCompany.setFloatingLabelText(companyListObjDialog?.get(0)?.compname)
//                                spinnerCompany.setHint(companyListObjDialog?.get(0)?.compname)
                            spinnerDatatype.setSelection(i, false)
                        }
                    }


                },
                500 // value in milliseconds
            )
        }

        spinnerDatatype!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, aaa!![position], Toast.LENGTH_SHORT).show()
                spinnerDatatype.setFloatingLabelText(aaa!![position])
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var customCategory = CustomCategoryDetail()
            customCategory.tablENAME = "TALENT_"+tableName.replace(" ","_").toUpperCase()+"_CUSTOM"
            customCategory.columNNAME = edtNamaKategoriDetail.text.toString()
            customCategory.columNTYPE = spinnerDatatype.floatingLabelText.toString()
            customCategory.datatype = "varchar(1000)"

            val customCategoryViewModel = CustomCategoryViewModel( customCategory,dao.getNIK(),dao.getToken(), context!!)

            customCategoryViewModel.insertCustomCategoryDetail
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
