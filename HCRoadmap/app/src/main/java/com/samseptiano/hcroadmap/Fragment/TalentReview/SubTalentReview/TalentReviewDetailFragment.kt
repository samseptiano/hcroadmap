package com.samseptiano.hcroadmap.Fragment.TalentReview.SubTalentReview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.Adapter.EmpListTalentReviewFinalAdapter
import com.samseptiano.hcroadmap.Adapter.TalentReviewDetailAdapter
import com.samseptiano.hcroadmap.DataClass.DirektoratList
import com.samseptiano.hcroadmap.DataClass.EmpTalentReviewFinal
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReview
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReviewPhase
import com.samseptiano.hcroadmap.DataClass.TalentReviewDetail
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewDetailViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import kotlinx.android.synthetic.main.talent_review_detail_fragment.view.*
import java.lang.IndexOutOfBoundsException


class TalentReviewDetailFragment : Fragment() {

    lateinit var rootview:View
    private lateinit var viewModel: TalentReviewDetailViewModel
    var talentReviewDetailList = mutableListOf<TalentReviewDetail>()
    var COMP_NAME:String?=""
    var COMP_ID:String?=""
    var POS_ID:String?=""
    var POS_NAME:String?=""
    var DIR_ID:String?=""
    var DIR_NAME:String?=""
    var STATUS:String?=""
    var TRID:String?=""

    var sizePhase:Int?=0

    var dirList = mutableListOf<DirektoratList>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootview =  inflater.inflate(R.layout.talent_review_detail_fragment, container, false)
        COMP_NAME = arguments?.getString("COMP_NAME").toString()
        COMP_ID = arguments?.getString("COMP_ID").toString()
        POS_ID = arguments?.getString("POS_ID").toString()
        POS_NAME = arguments?.getString("POS_NAME").toString()
        DIR_ID = arguments?.getString("DIR_ID").toString()
        DIR_NAME = arguments?.getString("DIR_NAME").toString()
        STATUS = arguments?.getString("STATUS").toString()
        TRID = arguments?.getString("TR_ID").toString()

        rootview.status.text = STATUS
        rootview.namaPerusahaan.text = COMP_NAME
        rootview.direktoratAsal.text = DIR_NAME
        rootview.targetPosisi.text = POS_NAME

        if(STATUS.equals("Open")){
            rootview.rvTalentReviewRanking.visibility = View.GONE
        }
        if(STATUS.equals("Closed")){
            rootview.btnHapustahapterakhir.visibility = View.GONE
            rootview.btnTambahTahap.visibility = View.GONE
            rootview.btncancelReview.visibility = View.GONE
            rootview.btnFinishSetup.visibility = View.GONE
        }
        else if(STATUS.equals("In Progress")){
            rootview.btnHapustahapterakhir.visibility = View.GONE
            rootview.btnTambahTahap.visibility = View.GONE
            rootview.btnFinishSetup.visibility = View.GONE
        }

        var dirIdLsit = mutableListOf<String>()
        var dirNameList = mutableListOf<String>()

        if(DIR_ID !=null && DIR_NAME !=null ) {
            dirIdLsit = DIR_ID!!.replace(" ", "").split(";") as MutableList<String>
            dirNameList = DIR_NAME!!.replace(" ", "").split(";") as MutableList<String>

            for(i in 0 until dirIdLsit.size){
                var dir = DirektoratList()

                dir.direktoratname = dirNameList[i]
                dir.direktoratid = dirIdLsit[i]
                dir.compalias = COMP_NAME
                dirList.add(dir)
            }
        }
        loadViewModel()

        rootview.btnHapustahapterakhir.setOnClickListener {
            hapusTahap()
        }
        rootview.btnTambahTahap.setOnClickListener {
            tambahTahap()
        }
        rootview.btncancelReview.setOnClickListener {
            batalReview(TRID!!)
        }

        return rootview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(TalentReviewDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }





    fun loadViewModelRanking(TRGROUPID:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        viewModel = TalentReviewDetailViewModel("","",TRGROUPID,dao.getusername(),dao.getToken(),null, context!!)

        viewModel.getEmpFinal
            .observe(this.viewLifecycleOwner, object : Observer<List<EmpTalentReviewFinal?>?> {
                override fun onChanged(currencyPojos: List<EmpTalentReviewFinal?>?) {
                    var empFinalList = (currencyPojos as MutableList<EmpTalentReviewFinal>?)!!
                    insertToRVRanking(empFinalList)
                }
            })

    }

    fun loadViewModel(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        viewModel = TalentReviewDetailViewModel("","",TRID.toString(),dao.getusername(),dao.getToken(),null, context!!)


    viewModel.getTalentReviewDetail
    .observe(this.viewLifecycleOwner, object : Observer<List<TalentReviewDetail?>?> {
        override fun onChanged(currencyPojos: List<TalentReviewDetail?>?) {
            talentReviewDetailList = (currencyPojos as MutableList<TalentReviewDetail>?)!!

            try {
                if(talentReviewDetailList.get(talentReviewDetailList.size-1).talentReviewGroups?.size== 0 || talentReviewDetailList.get(talentReviewDetailList.size-1).talentReviewGroups == null){
                    isButtonTambahDisplayed(false)
                }
                else {
                    sizePhase = talentReviewDetailList.size
                    isButtonTambahDisplayed(true)
                }

                if(STATUS.equals("Closed")){
                    isButtonTambahDisplayed(false)
                    //loadViewModelRanking("166")
                    TRID?.let { loadViewModelRanking(it) }
                }
                else if(STATUS.equals("In Progress")){
                    isButtonTambahDisplayed(false)
                }
                talentReviewDetailList?.let { insertToRV() }
            }
            catch (i:IndexOutOfBoundsException){
                    rootview.btnHapustahapterakhir.visibility=View.GONE
            }
            catch (e:NullPointerException){
                rootview.btnHapustahapterakhir.visibility=View.GONE

            }



        }
    })

}

    fun tambahTahap(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var postTalentReviewPhaseList = mutableListOf<POSTTalentReviewPhase>()
        var phase = POSTTalentReviewPhase()
        phase.phasename=(this.sizePhase?.plus(1)).toString()
        phase.trid = TRID
        phase.phasestatus="Open"
        phase.upduser=dao.getusername()
        postTalentReviewPhaseList.add(phase)

        var viewModel = TalentReviewDetailViewModel(dao.getNIK(),"","",dao.getusername(),dao.getToken(),postTalentReviewPhaseList as MutableList<Any>, context!!)

        var returnString = ""
        viewModel.insertTalentReviewPhase
            .observe(viewLifecycleOwner, object : Observer<String> {
                override fun onChanged(currencyPojos: String?) {
                    returnString = (currencyPojos as String?)!!
                    loadViewModel()
                }
            })
    }
    fun hapusTahap(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var postTalentReviewPhaseList = mutableListOf<POSTTalentReviewPhase>()
        var phase = POSTTalentReviewPhase()
        phase.phasename=(talentReviewDetailList.size).toString()
        phase.trid = TRID
        phase.phasestatus="Open"
        phase.upduser=dao.getusername()
        postTalentReviewPhaseList.add(phase)

        var viewModel = TalentReviewDetailViewModel(dao.getNIK(),"","",dao.getusername(),dao.getToken(),postTalentReviewPhaseList as MutableList<Any>, context!!)

        var returnString = ""
        viewModel.deleteTalentReviewPhase
            .observe(viewLifecycleOwner, object : Observer<String> {
                override fun onChanged(currencyPojos: String?) {
                    returnString = (currencyPojos as String?)!!
                    loadViewModel()
                }
            })
    }
    fun batalReview(trId:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var postTalentReviewList = mutableListOf<POSTTalentReview>()
        var postTalentReview = POSTTalentReview()
        postTalentReview.tr_id=TRID
        postTalentReview.tr_status="Open"
        postTalentReview.tr_name = ""
        postTalentReview.upd_user = dao.getusername()
        postTalentReview.origin_comp_id= ""
        postTalentReview.origin_dir_id= ""
        postTalentReview.target_position_id= ""
        postTalentReviewList.add(postTalentReview)

        val viewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","ALL",postTalentReviewList as MutableList<Any>,
            context!!
        )
        var returnString = ""
        viewModel.deleteTalentReview
            .observe(this, object : Observer<String> {
                override fun onChanged(currencyPojos: String?) {
                    returnString = (currencyPojos as String?)!!
                    activity?.onBackPressed()
                }
            })
    }


    fun insertToRV():Unit{
        val talentReviewDetailAdapter =

                TalentReviewDetailAdapter(talentReviewDetailList, context,activity,
                    this.fragmentManager!!, this@TalentReviewDetailFragment, dirList, STATUS.toString()
                ,COMP_ID.toString(),POS_ID.toString())


        rootview.rvTalentReviewDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = talentReviewDetailAdapter
        }

//        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context,1,GridLayoutManager.HORIZONTAL,false)
//
//        rootview.rvTalentReviewRanking.apply {
//            layoutManager = mLayoutManager
//            adapter = careerHistoryAdapter
//        }
    }

    fun insertToRVRanking(empFinalList: MutableList<EmpTalentReviewFinal>):Unit{
        val empListTalentReviewFinalAdapter =
                EmpListTalentReviewFinalAdapter(empFinalList, context,activity,
                    this.fragmentManager!!
                )


        rootview.rvTalentReviewRanking.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = empListTalentReviewFinalAdapter
        }

//        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context,1,GridLayoutManager.HORIZONTAL,false)
//
//        rootview.rvTalentReviewRanking.apply {
//            layoutManager = mLayoutManager
//            adapter = careerHistoryAdapter
//        }
    }

    fun isButtonTambahDisplayed(isDisplayed:Boolean){
        if(isDisplayed){
            rootview.btnTambahTahap.visibility = View.VISIBLE
        }
        else{
            rootview.btnTambahTahap.visibility = View.GONE

        }
    }

}
