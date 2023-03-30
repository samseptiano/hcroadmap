package com.samseptiano.hcroadmap.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.samseptiano.hcroadmap.Adapter.DBIconAdapter
import com.samseptiano.hcroadmap.BroadcastReceiver.CheckConnection
import com.samseptiano.hcroadmap.DataClass.Module
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.orgStructureFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentmappingFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.TalentReviewFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.ModuleRoom
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    private val talentmappingFragment =
        TalentmappingFragment()

    private val userManagementFragment =
        UserManagementFragment()


    private val orgStructureFragment =
        orgStructureFragment()

    private val ldpMasterSettingFragment =
        LDPMasterSettingFragment()


    private val talentReviewFragment =
        TalentReviewFragment()

    private val customCategoryFragment =
        CustomCategoryFragment()

    private val talentProfileDBFragment =
        TalentProfileDBFragment()

    lateinit var rootView:View
    lateinit var btntalentmapping:ImageButton



    var conn: CheckConnection =
        CheckConnection();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false)

        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar!!.setDisplayShowCustomEnabled(false)
        actionBar.setTitle(getString(R.string.app_name))
        loadIcon()
        return  rootView;
    }


    fun loadIcon(){
        var listModuleRoom = listOf<ModuleRoom>()
        var listModule = mutableListOf<Module>()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        listModuleRoom = dao.getAllModule()

        for (i in 0 until listModuleRoom.size){
            var aaa = Module()

            aaa.fgapp = listModuleRoom.get(i).fgapp
            val id = resources.getIdentifier(listModuleRoom.get(i).icon, "drawable", context!!.packageName)

            aaa.icon = id.toString()
            aaa.modulecode = listModuleRoom.get(i).modulecode
            aaa.moduledesc = listModuleRoom.get(i).moduledesc
            aaa.ordinal = listModuleRoom.get(i).ordinal
            aaa.pagename = listModuleRoom.get(i).pagename

            listModule.add(aaa)
        }

        val dBIconAdapter =  DBIconAdapter(listModule, context,activity,
            this.fragmentManager!!, this
        )

        rootView.rvIcon.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = dBIconAdapter
        }

    }

    fun action(moduleCode:String){
        when(moduleCode){
            "TLNTMPG"->{

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, talentmappingFragment)
                transaction?.addToBackStack("TalentmappingFragment")
                transaction?.commit()
            }
            "PPLRVW"->{
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, talentReviewFragment)
                transaction?.addToBackStack("TalentReviewFragment")
                transaction?.commit()
            }
            "USRMNG"->{

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, userManagementFragment)
                transaction?.addToBackStack("UserManagementFragment")
                transaction?.commit()
            }
            "TLNTPRF"->{
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, talentProfileDBFragment)
                transaction?.addToBackStack("TalentProfileDBFragment")
                transaction?.commit()
            }
            "CSTCTGSTG"->{
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, customCategoryFragment)
                transaction?.addToBackStack("CustomCategoryFragment")
                transaction?.commit()
            }
            "LDPMSTSTG"->{
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, ldpMasterSettingFragment)
                transaction?.addToBackStack("LDPMasterSettingFragment")
                transaction?.commit()
            }
            "ORGSTRSTG"->{

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.myFragment, orgStructureFragment)
                transaction?.addToBackStack("OrgStructureFragment")
                transaction?.commit()


            }
        }
    }

}
