package com.samseptiano.hcroadmap.Fragment.TalentMapping

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.DataClass.POST.POSTMAPPING
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import kotlinx.android.synthetic.main.fragment_item_list_dialog_list_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemListDialogFragment() : BottomSheetDialogFragment() {

    lateinit var rootView:View
    private val mOnClickInterface: onClickInterface? = null

    var empList = EmpList()
    var pos = 0;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false)

        rootView.tvEmpName.text = this.getArguments()?.getString("EMPNAME") ?: "-" ;
        pos = this.getArguments()?.getInt("POS") ?: 0
        empList = this.getArguments()?.getSerializable("EMPLIST") as EmpList



        rootView.editBtn.setOnClickListener(View.OnClickListener {
            context?.let { it1 -> showArchievementDialog(empList, it1,pos) }
            dismiss()
        })

        rootView.removeBtn.setOnClickListener(View.OnClickListener {
            removeItem(empList)
        })

        return rootView
    }

    fun removeItem(empList: EmpList){
                    //deleteEmployee(empList,ctx,empListTalentAdapter,pos)

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()

            var postmapping = POSTMAPPING()
            postmapping.archievement = empList.archievement
            postmapping.reason = empList.recommendation
            postmapping.compid = empList.compID.toString()
            postmapping.dirid = empList.dirId.toString()
            postmapping.trgrpid = "0"
            postmapping.posid = empList.posId.toString()
            postmapping.upduser = dao.getusername()
            postmapping.empnik = empList.empNIK


            APIConfig().getService()
                .deleteToMapping(postmapping,"Bearer "+dao.getToken())!!
                .enqueue(object : Callback<Void> {
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<Void>,
                        response: Response<Void>
                    ) {
                        mOnClickInterface?.removeData(empList,pos);

                    }
                })

    }

    fun showArchievementDialog(empList: EmpList, ctx: Context, pos:Int){

        var database = EmpListRoomDatabase.getDatabase(ctx)
        var dao = database.getEmpListDao()

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
                            //empListTalentAdapter.refreshAdapter(empList,pos)
                            mOnClickInterface?.editData(empList,pos);

                            dialog.dismiss()



                            if(response.body()!=null){

                            }
                        }
                    })

            }
            else{
                Toast.makeText(ctx,"Archievement dan recommendation wajib diisi!!", Toast.LENGTH_LONG).show()
            }
        }


        btnCancel.setOnClickListener { dialog.dismiss() }
    }

}
interface onClickInterface {
    fun editData(empList: EmpList,position: Int)
    fun removeData(empList: EmpList,position: Int)

}