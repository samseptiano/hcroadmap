package com.samseptiano.hcroadmap.Fragment

import android.app.Dialog
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
import com.jaredrummler.materialspinner.MaterialSpinner
import com.samseptiano.hcroadmap.Adapter.LDPSettingAdapter
import com.samseptiano.hcroadmap.DataClass.CompanyList
import com.samseptiano.hcroadmap.DataClass.DirektoratList
import com.samseptiano.hcroadmap.DataClass.LDPSetting
import com.samseptiano.hcroadmap.DataClass.TargetPosisiList
import com.samseptiano.hcroadmap.MainActivity

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import kotlinx.android.synthetic.main.fragment_ldpmaster_setting.view.*


class LDPMasterSettingFragment : Fragment() {

    lateinit var rootView:View;
    private var ldpSettingList: MutableList<LDPSetting>? = null
    lateinit var ldpSettingAdapter:LDPSettingAdapter

    private var companyList: MutableList<String>? = null
    private var companyListObj: MutableList<CompanyList>? = null

     var compID:String=""
     var compGrpId:String=""
     var compName:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_ldpmaster_setting, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("LDP Master Setting")

        loadAwardViewModel()
        rootView.btnTambahLDP.setOnClickListener {
            showAddDialog()
        }
        return rootView
    }

    fun loadAwardViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "900800020"
        val ldpSettingViewModel = LDPSettingViewModel( null,dao.getCompGrpId(),dao.getToken(), context!!)

        ldpSettingViewModel.ldpSetting
            .observe(this, object : Observer<List<LDPSetting?>?> {
                override fun onChanged(currencyPojos: List<LDPSetting?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            ldpSettingList = mutableListOf()
                            ldpSettingList!!.addAll(currencyPojos as MutableList<LDPSetting>)
                            insertToRV()
                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        ldpSettingAdapter = ldpSettingList?.let {
            LDPSettingAdapter(
                it, context,activity,
                this.fragmentManager!!,this
            )
        }!!

        rootView.rvLDPSetting.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ldpSettingAdapter
        }


    }


    private fun showAddDialog(){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_ldp_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val spinnerCompany = dialog.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val edtNamaLDP = dialog.findViewById(R.id.edtNamaLDP) as EditText

        loadViewModel(dialog)

        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.floatingLabelText = companyList!![position]
                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()
                        compName = companyListObj!![i].compname.toString()
                        compGrpId = companyListObj!![i].compgrpid.toString()

                    }
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var ldpSetting = LDPSetting()
            ldpSetting.compgrpid = compGrpId
            ldpSetting.compid=compID
            ldpSetting.compname = compName
            ldpSetting.ldp_id="0"
            ldpSetting.ldp_title=edtNamaLDP.text.toString()
            ldpSetting.upduser=dao.getusername()

            val ldpSettingViewModel = LDPSettingViewModel(ldpSetting,dao.getCompGrpId(),dao.getToken(), context!!)

            ldpSettingViewModel.insertLdpSetting
                .observe(this, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        loadAwardViewModel()
                    }
                })

            dialog.dismiss()

        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }
    fun showEditDialog(ldpSetting: LDPSetting){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_ldp_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val spinnerCompany = dialog.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val edtNamaLDP = dialog.findViewById(R.id.edtNamaLDP) as EditText

        loadViewModel(dialog)

        Handler().postDelayed(
            {

                for (i in 0 until (companyListObj?.size ?: 0)) {
                    if (ldpSetting.compgrpid.equals(companyListObj?.get(i)?.compgrpid)) {
//                                spinnerCompany.setFloatingLabelText(companyListObjDialog?.get(0)?.compname)
//                                spinnerCompany.setHint(companyListObjDialog?.get(0)?.compname)
                        spinnerCompany.setSelection(i, false)
                    }
                }


            },
            2000 // value in milliseconds
        )



        edtNamaLDP.setText(ldpSetting.ldp_title.toString())

        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.floatingLabelText = companyList!![position]
                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()
                        compName = companyListObj!![i].compname.toString()
                        compGrpId = companyListObj!![i].compgrpid.toString()

                    }
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var ldpSettings = LDPSetting()
            ldpSettings.compgrpid = compGrpId
            ldpSettings.compid=compID
            ldpSettings.compname = compName
            ldpSettings.ldp_id=ldpSetting.ldp_id
            ldpSettings.ldp_title=edtNamaLDP.text.toString()
            ldpSettings.upduser=dao.getusername()

            val ldpSettingViewModel = LDPSettingViewModel(ldpSettings,dao.getCompGrpId(),dao.getToken(), context!!)

            ldpSettingViewModel.insertLdpSetting
                .observe(this, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                                //loadAwardViewModel()
                        loadAwardViewModel()
                    }
                })

            dialog.dismiss()

        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    fun insertToSpinner(dialog: Dialog){
        val spinnerCompany = dialog.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>

                val talentMappingViewModel = context?.let {
                    TalentMappingViewModel("","","","","","",companyListObj as MutableList<Any>,
                        it
                    )
                }

                talentMappingViewModel?.insertIntoCompanyList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            companyList = currencyPojos as MutableList<String>?
                            spinnerCompany!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
    }
    fun loadViewModel(dialog: Dialog){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","",null,
                    context!!
                )

                talentMappingViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObj = currencyPojos as MutableList<CompanyList>?
                            insertToSpinner(dialog)
                        }
                    })


    }

}
