package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.EmpList
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
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmpListSearchTalentAdapter(private val empList: List<EmpList>, private val ctx: Context?, private val activity: Activity?, private val fm: FragmentManager?, private val POSID:String, private val displayTo:String?="") : RecyclerView.Adapter<EmpListHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): EmpListHolder {
        return EmpListHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.search_candidate_talent_mapping_employee_item, viewGroup, false))
    }

    override fun getItemCount(): Int = empList.size

    override fun onBindViewHolder(holder: EmpListHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(empList[position],ctx,activity,fm,POSID,displayTo?:"")
            }
        }
    }
}

class EmpListHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvName = view.tvNama
    private val tvGolongan = view.tvGolongan
    private val tvUmur = view.tvUmur
    private val tvArcv = view.tvArchievement
    private val tvRec = view.tvRecommendation

    private val tvDept = view.tvDept
    private val photo = view.findViewById<CircleImageView>(R.id.empPhoto)
    private val btnAdd = view.btnInsertToTalentMapping
    private val btnLihatTalentProfile = view.tvLihatTalentProfile

    private lateinit var empListRoom: EmpListRoom
    private var isUpdate = false
    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(
        empList: EmpList,
        ctx: Context,
        activity:Activity,
        fm: FragmentManager?,
        POSID: String,
        displayTo:String
    ) {
        tvName.text = empList.empName
        tvUmur.text = "Usia: "+empList.empAge+" TAHUN"
        tvGolongan.text = "Golongan: "+empList.empJobLevel
        tvArcv.text = empList.archievement
        tvRec.text = empList.recommendation

        tvDept.text = empList.empJobTitle+" - "+empList.empDept+" - "+empList.empCompany
        Picasso.get().load(empList.empPhoto)
            .into(photo)

        if(displayTo.equals("TALENTPROFILEDB")){
            btnAdd.visibility=View.GONE
        }


        btnAdd.setOnClickListener {

            showArchievementDialog(empList,ctx,POSID)

        }
        btnLihatTalentProfile.setOnClickListener {
            showTalentProfile(empList,activity,fm,displayTo)

        }
    }


    private fun addToMapping(empList: EmpList,ctx: Context,POSID:String){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var postmapping = POSTMAPPING()
        postmapping.archievement = empList.archievement.toString()
        postmapping.reason = empList.recommendation.toString()
        postmapping.compid = empList.compID.toString()
        postmapping.dirid = empList.dirId.toString()
        postmapping.trgrpid = "0"
        postmapping.posid = POSID
        postmapping.upduser = dao.getusername()
        postmapping.empnik = empList.empNIK


        APIConfig().getService()
            .postToMapping(postmapping,"Bearer "+dao.getToken())!!
            .enqueue(object : Callback<Void> {
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(ctx, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    Toast.makeText(ctx, "Employee Added!", Toast.LENGTH_SHORT).show()

                    if(response.body()!=null){

                    }
                }
            })

//        if (dao.getById(Integer.parseInt(empList.id!!)).isEmpty()){
//
//            var empRoom = EmpListRoom()
//            empRoom?.id=Integer.parseInt(empList.id!!)
//            empRoom?.empAge= empList.empAge!!
//            empRoom?.empBranch= empList.empBranch!!
//            empRoom?.empCompany= empList.empCompany!!
//            empRoom?.compGroupID= empList.compGroupID!!
//            empRoom?.empDept= empList.empDept!!
//            empRoom?.empOrg= empList.empOrg!!
//            empRoom?.empPhoto= empList.empPhoto!!
//            empRoom?.empName= empList.empName!!
//            empRoom?.empNIK= empList.empNIK!!
//            empRoom?.empJobTitle= empList.empJobTitle!!
//            empRoom?.archievement= empList.archievement!!
//            empRoom?.recommendation= empList.recommendation!!
//
//
//            if (empRoom != null) {
//                dao.insert(empRoom)
//            }
//        }
//        else{
//
//            var empRoom:EmpListRoom?=null
//            empRoom?.id=Integer.parseInt(empList.id!!)
//            empRoom?.empAge= empList.empAge!!
//            empRoom?.empBranch= empList.empBranch!!
//            empRoom?.empCompany= empList.empCompany!!
//            empRoom?.compGroupID= empList.compGroupID!!
//            empRoom?.empDept= empList.empDept!!
//            empRoom?.empOrg= empList.empOrg!!
//            empRoom?.empPhoto= empList.empPhoto!!
//            empRoom?.empName= empList.empName!!
//            empRoom?.empNIK= empList.empNIK!!
//            empRoom?.empJobTitle= empList.empJobTitle!!
//            empRoom?.archievement= empList.archievement!!
//            empRoom?.recommendation= empList.recommendation!!
//
//            empRoom?.let { dao.update(it) }
//        }


    }

    private fun showArchievementDialog(empList: EmpList,ctx: Context,POSID:String){
        val dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.archivement_recommendation_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnAdd) as Button
        val edtArchievement = dialog.findViewById(R.id.edtArchievement) as EditText
        val edtRecommendation = dialog.findViewById(R.id.edtRecommendation) as EditText

        edtArchievement.setText(empList.archievement)
        edtRecommendation.setText(empList.recommendation)

        btnSave.setOnClickListener {
            if(!edtArchievement.text.toString().equals("") && !edtRecommendation.text.toString().equals("")){
                empList.archievement=edtArchievement.text.toString()
                empList.recommendation = edtRecommendation.text.toString()
                addToMapping(empList,ctx,POSID)
                dialog.dismiss()
            }
            else{
                Toast.makeText(ctx,"Archievement dan recommendation wajib diisi!!",Toast.LENGTH_LONG).show()
            }
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    private fun showTalentProfile(
        empList: EmpList,
        activity: Activity,
        fm: FragmentManager?,
        displayTo:String?=""
    ){


        (activity as MainActivity?)?.globals?.posId = empList.posId
        (activity as MainActivity?)?.globals?.dirId = empList.dirId
        (activity as MainActivity?)?.globals?.compId = empList.compID

        val talentProfileFragment =
            TalentProfileFragment()

        val args = Bundle()
        args.putString("EMP_NAME", empList.empName)
        args.putString("EMP_PHOTO", empList.empPhoto)
        args.putString("JOB_TITLE_NAME", empList.empJobTitle)
        args.putString("JOB_LEVEL", empList.empJobLevel)
        args.putString("COMPANY_NAME", empList.empCompany)
        args.putString("ORG_NAME", empList.empOrg)
        args.putString("DIR_NAME", empList.empDept)
        args.putString("DIRID", empList.dirId)
        args.putString("NIK", empList.empNIK)
        if(displayTo.equals("TALENTPROFILEDB")) {
            args.putString("DISPLAYTO", "TALENTPROFILEDB")
        }
        args.putSerializable("DATA_DIRI", empList.dataDiri)

        talentProfileFragment.arguments=args
        val transaction = fm?.beginTransaction()
        transaction?.replace(R.id.myFragment, talentProfileFragment)
        transaction?.addToBackStack("TalentProfileFragment")
        transaction?.commit()


    }


}