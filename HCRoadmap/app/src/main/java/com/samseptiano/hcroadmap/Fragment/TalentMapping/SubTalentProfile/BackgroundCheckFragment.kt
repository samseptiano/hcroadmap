package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.BGCheck
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.BackGroundCheckViewModel
import kotlinx.android.synthetic.main.fragment_background_check.view.*


class BackgroundCheckFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpID:String?=""

    var bgCheck = mutableListOf<BGCheck>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_background_check, container, false)

        empNIK = arguments?.getString("NIK")
        compGrpID = arguments?.getString("COMPGRPID")

        loadDataViewModel()

        return rootView
    }


    fun loadDataViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "190700650"

        val backgroundCheckViewModel = BackGroundCheckViewModel(empNIK?:"",dao.getToken(),compGrpID?:"",
            context!!
        )

        backgroundCheckViewModel.bgCheck
            .observe(this, object : Observer<List<BGCheck?>?> {
                override fun onChanged(currencyPojos: List<BGCheck?>?) {
                    bgCheck = currencyPojos as MutableList<BGCheck>

                    if(bgCheck.isNotEmpty()) {

                        rootView.tvCheckDate.text =
                            bgCheck.get(0).bgchecKDATE?.split(" ")?.get(0) ?: ""

                        rootView.tvSummary.text = bgCheck.get(0).bgchecKSUMMARY
                        rootView.tvRecommendation.text = bgCheck.get(0).bgchecKRECOMMEND
                        rootView.tvFilePath.text = ".../" + bgCheck.get(0).bgchecKFILE
                        rootView.buttonDownload.setOnClickListener {
                            val url =
                                APIConfig().baseURLBGCHECKATTACHMENT + bgCheck.get(0).bgchecKFILE
                                    ?: ""
                            val i = Intent(Intent.ACTION_VIEW)
                            i.data = Uri.parse(url)
                            startActivity(i)
                        }
                    }
                }
            })
    }

}
