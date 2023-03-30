package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.Adapter.ViolationAdapter
import com.samseptiano.hcroadmap.DataClass.ConstraintResponse
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.ConstraintViewModel
import kotlinx.android.synthetic.main.fragment_constraint.view.*


class ConstraintFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpID:String?=""
    var constraintResponseList = ConstraintResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_constraint, container, false)
        empNIK = arguments?.getString("NIK")
        compGrpID = arguments?.getString("COMPGRPID")

        loadViewModel()

        return  rootView
    }



    fun loadViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        //empNIK = "190700650"

        val constraintViewModel = ConstraintViewModel(empNIK?:"",dao.getToken(),compGrpID?:"",
            context!!
        )

        constraintViewModel.constraint
            .observe(this, object : Observer<ConstraintResponse?> {
                override fun onChanged(currencyPojos: ConstraintResponse?) {
                    if (currencyPojos != null) {
                        constraintResponseList = currencyPojos

                        if(constraintResponseList.constraint !=null) {
                            rootView.constraintNote.setText(constraintResponseList.constraint!!.constrainTNOTE)
                            if(constraintResponseList.constraint!!.constrainTVALUE.equals("N")){
                                rootView.cbtidakAdaConstraint.isChecked=true
                            }
                            else {
                                rootView.cbAdaConstraint.isChecked=true
                            }
                        }
                        if(constraintResponseList.violations !=null) {
                            val violationAdapter =  ViolationAdapter(
                                constraintResponseList.violations!!, context,activity,
                                this@ConstraintFragment.fragmentManager!!
                            )

                            rootView.rvViolation.apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = violationAdapter
                            }
                        }
                    }
                }
            })
    }

}
