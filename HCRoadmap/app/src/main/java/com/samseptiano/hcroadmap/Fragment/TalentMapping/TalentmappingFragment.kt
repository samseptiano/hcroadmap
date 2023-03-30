package com.samseptiano.hcroadmap.Fragment.TalentMapping

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appyvet.rangebar.RangeBar
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.EmpListSearchTalentAdapter
import com.samseptiano.hcroadmap.Adapter.EmpListTalentAdapter
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Fragment.SearchCandidateFragment
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoom
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_talentmapping.*
import kotlinx.android.synthetic.main.fragment_talentmapping.view.*
import kotlinx.android.synthetic.main.search_candidate_filter_dialog.*
import kotlinx.android.synthetic.main.show_pdf_fullscreen_dialog.*
import kotlinx.android.synthetic.main.show_structure_fullscreen_dialog.*
import mabbas007.tagsedittext.TagsEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class TalentmappingFragment : Fragment() {
    lateinit var rvEmpList: RecyclerView


    private var filterTalentMappingCandidateFragment =
        FilterTalentMappingCandidateFragment()


    private var searchCandidateFragment =
        SearchCandidateFragment()

    lateinit var rootView:View

    lateinit var btnExpandFilter: CardView
    lateinit var btnFilterSearchCandidate: ImageButton

    lateinit var employeeAdapter:EmpListTalentAdapter

    var compID:String?="0"
    var posID:String?="0"
    var dirID:String?="0"



    //Parameter Filter
    var umurMin:Int?=0
    var umurMax:Int?=65
    var tahunMin:String?=""
    var tahunMax:String?=""
    var pendidikan:String?=""
    var golongan:String?=""
    var posisiAwal:String?=""

    lateinit var tagsPosisiAwalFilter:  MutableList<TargetPosisiList>
    lateinit var tagPendidikanFilter:  MutableList<String>
    lateinit var golonganFilter:  MutableList<String>

    lateinit var golonganList: MutableList<String>
    lateinit var tagPendidikan: MutableList<String>
    lateinit var tagPosisiAwalList: MutableList<String>

    private var empTalentMappingFilter: MutableList<EmpTalentMapping>? = null

    private var emp1Obj: MutableList<EmpTalentMapping>? = null
    private var empTalentMapping: MutableList<EmpTalentMapping>? = null


    private var companyList: MutableList<String>? = null
    private var positionList: MutableList<String>? = null
    private var direktoratList: MutableList<String>? = null

    private var companyListObj: MutableList<CompanyList>? = null
    private var positionListObj: MutableList<TargetPosisiList>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null
    var talentMappingAfterFilter:MutableList<EmpTalentMappingAfter> = mutableListOf()
    var talentMappingAfter:MutableList<EmpTalentMappingAfter> = mutableListOf()
    var listEmpListAdapter:MutableList<EmpList> = mutableListOf()


    var isExpanded:Boolean=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_talentmapping, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Talent Mapping")
        btnFilterSearchCandidate = rootView.findViewById(R.id.btnFilter)
        rvEmpList = rootView.findViewById(R.id.rvMapping)

        posID = (activity as MainActivity?)?.globals?.posId
        dirID = (activity as MainActivity?)?.globals?.dirId
        compID = (activity as MainActivity?)?.globals?.compId

        loadViewModel("COMPANY")
        initSpinner()
        loadEmployee()

        rootView.btnSearchCandidate.visibility = View.GONE
        rootView.btnTalentMapping.visibility = View.GONE

        rootView.btnSearchCandidate.setOnClickListener {
            val args = Bundle()

            (activity as MainActivity?)?.globals?.posId = posID
            (activity as MainActivity?)?.globals?.dirId = dirID
            (activity as MainActivity?)?.globals?.compId = compID

            args.putString("COMP_ID", compID)
            args.putString("POS_ID", posID)
            args.putString("DIR_ID", dirID)

            searchCandidateFragment.arguments=args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.myFragment, searchCandidateFragment)
            transaction?.addToBackStack("SearchCandidateFragment")
            transaction?.commit()
        }
        rootView.btnTalentMapping.setOnClickListener {
            val args = Bundle()

            (activity as MainActivity?)?.globals?.posId = posID
            (activity as MainActivity?)?.globals?.dirId = dirID
            (activity as MainActivity?)?.globals?.compId = compID

            args.putString("COMP_ID", compID)
            args.putString("POS_ID", posID)
            args.putString("DIR_ID", dirID)

            filterTalentMappingCandidateFragment.arguments=args
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.myFragment, filterTalentMappingCandidateFragment)
            transaction?.addToBackStack("FilterTalentMappingCandidateFragment")
            transaction?.commit()
        }





        rootView.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
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
            }
        })


        if(compID!="0" && posID!="0" && dirID != "0"){
            rootView.btnStruktur.visibility=View.VISIBLE
            rootView.btnSearchCandidate.visibility = View.VISIBLE
            rootView.btnTalentMapping.visibility = View.VISIBLE
        }


        rootView.btnCompare.setOnClickListener {
            showCompareDialog()
        }

        rootView.btnFilter.setOnClickListener {
            context?.let { it1 -> showDialogFilter(it1) }
        }

        rootView.btnStruktur.setOnClickListener {
            showStructureDialog()
        }

        btnFilterSearchCandidate.setOnClickListener {


            if(compID!="0" && posID!="0" && dirID!="0"){

                context?.let { showDialogFilter(it) }

            }
            else {
                Toast.makeText(context,"Company, Direktorat dan target Posisi belum dipilih",Toast.LENGTH_SHORT).show()
            }

        }

        rootView.imgTalentMapping.setOnClickListener {

                val args = Bundle()

                (activity as MainActivity?)?.globals?.posId = posID
                (activity as MainActivity?)?.globals?.dirId = dirID
                (activity as MainActivity?)?.globals?.compId = compID

                args.putString("COMP_ID", compID)
                args.putString("POS_ID", posID)
                args.putString("DIR_ID", dirID)

                filterTalentMappingCandidateFragment.arguments=args
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, filterTalentMappingCandidateFragment)
                transaction?.addToBackStack("FilterTalentMappingCandidateFragment")
                transaction?.commit()
        }
        return rootView
    }





    fun initSpinner() {
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerTargetposisi =
            rootView.findViewById(R.id.spinnerPosition) as SmartMaterialSpinner<Any>
        val spinnerDirektirat =
            rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>


        if(!(activity as MainActivity?)?.globals?.direktoratState.equals("")) {
            spinnerDirektirat.setHint((activity as MainActivity?)?.globals?.direktoratState)
            spinnerCompany.setHint((activity as MainActivity?)?.globals?.companyState)
            spinnerTargetposisi.setHint((activity as MainActivity?)?.globals?.positionState)
        }

        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.setFloatingLabelText(companyList!![position])
                (activity as MainActivity?)?.globals?.companyState = companyList!![position]

                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()

                        positionList = mutableListOf()
                        positionListObj = mutableListOf()
                        loadViewModel("POSITION")


                    }
                }

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spinnerDirektirat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDirektirat.setFloatingLabelText(direktoratList!![position])
                (activity as MainActivity?)?.globals?.direktoratState = direktoratList!![position]

                for(i in 0 until direktoratListObj!!.size){
                    if(direktoratListObj!![i].direktoratname!!.equals(spinnerDirektirat.floatingLabelText)){
                        dirID = direktoratListObj!![i].direktoratid.toString()
                    }
                }
                Toast.makeText(context, direktoratList!![position], Toast.LENGTH_SHORT).show()
                spinnerDirektirat.setErrorText("")
                rootView.btnStruktur.visibility=View.VISIBLE
                talentMappingAfter = mutableListOf()
                //loadData()
                loadEmployee()
                rootView.btnSearchCandidate.visibility = View.VISIBLE
                rootView.btnTalentMapping.visibility = View.VISIBLE
            }
            //Uploads/OrganizationStructure/StrukturOrganisasi_4_1.pdf

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spinnerTargetposisi!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerTargetposisi.setFloatingLabelText(positionList!![position])
                (activity as MainActivity?)?.globals?.positionState = positionList!![position]

                for(i in 0 until positionListObj!!.size){
                    if(positionListObj!![i].jobttlname!!.equals(spinnerTargetposisi.floatingLabelText)){
                        posID = positionListObj!![i].jobttlid.toString()

                        direktoratList = mutableListOf()
                        direktoratListObj = mutableListOf()

                        loadViewModel("DIREKTORAT")
                    }
                }
                Toast.makeText(context, positionList!![position], Toast.LENGTH_SHORT).show()
                spinnerTargetposisi.setErrorText("")

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }


    fun expandFilter(){
        if (isExpanded){
            lnFilterArea.visibility =View.GONE
            isExpanded=false
        }
        else{
            lnFilterArea.visibility =View.VISIBLE
            isExpanded=true
        }
    }

    fun getDataFromRoom(){
        val database = context?.let { EmpListRoomDatabase.getDatabase(it) }
        val dao = database?.getEmpListDao()
        val listItems = arrayListOf<EmpListRoom>()
        if (dao != null) {
            listItems.addAll(dao.getAll())
        }

        //initRv(listItems)
    }




    private fun initRv(listUserLoginAdapter: MutableList<EmpListRoom>){

        var listEmpListAdapters:MutableList<EmpList> = mutableListOf()
        for (i in listUserLoginAdapter.indices) {
            var empList = EmpList()
            empList?.id = listUserLoginAdapter[i].id.toString()
            empList?.empNIK = listUserLoginAdapter[i].empNIK
            empList?.empName = listUserLoginAdapter[i].empName
            empList?.empJobTitle = listUserLoginAdapter[i].empJobTitle
            empList?.empDept = listUserLoginAdapter[i].empDept
            empList?.compGroupID = listUserLoginAdapter[i].compGroupID
            empList?.empCompany = listUserLoginAdapter[i].empCompany
            empList?.empAge = listUserLoginAdapter[i].empAge
            empList?.empPhoto = listUserLoginAdapter[i].empPhoto
            empList?.empBranch = listUserLoginAdapter[i].empBranch
            empList?.archievement = listUserLoginAdapter[i].archievement
            empList?.recommendation = listUserLoginAdapter[i].recommendation
            empList?.empOrg = listUserLoginAdapter[i].empOrg
            empList?.compID = compID
            empList?.dirId = dirID
            empList?.posId = posID


            listEmpListAdapters?.add(empList)
        }

        val employeeAdapter = listEmpListAdapters?.let { EmpListTalentAdapter(it, context,activity,this.fragmentManager!!,this) }

        rvEmpList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = employeeAdapter
        }
    }


    fun loadViewModel(type:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        when(type){
            "COMPANY" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","",null,
                    context!!
                )

                talentMappingViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObj = currencyPojos as MutableList<CompanyList>?
//                            companyList = mutableListOf()
//                            for (i in 0 until currencyPojos?.size!!){
//                                companyList!!.add(currencyPojos!![i]?.compname.toString())
//                            }
//                            spinnerCompany!!.setItem(companyList!! as List<Any>)


                                insertToSpinner("COMPANY")


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
//                            positionList = mutableListOf()
//
//                            for (i in 0 until currencyPojos?.size!!){
//                                positionList!!.add(currencyPojos!![i]?.jobttlname.toString())
//                            }
//                            spinnerTargetposisi!!.setItem(positionList!! as List<Any>)

                            insertToSpinner("POSITION")

                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )

                talentMappingViewModel.direktorat
                    .observe(this, object : Observer<List<DirektoratList?>?> {
                        override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                            direktoratListObj = currencyPojos as MutableList<DirektoratList>?
//                            direktoratList = mutableListOf()
//                            for (i in 0 until currencyPojos?.size!!){
//                                direktoratList!!.add(currencyPojos!![i]?.direktoratname.toString())
//                            }
//                            spinnerDirektirat!!.setItem(direktoratList!! as List<Any>)

                            insertToSpinner("DIREKTORAT")
                        }
                    })
            }
        }

    }

    fun insertToSpinner(type:String){
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerTargetposisi = rootView.findViewById(R.id.spinnerPosition) as SmartMaterialSpinner<Any>
        val spinnerDirektirat = rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>

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
                            spinnerTargetposisi!!.setItem(currencyPojos!! as List<Any>)
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
                            spinnerDirektirat!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
        }
    }



    private fun showCompareDialog(){
        val dialog = this!!.context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.compare_dialog)
        dialog!!.show()
        val spinnerEmp1 = dialog.findViewById(R.id.spinnerKaryawan1) as SmartMaterialSpinner<Any>
        val spinnerEmp2 = dialog.findViewById(R.id.spinnerkaryawan2) as SmartMaterialSpinner<Any>
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnCompare = dialog.findViewById(R.id.btnCompare) as Button

        var emplistkary1 = EmpList()
        var emplistkary2 = EmpList()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()


        //=========== KARYAWAN ============================

        APIConfig().getService()
            .getUsers("ALL", "ALL",dao.getNIK(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<EmpTalentMapping>> {
                override fun onFailure(call: Call<List<EmpTalentMapping>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<List<EmpTalentMapping>>,
                    response: Response<List<EmpTalentMapping>>
                ) {

                    if(response.body()!=null){
                        var emp1List: MutableList<String>? = mutableListOf()

                        emp1Obj = response.body() as MutableList<EmpTalentMapping>
                        for (i in 0 until emp1Obj?.size!!){
                            emp1List?.add(emp1Obj!![i].empname.toString())
                        }
                        spinnerEmp1.item = (emp1List as List<Any>?)
                        spinnerEmp2.item = (emp1List as List<Any>?)
                    }
                }
            })

        //================================================

        spinnerEmp1!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {

                emplistkary1.id = position.toString()
                emplistkary1.empNIK = emp1Obj!![position].empnik
                emplistkary1.empName = emp1Obj!![position].empname
                emplistkary1.empDept = emp1Obj!![position].direktoratname
                emplistkary1.empBranch = emp1Obj!![position].emporgname
                emplistkary1.empCompany = emp1Obj!![position].locationname
                emplistkary1.empPhoto = emp1Obj!![position].empphoto
                emplistkary1.empAge = emp1Obj!![position].age
                emplistkary1.empOrg = emp1Obj!![position].emporgname
                emplistkary1.empJobTitle = emp1Obj!![position].empjobtitlename
                emplistkary1.compGroupID = emp1Obj!![position].compgrpid
                emplistkary1.empJobLevel = emp1Obj!![position].empjoblvl

                var dataDiri:EmpListDataDiri = EmpListDataDiri()
                dataDiri.empSignDate = emp1Obj!![position].empsigndate
                dataDiri.empLastPromotion = emp1Obj!![position].lastpromotiondate
                dataDiri.location = emp1Obj!![position].locationname
                dataDiri.emplastEducation = emp1Obj!![position].lasteducation
                dataDiri.empHomeBase = emp1Obj!![position].homebase
                dataDiri.empBirthPlace = emp1Obj!![position].empbirthplace
                dataDiri.empBirthDate = emp1Obj!![position].empdatebirth
                dataDiri.empAge = emp1Obj!![position].age
                dataDiri.empMaritalStatus = emp1Obj!![position].maritalstatus
                dataDiri.empRetirementDate = emp1Obj!![position].retirementdatae

                emplistkary1.dataDiri = dataDiri

                Toast.makeText(context, emp1Obj!![position].empname, Toast.LENGTH_SHORT).show()
                spinnerEmp1.setErrorText("")
            }
        }

        spinnerEmp2!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerEmp2.setFloatingLabelText(emp1Obj!![position].empname)


                emplistkary2.id = position.toString()
                emplistkary2.empNIK = emp1Obj!![position].empnik
                emplistkary2.empName = emp1Obj!![position].empname
                emplistkary2.empDept = emp1Obj!![position].direktoratname
                emplistkary2.empBranch = emp1Obj!![position].emporgname
                emplistkary2.empCompany = emp1Obj!![position].locationname
                emplistkary2.empPhoto = emp1Obj!![position].empphoto
                emplistkary2.empAge = emp1Obj!![position].age
                emplistkary2.empOrg = emp1Obj!![position].emporgname
                emplistkary2.empJobTitle = emp1Obj!![position].empjobtitlename
                emplistkary2.compGroupID = emp1Obj!![position].compgrpid
                emplistkary2.empJobLevel = emp1Obj!![position].empjoblvl

                var dataDiri:EmpListDataDiri = EmpListDataDiri()
                dataDiri.empSignDate = emp1Obj!![position].empsigndate
                dataDiri.empLastPromotion = emp1Obj!![position].lastpromotiondate
                dataDiri.location = emp1Obj!![position].locationname
                dataDiri.emplastEducation = emp1Obj!![position].lasteducation
                dataDiri.empHomeBase = emp1Obj!![position].homebase
                dataDiri.empBirthPlace = emp1Obj!![position].empbirthplace
                dataDiri.empBirthDate = emp1Obj!![position].empdatebirth
                dataDiri.empAge = emp1Obj!![position].age
                dataDiri.empMaritalStatus = emp1Obj!![position].maritalstatus
                dataDiri.empRetirementDate = emp1Obj!![position].retirementdatae

                emplistkary2.dataDiri = dataDiri



                Toast.makeText(context, emp1Obj!![position].empname, Toast.LENGTH_SHORT).show()
                spinnerEmp2.setErrorText("")
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnCompare.setOnClickListener {

            if(!emplistkary1.empNIK.equals(emplistkary2.empNIK)) {
                val compareFragment =
                    CompareFragment()

                val args = Bundle()
                args.putSerializable("DATA_DIRI1", emplistkary1)
                args.putSerializable("DATA_DIRI2", emplistkary2)

                compareFragment.arguments = args
                val transaction = this.fragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, compareFragment)
                transaction?.addToBackStack("CompareFragment")
                transaction?.commit()

                dialog.dismiss()
            }
            else {
                Toast.makeText(context,"Tidak dapat membandingkan karyawan yang sama",Toast.LENGTH_LONG).show()
            }
        }


    }


    private fun showStructureDialog(){
        val dialog = this!!.context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.show_structure_dialog)
        dialog!!.show()
        val spinnerDirektorat = dialog.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnShow = dialog.findViewById(R.id.btnShow) as Button
        var dirIDStructure=""

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()


        spinnerDirektorat.item = direktoratList as List<Any>?

        spinnerDirektorat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {

                dirIDStructure = direktoratListObj?.get(position)?.direktoratid.toString()

                Toast.makeText(context, direktoratListObj?.get(position)?.direktoratname, Toast.LENGTH_SHORT).show()
                spinnerDirektorat.setErrorText("")
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnShow.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","",dirID?:"","",null,
                context!!
            )

            talentMappingViewModel.orgStructure
                .observe(this, object : Observer<List<OrgStructure?>?> {
                    override fun onChanged(currencyPojos: List<OrgStructure?>?) {
                        if (currencyPojos != null) {
                            if(currencyPojos.size>0){
                                val baseUrl = APIConfig();
                                val URL = baseUrl.baseURL+ (currencyPojos?.get(0)?.structurEFILE ?: "")
                                if (URL.contains(".pdf")) {
                                    //                val browserIntent = Intent(
                                    //                    Intent.ACTION_VIEW,
                                    //                    Uri.parse("http://10.102.9.153/hcroadmap/Uploads/OrganizationStructure/StrukturOrganisasi_4_1.pdf")
                                    //                )
                                    //                startActivity(browserIntent)
                                    showStructurePDFFullscreenDialog(URL)
                                }
                                else{showStructureFullscreenDialog(URL)
                                }
                            }
                        }
                    }
                })



        }


    }


    private fun showStructureFullscreenDialog(url:String){
        val dialog = context?.let { Dialog(it, R.style.AppBaseTheme) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.show_structure_fullscreen_dialog)
        dialog!!.show()
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        Picasso.get().load(url).into(dialog.imgFullScreen)


    }

    private fun showStructurePDFFullscreenDialog(url:String){
        val dialog = context?.let { Dialog(it, R.style.AppBaseTheme) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.show_pdf_fullscreen_dialog)
        dialog!!.show()
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.pdfView.fromUri(Uri.parse(url))
            .enableSwipe(true)
            .load();

    }


    private fun showDialogFilter(ctx: Context) {
        val dialog = Dialog(ctx, R.style.AppBaseTheme)



        umurMax=65
        umurMin=0
        tahunMax=""
        tahunMin=""
        golongan=""
        pendidikan=""
        posisiAwal=""



        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.search_candidate_filter_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button
        val btnReset = dialog.findViewById(R.id.btnReset) as Button


        initSpinner(dialog)
        initRangebar(dialog)
        initTagAkademikDanGolongan(dialog)


        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnReset.setOnClickListener {
            initSpinner(dialog)
            initRangebar(dialog)
            initTagAkademikDanGolongan(dialog)
            initPensiun(dialog)
        }
        btnSave.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            val tagsAkademik = dialog.findViewById(R.id.tagsPendidikant) as TagsEditText
            tagPendidikanFilter = mutableListOf()

            for (i in tagsAkademik.getTags().indices) {
                tagPendidikanFilter.add(tagsAkademik.getTags().get(i))
                pendidikan+= tagsAkademik.getTags().get(i)+","

            }

            val tagsGolongan = dialog.findViewById(R.id.tagsGolongan) as TagsEditText
            golonganFilter = mutableListOf()

            for (i in tagsGolongan.getTags().indices) {
                golonganFilter.add(tagsGolongan.getTags().get(i))
                golongan+= tagsGolongan.getTags().get(i)+","

            }

            val tagsPosisiAwal = dialog.findViewById(R.id.tagsPosisiAwal) as TagsEditText
            tagsPosisiAwalFilter = mutableListOf()
            var monthList: List<TargetPosisiList> = mutableListOf()
            for (i in tagsPosisiAwal.getTags().indices) {
//                tagsPosisiAwalFilter.add(tagsPosisiAwal.getTags().get(i))

                monthList = positionListObj!!.filter { s -> s.jobttlname == tagsPosisiAwal.getTags().get(i) }


                for (i in 0 until monthList.size) {
                    tagsPosisiAwalFilter.add(monthList.get(i))

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
            ep.pensiunmax = dialog.edtTahunPensiunEnd.text.toString()
            ep.pensiunmin = dialog.edtTahunPensiunStart.text.toString()
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


    private fun initSpinner(dialog:Dialog) {
        val spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaanAwal) as SmartMaterialSpinner<Any>
        val tagPosisiAwal = dialog.findViewById(R.id.tagsPosisiAwal) as TagsEditText


        companyList = mutableListOf<String>()
        positionList = mutableListOf<String>()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        //=========== COMPANY ============================
        APIConfig().getService()
            .getCompany(dao.getCompGrpId(),dao.getNIK(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<CompanyList>> {
                override fun onFailure(call: Call<List<CompanyList>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<List<CompanyList>>,
                    response: Response<List<CompanyList>>
                ) {

                    if(response.body()!=null){
                        companyListObj = response.body() as MutableList<CompanyList>?
                        for (i in 0 until response.body()?.size!!){
                            companyList!!.add(response.body()!![i].compname.toString())
                        }
                        spinnerCompany!!.setItem(companyList!! as List<Any>)



                    }
                }
            })
        //================================================


        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerCompany.setFloatingLabelText(companyList!![position])

                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()
                    }
                }

                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()

                //=========== TARGET POSISI ============================
                APIConfig().getService()
                    .getTargetPosisi(compID.toString(),dao.getNIK(),"Bearer "+dao.getToken())
                    .enqueue(object : Callback<List<TargetPosisiList>> {
                        override fun onFailure(call: Call<List<TargetPosisiList>>, t: Throwable) {
                            Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                        override fun onResponse(
                            call: Call<List<TargetPosisiList>>,
                            response: Response<List<TargetPosisiList>>
                        ) {

                            if(response.body()!=null){
                                positionListObj = response.body() as MutableList<TargetPosisiList>?

                                tagPosisiAwalList = mutableListOf<String>()
                                tagPosisiAwal.setText("")

                                for (i in 0 until response.body()!!.size){
                                    tagPosisiAwalList.add(response.body()!!.get(i).jobttlname.toString())
                                }

                                val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                    context!!,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    tagPosisiAwalList
                                )

                                tagPosisiAwal.setTagsWithSpacesEnabled(true)
                                tagPosisiAwal.setAdapter(dataAdapter)
                                tagPosisiAwal.setThreshold(1)


                            }
                        }
                    })
                //================================================

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }

    private fun initRangebar(dialog:Dialog) {
        val rangeUmur = dialog.findViewById(R.id.rangeUmur) as RangeBar

        rangeUmur.setOnRangeBarChangeListener(RangeBar.OnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            if (leftPinIndex <= rightPinIndex) {
                umurMin = leftPinIndex
                umurMax = rightPinIndex

            } else {
                umurMin = rightPinIndex
                umurMax = leftPinIndex
            }
        })



    }

    private fun initPensiun(dialog:Dialog) {
        val edtTahunMin = dialog.findViewById(R.id.edtTahunPensiunStart) as EditText
        val edtTahunMax = dialog.findViewById(R.id.edtTahunPensiunEnd) as EditText
        val ctx: Context? = context

        edtTahunMax.setText("")
        edtTahunMin.setText("")

        edtTahunMin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                tahunMin = s.toString()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        edtTahunMax.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tahunMax = s.toString()
            }
        })
    }

    private fun initTagAkademikDanGolongan(dialog:Dialog) {
        val tagsGolongan = dialog.findViewById(R.id.tagsGolongan) as TagsEditText

        val tagsAkademik = dialog.findViewById(R.id.tagsPendidikant) as TagsEditText
        val ctx: Context? = context


        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        APIConfig().getService()
            .getgolonganPendidikan(dao.getNIK(),dao.getCompGrpId(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<GolonganPendidikan>> {
                override fun onFailure(call: Call<List<GolonganPendidikan>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<List<GolonganPendidikan>>,
                    response: Response<List<GolonganPendidikan>>
                ) {

                    if(response.body()!=null){

                        //================== TAG PENDIDIKAN ======================
                        tagPendidikan = mutableListOf<String>()
                        tagsAkademik.setText("")

                        for (i in 0 until response.body()!![0].pendidikan!!.size){
                            tagPendidikan.add(response.body()!![0].pendidikan!![i].edulvlname!!)
                        }

                        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                            ctx!!,
                            android.R.layout.simple_spinner_dropdown_item,
                            tagPendidikan
                        )

                        tagsAkademik.setTagsWithSpacesEnabled(true)
                        tagsAkademik.setAdapter(dataAdapter)
                        tagsAkademik.setThreshold(1)
                        //=====================================================================

                        //================== G0LONGAN ========================================
                        golonganList = mutableListOf<String>()
                        tagsGolongan.setText("")

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


    fun loadEmployee(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        APIConfig().getService()
            .getUsers(dirID.toString(), compID.toString(),dao.getNIK(),"Bearer "+dao.getToken())
            .enqueue(object : Callback<List<EmpTalentMapping>> {
                override fun onFailure(call: Call<List<EmpTalentMapping>>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
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

        val employeeAdapter =  EmpListSearchTalentAdapter(listEmpListAdapter, context,activity,
            this.fragmentManager!!,posID.toString()
        )

        rvEmpList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = employeeAdapter
        }
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


            var dataDiri:EmpListDataDiri = EmpListDataDiri()
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
            this.fragmentManager,posID.toString()
        )

        rvEmpList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = employeeAdapter
        }
    }


}
