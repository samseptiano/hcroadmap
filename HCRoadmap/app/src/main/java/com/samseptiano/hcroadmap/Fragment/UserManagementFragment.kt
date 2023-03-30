package com.samseptiano.hcroadmap.Fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.LDPSettingAdapter
import com.samseptiano.hcroadmap.Adapter.UserManagementAdapter
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.MainActivity

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Repo.UserManagementRepo
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.LDPSettingViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import com.samseptiano.hcroadmap.ViewModel.UserManagementViewModel
import kotlinx.android.synthetic.main.add_user_management_dialog.*
import kotlinx.android.synthetic.main.fragment_ldpmaster_setting.view.*
import kotlinx.android.synthetic.main.fragment_user_management.view.*
import kotlinx.android.synthetic.main.talent_review_detail_fragment.*
import mabbas007.tagsedittext.TagsEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class UserManagementFragment : Fragment() {


    lateinit var rootView:View
    private var userManagementList: MutableList<UserManagement>? = null
    private var userManagementFilter: MutableList<UserManagement>? = null

    private var empObj: MutableList<EmpTalentMapping>? = null


    lateinit var userManagementAdapter:UserManagementAdapter

    private var companyList: MutableList<String>? = null
    private var direktoratList: MutableList<String>? = null

    private var companyListDialog: MutableList<String>? = null
    private var direktoratListDialog: MutableList<String>? = null

    var compID:String?=""
    var dirID:String?=""

    private var companyListObj: MutableList<CompanyList>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null

    private var companyListObjDialog: MutableList<CompanyList>? = null
    private var direktoratListObjDialog: MutableList<DirektoratList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("User Management Setting")
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_user_management, container, false)
        loadViewModel("COMPANY")
        initSpinner()
        loadAwardViewModel()
        rootView.btnTambahPrivilege.setOnClickListener {
            showAddDialog()
        }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val mSearch = menu.findItem(R.id.mi_search)
        val mSearchView =
            mSearch.actionView as SearchView
        mSearchView.queryHint = "Search"
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                var charText = s.toString().toLowerCase(Locale.getDefault())
                userManagementList = mutableListOf()
                if (charText.isEmpty()) {
                    userManagementList = userManagementFilter
                } else {
                    for (i in 0 until userManagementFilter!!.size) {
                        if (userManagementFilter?.get(i)?.empname.toString().toLowerCase(Locale.getDefault())
                                .contains(charText)
                        ) {
                            userManagementList?.add(userManagementFilter?.get(i)!!)
                        }
                    }
                }
                insertToRV()
                //userManagementList?.let { inser(it) }
                return true
            }
        })
    }


    public fun loadAwardViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "900800020"
        val userManagementViewModel = UserManagementViewModel( null,"0","100",dao.getToken(), context!!)

        userManagementViewModel.userManagement
            .observe(this, object : Observer<List<UserManagement?>?> {
                override fun onChanged(currencyPojos: List<UserManagement?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            userManagementFilter = mutableListOf()
                            userManagementList = mutableListOf()
                            userManagementList!!.addAll(currencyPojos as MutableList<UserManagement>)
                            userManagementFilter!!.addAll(currencyPojos as MutableList<UserManagement>)

                            insertToRV()
                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        userManagementAdapter = userManagementList?.let {
            UserManagementAdapter(
                it, context,activity,
                this.fragmentManager!!,this
            )
        }!!

        rootView.rvUserManagement.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userManagementAdapter
        }


    }

    private fun initSpinner() {
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerDirektorat =
            rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>



        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.setFloatingLabelText(companyList!![position])

                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()

                        direktoratList = mutableListOf()
                        direktoratListObj = mutableListOf()
                        loadViewModel("DIREKTORAT")


                    }
                }

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerDirektorat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDirektorat.setFloatingLabelText(direktoratList!![position])

                for(i in 0 until direktoratListObj!!.size){
                    if(direktoratListObj!![i].direktoratname!!.equals(spinnerDirektorat.floatingLabelText)){
                        dirID = direktoratListObj!![i].direktoratid.toString()

                    }
                }
                Toast.makeText(context, direktoratList!![position], Toast.LENGTH_SHORT).show()
                spinnerDirektorat.setErrorText("")

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    fun loadViewModel(type:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        when(type){
            "COMPANY" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObj = currencyPojos as MutableList<CompanyList>?
                            insertToSpinner("COMPANY")


                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.direktorat
                    .observe(this, object : Observer<List<DirektoratList?>?> {
                        override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                            direktoratListObj = currencyPojos as MutableList<DirektoratList>?
                            insertToSpinner("DIREKTORAT")
                        }
                    })
            }
        }

    }

    fun insertToSpinner(type:String){
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerDirektorat = rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>

        when(type){
            "COMPANY" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",companyListObj as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertIntoCompanyList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            companyList = currencyPojos as MutableList<String>?
                            spinnerCompany!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",direktoratListObj as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertintoDirektoratList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            direktoratList = currencyPojos as MutableList<String>?
                            spinnerDirektorat!!.setItem(currencyPojos!! as List<Any>)

                        }
                    })
            }
        }
    }

    fun showAddDialog(usr:UserManagement?=null){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_user_management_dialog)
        dialog!!.show()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        var emp1List: MutableList<String>? = mutableListOf()
        var dirListObjFilter = mutableListOf<DirektoratList>()
        compID = "1"

        var millis = 2000

        val btnOk = dialog.findViewById(R.id.btnOK) as Button
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val spinnerFlagSalary = dialog.findViewById(R.id.spinnerFlagSalary) as SmartMaterialSpinner<Any>
        val spinnerUpload = dialog.findViewById(R.id.spinnerFlagUpload) as SmartMaterialSpinner<Any>
        val spinnerPICMapping = dialog.findViewById(R.id.spinnerFlagPICMapping) as SmartMaterialSpinner<Any>
        val spinnerRole = dialog.findViewById(R.id.spinnerRole) as SmartMaterialSpinner<Any>
        val spinnerPIC = dialog.findViewById(R.id.spinnerNamaPIC) as SmartMaterialSpinner<Any>
        val tagsDirektoratMapping = dialog.findViewById(R.id.tagsDirektoratMapping) as TagsEditText

        var empChoice = mutableListOf<EmpTalentMapping>()
        var aa:MutableList<String> = mutableListOf()
        aa.add("YES")
        aa.add("NO")
        spinnerFlagSalary.setItem(aa!! as List<Any>)
        spinnerUpload.setItem(aa!! as List<Any>)
        spinnerPICMapping.setItem(aa!! as List<Any>)

        var bb:MutableList<String> = mutableListOf()
        bb.add("SUPER ADMIN")
        bb.add("ADMIN")
        bb.add("NON ADMIN")
        spinnerRole.setItem(bb!! as List<Any>)





        //=========== KARYAWAN ============================

//        if(empObj == null) {
//            millis=3000
            APIConfig().getService()
                .getUsers("ALL", "ALL", dao.getNIK(), "Bearer " + dao.getToken())
                .enqueue(object : Callback<List<EmpTalentMapping>> {
                    override fun onFailure(call: Call<List<EmpTalentMapping>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<EmpTalentMapping>>,
                        response: Response<List<EmpTalentMapping>>
                    ) {

                        if (response.body() != null) {
                            emp1List = mutableListOf()

                            empObj = response.body() as MutableList<EmpTalentMapping>
                            for (i in 0 until empObj?.size!!) {
                                emp1List?.add(empObj!![i].empname.toString())
                            }
                            spinnerPIC.item = (emp1List as List<Any>?)
                        }
                    }
                })
//        }
        //================================================

        direktoratList = mutableListOf()
        direktoratListObj = mutableListOf()
        loadViewModel("DIREKTORAT")
        Handler().postDelayed({
            val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_spinner_dropdown_item,
                this!!.direktoratList!!
            )

            tagsDirektoratMapping.setTagsWithSpacesEnabled(true)
            tagsDirektoratMapping.setAdapter(dataAdapter)
            tagsDirektoratMapping.setThreshold(1)

            if(usr!=null){
                var xx = EmpTalentMapping()
                xx.empname = usr.empname
                xx.empnik = usr.nik
                empChoice.add(xx)
//                for (i in 0 until (empObj?.size ?: 0)) {
//                    if (usr.nik.equals(empObj?.get(i)?.empnik)) {
//                        spinnerPIC.setSelection(i, false)
//                    }
//                }
//                var monthList = empObj!!.filter { s -> s.empnik == usr.nik }
//                var index = empObj!!.indexOf(monthList[0])
//                spinnerPIC.setSelection(index, false)
                  spinnerPIC.setHint(usr.empname)
                  spinnerPIC.setFloatingLabelText(usr.empname)

                for (i in 0 until (aa?.size ?: 0)) {
                    if (aa?.get(i).contains(usr.flagpic.toString())) {
                        spinnerPICMapping.setSelection(i, false)
                    }
                    if (aa?.get(i).contains(usr.flagsalary.toString())) {
                        spinnerFlagSalary.setSelection(i, false)
                    }
                    if (aa?.get(i).contains(usr.flaguploadstruktur.toString())) {
                        spinnerUpload.setSelection(i, false)
                    }
                }

                for (i in 0 until (bb?.size ?: 0)) {
                    if (usr.role?.split("-")?.get(1).equals(bb?.get(i))) {
                        spinnerRole.setSelection(i, false)
                    }
                }

                if(usr.usermanagementorg!=null){
                    var xxx = mutableListOf<String>()
                    for(i in 0 until usr.usermanagementdir!!.size){
                        xxx.add(usr.usermanagementdir!![i]?.dirname.toString())
                    }
                    tagsDirektoratMapping.setTags(xxx.toTypedArray())
                }

            }

        }, millis.toLong())


        spinnerPIC!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, emp1List?.get(position), Toast.LENGTH_SHORT).show()
                spinnerPIC.floatingLabelText = emp1List!![position]

                    empChoice = empObj!!.filter { s -> s.empname == spinnerPIC.floatingLabelText } as MutableList<EmpTalentMapping>


            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerFlagSalary!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, aa?.get(position), Toast.LENGTH_SHORT).show()
                spinnerFlagSalary.floatingLabelText = aa!![position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerPICMapping!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, aa?.get(position), Toast.LENGTH_SHORT).show()
                spinnerPICMapping.floatingLabelText = aa!![position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerUpload!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, aa?.get(position), Toast.LENGTH_SHORT).show()
                spinnerUpload.floatingLabelText = aa!![position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerRole!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, bb?.get(position), Toast.LENGTH_SHORT).show()
                spinnerRole.floatingLabelText = bb!![position]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        btnOk.setOnClickListener {


            var monthList: List<DirektoratList> = mutableListOf()
            for (i in tagsDirektoratMapping.getTags().indices) {

                monthList = direktoratListObj!!.filter { s -> s.direktoratname == tagsDirektoratMapping.getTags().get(i) }

                for (i in 0 until monthList.size) {
                    dirListObjFilter.add(monthList.get(i))
                }
            }

            var listDir = mutableListOf<UsermanagementdirItem>()
            for (i in 0 until dirListObjFilter.size) {
                var aa = UsermanagementdirItem()
                aa.dirid = dirListObjFilter[i].direktoratid
                aa.dirname = dirListObjFilter[i].direktoratname
                listDir.add(aa)
            }

            var aaa = UserManagement();
            aaa.nik = empChoice[0].empnik
            aaa.empname = empChoice[0].empname
            aaa.compgrpid = dao.getCompGrpId()
            aaa.flagpic = spinnerPICMapping.floatingLabelText.substring(0,1)
            aaa.flagsalary = spinnerFlagSalary.floatingLabelText.substring(0,1)
            aaa.flaguploadstruktur = spinnerUpload.floatingLabelText.substring(0,1)
            aaa.role = spinnerRole.floatingLabelText.toString()
            aaa.upduser = dao.getusername()
            aaa.groupcode = ""

            aaa.usermanagementdir = listDir
            if (usr != null) {
                aaa.usermanagementorg = usr.usermanagementorg
            }
            val userManagementViewModel = UserManagementViewModel(aaa,"0","0",dao.getToken(),
                context!!
            )

            userManagementViewModel.insertuserManagement
                .observe(this, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        dialog.dismiss()
                       loadAwardViewModel()
                    }
                })

        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }

}
