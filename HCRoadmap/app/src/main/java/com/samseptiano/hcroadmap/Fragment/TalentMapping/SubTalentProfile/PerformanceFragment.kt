package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.Adapter.OtherArchievementAdapter
import com.samseptiano.hcroadmap.Adapter.PAAdapter
import com.samseptiano.hcroadmap.DataClass.OtherArchievement
import com.samseptiano.hcroadmap.DataClass.PA

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.PerfomanceViewModel
import kotlinx.android.synthetic.main.fragment_performance.view.*


class PerformanceFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpId:String?=""
    var paList = mutableListOf<PA>()
    var otherArchievementList = mutableListOf<OtherArchievement>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_performance, container, false)
        empNIK = arguments?.getString("NIK")
        compGrpId = arguments?.getString("COMPGRPID")
        loadPAViewModel()
        loadAwardViewModelOtherArch()
        return rootView
    }



    fun loadPAViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        empNIK = "T040400126"
        val perfomanceViewModel = PerfomanceViewModel(empNIK?:"", compGrpId?:"",dao.getToken(), context!!)

        perfomanceViewModel.pa
            .observe(this, object : Observer<List<PA?>?> {
                override fun onChanged(currencyPojos: List<PA?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            paList = currencyPojos as MutableList<PA>
                                insertToRV()

                        }
                    }
                }
            })
    }
    fun insertToRV():Unit{
        val paAdapter =  PAAdapter(paList, context,activity,
            this.fragmentManager!!,this
        )
        rootView.rvPA.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = paAdapter
        }

    }

    fun loadAwardViewModelOtherArch(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        empNIK = "190500476"
        val perfomanceViewModel = PerfomanceViewModel(empNIK?:"", compGrpId?:"",dao.getToken(), context!!)

        perfomanceViewModel.otherArchievement
            .observe(this, object : Observer<List<OtherArchievement?>?> {
                override fun onChanged(currencyPojos: List<OtherArchievement?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            otherArchievementList = currencyPojos as MutableList<OtherArchievement>
                            insertToRVOtherArchievement()

                        }
                    }
                }
            })
    }
    fun insertToRVOtherArchievement():Unit{
        val otherArchievementAdapter =  OtherArchievementAdapter(otherArchievementList, context,activity,
            this.fragmentManager!!,this
        )
        rootView.rvOtherArchievement.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = otherArchievementAdapter
        }

    }

}
