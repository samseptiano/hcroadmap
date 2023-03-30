package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.Adapter.LDPAdapter
import com.samseptiano.hcroadmap.Adapter.TrainingAdapter
import com.samseptiano.hcroadmap.DataClass.LDP
import com.samseptiano.hcroadmap.DataClass.Training

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.LearningViewModel
import kotlinx.android.synthetic.main.fragment_learning.view.*


class LearningFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpID:String?=""

    var trainingList = mutableListOf<Training>()
    var ldpList = mutableListOf<LDP>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_learning, container, false)

        empNIK = arguments?.getString("NIK")
        compGrpID = arguments?.getString("COMPGRPID")

        loadLDPViewModel()

        loadTrainingViewModel("INTERNAL")
        loadTrainingViewModel("EKSTERNAL")
        loadTrainingViewModel("KM")

        return rootView
    }


    fun loadTrainingViewModel(type:String){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val learningViewModel = LearningViewModel(empNIK?:"",dao.getToken(),compGrpID?:"",type,
            context!!
        )

        learningViewModel.Training
            .observe(this, object : Observer<List<Training?>?> {
                override fun onChanged(currencyPojos: List<Training?>?) {
                    trainingList = currencyPojos as MutableList<Training>
                    insertToRV(type)
                }
            })
    }

    fun loadLDPViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val learningViewModel = LearningViewModel(empNIK?:"",dao.getToken(),compGrpID?:"","",
            context!!
        )

        learningViewModel.LDP
            .observe(this, object : Observer<List<LDP?>?> {
                override fun onChanged(currencyPojos: List<LDP?>?) {
                    ldpList = currencyPojos as MutableList<LDP>
                    insertToRVLDP()
                }
            })
    }

    fun insertToRV(type: String):Unit{
        val trainingAdapter =  TrainingAdapter(trainingList, context,activity,
            this.fragmentManager!!
        )

        if(type.equals("INTERNAL")) {
            rootView.rvTrainingInternal.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trainingAdapter
            }
        }
        if(type.equals("EXTERNAL")) {
            rootView.rvTrainingExternal.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trainingAdapter
            }
        }
        if(type.equals("KM")) {
            rootView.rvKnowledgeManagement.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trainingAdapter
            }
        }
    }

    fun insertToRVLDP():Unit{
        val trainingAdapter =  LDPAdapter(ldpList, context,activity,
            this.fragmentManager!!
        )

            rootView.rvLDP.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = trainingAdapter
            }
    }

}
