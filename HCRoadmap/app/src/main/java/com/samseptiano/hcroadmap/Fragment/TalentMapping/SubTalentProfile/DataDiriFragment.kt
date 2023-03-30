package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.CareerHistoryAdapter
import com.samseptiano.hcroadmap.DataClass.CareerHistory
import com.samseptiano.hcroadmap.DataClass.EmpListDataDiri
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.DataDiriViewModel
import com.samseptiano.hcroadmap.dateConverter
import kotlinx.android.synthetic.main.fragment_data_diri.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DataDiriFragment : Fragment() {


    var empNIK:String?=""
    var dataDiri:EmpListDataDiri= EmpListDataDiri()
    var carrerHistory = mutableListOf<CareerHistory>()

    var isdataDiriExpanded:Boolean=true
    var iscareerHistoryExpanded:Boolean=true

    lateinit var rootview:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_data_diri, container, false)
        dataDiri = arguments?.getSerializable("DATA_DIRI") as EmpListDataDiri
        empNIK = arguments?.getString("NIK")

        rootview.tvSignDate.text=dateConverter.convertToLocalDate(dataDiri.empSignDate!!.split(" ")[0])
        if(dataDiri.empLastPromotion!=null) {
            rootview.tvLastPromotion.text = dateConverter.convertToLocalDate(dataDiri.empLastPromotion!!.split(" ")[0])
        }
        rootview.tvLocation.text=dataDiri.location
        rootview.tvHomeBase.text=dataDiri.empHomeBase
        rootview.tvBirthPlace.text=dataDiri.empBirthPlace
        rootview.tvBirthDate.text=dateConverter.convertToLocalDate(dataDiri.empBirthDate!!.split(" ")[0])
        rootview.tvAge.text=dataDiri.empAge+" TAHUN"
        rootview.tvLastEducation.text = dataDiri.emplastEducation
        rootview.tvMaritalStatus.text=dataDiri.empMaritalStatus
        rootview.tvRetirementDate.text=dateConverter.convertToLocalDate(dataDiri.empRetirementDate!!.split(" ")[0])



        loadCareerHistoryViewModel()
//        loadCareerHistory()

        return rootview
    }


    fun loadCareerHistoryViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val dataDiriViewModel = DataDiriViewModel(empNIK?:"",dao.getToken(), context!!)

        dataDiriViewModel.careerHistory
            .observe(this, object : Observer<List<CareerHistory?>?> {
                override fun onChanged(currencyPojos: List<CareerHistory?>?) {
                    carrerHistory = currencyPojos as MutableList<CareerHistory>
                    insertToRV()
                }
            })
    }

    fun loadCareerHistory():Unit{
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()


        //=======================================
        empNIK?.let {
            APIConfig().getService()
                .getCareerHistory(it,"Bearer "+dao.getToken())
                .enqueue(object : Callback<List<CareerHistory>> {
                    override fun onFailure(call: Call<List<CareerHistory>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<CareerHistory>>,
                        response: Response<List<CareerHistory>>
                    ) {

                        if(response.body()!=null){
                            carrerHistory = response.body() as MutableList<CareerHistory>
                            insertToRV()
                        }
                    }
                })
        }
        //================================================


    }

    fun insertToRV():Unit{
        val careerHistoryAdapter =  CareerHistoryAdapter(carrerHistory, context,activity,
            this.fragmentManager!!
        )

        rootview.rvCareerHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = careerHistoryAdapter
        }
    }
}
