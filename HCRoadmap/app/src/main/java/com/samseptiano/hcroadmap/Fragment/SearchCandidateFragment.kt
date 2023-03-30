package com.samseptiano.hcroadmap.Fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appyvet.rangebar.RangeBar
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.EmpListSearchTalentAdapter
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import kotlinx.android.synthetic.main.fragment_search_candidate.view.*
import kotlinx.android.synthetic.main.fragment_search_candidate.view.btnFilter
import kotlinx.android.synthetic.main.fragment_search_candidate.view.rvMapping
import kotlinx.android.synthetic.main.fragment_talentmapping.view.*
import kotlinx.android.synthetic.main.search_candidate_filter_dialog.*
import mabbas007.tagsedittext.TagsEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SearchCandidateFragment : Fragment() {


    lateinit var rootView:View
    private var empTalentMapping: MutableList<EmpTalentMapping>? = null
    private var empTalentMappingFilter: MutableList<EmpTalentMapping>? = null
    var listEmpListAdapter:MutableList<EmpList> = mutableListOf()

    var compID="0"
    var dirID="0"
    var posID="0"

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


    private var companyList: MutableList<String>? = null
    private var positionList: MutableList<String>? = null

    private var companyListObj: MutableList<CompanyList>? = null
    private var positionListObj: MutableList<TargetPosisiList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_candidate, container, false)

        compID = arguments?.getString("COMP_ID").toString()
        posID = arguments?.getString("POS_ID").toString()
        dirID = arguments?.getString("DIR_ID").toString()
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Search Candidate")
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })

        loadEmployee()

        rootView.btnFilter.setOnClickListener {
            context?.let { it1 -> showDialogFilter(it1) }
        }

        rootView.edtSearchName.addTextChangedListener(object : TextWatcher {
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


        return rootView
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



        val employeeAdapter =  EmpListSearchTalentAdapter(listEmpListAdapter, context,activity,
            this.fragmentManager!!,posID.toString()
        )

        rootView.rvMapping.apply {
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
            this.fragmentManager,posID.toString()
        )

        rootView.rvMapping.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = employeeAdapter
        }
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

}