package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Fragment.TalentReview.SubTalentReview.TalentReviewDetailFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewDetailViewModel
import kotlinx.android.synthetic.main.add_group_dialog.*
import kotlinx.android.synthetic.main.create_talent_review_dialog.btnCancel
import kotlinx.android.synthetic.main.people_review_tahap_datail_item.view.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*


class TalentReviewDetailAdapter(private val talentReviewList: List<TalentReviewDetail>, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager, private val fg: TalentReviewDetailFragment, private val dirList:MutableList<DirektoratList>, private val status:String, private val compID: String, private  val posID: String) : RecyclerView.Adapter<TalentReviewDetailHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): TalentReviewDetailHolder {
        return TalentReviewDetailHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.people_review_tahap_datail_item, viewGroup, false))
    }

    override fun getItemCount(): Int = talentReviewList.size

    fun onUpdate() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TalentReviewDetailHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {



                holder.bindEmpList(talentReviewList[position],ctx,activity,fm,position,fg,
                    talentReviewList.toMutableList(), this, dirList,status
                    ,compID,posID)


            }
        }
    }
}

class TalentReviewDetailHolder(view: View) : RecyclerView.ViewHolder(view) {
    val btnTambahGroup = view.btnTambahGroup

    val tvtahapName = view.tvTahap
    val rvGroup = view.rvTalentReviewDetailTahapList

    fun bindEmpList(talentReview: TalentReviewDetail, ctx: Context, activity:Activity, fm:FragmentManager, pos:Int, fg: TalentReviewDetailFragment, talentReviewList: MutableList<TalentReviewDetail>, tr: TalentReviewDetailAdapter, dirList: MutableList<DirektoratList>, status:String, compID:String,posID:String) {
        tvtahapName.text = "TAHAP "+talentReview.phasENAME

        if(status.equals("Closed") || status.equals("In Progress")){
            btnTambahGroup.visibility = View.GONE
        }

        lateinit var  talentReviewDetailGroupAdapter:TalentReviewDetailGroupAdapter
        if(talentReview.talentReviewGroups !=null){
            talentReviewDetailGroupAdapter =  TalentReviewDetailGroupAdapter(talentReview.talentReviewGroups as List<TalentReviewGroupsItem>, ctx,activity,fm, fg,dirList,talentReview,status,talentReview.phasENAME.toString(),compID,posID)

            rvGroup.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = talentReviewDetailGroupAdapter
            }
        }
        else{
            talentReviewDetailGroupAdapter =  TalentReviewDetailGroupAdapter(null, ctx,activity,fm, fg,dirList,talentReview,status,talentReview.phasENAME.toString(),compID,posID)

            rvGroup.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = talentReviewDetailGroupAdapter
            }
        }


        btnTambahGroup.setOnClickListener {
            showTambahGroupDialog(talentReview,ctx,pos.toString(),dirList,fg, talentReviewDetailGroupAdapter,activity,fm)
        }


    }

    private fun showTambahGroupDialog(talentReviewDetail: TalentReviewDetail, ctx: Context, POSID:String, dirList: MutableList<DirektoratList>, fg: TalentReviewDetailFragment, talentReviewDetailGroupAdapter: TalentReviewDetailGroupAdapter, activity: Activity, fm: FragmentManager){
        val dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_group_dialog)
        dialog!!.show()

        var DirektoratList = mutableListOf<String>()
        var ModeratorList = mutableListOf<String>()
        var ModeratorListObj = mutableListOf<Moderator>()
        var dirid:String=""

        dialog.tvDialogTitle.text = "Tahap "+(position+1).toString()

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
            aaa.tRGROUPID="0"
            talentReviewDetailTemp.talentReviewGroups!!.add(aaa)

            talentReviewList.add(talentReviewDetailTemp)


            var viewModel = TalentReviewDetailViewModel(dao.getNIK(),dirid,"",dao.getusername(),dao.getToken(),talentReviewList as MutableList<Any>, ctx!!)

            var returnString = ""
            viewModel.insertTalentReviewDetail
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        returnString = (currencyPojos as String?)!!
                        dialog.dismiss()
                        fg.loadViewModel()
//                        talentReviewDetail.talentReviewGroups!!.add(aaa)
//                        var talentReviewDetailGroupAdapter =  TalentReviewDetailGroupAdapter(talentReviewDetail.talentReviewGroups as List<TalentReviewGroupsItem>, ctx,activity,fm, fg)
//
//                        rvGroup.apply {
//                            layoutManager = LinearLayoutManager(context)
//                            adapter = talentReviewDetailGroupAdapter
//                        }
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