package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.DataClass.POST.POSTTalentReview
import com.samseptiano.hcroadmap.Fragment.TalentReview.SubTalentReview.TalentReviewDetailFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.TalentReviewFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.career_history_item.view.tvTanggal
import kotlinx.android.synthetic.main.create_talent_review_dialog.*
import kotlinx.android.synthetic.main.people_review_item.view.*
import mabbas007.tagsedittext.TagsEditText
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TalentReviewAdapter(private val talentReviewList: List<TalentReview>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager,private val fg: TalentReviewFragment) : RecyclerView.Adapter<TalentReviewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): TalentReviewHolder {
        return TalentReviewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.people_review_item, viewGroup, false))
    }

    override fun getItemCount(): Int = talentReviewList.size

    fun onUpdate() {
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TalentReviewHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {

                if(talentReviewList.get(position).isOpened == false){
                    holder.closeLnButton()
                }
                else{
                    talentReviewList.get(position).tRSTATUS?.let { holder.openLnButton(it) }
                }
                holder.bindEmpList(talentReviewList[position],ctx,activity,fm,position,fg,
                    talentReviewList.toMutableList(), this
                )


            }
        }
    }
}

class TalentReviewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val tvNomor = view.tvNomor
    private val tvTanggal = view.tvTanggal
    private val tvStatus = view.tvStatus
    private val tvNamaReviewer = view.tvNamaReviewer
    private val tvDirektorat = view.tvDirAsal
    private val tvCompany = view.tvCompany
    private val tvTargetPosisi = view.tvTargetPosisi
    private val lnData = view.lnData

    private val lnViewDup = view.lnViewDuplicate
    private val btnView = view.btnView
    private val btnDuplicate = view.btnDuplicate


    private val lnViewEditDEl = view.lnViewEditRemove
    private val btnView1 = view.btnView1
    private val btnEdit = view.btnEdit1
    private val btnDelete1 = view.btnRemove1

    fun bindEmpList(talentReview: TalentReview,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int,fg:TalentReviewFragment,talentReviewList: MutableList<TalentReview>,tr: TalentReviewAdapter) {
        if(talentReview.creatEDATE!=null) {
            tvTanggal.text = dateConverter.convertToLocalDate(talentReview.creatEDATE!!.split(" ")[0])
        }
        tvStatus.text = talentReview.tRSTATUS
        tvNamaReviewer.text = talentReview.tRNAME
        tvDirektorat.text = talentReview.direktoratname
        tvCompany.text = talentReview.compname
        tvTargetPosisi.text = talentReview.jobttlname
        tvNomor.text = (pos+1).toString()+". "


        when(tvStatus.text){
            "Open" ->{
                if(lnViewEditDEl.visibility == View.VISIBLE){
                    lnViewEditDEl.visibility = View.GONE
                    talentReviewList?.find { it.isOpened == true }?.isOpened = false

                    talentReviewList.get(pos).isOpened = false

                }
                else {
                    lnViewEditDEl.visibility = View.VISIBLE
                    talentReviewList?.find { it.isOpened == true }?.isOpened = false
                    talentReviewList.get(pos).isOpened = true
                }
            }
            "In Progress","Closed"->{
                if(lnViewDup.visibility == View.VISIBLE){
                    lnViewDup.visibility = View.GONE
                    talentReviewList?.find { it.isOpened == true }?.isOpened = false

                    talentReviewList.get(pos).isOpened = false

                }
                else {
                    lnViewDup.visibility = View.VISIBLE
                    talentReviewList?.find { it.isOpened == true }?.isOpened = false
                    talentReviewList.get(pos).isOpened = true

                }
            }
        }

        btnDuplicate.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var postTalentReviewList = mutableListOf<POSTTalentReview>()
            var postTalentReviews = POSTTalentReview()
            postTalentReviews.tr_id="0"
            postTalentReviews.tr_status="Open"
            postTalentReviews.tr_name = talentReview.tRNAME
            postTalentReviews.upd_user = dao.getusername()
            postTalentReviews.origin_comp_id= talentReview.compgrpid
            postTalentReviews.comp_id = talentReview.compgrpid
            postTalentReviews.origin_dir_id= talentReview.direktoratid?.replace(";",",")
            postTalentReviews.target_position_id= talentReview.targeTPOSITIONID
            postTalentReviewList.add(postTalentReviews)


            val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","ALL",postTalentReviewList as MutableList<Any>,
                ctx!!
            )



            talentReviewViewModel.insertTalentReview
                .observe(fg, object : Observer<String?> {
                    override fun onChanged(currencyPojos: String?) {
                        var strReturn = currencyPojos as String
                        fg.loadTalentReview()
                    }
                })


        }

        btnEdit.setOnClickListener {
            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var postTalentReview = POSTTalentReview()
            postTalentReview.tr_id=talentReview.tRID
            postTalentReview.tr_status="Open"
            postTalentReview.tr_name = talentReview.tRNAME
            postTalentReview.upd_user = dao.getusername()
            postTalentReview.origin_comp_id= talentReview.compgrpid
            postTalentReview.origin_dir_id= talentReview.direktoratid?:""
            postTalentReview.target_position_id= talentReview.targeTPOSITIONID
            postTalentReview.comp_id= talentReview.compid

            fg.showAddReviewDialog(postTalentReview)
        }

        btnDelete1.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var postTalentReviewList = mutableListOf<POSTTalentReview>()
            var postTalentReview = POSTTalentReview()
            postTalentReview.tr_id=talentReview.tRID
            postTalentReview.tr_status="Open"
            postTalentReview.tr_name = ""
            postTalentReview.upd_user = dao.getusername()
            postTalentReview.origin_comp_id= ""
            postTalentReview.origin_dir_id= ""
            postTalentReview.target_position_id= ""
            postTalentReviewList.add(postTalentReview)


            fg.deleteTalentReview(postTalentReviewList.toMutableList())
            EventBus.getDefault().post(postTalentReviewList);

        }

        btnView1.setOnClickListener {

            val talentReviewDetailFragment =
                TalentReviewDetailFragment()

            val args = Bundle()
            args.putString("COMP_NAME", talentReview.compname)
            args.putString("COMP_ID", talentReview.compid)
            args.putString("POS_ID", talentReview.targeTPOSITIONID)
            args.putString("POS_NAME", talentReview.jobttlname)
            args.putString("DIR_ID", talentReview.direktoratid)
            args.putString("DIR_NAME", talentReview.direktoratname)
            args.putString("STATUS", talentReview.tRSTATUS)
            args.putString("TR_ID", talentReview.tRID)

            talentReviewDetailFragment.arguments=args
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.myFragment, talentReviewDetailFragment)
            transaction?.addToBackStack("TalentReviewDetailFragment")
            transaction?.commit()
        }

        btnView.setOnClickListener {

            val talentReviewDetailFragment =
                TalentReviewDetailFragment()

            val args = Bundle()
            args.putString("COMP_NAME", talentReview.compname)
            args.putString("COMP_ID", talentReview.compid)
            args.putString("POS_ID", talentReview.targeTPOSITIONID)
            args.putString("POS_NAME", talentReview.jobttlname)
            args.putString("DIR_ID", talentReview.direktoratid)
            args.putString("DIR_NAME", talentReview.direktoratname)
            args.putString("STATUS", talentReview.tRSTATUS)
            args.putString("TR_ID", talentReview.tRID)

            talentReviewDetailFragment.arguments=args
            val transaction = fm?.beginTransaction()
            transaction?.replace(R.id.myFragment, talentReviewDetailFragment)
            transaction?.addToBackStack("TalentReviewDetailFragment")
            transaction?.commit()
        }
    }


    fun closeLnButton(){
        lnViewDup.visibility = View.GONE
        lnViewEditDEl.visibility = View.GONE
    }
    fun openLnButton(str:String){
        when(str){
            "Open" ->{
                    lnViewEditDEl.visibility = View.VISIBLE
            }
            "In Progress","Closed"->{
                    lnViewDup.visibility = View.VISIBLE
            }
        }
    }
}