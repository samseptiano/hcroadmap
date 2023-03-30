package com.samseptiano.hcroadmap.Fragment

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appyvet.rangebar.RangeBar
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.EmpListSearchTalentAdapter
import com.samseptiano.hcroadmap.DataClass.*

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import kotlinx.android.synthetic.main.fragment_filter_talent_mapping_candidate.view.*
import kotlinx.android.synthetic.main.fragment_talent_profile_db.view.*
import kotlinx.android.synthetic.main.search_candidate_filter_dialog.*
import mabbas007.tagsedittext.TagsEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class TalentProfileDBFragment : Fragment() {


    lateinit var rootView: View
    lateinit var btnShowFilterDialog: Button
    lateinit var rvEmpList: RecyclerView

    var isExpandedFilter:Boolean = true

    //Parameter Filter
    var umurMin:Int?=0
    var umurMax:Int?=55
    var tahunMin:Int?=Calendar.getInstance().get(Calendar.YEAR)
    var tahunMax:Int?=(Calendar.getInstance().get(Calendar.YEAR)+65)

    lateinit var tagsPosisiAwalFilter:  MutableList<TargetPosisiList>
    lateinit var tagPendidikanFilter:  MutableList<String>
    lateinit var golonganFilter:  MutableList<String>

    var pendidikan:String?="ALL"
    var golongan:String?=""
    var posisiAwal:String?=""

    lateinit var compID:String
    lateinit var posID:String
    lateinit var dirID:String


    lateinit var golonganList: MutableList<String>
    lateinit var tagPendidikan: MutableList<String>
    lateinit var tagPosisiAwalList: MutableList<String>

    private var companyList: MutableList<String>? = null
    private var positionList: MutableList<String>? = null
    private var direktoratList: MutableList<String>? = null

    private var companyListObj: MutableList<CompanyList>? = null
    private var positionListObj: MutableList<TargetPosisiList>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null

    private var emp1Obj: MutableList<EmpTalentMapping>? = null


    private var empTalentMapping: MutableList<EmpTalentMapping>? = null
    private var empTalentMappingFilter: MutableList<EmpTalentMapping>? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_talent_profile_db, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Search Talent Mapping")
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })


        rootView.imgExpandFilter.setOnClickListener {
            if(isExpandedFilter){
                isExpandedFilter=false
                rootView.lnFilterArea.visibility = View.GONE

            }
            else{
                isExpandedFilter=true
                rootView.lnFilterArea.visibility = View.VISIBLE

            }
        }

        btnShowFilterDialog = rootView.findViewById(R.id.btnFilter)
        rvEmpList = rootView.findViewById(R.id.rvTalentProfileDB)


        compID = arguments?.getString("COMP_ID").toString()
        posID = arguments?.getString("POS_ID").toString()
        dirID = arguments?.getString("DIR_ID").toString()


        context?.let { showDialogFilter(it) }

        btnShowFilterDialog.setOnClickListener {
            context?.let { it1 -> this.showDialogFilter(it1) }
        }


//        rootView.edtSearch.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    var charText = s.toString().toLowerCase(Locale.getDefault())
//                    empTalentMapping = mutableListOf()
//                    if (charText.isEmpty()) {
//                        empTalentMapping = empTalentMappingFilter
//                    } else {
//                        for (i in 0 until empTalentMappingFilter!!.size) {
//                            if (empTalentMappingFilter?.get(i)?.empname.toString().toLowerCase(Locale.getDefault())
//                                    .contains(charText)
//                            ) {
//                                empTalentMapping?.add(empTalentMappingFilter?.get(i)!!)
//                            }
//                        }
//                    }
//                insertToRV()
//
//            }
//        })

        loadEmployee()



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
                empTalentMapping = mutableListOf()
                if (charText.isEmpty()) {
                    empTalentMapping = empTalentMappingFilter
                } else {
                    for (i in 0 until empTalentMappingFilter!!.size) {
                        if (empTalentMappingFilter?.get(i)?.empname.toString().toLowerCase(Locale.getDefault())
                                .contains(charText)
                        ) {
                            empTalentMapping?.add(empTalentMappingFilter?.get(i)!!)
                        }
                    }
                }
                insertToRV()
                return true
            }
        })
    }


    private fun showDialogFilter(ctx:Context) {
        val dialog = Dialog(ctx, R.style.AppBaseTheme)



        umurMax=65
        umurMin=0
        golongan=""
        posisiAwal=""



        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.talent_profile_filter_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val btnReset = dialog.findViewById(R.id.btnReset) as Button


        initSpinner(dialog)
        initTagGolongan(dialog)


        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnReset.setOnClickListener {
            initSpinner(dialog)
            initTagGolongan(dialog)
        }
        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            val tagsGolongan = dialog.findViewById(R.id.tagsGolongan) as TagsEditText
            golonganFilter = mutableListOf()

            for (i in tagsGolongan.getTags().indices) {
                golonganFilter.add(tagsGolongan.getTags().get(i))
                golongan+= tagsGolongan.getTags().get(i)+","

            }

            val tagsPosisiAwal = dialog.findViewById(R.id.tagsPosisi) as TagsEditText
            tagsPosisiAwalFilter = mutableListOf()
            var monthList: List<TargetPosisiList> = mutableListOf()
            for (i in tagsPosisiAwal.getTags().indices) {
//                tagsPosisiAwalFilter.add(tagsPosisiAwal.getTags().get(i))

                if(tagsPosisiAwal.getTags().get(i).contains("ALL")){
                    var aaa = TargetPosisiList()
                    aaa.jobttlcode="ALL"
                    aaa.jobttlname="ALL"
                    aaa.jobttlid="ALL"
                    tagsPosisiAwalFilter.add(aaa)

                }
                else{
                    monthList = positionListObj!!.filter { s -> s.jobttlname == tagsPosisiAwal.getTags().get(i) }


                    for (i in 0 until monthList.size) {
                        tagsPosisiAwalFilter.add(monthList.get(i))

                    }
                }


            }


            for (i in 0 until tagsPosisiAwalFilter.size) {
                posisiAwal+= tagsPosisiAwalFilter.get(i).jobttlid+","

            }


            var ep = EmpListFilter()
            ep.compgrpid=compID
            ep.direktoratid=dirID
            ep.jobttlid = posisiAwal
            ep.golongan = golongan
            ep.pendidikan = pendidikan
            ep.umurmax = umurMax.toString()
            ep.umurmin = umurMin.toString()
            ep.pensiunmax = tahunMax.toString()
            ep.pensiunmin = tahunMin.toString()
            ep.compid = compID
            ep.empnik = dao.getNIK()
            APIConfig().getService()
                .getUsersFiltered(ep,"Bearer "+dao.getToken())!!
                .enqueue(object : Callback<List<EmpTalentMapping?>> {
                    override fun onFailure(call: Call<List<EmpTalentMapping?>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<EmpTalentMapping?>>,
                        response: Response<List<EmpTalentMapping?>>
                    ) {
                        if(response.body()!=null){
                            empTalentMapping= mutableListOf()
                            empTalentMappingFilter= mutableListOf()
                            empTalentMappingFilter = response.body() as MutableList<EmpTalentMapping>?
                            empTalentMapping = response.body() as MutableList<EmpTalentMapping>?
                            insertToRV()
                        }
                    }
                })

            positionListObj
            dialog.dismiss()

        }

    }


    private fun initSpinner(dialog: Dialog) {
        val spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaan) as SmartMaterialSpinner<Any>
        val spinnerDirektorat = dialog.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>

        companyList = mutableListOf<String>()
        positionList = mutableListOf<String>()
        direktoratList = mutableListOf()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        loadViewModel("COMPANY",dialog)


        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerCompany.setFloatingLabelText(companyList!![position])

                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()
                    }
                }

                loadViewModel("POSITION",dialog)
                loadViewModel("DIREKTORAT",dialog)

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spinnerDirektorat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDirektorat.setFloatingLabelText(direktoratList!![position])

                if(direktoratListObj?.size ?:0>0) {
                    for (i in 0 until direktoratListObj!!.size) {
                        if (direktoratListObj!![i].direktoratname!!.equals(spinnerDirektorat.floatingLabelText)) {
                            dirID = direktoratListObj!![i].direktoratid.toString()
                        }
                        else if(spinnerDirektorat.floatingLabelText.equals("ALL")){
                            dirID="ALL"
                        }
                    }
                }

                spinnerDirektorat.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }


    private fun initTagGolongan(dialog: Dialog) {
        val tagsGolongan = dialog.findViewById(R.id.tagsGolongan) as TagsEditText
        val ctx: Context? = context


        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        APIConfig().getService()
            .getgolonganPendidikan(dao.getNIK(),dao.getCompGrpId(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<GolonganPendidikan>> {
                override fun onFailure(call: Call<List<GolonganPendidikan>>, t: Throwable) {
                    //Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<List<GolonganPendidikan>>,
                    response: Response<List<GolonganPendidikan>>
                ) {

                    if(response.body()!=null){
                        //================== G0LONGAN ========================================
                        golonganList = mutableListOf<String>()
                        tagsGolongan.setText("")

                        golonganList.add("ALL")
                        for (i in 0 until response.body()!![0].joblevel!!.size){
                            golonganList.add(response.body()!![0].joblevel!![i].joblevel!!)
                        }

                        val dataAdapter2: ArrayAdapter<String> = ArrayAdapter<String>(
                            ctx!!,
                            android.R.layout.simple_spinner_dropdown_item,
                            golonganList
                        )

                        tagsGolongan.setTagsWithSpacesEnabled(true)
                        tagsGolongan.setAdapter(dataAdapter2)
                        tagsGolongan.setThreshold(1)

                        //====================================================================

                    }
                }
            })


    }


    private fun loadEmployee(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        APIConfig().getService()
            .getUsers(dirID, compID,dao.getNIK(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<EmpTalentMapping>> {
                override fun onFailure(call: Call<List<EmpTalentMapping>>, t: Throwable) {
//                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<List<EmpTalentMapping>>,
                    response: Response<List<EmpTalentMapping>>
                ) {

                    if(response.body()!=null){
                        empTalentMappingFilter = response.body() as MutableList<EmpTalentMapping>?

                        empTalentMapping = response.body() as MutableList<EmpTalentMapping>?
                        insertToRV()

                    }
                }
            })




//        val listEmpListAdapter = listOf(
//            EmpList(
//                id="1",
//                empNIK = "1",
//                empName = "Thor",
//                empDept = "Odinson",
//                empBranch = "JotunHeim",
//                empCompany = "Avengers",
//                empPhoto = "https://www.greenscene.co.id/wp-content/uploads/2019/04/thor-1.jpg",
//                empAge = "35",
//                empOrg = "Nord Myth",
//                empJobTitle = "God Of Thunder",
//                compGroupID = "4")
//        )

//        val employeeAdapter =  EmpListSearchTalentAdapter(listEmpListAdapter, context,activity,
//            this.fragmentManager!!,posID
//        )
//
//        rvEmpList.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = employeeAdapter
//        }
    }


    fun insertToRV():Unit{
        var listEmpListAdapter = mutableListOf<EmpList>()
        for (i in 0 until empTalentMapping?.size!!){
            var empList = EmpList()
            empList.id = i.toString()
            empList.empNIK = empTalentMapping!![i].empnik
            empList.empName = empTalentMapping!![i].empname
            empList.empDept = empTalentMapping!![i].direktoratname
            empList.empBranch = empTalentMapping!![i].emporgname
            empList.empCompany = empTalentMapping!![i].locationname
            empList.empPhoto = empTalentMapping!![i].empphoto
            empList.empAge = empTalentMapping!![i].age
            empList.empOrg = empTalentMapping!![i].emporgname
            empList.empJobTitle = empTalentMapping!![i].empjobtitlename
            empList.compGroupID = empTalentMapping!![i].compgrpid
            empList.empJobLevel = empTalentMapping!![i].empjoblvl
            empList.posId = empTalentMapping!![i].posid
            empList.compID = empTalentMapping!![i].compid
            empList.archievement = empTalentMapping!![i].speciaLACHV
            empList.recommendation = empTalentMapping!![i].reCREASON

            empList.dirId = dirID


            var dataDiri: EmpListDataDiri = EmpListDataDiri()
            dataDiri.empSignDate = empTalentMapping!![i].empsigndate
            dataDiri.empLastPromotion = empTalentMapping!![i].lastpromotiondate
            dataDiri.location = empTalentMapping!![i].locationname
            dataDiri.emplastEducation = empTalentMapping!![i].lasteducation
            dataDiri.empHomeBase = empTalentMapping!![i].homebase
            dataDiri.empBirthPlace = empTalentMapping!![i].empbirthplace
            dataDiri.empBirthDate = empTalentMapping!![i].empdatebirth
            dataDiri.empAge = empTalentMapping!![i].age
            dataDiri.empMaritalStatus = empTalentMapping!![i].maritalstatus
            dataDiri.empRetirementDate = empTalentMapping!![i].retirementdatae
            dataDiri.compGroupID = empTalentMapping!![i].compgrpid
            empList.dataDiri = dataDiri

            listEmpListAdapter.add(empList)
        }


        val employeeAdapter =  EmpListSearchTalentAdapter(listEmpListAdapter, context,activity,
            this.fragmentManager,posID, "TALENTPROFILEDB"
        )

        rvEmpList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = employeeAdapter
        }
    }


    fun loadViewModel(type:String,dialog: Dialog){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        when(type){
            "COMPANY" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),"ALL","","","",null,
                    context!!
                )

                talentMappingViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObj = currencyPojos as MutableList<CompanyList>?
                            insertToSpinner("COMPANY",dialog)


                        }
                    })
            }
            "POSITION" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )

                talentMappingViewModel.targetposisi
                    .observe(this, object : Observer<List<TargetPosisiList?>?> {
                        override fun onChanged(currencyPojos: List<TargetPosisiList?>?) {
                            positionListObj = currencyPojos as MutableList<TargetPosisiList>?
                            insertToSpinner("POSITION",dialog)

                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )
                talentMappingViewModel.direktoratAll
                    .observe(this, object : Observer<List<DirektoratList?>?> {
                        override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                            direktoratListObj = currencyPojos as MutableList<DirektoratList>?
                            insertToSpinner("DIREKTORAT",dialog)
                        }
                    })
            }
        }

    }

    fun insertToSpinner(type:String, dialog: Dialog){
        val spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaan) as SmartMaterialSpinner<Any>
        val tagsPosisi = dialog.findViewById(R.id.tagsPosisi) as TagsEditText
        val spinnerDirektirat = dialog.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>

        when(type){
            "COMPANY" ->{
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
            "POSITION" ->{
                val talentMappingViewModel = context?.let {
                    TalentMappingViewModel("","","","","","",positionListObj as MutableList<Any>,
                        it
                    )
                }

                talentMappingViewModel?.insertIntoTargetPosisiList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            positionList  = currencyPojos as MutableList<String>?
                            positionList?.add(0,"ALL")
                            val dataAdapter2: ArrayAdapter<String> = ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_spinner_dropdown_item,
                                positionList as List<String>
                            )


                            tagsPosisi.setTagsWithSpacesEnabled(true)
                            tagsPosisi.setAdapter(dataAdapter2)
                            tagsPosisi.setThreshold(1)
                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentMappingViewModel = context?.let {
                    TalentMappingViewModel("","","","","","",direktoratListObj as MutableList<Any>,
                        it
                    )
                }

                talentMappingViewModel?.insertintoDirektoratList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            direktoratList = currencyPojos as MutableList<String>?
                            direktoratList?.add(0,"ALL")
                            spinnerDirektirat!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
        }
    }

}
