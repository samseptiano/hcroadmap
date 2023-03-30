package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentProfileFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.SubTalentReview.TalentReviewDetailFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.TalentReviewFinal
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewDetailViewModel
import kotlinx.android.synthetic.main.add_group_dialog.*
import kotlinx.android.synthetic.main.career_history_item.view.tvDirektorat
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.career_history_item.view.tvTanggal
import kotlinx.android.synthetic.main.create_talent_review_dialog.btnCancel
import kotlinx.android.synthetic.main.create_talent_review_dialog.btnSave
import kotlinx.android.synthetic.main.create_talent_review_dialog.tagsDirektorat
import kotlinx.android.synthetic.main.people_review_tahap_detail_sub_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TalentReviewDetailGroupAdapter(private val talentReviewList: List<TalentReviewGroupsItem>?=null, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager, private val fg: TalentReviewDetailFragment, private val dirList:MutableList<DirektoratList>, private val talentReviewDetail: TalentReviewDetail,private val status:String, private val phaseName:String,private val compID: String,private val posID: String) : RecyclerView.Adapter<TalentReviewDetailGroupHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): TalentReviewDetailGroupHolder {
        return TalentReviewDetailGroupHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.people_review_tahap_detail_sub_item, viewGroup, false))
    }

    override fun getItemCount(): Int = talentReviewList?.size ?:0

    fun onUpdate() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TalentReviewDetailGroupHolder, position: Int) {
        if (ctx != null) {
            if (activity != null && talentReviewList!=null) {

                holder.bindEmpList(talentReviewList[position],ctx,activity,fm,position,fg,
                    talentReviewList.toMutableList(), this,dirList,talentReviewDetail,status,phaseName,compID,posID
                )


            }
        }
    }
}

class TalentReviewDetailGroupHolder(view: View) : RecyclerView.ViewHolder(view) {

    val tvNomor = view.tvNomor
    val tvTanggal = view.tvTanggal
    val tvDeadline = view.tvDeadline
    val tvNamaReviewer = view.tvNamaReviewers
    val tvNamaModerator = view.tvNamaMederator
    val tvStatus = view.tvStatuss
    val tvDirektorat = view.tvDirektorat
    val btnEdit = view.btnEdit
    val btnRemove = view.btnRemove
    val btnView = view.btnView

    fun bindEmpList(talentReview: TalentReviewGroupsItem, ctx: Context, activity:Activity, fm:FragmentManager, pos:Int, fg: TalentReviewDetailFragment, talentReviewList: MutableList<TalentReviewGroupsItem>, tr: TalentReviewDetailGroupAdapter, dirList:MutableList<DirektoratList>, talentReviewDetail: TalentReviewDetail,status:String,phaseName: String, compID:String,posID: String) {

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        tvNomor.text = (pos+1).toString()+". "
        tvTanggal.text = talentReview.starTDATE
        tvDeadline.text = talentReview.duEDATE
        tvStatus.text = talentReview.tRGROUPSTATUS

        var isReviewerOrdModerator = 0;
        for(i in 0 until (talentReview.talentReviewReviewers?.size ?: 0)){
            if(talentReview.talentReviewReviewers?.get(i)?.revieweRNIK?.equals(dao.getNIK())!!){
                isReviewerOrdModerator++
            }
        }
        for(i in 0 until (talentReview.talentReviewModerators?.size ?: 0)){
            if(talentReview.talentReviewModerators?.get(i)?.moderatoRNIK?.equals(dao.getNIK())!!){
                isReviewerOrdModerator++
            }
        }
        if(isReviewerOrdModerator>0){
            btnView.visibility=View.VISIBLE
        }
        if(status.equals("Closed") || status.equals("In Progress")){
            btnEdit.visibility=View.GONE
            btnRemove.visibility=View.GONE

        }

        var direktoratname = ""
        for(i in talentReview.talentReviewDirektorats?.indices!!){
            direktoratname += talentReview.talentReviewDirektorats!!.get(i)?.direktoratName+";"
        }
        tvDirektorat.text = direktoratname

        var reviewerName = ""
        for(i in talentReview.talentReviewReviewers?.indices!!){
            reviewerName += talentReview.talentReviewReviewers!!.get(i)?.empname+";"
        }
        tvNamaReviewer.text = reviewerName

        var moderatorName = ""
        for(i in talentReview.talentReviewModerators?.indices!!){
            moderatorName += talentReview.talentReviewModerators!!.get(i)?.empname+";"
        }
        tvNamaModerator.text = moderatorName


        btnView.setOnClickListener{


            val talentReviewFinal =
                TalentReviewFinal()

            val args = Bundle()
            args.putString("TRGRPID", talentReview.tRGROUPID)
            args.putString("TAHAP", phaseName)
            args.putString("COMPID", compID)
            args.putString("POSID", posID)
            args.putString("TAHAP", phaseName)

            args.putSerializable("TALENTREVIEWFINAL", talentReview)

            talentReviewFinal.arguments=args
            val transaction = fm.beginTransaction()
            transaction?.replace(R.id.myFragment, talentReviewFinal)
            transaction?.addToBackStack("TalentReviewFinal")
            transaction?.commit()



        }

        btnRemove.setOnClickListener {
            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var list = mutableListOf<TalentReviewGroupsItem>()
            list.add(talentReview)
            var viewModel = TalentReviewDetailViewModel(dao.getNIK(),"","",dao.getusername(),dao.getToken(),list as MutableList<Any>, ctx!!)

            var returnString = ""
            viewModel.deleteTalentReviewDetail
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        returnString = (currencyPojos as String?)!!
                        fg.loadViewModel()
                    }
                })
        }

        btnEdit.setOnClickListener {
            showEditGroupDialog(talentReviewDetail,ctx,pos,dirList,fg,activity,fm)
        }

    }

    private fun showEditGroupDialog(talentReviewDetail: TalentReviewDetail, ctx: Context, POS:Int, dirList: MutableList<DirektoratList>, fg: TalentReviewDetailFragment, activity: Activity, fm: FragmentManager){
        val dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_group_dialog)
        dialog!!.show()



        var DirektoratList = mutableListOf<String>()
        var ModeratorList = mutableListOf<String>()
        var ModeratorListObj = mutableListOf<Moderator>()
        var dirid:String=""
        dialog.tvDialogTitle.text = "Tahap "+talentReviewDetail.phasENAME
        dialog.edtTanggalPelaksanaan.setText(
        talentReviewDetail.talentReviewGroups?.get(POS)?.starTDATE?.split(" ")?.get(0)?.toString())
        dialog.edtTanggalDeadline.setText(
            talentReviewDetail.talentReviewGroups?.get(POS)?.duEDATE?.split(" ")?.get(0)?.toString())
        dialog.edtJamPelaksanaan.setText(
            talentReviewDetail.talentReviewGroups?.get(POS)?.duEDATE?.split(" ")?.get(1)?.toString())
        dialog.edtJamDeadline.setText(
            talentReviewDetail.talentReviewGroups?.get(POS)?.duEDATE?.split(" ")?.get(1)?.toString())

        val TRGROUPID = talentReviewDetail.talentReviewGroups?.get(POS)?.tRGROUPID




        dialog.tagsDirektorat.setText("")
        for (i in 0 until dirList!!.size){
            DirektoratList.add(dirList.get(i).direktoratname.toString())
            dirid += dirList.get(i).direktoratid+","
        }

        GlobalScope.launch (Dispatchers.Main) {
            ModeratorListObj = getModerator(ctx, fg, dirid)

            dialog.tagsModerator.setText("")
            dialog.tagsReviewer.setText("")

            for (i in 0 until ModeratorListObj!!.size){
                ModeratorList.add(ModeratorListObj.get(i).empName.toString())
            }

            val dataAdapter2: ArrayAdapter<String> = ArrayAdapter<String>(
                ctx!!,
                android.R.layout.simple_spinner_dropdown_item,
                ModeratorList
            )

            dialog.tagsModerator.setTagsWithSpacesEnabled(true)
            dialog.tagsModerator.setAdapter(dataAdapter2)
            dialog.tagsModerator.setThreshold(1)

            dialog.tagsReviewer.setTagsWithSpacesEnabled(true)
            dialog.tagsReviewer.setAdapter(dataAdapter2)
            dialog.tagsReviewer.setThreshold(1)


            var aaa = mutableListOf<String>()
            for (i in 0 until (ModeratorListObj?.size ?: 0)) {

                for (j in 0 until (talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewModerators?.size ?: 0)) {
                    if (ModeratorListObj?.get(i)?.empName.toString()
                            .equals(
                                talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewModerators?.get(j)?.empname
                            )
                    ) {
                        aaa.add(ModeratorListObj?.get(i)?.empName.toString())
                    }
                }
            }
            dialog.tagsModerator.setTags(aaa.toTypedArray())


            var bbb = mutableListOf<String>()
            for (i in 0 until (ModeratorListObj?.size ?: 0)) {

                for (j in 0 until (talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewReviewers?.size ?: 0)) {
                    if (ModeratorListObj?.get(i)?.empName.toString()
                            .equals(
                                talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewReviewers?.get(j)?.empname
                            )
                    ) {
                        bbb.add(ModeratorListObj?.get(i)?.empName.toString())
                    }
                }
            }
            dialog.tagsReviewer.setTags(bbb.toTypedArray())

            var ccc = mutableListOf<String>()
            for (i in 0 until (dirList?.size ?: 0)) {

                for (j in 0 until (talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewDirektorats?.size ?: 0)) {
                    if (dirList?.get(i)?.direktoratname.toString()
                            .equals(
                                talentReviewDetail.talentReviewGroups?.get(POS)?.talentReviewDirektorats?.get(j)?.direktoratName
                            )
                    ) {
                        ccc.add(dirList?.get(i)?.direktoratname.toString())
                    }
                }
            }
            dialog.tagsDirektorat.setTags(ccc.toTypedArray())

        }

        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            ctx!!,
            android.R.layout.simple_spinner_dropdown_item,
            DirektoratList
        )
        dialog.tagsDirektorat.setTagsWithSpacesEnabled(true)
        dialog.tagsDirektorat.setAdapter(dataAdapter)
        dialog.tagsDirektorat.setThreshold(1)



        dialog.btnSave.setOnClickListener {
            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var talentReviewList: MutableList<TalentReviewDetail>
            talentReviewList = mutableListOf()

            var talentReviewDetailTemp = TalentReviewDetail()
            talentReviewDetailTemp = talentReviewDetail
            talentReviewDetailTemp.upduser = dao.getusername()
            talentReviewDetailTemp.talentReviewGroups = mutableListOf()

            var moderatorList = mutableListOf<TalentReviewModeratorsItem>()
            var direktoratList = mutableListOf<TalentReviewDirektoratsItem>()
            var reviewerList = mutableListOf<TalentReviewReviewersItem>()


            for(i in ModeratorListObj.indices){

                for(j in dialog.tagsModerator.getTags().indices) {
                    if (dialog.tagsModerator.getTags().get(j).equals(ModeratorListObj.get(i).empName)) {

                        var xx = TalentReviewModeratorsItem()
                        xx.empname = ModeratorListObj.get(i).empName
                        xx.moderatoRNIK = ModeratorListObj.get(i).empNIK
                        moderatorList.add(xx)
                    }
                }
            }

            for(i in ModeratorListObj.indices){

                for(j in dialog.tagsReviewer.getTags().indices) {
                    if (dialog.tagsReviewer.getTags().get(j).equals(ModeratorListObj.get(i).empName)) {

                        var xx = TalentReviewReviewersItem()
                        xx.empname = ModeratorListObj.get(i).empName
                        xx.revieweRNIK = ModeratorListObj.get(i).empNIK
                        reviewerList.add(xx)
                    }
                }
            }

            for(i in 0 until dirList.size) {
                for (j in dialog.tagsDirektorat.getTags().indices) {
                    if (dialog.tagsDirektorat.getTags().get(j).equals(dirList.get(i).direktoratname)) {

                        var xx = TalentReviewDirektoratsItem()
                        xx.origiNDIRID = dirList.get(i).direktoratid
                        xx.direktoratName = dirList.get(i).direktoratname
                        xx.compgrpid = dao.getCompGrpId()

                        direktoratList.add(xx)
                    }
                }
            }


            var aaa = TalentReviewGroupsItem()
            aaa.talentReviewModerators = moderatorList
            aaa.talentReviewDirektorats = direktoratList
            aaa.talentReviewReviewers = reviewerList
            aaa.duEDATE = dialog.edtTanggalDeadline.text.toString()+" "+dialog.edtJamDeadline.text.toString()+":00"
            aaa.starTDATE = dialog.edtTanggalPelaksanaan.text.toString()+" "+dialog.edtJamPelaksanaan.text.toString()+":00"
            aaa.phasEID = talentReviewDetailTemp.phasEID
            aaa.tRGROUPSTATUS = "Open"
            aaa.tRGROUPID = TRGROUPID
            talentReviewDetailTemp.talentReviewGroups!!.add(aaa)

            talentReviewList.add(talentReviewDetailTemp)


            var viewModel = TalentReviewDetailViewModel(dao.getNIK(),dirid,"",dao.getusername(),dao.getToken(),talentReviewList as MutableList<Any>, ctx!!)

            var returnString = ""
            viewModel.updateTalentReviewDetail
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        returnString = (currencyPojos as String?)!!
                        dialog.dismiss()
                        fg.loadViewModel()
                    }
                })

        }

        dialog.edtTanggalPelaksanaan.setOnClickListener {
            showDatePicker(dialog.edtTanggalPelaksanaan,ctx)
        }
        dialog.edtTanggalDeadline.setOnClickListener {
            showDatePicker(dialog.edtTanggalDeadline,ctx)
        }
        dialog.edtJamPelaksanaan.setOnClickListener {
            showTimePicker(dialog.edtJamPelaksanaan,ctx)
        }
        dialog.edtJamDeadline.setOnClickListener {
            showTimePicker(dialog.edtJamDeadline,ctx)
        }

        dialog.btnCancel.setOnClickListener {

            dialog.dismiss()
        }
    }

    suspend fun getModerator(ctxContext: Context, fg: TalentReviewDetailFragment, dirid:String):MutableList<Moderator>{

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = ctxContext?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()



        var viewModel = TalentReviewDetailViewModel(dao.getNIK(),dirid,"",dao.getusername(),dao.getToken(),null, ctxContext!!)

        var ModeratorList = mutableListOf<Moderator>()
        viewModel.getModerator
            .observe(fg, object : Observer<List<Moderator?>?> {
                override fun onChanged(currencyPojos: List<Moderator?>?) {
                    ModeratorList = (currencyPojos as MutableList<Moderator>?)!!


                }
            })

        delay(1000L) // pretend we are doing something useful here, too

        return ModeratorList
    }
    fun showTimePicker(edittext:EditText,ctx:Context){
        // Get Current Time

        // Get Current Time
        val c = Calendar.getInstance()
        var mHour = c[Calendar.HOUR_OF_DAY]
        var mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            ctx,
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> edittext.setText("$hourOfDay:$minute") },
            mHour,
            mMinute,
            false
        )
        timePickerDialog.show()
    }

    fun showDatePicker(edittext:EditText,ctx:Context){
        val newCalendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)


        val datePickerDialog = DatePickerDialog(
            ctx,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val newDate = Calendar.getInstance()
                newDate[year, monthOfYear] = dayOfMonth
                try {
                    edittext.setText(
                        dateFormatter!!.format(newDate.time).replace("/", "-")
                    )
                    Toast.makeText(
                        ctx,
                        "Tanggal dipilih : " + dateFormatter!!.format(newDate.time),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            newCalendar[Calendar.YEAR],
            newCalendar[Calendar.MONTH],
            newCalendar[Calendar.DAY_OF_MONTH]
        )

        datePickerDialog!!.show()
    }

}