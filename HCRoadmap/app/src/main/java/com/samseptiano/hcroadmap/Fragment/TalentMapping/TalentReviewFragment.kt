package com.samseptiano.hcroadmap.Fragment.TalentReview

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.Adapter.TalentReviewAdapter
import com.samseptiano.hcroadmap.DataClass.CompanyList
import com.samseptiano.hcroadmap.DataClass.DirektoratList
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReview
import com.samseptiano.hcroadmap.DataClass.TalentReview
import com.samseptiano.hcroadmap.DataClass.TargetPosisiList
import com.samseptiano.hcroadmap.Fragment.TalentMapping.FilterTalentMappingCandidateFragment
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import kotlinx.android.synthetic.main.create_talent_review_dialog.*
import kotlinx.android.synthetic.main.fragment_talentreview.*
import kotlinx.android.synthetic.main.fragment_talentreview.view.*
import mabbas007.tagsedittext.TagsEditText
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.math.log


class TalentReviewFragment : Fragment() {
    lateinit var rvEmpList: RecyclerView

    private var userIsInteracting = false

    private var isInitDialog:Boolean=true

    private var filterTalentMappingCandidateFragment =
        FilterTalentMappingCandidateFragment()

    lateinit var rootView:View

    lateinit var btnExpandFilter: ImageButton
    lateinit var lnFilter:LinearLayout


    var compID:String?=""
    var posID:String?=""
    var dirID:String?=""

    var compGrpIDDialog:String?=""
    var compIDDialog:String?=""
    var posIDDialog:String?=""
    var dirIDDialog:String?=""

    private var companyList: MutableList<String>? = null
    private var positionList: MutableList<String>? = null
    private var direktoratList: MutableList<String>? = null

    private var companyListDialog: MutableList<String>? = null
    private var positionListDialog: MutableList<String>? = null
    private var direktoratListDialog: MutableList<String>? = null

    var isExpanded:Boolean=true


    private var companyListObj: MutableList<CompanyList>? = null
    private var positionListObj: MutableList<TargetPosisiList>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null

    private var companyListObjDialog: MutableList<CompanyList>? = null
    private var positionListObjDialog: MutableList<TargetPosisiList>? = null
    private var direktoratListObjDialog: MutableList<DirektoratList>? = null

    private var talentReviewObj: MutableList<TalentReview>? = null
    private var talentReviewObjFilter: MutableList<TalentReview>? = null


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
        rootView = inflater.inflate(R.layout.fragment_talentreview, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Talent Review")
        btnExpandFilter = rootView.findViewById(R.id.imgExpandFilter)
        lnFilter = rootView.findViewById(R.id.lnFilter)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        loadViewModel("COMPANY")
        insertToSpinner("STATUS")
        initSpinner()
        loadTalentReview()

        rootView.btnBuatReview.setOnClickListener {
            showAddReviewDialog()
        }

        btnExpandFilter.setOnClickListener {
            expandFilter()
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
                talentReviewObj = mutableListOf()
                if (charText.isEmpty()) {
                    talentReviewObj = talentReviewObjFilter
                } else {
                    for (i in 0 until talentReviewObjFilter!!.size) {
                        if (talentReviewObjFilter?.get(i)?.tRNAME.toString().toLowerCase(Locale.getDefault())
                                .contains(charText)
                        ) {
                            talentReviewObj?.add(talentReviewObjFilter?.get(i)!!)
                        }
                    }
                }
                talentReviewObj?.let { initRv(it) }
                return true
            }
        })
    }

    private fun initSpinner() {
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerTargetposisi =
            rootView.findViewById(R.id.spinnerPosition) as SmartMaterialSpinner<Any>
        val tagsDirektorat =
            rootView.findViewById(R.id.tagsDirektorat) as TagsEditText


        if(!(activity as MainActivity?)?.globals?.direktoratState.equals("")) {
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
            "POSITION" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.targetposisi
                    .observe(this, object : Observer<List<TargetPosisiList?>?> {
                        override fun onChanged(currencyPojos: List<TargetPosisiList?>?) {
                            positionListObj = currencyPojos as MutableList<TargetPosisiList>?
                            insertToSpinner("POSITION")

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
        val spinnerTargetposisi = rootView.findViewById(R.id.spinnerPosition) as SmartMaterialSpinner<Any>
        val tagDirektorat = rootView.findViewById(R.id.tagsDirektorat) as TagsEditText
        val spinnerStatus = rootView.findViewById(R.id.spinnerStatus) as SmartMaterialSpinner<Any>

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
            "POSITION" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",positionListObj as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertIntoTargetPosisiList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            positionList  = currencyPojos as MutableList<String>?
                            spinnerTargetposisi!!.setItem(currencyPojos!! as List<Any>)
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

                            tagDirektorat.setText("")


                            val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_spinner_dropdown_item,
                                direktoratList as MutableList<String>
                            )
                            tagDirektorat.setTagsWithSpacesEnabled(true)
                            tagDirektorat.setAdapter(dataAdapter)
                            tagDirektorat.setThreshold(1)

                        }
                    })
            }
            "STATUS" ->{
                var statusList = mutableListOf<String>()
                statusList.add("ALL")
                statusList.add("IN PROGRESS")
                statusList.add("DONE")
                spinnerStatus!!.setItem(statusList!! as List<Any>)

            }
        }
    }

    fun expandFilter(){
        if (isExpanded){
            lnFilter.visibility =View.GONE
            isExpanded=false
        }
        else{
            lnFilter.visibility =View.VISIBLE
            isExpanded=true
        }
    }


    fun loadTalentReview(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","",null,
                    context!!
                )
        talentReviewViewModel.talentReview
                    .observe(this.viewLifecycleOwner, object : Observer<List<TalentReview?>?> {
                        override fun onChanged(currencyPojos: List<TalentReview?>?) {
                            talentReviewObj = mutableListOf()
                            talentReviewObjFilter = mutableListOf()
                            talentReviewObj = currencyPojos as MutableList<TalentReview>?
                            talentReviewObjFilter = currencyPojos as MutableList<TalentReview>?

                            talentReviewObj?.let { initRv(it) }


                        }
                    })
    }

    private fun initRv(talentReviewList: MutableList<TalentReview>){

        val talentReviewAdapter = talentReviewList?.let { TalentReviewAdapter(it, context,activity,this.fragmentManager!!,this) }

        rvTalentReview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = talentReviewAdapter
        }
    }





    fun showAddReviewDialog(postTalentReview:POSTTalentReview?=null){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(false)
        dialog!!.setContentView(R.layout.create_talent_review_dialog)
        dialog!!.show()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaan) as SmartMaterialSpinner<Any>
        var spinnerTargetposisi = dialog.findViewById(R.id.spinnerTargetPosisi) as SmartMaterialSpinner<Any>
        var tagDirektorat = dialog.findViewById(R.id.tagsDirektorat) as TagsEditText

            if(postTalentReview!=null){
                compIDDialog = postTalentReview.comp_id
                posIDDialog = postTalentReview.target_position_id
                dirIDDialog = postTalentReview.origin_dir_id

                var milliSec:Long=800

                if(companyListObjDialog?.size?:0<1 && positionListObjDialog?.size?:0<1) {

                    milliSec=2000;

                    //========== COMPANY ========================
                    var talentReviewViewModel = TalentReviewViewModel(
                        dao.getNIK(), dao.getToken(), dao.getCompGrpId() ?: "", "", "", "ALL", null,
                        context!!
                    )
                    talentReviewViewModel.company
                        .observe(this, object : Observer<List<CompanyList?>?> {
                            override fun onChanged(currencyPojos: List<CompanyList?>?) {
                                companyListObjDialog = currencyPojos as MutableList<CompanyList>?
                                insertToSpinnerDialog("COMPANY", dialog)
                            }
                        })
                    //============================================
                    //================== POSISI ===========================
                    talentReviewViewModel = TalentReviewViewModel(
                        dao.getNIK(), dao.getToken(), compIDDialog ?: "", "", "", "", null,
                        context!!
                    )

                    talentReviewViewModel.targetposisi
                        .observe(this, object : Observer<List<TargetPosisiList?>?> {
                            override fun onChanged(currencyPojos: List<TargetPosisiList?>?) {
                                positionListObjDialog =
                                    currencyPojos as MutableList<TargetPosisiList>?
                                insertToSpinnerDialog("POSITION", dialog)

                            }
                        })
                    //=======================================================
                    //================== DIREKTORAT ===========================
                    talentReviewViewModel = TalentReviewViewModel(
                        dao.getNIK(), dao.getToken(), compIDDialog ?: "", "", "", "", null,
                        context!!
                    )
                    talentReviewViewModel.direktorat
                        .observe(this, object : Observer<List<DirektoratList?>?> {
                            override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                                direktoratListObjDialog =
                                    currencyPojos as MutableList<DirektoratList>?
                                insertToSpinnerDialog("DIREKTORAT", dialog)
                            }
                        })
                    //==========================================================
                }
                else{
                    insertToSpinnerDialog("COMPANY", dialog)
                    insertToSpinnerDialog("POSITION", dialog)
                    insertToSpinnerDialog("DIREKTORAT", dialog)

                }
                Handler().postDelayed(
                    {

                        dialog.edtReviewName.setText(postTalentReview.tr_name)

                        try {
                            for (i in 0 until (companyListObjDialog?.size ?: 0)) {
                                if (compIDDialog.equals(companyListObjDialog?.get(i)?.compid)) {
//                                spinnerCompany.setFloatingLabelText(companyListObjDialog?.get(0)?.compname)
//                                spinnerCompany.setHint(companyListObjDialog?.get(0)?.compname)
                                    spinnerCompany.setSelection(i, false)
                                }
                            }

                            for (j in 0 until (positionListObjDialog?.size ?: 0)) {
                                if (posIDDialog.equals(positionListObjDialog?.get(j)?.jobttlid)) {
                                    spinnerTargetposisi.setFloatingLabelText(
                                        positionListObjDialog?.get(
                                            j
                                        )?.jobttlname
                                    )
//                                spinnerTargetposisi.setHint(positionListObjDialog?.get(i)?.jobttlname)
                                    spinnerTargetposisi.setSelection(j, false)
                                }
                            }

                            var aaa = mutableListOf<String>()
                            for (i in 0 until (direktoratListObjDialog?.size ?: 0)) {

                                for (j in 0 until (dirIDDialog?.replace(" ".toRegex(), "")
                                    ?.split(";")?.size ?: 0)) {
                                    if (direktoratListObjDialog?.get(i)?.direktoratid.toString()
                                            .equals(
                                                dirIDDialog?.replace(" ".toRegex(), "")?.split(";")
                                                    ?.get(j)
                                            )
                                    ) {
                                        aaa.add(direktoratListObjDialog?.get(i)?.direktoratname.toString())
                                    }
                                }
                            }
                            tagDirektorat.setTags(aaa.toTypedArray())
                        }catch (e:java.lang.NullPointerException){
                            dialog.dismiss()
                        }
//                        catch (i:IndexOutOfBoundsException){
//                            dialog.dismiss()
//                            Toast.makeText(context,i.message,Toast.LENGTH_LONG).show()
//                        }

                        spinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {

                                    Toast.makeText(context, companyListDialog!![position], Toast.LENGTH_SHORT).show()
                                spinnerCompany.setFloatingLabelText(companyList!![position])
//                (activity as MainActivity?)?.globals?.companyState = companyList!![position]
                                for(i in 0 until companyListObjDialog!!.size){
                                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                                        compIDDialog = companyListObjDialog!![i].compid.toString()
                                        compGrpIDDialog = companyListObjDialog!![i].compgrpid.toString()
                                        positionListDialog = mutableListOf()
                                        positionListObjDialog = mutableListOf()

                                        loadViewModelDialog("POSITION", dialog)


                                    }
                                }

                                spinnerCompany.setErrorText("")
                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }

                        spinnerTargetposisi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                                try {
                                    spinnerTargetposisi.setFloatingLabelText(positionListDialog!![position]?:"")
                                }catch (e:IndexOutOfBoundsException){
                                    spinnerTargetposisi.setFloatingLabelText("")
                                }
                                catch (e:NullPointerException){
                                    spinnerTargetposisi.setFloatingLabelText("")
                                }


                                for(i in 0 until positionListObjDialog!!.size){
                                    if(positionListObjDialog!![i].jobttlname!!.equals(spinnerTargetposisi.floatingLabelText)){
                                        posIDDialog = positionListObjDialog!![i].jobttlid.toString()

                                        direktoratListDialog = mutableListOf()
                                        direktoratListObjDialog = mutableListOf()
                                        loadViewModelDialog("DIREKTORAT", dialog)
                                    }
                                }

                                try {
                                    Toast.makeText(context, positionListDialog!![position], Toast.LENGTH_SHORT).show()
                                }catch (e:IndexOutOfBoundsException){
                                }
                                catch (e:NullPointerException){
                                }

                                spinnerTargetposisi.setErrorText("")

                            }

                            override fun onNothingSelected(adapterView: AdapterView<*>) {}
                        }


                    },
                    milliSec // value in milliseconds
                )

            }
            else {
                loadViewModelDialog("COMPANY", dialog)
                initSpinnerDialog(dialog)
            }
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnSave) as Button

        btnSave.setOnClickListener {


            val tagdirektorat = dialog.findViewById(R.id.tagsDirektorat) as TagsEditText
            var tagsDirektoratListFilter = mutableListOf<DirektoratList>()
            var monthList: List<DirektoratList> = mutableListOf()
            for (i in tagdirektorat.getTags().indices) {
//                tagsPosisiAwalFilter.add(tagsPosisiAwal.getTags().get(i))

                monthList = direktoratListObjDialog!!.filter { s -> s.direktoratname == tagdirektorat.getTags().get(i) }


                for (i in 0 until monthList.size) {
                    tagsDirektoratListFilter.add(monthList.get(i))

                }
            }

            dirIDDialog=""
            for (i in 0 until tagsDirektoratListFilter.size) {
                dirIDDialog+= tagsDirektoratListFilter.get(i).direktoratid+","

            }

            var postTalentReviewList = mutableListOf<POSTTalentReview>()
            var postTalentReviews = POSTTalentReview()
            postTalentReviews.tr_id=postTalentReview?.tr_id?:"0"
            postTalentReviews.tr_status="Open"
            postTalentReviews.tr_name = dialog.edtReviewName.text.toString()
            postTalentReviews.upd_user = dao.getusername()
            postTalentReviews.origin_comp_id= compGrpIDDialog
            postTalentReviews.origin_dir_id= dirIDDialog
            postTalentReviews.target_position_id= posIDDialog
            postTalentReviewList.add(postTalentReviews)
            val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","ALL",postTalentReviewList as MutableList<Any>,
                context!!
            )

            talentReviewViewModel.insertTalentReview
                .observe(this, object : Observer<String?> {
                    override fun onChanged(currencyPojos: String?) {
                        var strReturn = currencyPojos as String
                        dialog.dismiss()
                        dirIDDialog=""
                        loadTalentReview()

                    }
                })

        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }


    fun loadViewModelDialog(type:String,dialog: Dialog){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        when(type){
            "COMPANY" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","ALL",null,
                    context!!
                )

                talentReviewViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObjDialog = currencyPojos as MutableList<CompanyList>?
                            insertToSpinnerDialog("COMPANY",dialog)


                        }
                    })
            }
            "POSITION" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),compIDDialog?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.targetposisi
                    .observe(this, object : Observer<List<TargetPosisiList?>?> {
                        override fun onChanged(currencyPojos: List<TargetPosisiList?>?) {
//                            if(positionListObjDialog?.size!! <1) {
                                positionListObjDialog =
                                    currencyPojos as MutableList<TargetPosisiList>?
                                insertToSpinnerDialog("POSITION", dialog)
//                            }
                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),compIDDialog?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.direktorat
                    .observe(this, object : Observer<List<DirektoratList?>?> {
                        override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                            direktoratListObjDialog = currencyPojos as MutableList<DirektoratList>?
                            insertToSpinnerDialog("DIREKTORAT",dialog)
                        }
                    })
            }
        }

    }
    fun insertToSpinnerDialog(type:String, dialog: Dialog){
        val spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaan) as SmartMaterialSpinner<Any>
        val spinnerTargetposisi = dialog.findViewById(R.id.spinnerTargetPosisi) as SmartMaterialSpinner<Any>
        val tagDirektorat = dialog.findViewById(R.id.tagsDirektorat) as TagsEditText

        when(type){
            "COMPANY" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",companyListObjDialog as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertIntoCompanyList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            companyListDialog = currencyPojos as MutableList<String>?
                            spinnerCompany!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
            "POSITION" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",positionListObjDialog as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertIntoTargetPosisiList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            positionListDialog  = currencyPojos as MutableList<String>?
                            spinnerTargetposisi!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentReviewViewModel = context?.let {
                    TalentReviewViewModel("","","","","","",direktoratListObjDialog as MutableList<Any>,
                        it
                    )
                }

                talentReviewViewModel?.insertintoDirektoratList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            direktoratListDialog = currencyPojos as MutableList<String>?

                            tagDirektorat.setText("")


                            val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                context!!,
                                android.R.layout.simple_spinner_dropdown_item,
                                direktoratListDialog as MutableList<String>
                            )
                            tagDirektorat.setTagsWithSpacesEnabled(true)
                            tagDirektorat.setAdapter(dataAdapter)
                            tagDirektorat.setThreshold(1)

                        }
                    })
            }
        }
    }
     fun initSpinnerDialog(dialog: Dialog, isEdit:String?="") {
        val spinnerCompany = dialog.findViewById(R.id.spinnerPerusahaan) as SmartMaterialSpinner<Any>
        val spinnerTargetposisi =
            dialog.findViewById(R.id.spinnerTargetPosisi) as SmartMaterialSpinner<Any>
        val tagsDirektorat =
            dialog.findViewById(R.id.tagsDirektorat) as TagsEditText

        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyListDialog!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.setFloatingLabelText(companyList!![position])
//                (activity as MainActivity?)?.globals?.companyState = companyList!![position]

                for(i in 0 until companyListObjDialog!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compIDDialog = companyListObjDialog!![i].compid.toString()
                        compGrpIDDialog = companyListObjDialog!![i].compgrpid.toString()
                        positionListDialog = mutableListOf()
                        positionListObjDialog = mutableListOf()
                        if (!isEdit.equals("Y")) {
                            loadViewModelDialog("POSITION", dialog)
                        }

                    }
                }

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
        spinnerTargetposisi!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                try {
                    spinnerTargetposisi.setFloatingLabelText(positionListDialog!![position]?:"")
                }catch (e:IndexOutOfBoundsException){
                    spinnerTargetposisi.setFloatingLabelText("")
                }
                catch (e:NullPointerException){
                    spinnerTargetposisi.setFloatingLabelText("")
                }


                for(i in 0 until positionListObjDialog!!.size){
                    if(positionListObjDialog!![i].jobttlname!!.equals(spinnerTargetposisi.floatingLabelText)){
                        posIDDialog = positionListObjDialog!![i].jobttlid.toString()

                        direktoratListDialog = mutableListOf()
                        direktoratListObjDialog = mutableListOf()
                        if (!isEdit.equals("Y")) {
                            loadViewModelDialog("DIREKTORAT", dialog)
                        }
                    }
                }

                try {
                    Toast.makeText(context, positionListDialog!![position], Toast.LENGTH_SHORT).show()
                }catch (e:IndexOutOfBoundsException){
                }
                catch (e:NullPointerException){
                }

                spinnerTargetposisi.setErrorText("")

            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MutableList<POSTTalentReview>?) {
        deleteTalentReview(event as MutableList<Any>)
        EventBus.getDefault().unregister(this)

    }

    override fun onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    fun deleteTalentReview(postTalentReviewList:MutableList<Any>){


        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","ALL",postTalentReviewList as MutableList<Any>,
            context!!
        )

        talentReviewViewModel.deleteTalentReview
            .observe(this, object : Observer<String?> {
                override fun onChanged(currencyPojos: String?) {
                    var strReturn = currencyPojos as String
                    loadTalentReview()

                }
            })
    }

    fun onUserInteraction() {
        this.onUserInteraction()
        userIsInteracting = true
    }
}

