package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.AssessmentResult
import com.samseptiano.hcroadmap.DataClass.EmpListDataDiri
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.AssessmentResultViewModel
import com.samseptiano.hcroadmap.dateConverter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_assessment_result.view.*
import kotlinx.android.synthetic.main.show_structure_fullscreen_dialog.*


class AssessmentResultFragment : Fragment() {


    var empNIK:String?=""
    var dataDiri: EmpListDataDiri = EmpListDataDiri()
    var assessmentResult = mutableListOf<AssessmentResult>()


    lateinit var rootView:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_assessment_result, container, false)
        empNIK = arguments?.getString("NIK")

        loadAssessmentResultViewModel()
        return rootView
    }



    fun loadAssessmentResultViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "190700650"

        val assessmentResult = AssessmentResultViewModel(empNIK?:"",dao.getToken(), context!!)

        assessmentResult.assessmentresult
            .observe(this, object : Observer<List<AssessmentResult?>?> {
                override fun onChanged(currencyPojos: List<AssessmentResult?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            rootView.tvtanggalAssessment.text = currencyPojos!![0]?.assesmenTDATE?.let {
                                dateConverter.convertToLocalDate(
                                    it
                                )
                            }
                            rootView.tvPICAssessment.text = currencyPojos!![0]?.assesmenTPIC
                            rootView.tvAssessmentScore.text = currencyPojos!![0]?.assesmenTSCORE
                            rootView.tvAssessmentResult.text = currencyPojos!![0]?.assesmenTRESULT
                            rootView.tvAssessmentCapacity.text = currencyPojos!![0]?.assessmenTCAPACITY
                            rootView.tvDISC.text = currencyPojos!![0]?.assessmenTDISC

                            Glide.with(context!!).load(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTIMG1)
                                .into(rootView.imgAssessment1)

                            Glide.with(context!!).load(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTIMG2)
                                .into(rootView.imgAssessment2)
                            Glide.with(context!!).load(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTCHARTPIC)
                                .into(rootView.imgAssessment3)
                            Glide.with(context!!).load(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTHAVPIC)
                                .into(rootView.imgAssessment4)
                            rootView.imgAssessment1.setOnClickListener {
                                showStructureFullscreenDialog(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTIMG1)
                            }
                            rootView.imgAssessment2.setOnClickListener {
                                showStructureFullscreenDialog(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTIMG2)
                            }
                            rootView.imgAssessment3.setOnClickListener {
                                showStructureFullscreenDialog(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTCHARTPIC)
                            }
                            rootView.imgAssessment4.setOnClickListener {
                                showStructureFullscreenDialog(APIConfig().baseURLImage + currencyPojos!![0]?.assessmenTHAVPIC)
                            }

                        }
                    }
                }
            })
    }



    private fun showStructureFullscreenDialog(url:String){
        val dialog = context?.let { Dialog(it, R.style.AppBaseTheme) }
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.show_structure_fullscreen_dialog)
        dialog!!.show()
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        Picasso.get().load(url).into(dialog.imgFullScreen)


    }


}
