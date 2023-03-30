package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.samseptiano.hcroadmap.DataClass.Feedback360

import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.Feedback360ViewModel
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.fragment_feedback360.view.*
import kotlinx.android.synthetic.main.fragment_feedback360.view.tvAreaImprovement
import kotlinx.android.synthetic.main.fragment_feedback360.view.tvStrength


class feedback360Fragment : Fragment() {

    lateinit var rootView: View
    var empNIK:String?=""
    var compGrpID:String?=""

    var feedback360 = mutableListOf<Feedback360>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_feedback360, container, false)
        empNIK = arguments?.getString("NIK")
        compGrpID = arguments?.getString("COMPGRPID")

        loadViewModel()
        return rootView
    }

    fun loadViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "190700650"

        val feedback360ViewModel = Feedback360ViewModel(empNIK?:"",dao.getToken(),compGrpID?:"",
            context!!
        )

        feedback360ViewModel.feedback
            .observe(this, object : Observer<List<Feedback360?>?> {
                override fun onChanged(currencyPojos: List<Feedback360?>?) {
                    feedback360 = currencyPojos as MutableList<Feedback360>

                    if(feedback360.isNotEmpty()) {

                        rootView.tvTanggal.text = dateConverter.convertToLocalDate(
                            feedback360.get(0).feedbacKDATE?.split(" ")?.get(0) ?: "")

                        rootView.tvStrength.text = feedback360.get(0).feedbackTSTRENGTH
                        rootView.tvAreaImprovement.text = feedback360.get(0).feedbackTIMPROVEMENT
                    }
                }
            })
    }

}
