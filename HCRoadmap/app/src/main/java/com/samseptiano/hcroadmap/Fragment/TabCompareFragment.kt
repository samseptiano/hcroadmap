package com.samseptiano.hcroadmap.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.samseptiano.hcroadmap.DataClass.EmpList
import com.samseptiano.hcroadmap.DataClass.OrgStructure
import com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter.PagerAdapters
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.*
import com.samseptiano.hcroadmap.R


class TabCompareFragment : Fragment() {

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    var empList = EmpList()
    var empList2 = EmpList()
    var tab=""
    var isCompared="N"
    var tabActive:Int=0
    lateinit var rootView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_compare, container, false)

        empList = arguments?.getSerializable("DATA_DIRI1") as EmpList
        empList2 = arguments?.getSerializable("DATA_DIRI2") as EmpList
        tab = arguments?.getString("TAB").toString()
        isCompared = arguments?.getString("ISCOMPARED").toString()

        when (tab) {
            "DATADIRI" -> inittabLayout(DataDiriFragment(),DataDiriFragment())
            "ASSESSMENTRESULT" -> inittabLayout(AssessmentResultFragment(),AssessmentResultFragment())
            "LEARNING" -> inittabLayout(LearningFragment(),LearningFragment())
            "BACKGROUNDCHECK" -> inittabLayout(BackgroundCheckFragment(),BackgroundCheckFragment())
            "FEEDBACK360" -> inittabLayout(feedback360Fragment(),feedback360Fragment())
            "CONSTRAINT" -> inittabLayout(ConstraintFragment(),ConstraintFragment())
            "ATTACHMENT" -> inittabLayout(AttachmentFragment(),AttachmentFragment())
            "INOVASI" -> inittabLayout(InovasiFragment(),InovasiFragment())
            "AWARD" -> inittabLayout(AwardFragment(),AwardFragment())
            "PERFORMANCE" -> inittabLayout(PerformanceFragment(),PerformanceFragment())
            "ORGSTRUCTURE" -> inittabLayout(orgStructureFragment(),orgStructureFragment())

        }

        return rootView
    }

    private fun inittabLayout(fragment:Fragment, fragment2: Fragment){
        viewPager = rootView.findViewById(R.id.ViewPager)
        setupViewPager(viewPager!!,fragment,fragment2)

        tabLayout = rootView.findViewById(R.id.Tabs)

        tabLayout!!.setupWithViewPager(viewPager)

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager!!.offscreenPageLimit = 2

        tabLayout!!.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupViewPager(viewPager: ViewPager,fragment:Fragment, fragment2: Fragment) {
        var adapter = PagerAdapters(context,childFragmentManager,2)
        var fr =
            fragment


        var args = Bundle()
        args.putString("EMP_NAME", empList.empName)
        args.putString("EMP_PHOTO", empList.empPhoto)
        args.putString("JOB_TITLE_NAME", empList.empJobTitle)
        args.putString("JOB_LEVEL", empList.empJobLevel)
        args.putString("COMPANY_NAME", empList.empCompany)
        args.putString("COMPGRPID", empList.compGroupID)
        args.putString("ORG_NAME", empList.empOrg)
        args.putString("DIR_NAME", empList.empDept)
        args.putString("NIK", empList.empNIK)
        args.putSerializable("DATA_DIRI", empList.dataDiri)
        args.putSerializable("ISCOMPARED", isCompared)
        args.putInt("TABACTIVE", tabActive)

        fr.arguments=args
        adapter.addFragment(fr, empList.empName.toString())


        //==================================================================
        var fr2 =
            fragment2

        var args2 = Bundle()
        args2.putString("EMP_NAME", empList2.empName)
        args2.putString("EMP_PHOTO", empList2.empPhoto)
        args2.putString("JOB_TITLE_NAME", empList2.empJobTitle)
        args2.putString("JOB_LEVEL", empList2.empJobLevel)
        args2.putString("COMPANY_NAME", empList2.empCompany)
        args2.putString("COMPGRPID", empList2.compGroupID)
        args2.putString("ORG_NAME", empList2.empOrg)
        args2.putString("DIR_NAME", empList2.empDept)
        args2.putString("NIK", empList2.empNIK)
        args2.putSerializable("DATA_DIRI", empList2.dataDiri)
        args2.putSerializable("ISCOMPARED", isCompared)
        args2.putInt("TABACTIVE", tabActive)

        fr2.arguments=args2
        adapter.addFragment(fr2, empList2.empName.toString())
        //==================================================================
        viewPager.adapter = adapter
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Compare Talent")

    }

}