package com.samseptiano.hcroadmap.Fragment.TalentReview

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.EmpListTalentReviewFinalAdapter
import com.samseptiano.hcroadmap.Adapter.EmpListTalentReviewkandidatFinalAdapter
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Fragment.TalentMapping.CompareFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.FilterTalentMappingCandidateFragment
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentReviewDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_talent_review_final.view.*
import kotlinx.android.synthetic.main.fragment_talent_review_final.view.btnCompare
import kotlinx.android.synthetic.main.fragment_talentmapping.view.*
import kotlinx.android.synthetic.main.show_pdf_fullscreen_dialog.*
import kotlinx.android.synthetic.main.show_structure_fullscreen_dialog.*
import kotlinx.android.synthetic.main.talent_review_detail_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TalentReviewFinal : Fragment() {

    lateinit var rootView:View
    private lateinit var viewModel: TalentReviewDetailViewModel
    var talentMappingAfter:MutableList<EmpTalentMappingAfter> = mutableListOf()
    private var emp1Obj: MutableList<EmpTalentMapping>? = null
    private var direktoratList: MutableList<String>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null

    var TRGROUPID=""
    var TAHAP=""
    var COMPID=""
    var POSID=""
    var DIRID=""

    lateinit var talentReviewGroupsItem: TalentReviewGroupsItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_talent_review_final, container, false)
        TRGROUPID = arguments?.getString("TRGRPID").toString()
        TAHAP = arguments?.getString("TAHAP").toString()
        COMPID = arguments?.getString("COMPID").toString()
        POSID = arguments?.getString("POSID").toString()

        talentReviewGroupsItem = arguments?.getSerializable("TALENTREVIEWFINAL") as TalentReviewGroupsItem

        rootView.btnCompare.setOnClickListener {
            showCompareDialog()
        }

        rootView.btnStrukturFinal.setOnClickListener {
            showStructureDialog()
        }
        rootView.btnFilterKandidat.setOnClickListener{
                var filterTalentMappingCandidateFragment =
                    FilterTalentMappingCandidateFragment()

                if(COMPID!="0" && POSID!="0" && DIRID!="0"){
                    val args = Bundle()

                    args.putString("COMP_ID", COMPID)
                    args.putString("POS_ID", POSID)
                    args.putString("DIR_ID", DIRID)

                    filterTalentMappingCandidateFragment.arguments=args
                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                    transaction?.replace(R.id.myFragment, filterTalentMappingCandidateFragment)
                    transaction?.addToBackStack("FilterTalentMappingCandidateFragment")
                    transaction?.commit()
                }
                else {
                    Toast.makeText(context,"Company, Direktorat dan target Posisi belum dipilih",Toast.LENGTH_SHORT).show()
                }


        }



        loadFragment()
        return rootView
    }

    fun loadFragment(){
        loadViewModelRanking(TRGROUPID)
        loadData()
    }

    fun loadViewModelRanking(TRGROUPID:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        rootView.tvkandidatFinal.text = "Kandidat Final Tahap "+TAHAP

        viewModel = TalentReviewDetailViewModel("","",TRGROUPID,dao.getusername(),dao.getToken(),null, context!!)

        viewModel.getKandidatFinal
            .observe(this.viewLifecycleOwner, object : Observer<List<EmpTalentReviewFinal?>?> {
                override fun onChanged(currencyPojos: List<EmpTalentReviewFinal?>?) {
                    var empFinalList = (currencyPojos as MutableList<EmpTalentReviewFinal>?)!!
                    insertToRVRanking(empFinalList)
                }
            })

    }

    fun insertToRVRanking(empFinalList: MutableList<EmpTalentReviewFinal>):Unit{
        val empListTalentReviewFinalAdapter =
            EmpListTalentReviewkandidatFinalAdapter(empFinalList, context,activity,
                this.fragmentManager!!,"FINAL", TRGROUPID,this
            )


        rootView.rvKandidatFinal.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = empListTalentReviewFinalAdapter
        }

    }
    fun insertToRVRekomensasi(empFinalList: MutableList<EmpTalentReviewFinal>):Unit{
        val empListTalentReviewFinalAdapter =
            EmpListTalentReviewkandidatFinalAdapter(empFinalList, context,activity,
                this.fragmentManager!!, "RECOMMENDATION",TRGROUPID,this
            )


        rootView.rvRekomendasi.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = empListTalentReviewFinalAdapter
        }

    }
    fun loadData(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        for(i in 0 until (talentReviewGroupsItem.talentReviewDirektorats?.size ?: 0)){
            DIRID+=","+ (talentReviewGroupsItem.talentReviewDirektorats?.get(i)?.origiNDIRID ?: "")
        }

        val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),COMPID?:"",POSID?:"",DIRID?:"","0",null,
            context!!
        )

        talentMappingViewModel.empAftermapping
            .observe(viewLifecycleOwner, object : Observer<List<EmpTalentMappingAfter?>?> {
                override fun onChanged(currencyPojos: List<EmpTalentMappingAfter?>?) {
                    talentMappingAfter = currencyPojos as MutableList<EmpTalentMappingAfter>

                    var aaa = mutableListOf<EmpTalentReviewFinal>()
                    var empTalentReviewFinal = EmpTalentReviewFinal()
                    for(i in 0 until talentMappingAfter.size){
                        empTalentReviewFinal = EmpTalentReviewFinal()
                        empTalentReviewFinal.empphoto = talentMappingAfter.get(i).empphoto
                        empTalentReviewFinal.age = talentMappingAfter.get(i).age
                        empTalentReviewFinal.compalias = talentMappingAfter.get(i).compalias
                        empTalentReviewFinal.compgrpid = talentMappingAfter.get(i).compgrpid
                        empTalentReviewFinal.compid = talentMappingAfter.get(i).compid
                        empTalentReviewFinal.direktoratname = talentMappingAfter.get(i).direktoratname
                        empTalentReviewFinal.dirid = talentMappingAfter.get(i).dirid
                        empTalentReviewFinal.empbirthplace = talentMappingAfter.get(i).empbirthplace
                        empTalentReviewFinal.empdatebirth = talentMappingAfter.get(i).empdatebirth
                        empTalentReviewFinal.empnik = talentMappingAfter.get(i).empnik
                        empTalentReviewFinal.empjobtitlename = talentMappingAfter.get(i).empjobtitlename
                        empTalentReviewFinal.empname = talentMappingAfter.get(i).empname
                        empTalentReviewFinal.empjoindate = talentMappingAfter.get(i).empjoindate
                        empTalentReviewFinal.maritalstatus = talentMappingAfter.get(i).maritalstatus
                        empTalentReviewFinal.homebase = talentMappingAfter.get(i).homebase
                        empTalentReviewFinal.locationname = talentMappingAfter.get(i).locationname
                        empTalentReviewFinal.speciaLACHV = talentMappingAfter.get(i).speciaLACHV
                        empTalentReviewFinal.reCREASON = talentMappingAfter.get(i).reCREASON
                        empTalentReviewFinal.emporgid = talentMappingAfter.get(i).emporgid
                        empTalentReviewFinal.orgname = talentMappingAfter.get(i).orgname
                        empTalentReviewFinal.empjoblvl = talentMappingAfter.get(i).empjoblvl
                        empTalentReviewFinal.empjobtitleid = talentMappingAfter.get(i).empjobtitleid
                        aaa.add(empTalentReviewFinal)
                    }
                    insertToRVRekomensasi(aaa)

                }
            })

    }
    fun showCompareDialog(){
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

                var dataDiri: EmpListDataDiri = EmpListDataDiri()
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

                var dataDiri: EmpListDataDiri = EmpListDataDiri()
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
                Toast.makeText(context,"Tidak dapat membandingkan karyawan yang sama", Toast.LENGTH_LONG).show()
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

            val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","",DIRID?:"","",null,
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

}