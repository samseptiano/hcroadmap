package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.Adapter.AwardAdapter
import com.samseptiano.hcroadmap.DataClass.Award
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.AwardViewModel


class AwardFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpId:String?=""
    var awardList = mutableListOf<Award>()
    val pageSize = 100
    var pagenumber = 0

    lateinit var rvAward:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_award, container, false)

        empNIK = arguments?.getString("NIK")
        compGrpId = arguments?.getString("COMPGRPID")
        rvAward = rootView.findViewById(R.id.rvAward)

        loadAwardViewModel()

        return rootView
    }

    fun loadAwardViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "900800020"
        val awardViewModel = AwardViewModel(empNIK?:"", compGrpId?:"",pageSize,pagenumber,dao.getToken(), context!!)

        awardViewModel.award
            .observe(this, object : Observer<List<Award?>?> {
                override fun onChanged(currencyPojos: List<Award?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            if(pagenumber==0) {
                                awardList = currencyPojos as MutableList<Award>
                                insertToRV()
                            }else{
                                awardList.addAll(currencyPojos as MutableList<Award>)
                                insertToRV()
                            }




                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        val awardAdapter =  AwardAdapter(awardList, context,activity,
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

    fun scrollRV(){
        rvAward.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    pagenumber = pagenumber+pageSize
                    loadAwardViewModel()
                    //Toast.makeText(getContext(), "Last", Toast.LENGTH_LONG).show();
                }
            }
        })
    }
}
