package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.AwardFragment
import com.samseptiano.hcroadmap.Fragment.UserManagementFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Repo.UserManagementRepo
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import com.samseptiano.hcroadmap.ViewModel.UserManagementViewModel
import kotlinx.android.synthetic.main.add_user_management_dialog.*
import kotlinx.android.synthetic.main.award_item.view.*
import kotlinx.android.synthetic.main.career_history_item.view.tvNomor
import kotlinx.android.synthetic.main.fragment_user_management.view.*
import kotlinx.android.synthetic.main.show_direktorat_dialog.*
import kotlinx.android.synthetic.main.user_management_item.view.*
import mabbas007.tagsedittext.TagsEditText
import java.util.*


class UserManagementAdapter(private val userManagementList: List<UserManagement>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg: UserManagementFragment) : RecyclerView.Adapter<UsermanagementHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): UsermanagementHolder {
        return UsermanagementHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.user_management_item, viewGroup, false))
    }

    override fun getItemCount(): Int = userManagementList.size

    override fun onBindViewHolder(holder: UsermanagementHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
//                if(position == awardList.size-1){
//                    fg.loadAwardViewModel()
//                }
                holder.bindEmpList(userManagementList[position],ctx,activity,fm,position,fg)
            }
        }
    }
}

class UsermanagementHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNomor = view.tvNomor
    private val tvNamaPIC = view.tvNamaPIC
    private val tvRole = view.tvRole
    private val tvJumlahDir = view.tvJumlahDirektorat
    private val tvJumlahOrg = view.tvJumlahOrg
    private val tvPICMapping = view.tvPICMapping
    private val tvPICSalary = view.tvPICSalary
    private val tvPICUpload = view.tvPICUpload
    private val btnEdit = view.btnEdit
    private val btnEditOrg = view.btnEditOrg
    private val btnRemove = view.btnRemove

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(usermanagement: UserManagement,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int, fg:UserManagementFragment) {
        tvNomor.text = (pos+1).toString()+". "
        tvNamaPIC.text = usermanagement.empname
        tvRole.text = usermanagement.role?.split("-")?.get(1) ?: ""
        tvJumlahDir.text = usermanagement.totalusermanagementdir+" Direktorat"?:"0 Direktorat"
        tvJumlahOrg.text = usermanagement.totalusermanagementorg+" Organization"?:"0 Organization"
        tvPICMapping.text = usermanagement.flagpic
        tvPICSalary.text = usermanagement.flagsalary
        tvPICUpload.text = usermanagement.flaguploadstruktur.toString()

        tvJumlahOrg.setOnClickListener {
            showDirektoratDialog(usermanagement as Any,ctx,pos,activity,fm,"ORG",fg)
        }
        tvJumlahDir.setOnClickListener {
            showDirektoratDialog(usermanagement as Any,ctx,pos,activity,fm,"DIR",fg)
        }

        btnEdit.setOnClickListener {
            fg.showAddDialog(usermanagement)
        }
        btnRemove.setOnClickListener {


            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()


            var aaa = UserManagement();
            aaa.nik = usermanagement.nik
            aaa.empname = usermanagement.empname
            aaa.compgrpid = usermanagement.compgrpid
            aaa.flagpic = usermanagement.flagpic
            aaa.flagsalary = usermanagement.flagsalary
            aaa.flaguploadstruktur = usermanagement.flaguploadstruktur
            aaa.role = usermanagement.role
            aaa.upduser = dao.getusername()
            aaa.groupcode = usermanagement.groupcode

            aaa.usermanagementdir = usermanagement.usermanagementdir

            val userManagementViewModel = UserManagementViewModel(aaa,"0","0",dao.getToken(),
                ctx!!
            )

            userManagementViewModel.deleteuserManagement
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        fg.loadAwardViewModel()
                    }
                })


        }
        btnEditOrg.setOnClickListener {
            showAddOrgDialog(ctx,fg,usermanagement)
        }
    }

    private fun showDirektoratDialog(obj: Any, ctx: Context, POSID:Int,activity:Activity,fm:FragmentManager,type:String, fg:UserManagementFragment){
        val dialog = Dialog(ctx)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.show_direktorat_dialog)
        dialog!!.show()

        val btnOk = dialog.findViewById(R.id.btnOk) as Button

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val userManagementViewModel = ctx?.let {
            UserManagementViewModel(obj,"","",dao.getToken(),
                it
            )
        }

        if(type.equals("ORG")){
            userManagementViewModel?.userManagementOrg
                ?.observe(fg, object : androidx.lifecycle.Observer<List<UsermanagementorgItem?>?> {
                    override fun onChanged(currencyPojos: List<UsermanagementorgItem?>?) {
                        var orgObjList = currencyPojos as MutableList<UsermanagementorgItem>
                        var direktoratAdapter =
                            DirektoratAdapter(
                                orgObjList, ctx,
                                activity,fm,type
                            )

                        dialog.rvDirektorat.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = direktoratAdapter
                        }

                    }
                })

        }
       else if(type.equals("DIR")){
            userManagementViewModel?.userManagementDir
                ?.observe(fg, object : androidx.lifecycle.Observer<List<UsermanagementdirItem?>?> {
                    override fun onChanged(currencyPojos: List<UsermanagementdirItem?>?) {
                        var orgObjList = currencyPojos as MutableList<UsermanagementdirItem>
                        var direktoratAdapter =
                            DirektoratAdapter(
                                orgObjList, ctx,
                                activity,fm,type
                            )

                        dialog.rvDirektorat.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = direktoratAdapter
                        }

                    }
                })

        }




        btnOk.setOnClickListener { dialog.dismiss() }
    }


    fun showAddOrgDialog(context: Context,fg:UserManagementFragment,usermanagement: UserManagement){
        val dialog = context?.let { Dialog(it) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_org_dialog)
        dialog!!.show()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        var orgObjList = mutableListOf<Org>()
        var orgList = mutableListOf<String>()
        var tagOrg = dialog.findViewById<TagsEditText>(R.id.tagsOrganization)

        var aaa = Org()

        var dir=""


        if(usermanagement.usermanagementdir != null) {
            for (i in 0 until usermanagement.usermanagementdir?.size!!) {
                dir += usermanagement.usermanagementdir!![i]?.dirid + ","
            }
            aaa.dirid = dir
        }
        val userManagementViewModel = context?.let {
            UserManagementViewModel(aaa,"","",dao.getToken(),
                it
            )
        }

        userManagementViewModel?.getOrg
            ?.observe(fg, object : androidx.lifecycle.Observer<List<Org?>?> {
                override fun onChanged(currencyPojos: List<Org?>?) {
                   orgObjList = currencyPojos as MutableList<Org>

                    if(usermanagement.usermanagementorg!=null){
                        var xxx = mutableListOf<String>()
                        for(i in 0 until usermanagement.usermanagementorg!!.size){
                            xxx.add(usermanagement.usermanagementorg!![i]?.orgname.toString())
                        }
                        tagOrg.setTags(xxx.toTypedArray())
                    }

                    for(i in 0 until orgObjList.size){
                        orgList.add(orgObjList[i].orgname.toString())
                    }


                    val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_dropdown_item,
                        orgList
                    )

                    tagOrg.setTagsWithSpacesEnabled(true)
                    tagOrg.setAdapter(dataAdapter)
                    tagOrg.setThreshold(1)

                }
            })


        dialog.btnOK.setOnClickListener {

            var orgObjListFilter: MutableList<Org> = mutableListOf()
            var monthList: List<Org> = mutableListOf()
            for (i in tagOrg.getTags().indices) {

                monthList = orgObjList!!.filter { s -> s.orgname == tagOrg.getTags().get(i) }

                for (i in 0 until monthList.size) {
                    orgObjListFilter.add(monthList.get(i))
                }
            }

            var listDir = mutableListOf<UsermanagementorgItem>()
            for (i in 0 until orgObjListFilter.size) {
                var aa = UsermanagementorgItem()
                aa.dirid = orgObjListFilter[i].dirid
                aa.dirname = orgObjListFilter[i].dirid
                aa.orgid = orgObjListFilter[i].orgid
                aa.orgname = orgObjListFilter[i].orgname
                aa.compgrpid = usermanagement.compgrpid
                listDir.add(aa)
            }

            var aaa = UserManagement();
            aaa.nik = usermanagement.nik
            aaa.empname = usermanagement.empname
            aaa.compgrpid = usermanagement.compgrpid
            aaa.flagpic = usermanagement.flagpic
            aaa.flagsalary = usermanagement.flagsalary
            aaa.flaguploadstruktur = usermanagement.flaguploadstruktur
            aaa.role = usermanagement.role
            aaa.upduser = dao.getusername()
            aaa.groupcode = usermanagement.groupcode

            aaa.usermanagementdir = usermanagement.usermanagementdir
            aaa.usermanagementorg = listDir

            val userManagementViewModel = UserManagementViewModel(aaa,"0","0",dao.getToken(),
                context!!
            )

            userManagementViewModel.insertuserManagement
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        dialog.dismiss()
                       fg.loadAwardViewModel()
                    }
                })
        }
        dialog.btnCancel.setOnClickListener { dialog.dismiss() }
    }



    }


