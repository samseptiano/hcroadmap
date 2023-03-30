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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.DataClass.POST.POSTMAPPING
import com.samseptiano.hcroadmap.Fragment.TalentMapping.*
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
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvDept
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvNama
import kotlinx.android.synthetic.main.search_candidate_talent_mapping_employee_item.view.tvUmur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EmpListTalentAdapter(private var empList: MutableList<EmpList>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager,private val talentmappingFragment: Fragment) : RecyclerView.Adapter<EmpListTalentHolder>(),onClickInterface{

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): EmpListTalentHolder {
        return EmpListTalentHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.candidate_talent_mapping_employee_item, viewGroup, false))
    }

    override fun getItemCount(): Int = empList.size

    fun refreshAdapter(empListt: EmpList,pos:Int){
        empList.removeAt(pos)
        empList.add(empListt)
        notifyDataSetChanged()
    }
    fun removeItem(pos:Int){
        empList.removeAt(pos)
        notifyDataSetChanged()
    }



    override fun onBindViewHolder(holder: EmpListTalentHolder, position: Int) {
        if (ctx != null) {
            holder.bindEmpList(empList[position],ctx,this, position,activity,fm)

        }
    }


    override fun editData(empList: EmpList,position: Int) {
        refreshAdapter(empList,position)
        var aaa = talentmappingFragment as FilterTalentMappingCandidateFragment
        aaa.loadData()
    }

    override fun removeData(empList: EmpList, position: Int) {
        removeItem(position)
        var aaa = talentmappingFragment as FilterTalentMappingCandidateFragment
        aaa.loadData()
    }

}

class EmpListTalentHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvName = view.tvNama
    private val tvGolongan = view.tvGolonganMapping
    private val tvUmur = view.tvUmur
    private val tvDept = view.tvDept
    private val photo = view.findViewById<CircleImageView>(R.id.empPhoto)
    private val tvArchievement = view.tvArchievement
    private val tvRommendation = view.tvRecommendation
    private val btnSetting = view.imgBtnSetting
    private val btnShowtalentProfile = view.tvLihatTalentProfile


    private var isUpdate = false
    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao

    fun bindEmpList(empList: EmpList,ctx: Context, empListTalentAdapter: EmpListTalentAdapter, pos:Int,activity: Activity?,fm: FragmentManager) {
        tvName.text = empList.empName
        tvUmur.text = "Usia: "+empList.empAge+" TAHUN"
        tvGolongan.text = "Golongan: "+empList.empJobLevel
        tvDept.text = empList.empJobTitle+" - "+empList.empDept+" - "+empList.empCompany
        Picasso.get().load(empList.empPhoto)
            .into(photo)

        tvArchievement.text = empList.archievement
        tvRommendation.text = empList.recommendation

        btnShowtalentProfile.setOnClickListener {
            showTalentProfile(empList, activity, fm)

        }

        btnSetting.setOnClickListener {

            val bundle = Bundle()
            bundle.putSerializable("EMPLIST", empList)
            bundle.putInt("POS", pos)
            bundle.putString("EMPNAME", empList.empName)
            val fragInfo = ItemListDialogFragment()
            fragInfo.setArguments(bundle)

            val bottomSheet =fragInfo
            bottomSheet.show(fm,
                "ModalBottomSheet")

        }

//        btnRemove.setOnClickListener {


    }

    private fun deleteEmployee(empList: EmpList,ctx:Context,empListTalentAdapter:EmpListTalentAdapter,position: Int){

        database = EmpListRoomDatabase.getDatabase(ctx)
        dao = database.getEmpListDao()

        var empRoom = EmpListRoom()
        empRoom?.id=Integer.parseInt(empList.id!!)
        empRoom?.empAge= empList.empAge!!
        empRoom?.empBranch= empList.empBranch!!
        empRoom?.empCompany= empList.empCompany!!
        empRoom?.compGroupID= empList.compGroupID!!
        empRoom?.empDept= empList.empDept!!
        empRoom?.empOrg= empList.empOrg!!
        empRoom?.empPhoto= empList.empPhoto!!
        empRoom?.empName= empList.empName!!
        empRoom?.empNIK= empList.empNIK!!
        empRoom?.empJobTitle= empList.empJobTitle!!
        empRoom?.archievement= empList.archievement!!
        empRoom?.recommendation= empList.recommendation!!



        if (empRoom != null) {
            dao.delete(empRoom)
            empListTalentAdapter.removeItem(position)
            Toast.makeText(ctx, "Employee removed!!", Toast.LENGTH_SHORT).show()
        }
    }


    fun showArchievementDialog(empList: EmpList,ctx: Context, empListTalentAdapter:EmpListTalentAdapter,pos:Int){

        database = EmpListRoomDatabase.getDatabase(ctx)
        dao = database.getEmpListDao()

        val dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.archivement_recommendation_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnSave = dialog.findViewById(R.id.btnAdd) as Button
        val edtArchievement = dialog.findViewById(R.id.edtArchievement) as EditText
        val edtRecommendation = dialog.findViewById(R.id.edtRecommendation) as EditText

        btnSave.setText("Update")
        edtArchievement.setText(empList.archievement)
        edtRecommendation.setText(empList.recommendation)

        btnSave.setOnClickListener {
            if(!edtArchievement.text.toString().equals("") && !edtRecommendation.text.toString().equals("")){

                lateinit var database: UserLoginRoomDatabase
                lateinit var dao: UserloginDao
                database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
                dao = database.getUserLoginDao()

                var postmapping = POSTMAPPING()
                postmapping.archievement = edtArchievement.text.toString()
                postmapping.reason = edtRecommendation.text.toString()
                postmapping.compid = empList.compID.toString()
                postmapping.dirid = empList.dirId.toString()
                postmapping.trgrpid = "0"
                postmapping.posid = empList.posId.toString()
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
                            empList.recommendation = edtRecommendation.text.toString()
                            empList.archievement = edtRecommendation.text.toString()
                            Toast.makeText(ctx, "Employee Added!", Toast.LENGTH_SHORT).show()
                            empListTalentAdapter.refreshAdapter(empList,pos)
                            dialog.dismiss()


                            if(response.body()!=null){

                            }
                        }
                    })

//                var empRoom = EmpListRoom()
//                empRoom?.id=Integer.parseInt(empList.id!!)
//                empRoom?.empAge= empList.empAge!!
//                empRoom?.empBranch= empList.empBranch!!
//                empRoom?.empCompany= empList.empCompany!!
//                empRoom?.compGroupID= empList.compGroupID!!
//                empRoom?.empDept= empList.empDept!!
//                empRoom?.empOrg= empList.empOrg!!
//                empRoom?.empPhoto= empList.empPhoto!!
//                empRoom?.empName= empList.empName!!
//                empRoom?.empNIK= empList.empNIK!!
//                empRoom?.empJobTitle= empList.empJobTitle!!
//                empRoom?.archievement= edtArchievement.text.toString()!!
//                empRoom?.recommendation= edtRecommendation.text.toString()!!
//
//                empList.recommendation = edtRecommendation.text.toString()!!
//                empList.archievement = edtArchievement.text.toString()!!
//
//                dao.update(empRoom)
//                empListTalentAdapter.refreshAdapter(empList,pos)
//                dialog.dismiss()
//                Toast.makeText(ctx,"Archievement dan recommendation terupdate!!",Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(ctx,"Archievement dan recommendation wajib diisi!!",Toast.LENGTH_LONG).show()
            }
        }


        btnCancel.setOnClickListener { dialog.dismiss() }
    }

    private fun showTalentProfile(empList: EmpList, activity: Activity?, fm: FragmentManager){
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
        args.putString("NIK", empList.empNIK)
        args.putString("DIRID", empList.dirId)
        args.putSerializable("DATA_DIRI", empList.dataDiri)


        (activity as MainActivity?)?.globals?.posId = empList.posId
        (activity as MainActivity?)?.globals?.dirId = empList.dirId
        (activity as MainActivity?)?.globals?.compId = empList.compID


        talentProfileFragment.arguments=args
        val transaction = fm.beginTransaction()
        transaction?.replace(R.id.myFragment, talentProfileFragment)
        transaction?.addToBackStack("TalentProfileFragment")
        transaction?.commit()


    }

}
