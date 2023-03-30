package com.samseptiano.hcroadmap.Fragment.TalentMapping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.Fragment.TabCompareFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter.PagerAdapters
import com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter.ViewPagerAdapter
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.*
import com.samseptiano.hcroadmap.R
import kotlinx.android.synthetic.main.tab_talent_profile.view.*


class CompareFragment : Fragment(){
    lateinit var rootView:View

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    var empList = EmpList()
    var empList2 = EmpList()

    var tabActive:Int=0


    interface compareInterface{
        fun getTabActive(tabPos:Int)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_compare, container, false)

        empList = arguments?.getSerializable("DATA_DIRI1") as EmpList
        empList2 = arguments?.getSerializable("DATA_DIRI2") as EmpList

        (activity as AppCompatActivity?)!!.supportActionBar!!.title="Compare Karyawan"
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })

        rootView.btnDataDiri.setOnClickListener {
            goToTab(rootView.btnDataDiri, TabCompareFragment(),"DATADIRI")

        }
        rootView.btnAssessmentResult.setOnClickListener {
            goToTab(rootView.btnAssessmentResult, TabCompareFragment(),"ASSESSMENTRESULT")
            //viewpager2(AssessmentResultFragment(),AssessmentResultFragment())

        }
        rootView.btnLearning.setOnClickListener {
            goToTab(rootView.btnLearning, TabCompareFragment(),"LEARNING")

        }
        rootView.btnBgCheck.setOnClickListener {
            goToTab(rootView.btnBgCheck, TabCompareFragment(),"BACKGROUNDCHECK")

        }
        rootView.btn360Feedback.setOnClickListener {
            goToTab(rootView.btn360Feedback, TabCompareFragment(),"FEEDBACK360")

        }
        rootView.btnConstraint.setOnClickListener {
            goToTab(rootView.btnConstraint, TabCompareFragment(),"CONSTRAINT")

        }
        rootView.btnAttachment.setOnClickListener {
            goToTab(rootView.btnAttachment, TabCompareFragment(),"ATTACHMENT")

        }
        rootView.btnInovasi.setOnClickListener {
            goToTab(rootView.btnInovasi, TabCompareFragment(),"INOVASI")

        }
        rootView.btnAward.setOnClickListener {
            goToTab(rootView.btnAward, TabCompareFragment(),"AWARD")

        }
        rootView.btnPerformance.setOnClickListener {
            goToTab(rootView.btnPerformance, TabCompareFragment(),"PERFORMANCE")

        }
        rootView.btnOrgStructure.setOnClickListener {
            goToTab(rootView.btnOrgStructure, TabCompareFragment(),"ORGSTRUCTURE")

        }

        goToTab(rootView.btnDataDiri,TabCompareFragment(),"DATADIRI")
        //viewpager2(DataDiriFragment(),DataDiriFragment())



        return rootView
    }


    private fun goToTab(button: Button, fragment:Fragment,tab:String){
        clearButtonColor()
        button.setBackgroundResource(R.drawable.shapepurple);

        val dataFragment =
            fragment
        val args = Bundle()
        args.putSerializable("DATA_DIRI1", empList)
        args.putSerializable("DATA_DIRI2", empList2)
        args.putSerializable("ISCOMPARED", "Y")
        args.putInt("TABACTIVE", tabActive)
        args.putString("TAB", tab)

        dataFragment.arguments=args
        // Begin the transaction
        val ft: FragmentTransaction = fragmentManager!!.beginTransaction()
        ft.replace(R.id.fragmentTabCompare, dataFragment)
        ft.commit()
    }

//    private fun inittabLayout(button: Button, fragment:Fragment,fragment2:Fragment){
//        viewPager = rootView.findViewById(R.id.ViewPager)
//        setupViewPager(viewPager!!,button,fragment,fragment2)
//        button.setBackgroundResource(R.drawable.shapepurple);
//
//        tabLayout = rootView.findViewById(R.id.Tabs)
//
//        tabLayout!!.setupWithViewPager(viewPager)
//
//        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        viewPager!!.offscreenPageLimit = 2
//
//        tabLayout!!.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager!!.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//    }
//
//    private fun setupViewPager(viewPager: ViewPager,button: Button, fragment:Fragment, fragment2: Fragment) {
//        clearButtonColor()
//        var adapter = PagerAdapters(context,parentFragmentManager,2)
//        var fr =
//            fragment
//
//
//        var args = Bundle()
//        args.putString("EMP_NAME", empList.empName)
//        args.putString("EMP_PHOTO", empList.empPhoto)
//        args.putString("JOB_TITLE_NAME", empList.empJobTitle)
//        args.putString("JOB_LEVEL", empList.empJobLevel)
//        args.putString("COMPANY_NAME", empList.empCompany)
//        args.putString("ORG_NAME", empList.empOrg)
//        args.putString("DIR_NAME", empList.empDept)
//        args.putString("NIK", empList.empNIK)
//        args.putSerializable("DATA_DIRI", empList.dataDiri)
//        args.putSerializable("ISCOMPARED", "Y")
//        args.putInt("TABACTIVE", tabActive)
//
//        fr.arguments=args
//        adapter.addFragment(fr, empList.empName.toString())
//
//
//        //==================================================================
//        var fr2 =
//            fragment2
//
//        var args2 = Bundle()
//        args2.putString("EMP_NAME", empList2.empName)
//        args2.putString("EMP_PHOTO", empList2.empPhoto)
//        args2.putString("JOB_TITLE_NAME", empList2.empJobTitle)
//        args2.putString("JOB_LEVEL", empList2.empJobLevel)
//        args2.putString("COMPANY_NAME", empList2.empCompany)
//        args2.putString("ORG_NAME", empList2.empOrg)
//        args2.putString("DIR_NAME", empList2.empDept)
//        args2.putString("NIK", empList2.empNIK)
//        args2.putSerializable("DATA_DIRI", empList2.dataDiri)
//        args2.putSerializable("ISCOMPARED", "Y")
//        args2.putInt("TABACTIVE", tabActive)
//
//        fr2.arguments=args2
//        adapter.addFragment(fr2, empList2.empName.toString())
//        //==================================================================
//        viewPager.adapter = adapter
//        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Compare Talent")
//
//    }
//
    fun clearButtonColor(){
        rootView.btnDataDiri.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnAssessmentResult.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnLearning.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnBgCheck.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btn360Feedback.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnConstraint.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnAttachment.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnInovasi.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnAward.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnPerformance.setBackgroundResource(R.drawable.shape_purple_border);
        rootView.btnOrgStructure.setBackgroundResource(R.drawable.shape_purple_border);

    }
}
