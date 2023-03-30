package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.Adapter.AwardAdapter
import com.samseptiano.hcroadmap.Adapter.InovasiAdapter
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.DataClass.Inovasi

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.AwardViewModel
import com.samseptiano.hcroadmap.ViewModel.InovasiViewModel


class InovasiFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpId:String?=""
    var awardList = mutableListOf<Inovasi>()

    lateinit var rvAward: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_inovasi, container, false)

        empNIK = arguments?.getString("NIK")
        compGrpId = arguments?.getString("COMPGRPID")
        rvAward = rootView.findViewById(R.id.rvInovasi)

        loadAwardViewModel()

        return rootView    }

    fun loadAwardViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        empNIK = "020601233"
        val awardViewModel = InovasiViewModel(empNIK?:"", compGrpId?:"0",dao.getToken(), context!!)

        awardViewModel.award
            .observe(this, object : Observer<List<Inovasi?>?> {
                override fun onChanged(currencyPojos: List<Inovasi?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                                awardList = currencyPojos as MutableList<Inovasi>
                                insertToRV()

                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        val awardAdapter =  InovasiAdapter(awardList, context,activity,
            this.fragmentManager!!,this
        )

        rvAward.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = awardAdapter
        }

//        Handler().postDelayed({
//            scrollRV()
//        }, 1000)
    }
}
