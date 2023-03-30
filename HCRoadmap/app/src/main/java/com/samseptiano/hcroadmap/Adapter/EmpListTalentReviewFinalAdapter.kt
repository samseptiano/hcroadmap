package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.DataClass.EmpListDataDiri
import com.samseptiano.hcroadmap.DataClass.EmpTalentReviewFinal
import com.samseptiano.hcroadmap.DataClass.POST.POSTMAPPING
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentProfileFragment
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoom
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.candidate_talent_mapping_employee_item.view.*
import kotlinx.android.synthetic.main.candidate_talent_mapping_employee_item.view.tvArchievement
import kotlinx.android.synthetic.main.candidate_talent_mapping_employee_item.view.tvLihatTalentProfile
import kotlinx.android.synthetic.main.candidate_talent_mapping_employee_item.view.tvRecommendation
import kotlinx.android.synthetic.main.candidate_talent_review_employee_final_item.view.*
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.*
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvDept
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvNama
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvUmur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmpListTalentReviewFinalAdapter(private var empList: MutableList<EmpTalentReviewFinal>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager) : RecyclerView.Adapter<EmpListTalentReviewFinalHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): EmpListTalentReviewFinalHolder {
        return EmpListTalentReviewFinalHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.candidate_talent_review_employee_final_item, viewGroup, false))
    }

    override fun getItemCount(): Int = empList.size

    fun refreshAdapter(empListt: EmpTalentReviewFinal,pos:Int){
        empList.removeAt(pos)
        empList.add(empListt)
        notifyDataSetChanged()
    }
    fun removeItem(pos:Int){
        empList.removeAt(pos)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: EmpListTalentReviewFinalHolder, position: Int) {
        if (ctx != null) {
            holder.bindEmpList(empList[position],ctx,this, position,activity,fm)

        }
    }
}

class EmpListTalentReviewFinalHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvName = view.tvNama
    private val tvJobTitle = view.tvGolongan
    private val tvUmur = view.tvUmur
    private val tvRanking = view.tvRanking
    private val tvDept = view.tvDept
    private val photo = view.findViewById<CircleImageView>(R.id.empPhoto)
    private val tvArchievement = view.tvArchievement
    private val tvRommendation = view.tvRecommendation

    private val btnShowtalentProfile = view.tvLihatTalentProfile


    private var isUpdate = false
    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao

    fun bindEmpList(
        empList: EmpTalentReviewFinal,
        ctx: Context, empListTalentAdapter: EmpListTalentReviewFinalAdapter, pos:Int,
        activity: Activity?,
        fm: FragmentManager) {
        tvName.text = empList.empname
        tvUmur.text = empList.age+" TAHUN"
        tvJobTitle.text = empList.empjobtitlename
        tvDept.text = empList.direktoratname+" - "+empList.locationname
        Picasso.get().load(empList.empphoto.toString())
            .into(photo)

        tvArchievement.text = empList.speciaLACHV
        tvRommendation.text = empList.reCREASON
        tvRanking.text = empList.ranking

        btnShowtalentProfile.setOnClickListener {
            showTalentProfile(empList, activity, fm)
        }
    }


    private fun showTalentProfile(empList: EmpTalentReviewFinal, activity: Activity?, fm: FragmentManager){

        (activity as MainActivity?)?.globals?.posId = empList.emppositionid.toString()
        (activity as MainActivity?)?.globals?.dirId = empList.dirid.toString()
        (activity as MainActivity?)?.globals?.compId = empList.compid.toString()
        val talentProfileFragment =
            TalentProfileFragment()

        var empListDataDiri = EmpListDataDiri()
        empListDataDiri.empSignDate = empList.empsigndate
        empListDataDiri.empLastPromotion = empList.lastpromotiondate.toString()
        empListDataDiri.emplastEducation = empList.lasteducation
        empListDataDiri.empHomeBase = empList.homebase
        empListDataDiri.empBirthPlace = empList.empbirthplace
        empListDataDiri.empBirthDate = empList.empdatebirth
        empListDataDiri.empMaritalStatus = empList.maritalstatus
        empListDataDiri.empAge = empList.age
        empListDataDiri.empRetirementDate = empList.retirementdate
        empListDataDiri.compGroupID = empList.compgrpid
        empListDataDiri.location = empList.locationname

        val args = Bundle()
        args.putString("EMP_NAME", empList.empname)
        args.putString("EMP_PHOTO", empList.empphoto.toString())
        args.putString("JOB_TITLE_NAME", empList.empjobtitlename)
        args.putString("JOB_LEVEL", empList.empjoblvl)
        args.putString("COMPANY_NAME", empList.compalias)
        args.putString("ORG_NAME", empList.orgname)
        args.putString("DIR_NAME", empList.direktoratname)
        args.putString("NIK", empList.empnik)
        args.putString("DIRID", empList.dirid.toString())
        args.putSerializable("DATA_DIRI", empListDataDiri)

        talentProfileFragment.arguments=args
        val transaction = fm.beginTransaction()
        transaction?.replace(R.id.myFragment, talentProfileFragment)
        transaction?.addToBackStack("TalentProfileFragment")
        transaction?.commit()


    }

}