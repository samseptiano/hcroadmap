package com.samseptiano.hcroadmap.Fragment.TalentMapping

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appyvet.rangebar.RangeBar
import com.appyvet.rangebar.RangeBar.OnRangeBarChangeListener
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.Adapter.EmpListSearchTalentAdapter
import com.samseptiano.hcroadmap.Adapter.EmpListTalentAdapter
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import kotlinx.android.synthetic.main.compare_dialog.*
import kotlinx.android.synthetic.main.fragment_filter_talent_mapping_candidate.view.*
import kotlinx.android.synthetic.main.search_candidate_filter_dialog.*
import kotlinx.android.synthetic.main.search_candidate_filter_dialog.btnCancel
import mabbas007.tagsedittext.TagsEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class FilterTalentMappingCandidateFragment : Fragment() {


    lateinit var rootView: View
    lateinit var btnShowFilterDialog:Button
    lateinit var rvEmpList:RecyclerView
    lateinit var employeeAdapter:EmpListTalentAdapter

    var talentMappingAfterFilter:MutableList<EmpTalentMappingAfter> = mutableListOf()
    var talentMappingAfter:MutableList<EmpTalentMappingAfter> = mutableListOf()
    var listEmpListAdapter:MutableList<EmpList> = mutableListOf()


        var pendidikan:String?=""
        var golongan:String?=""
        var posisiAwal:String?=""

        lateinit var compID:String
        lateinit var posID:String
        lateinit var dirID:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_filter_talent_mapping_candidate, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Mapping")
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })

        rvEmpList = rootView.findViewById(R.id.rvSearchCandidate)

        compID = arguments?.getString("COMP_ID").toString()
        posID = arguments?.getString("POS_ID").toString()
        dirID = arguments?.getString("DIR_ID").toString()

        loadData()



        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val mSearch = menu.findItem(R.id.mi_search)
        val mSearchView =
            mSearch.actionView as SearchView
        mSearchView.queryHint = "Search"
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                var charText = s.toString().toLowerCase(Locale.getDefault())
                    talentMappingAfter = mutableListOf()
                    if (charText.isEmpty()) {
                        talentMappingAfter = talentMappingAfterFilter
                    } else {
                        for (i in 0 until talentMappingAfterFilter!!.size) {
                            if (talentMappingAfterFilter?.get(i)?.empname.toString().toLowerCase(Locale.getDefault())
                                    .contains(charText)
                            ) {
                                talentMappingAfter?.add(talentMappingAfterFilter?.get(i)!!)
                            }
                        }
                    }
                displayToRV()
                return true
            }
        })
    }



    fun loadData(){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),compID?:"",posID?:"",dirID?:"","0",null,
            context!!
        )

        talentMappingViewModel.empAftermapping
            .observe(viewLifecycleOwner, object : Observer<List<EmpTalentMappingAfter?>?> {
                override fun onChanged(currencyPojos: List<EmpTalentMappingAfter?>?) {
                    talentMappingAfterFilter = currencyPojos as MutableList<EmpTalentMappingAfter>
                    talentMappingAfter = currencyPojos as MutableList<EmpTalentMappingAfter>

                    displayToRV()

                }
            })

    }

    fun displayToRV(){
        val talentMappingViewModel = context?.let {
            TalentMappingViewModel("","","","","","",talentMappingAfter as MutableList<Any>,
                it
            )
        }

        talentMappingViewModel?.insertintoEmpList
            ?.observe(this, object : Observer<List<EmpList?>?> {
                override fun onChanged(currencyPojos: List<EmpList?>?) {
                    listEmpListAdapter = currencyPojos as MutableList<EmpList>
                    employeeAdapter = listEmpListAdapter?.let { EmpListTalentAdapter(it, context,activity,this@FilterTalentMappingCandidateFragment.fragmentManager!!,this@FilterTalentMappingCandidateFragment) }
                    rvEmpList.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = employeeAdapter
                    }

                    employeeAdapter.notifyDataSetChanged()                }
            })
    }
}

