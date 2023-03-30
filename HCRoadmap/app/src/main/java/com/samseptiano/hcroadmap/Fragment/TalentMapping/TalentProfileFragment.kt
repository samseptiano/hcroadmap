package com.samseptiano.hcroadmap.Fragment.TalentMapping

import android.graphics.Color
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.samseptiano.hcroadmap.DataClass.EmpListDataDiri
import com.samseptiano.hcroadmap.DataClass.Feedback360
import com.samseptiano.hcroadmap.Fragment.TalentMapping.PagerAdapter.PagerAdapters
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.*
import com.samseptiano.hcroadmap.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_talent_profile.view.*
import kotlinx.android.synthetic.main.tab_talent_profile.view.*


class TalentProfileFragment : Fragment(){

   lateinit var mCallback: TalentProfileInterface

    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    lateinit var rootView:View

    var empNIK:String?=""
    var empName:String?=""
    var empPhoto:String?=""
    var jobLevel:String?=""
    var jobTitle:String?=""
    var compName:String?=""
    var orgName:String?=""
    var dirName:String?=""
    var isCompared:String?=""
    var dirId:String?=""
    var displayTo:String?=""
    var dataDiri:EmpListDataDiri= EmpListDataDiri()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    interface TalentProfileInterface{
        fun getTabActive(tabPos:Int)
    }


    companion object {

        fun newInstance(): TalentProfileFragment {
            return TalentProfileFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        
        rootView = inflater.inflate(R.layout.fragment_talent_profile, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.onBackPressed()

            }
        })



        empNIK = arguments?.getString("NIK").toString()
        empPhoto = arguments?.getString("EMP_PHOTO").toString()
        empName = arguments?.getString("EMP_NAME").toString()
        jobTitle = arguments?.getString("JOB_TITLE_NAME").toString()
        jobLevel = arguments?.getString("JOB_LEVEL").toString()
        compName = arguments?.getString("COMPANY_NAME").toString()
        orgName = arguments?.getString("ORG_NAME").toString()
        dirName = arguments?.getString("DIR_NAME").toString()
        dataDiri = arguments?.getSerializable("DATA_DIRI") as EmpListDataDiri
        isCompared = arguments?.getString("ISCOMPARED").toString()
        dirId = arguments?.getString("DIRID").toString()
        displayTo =  arguments?.getString("DISPLAYTO").toString()

        Picasso.get().load(empPhoto)
            .into(rootView.empPhoto)
        rootView.tvEmpName.text = empName
        rootView.tvJobTitle.text=jobTitle
        rootView.tvGolongan.text= "GOLONGAN "+jobLevel
        rootView.tvOrg.text = dirName+" - "+compName

       // inittabLayout()

        initFragment()
        rootView.btnDataDiri.setOnClickListener {
            tabClicked(rootView.btnDataDiri,DataDiriFragment())

        }
        rootView.btnAssessmentResult.setOnClickListener {
            tabClicked(rootView.btnAssessmentResult,AssessmentResultFragment())
        }
        rootView.btnLearning.setOnClickListener {
            tabClicked(rootView.btnLearning,LearningFragment())

        }
        rootView.btnBgCheck.setOnClickListener {
            tabClicked(rootView.btnBgCheck,BackgroundCheckFragment())

        }
        rootView.btn360Feedback.setOnClickListener {
            tabClicked(rootView.btn360Feedback,feedback360Fragment())

        }
        rootView.btnConstraint.setOnClickListener {
            tabClicked(rootView.btnConstraint,ConstraintFragment())

        }
        rootView.btnAttachment.setOnClickListener {
            tabClicked(rootView.btnAttachment,AttachmentFragment())

        }
        rootView.btnInovasi.setOnClickListener {
            tabClicked(rootView.btnInovasi,InovasiFragment())

        }
        rootView.btnAward.setOnClickListener {
            tabClicked(rootView.btnAward,AwardFragment())

        }
        rootView.btnPerformance.setOnClickListener {
            tabClicked(rootView.btnPerformance,PerformanceFragment())

        }
        rootView.btnOrgStructure.setOnClickListener {
            tabClicked(rootView.btnOrgStructure,orgStructureFragment())

        }

        return rootView
    }


        private fun inittabLayout(){
            viewPager = rootView.findViewById<View>(R.id.ViewPager) as ViewPager

            setupViewPager(viewPager!!)

            tabLayout = rootView.findViewById<View>(R.id.Tabs) as TabLayout
            tabLayout!!.setupWithViewPager(viewPager)

            viewPager!!.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
            viewPager!!.offscreenPageLimit = 9

            tabLayout!!.setOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    viewPager!!.currentItem = tab.position
                    if (isCompared.equals("Y")) {
                        //(this as CompareFragment?)?.getTabActivee(tab.position)
                        //Kirim HaloEvent kepada komponen lain yang subscribe (KelasB)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })


        }
        private fun setupViewPager(viewPager: ViewPager) {
        if (isCompared.equals("Y")) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Compare Talent")
        }
        else{
            (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle(empName)

        }
        val adapter = PagerAdapters(context,childFragmentManager,9)

        var fr: Fragment?
        fr =
            DataDiriFragment()
        val args = Bundle()
        args.putString("NIK", empNIK)
        args.putSerializable("DATA_DIRI", dataDiri)
        fr.arguments=args
        adapter.addFragment(fr, "Data Diri")

        //==================================================================
        var fr2: Fragment?
        fr2 =
            AssessmentResultFragment()
        val args2 = Bundle()
        args2.putString("NIK", empNIK)
        fr2.arguments=args2
        adapter.addFragment(fr2, "Assessment Result")
        //==================================================================

        //==================================================================
        var fr3: Fragment?
        fr3 =
            LearningFragment()
        val args3 = Bundle()
        args3.putString("NIK", empNIK)
        args3.putString("COMPGRPID", dataDiri.compGroupID)

        fr3.arguments=args3
        adapter.addFragment(fr3, "Learning")
        //==================================================================

        //==================================================================
        var fr4: Fragment?
        fr4 =
            BackgroundCheckFragment()
        val args4 = Bundle()
        args4.putString("NIK", empNIK)
        args4.putString("COMPGRPID", dataDiri.compGroupID)

        fr4.arguments=args4
        adapter.addFragment(fr4, "Background Check")
        //==================================================================
        //==================================================================
        var fr5: Fragment?
        fr5 =
            feedback360Fragment()
        val args5 = Bundle()
        args5.putString("NIK", empNIK)
        args5.putString("COMPGRPID", dataDiri.compGroupID)

        fr5.arguments=args5
        adapter.addFragment(fr5, "Feedback 360")
        //==================================================================
        //==================================================================
        var fr6: Fragment?
        fr6 =
            ConstraintFragment()
        val args6 = Bundle()
        args6.putString("NIK", empNIK)
        args6.putString("COMPGRPID", dataDiri.compGroupID)
        fr6.arguments=args6
        adapter.addFragment(fr6, "Constraint")
        //==================================================================
//        fr = EngageentProfiling()
//        adapter.addFragment(fr, "Engagement Profiling")
//        fr = EngageentProfiling()
//        adapter.addFragment(fr, "Salary")
        //=================================================================
        var fr7: Fragment?
        fr7 =
            AttachmentFragment()
        val args7 = Bundle()
        args7.putString("NIK", empNIK)
        args7.putString("COMPGRPID", dataDiri.compGroupID)
        args7.putString("DISPLAYTO",displayTo)
        fr7.arguments=args7
        adapter.addFragment(fr7, "Attachment")
        //=================================================================
        fr =
            InovasiFragment()
        adapter.addFragment(fr, "Inovasi")

        //=================================================================
        var fr9: Fragment?
        fr9 =
            AwardFragment()
        val args9 = Bundle()
        args9.putString("NIK", empNIK)
        args9.putString("COMPGRPID", dataDiri.compGroupID)
        fr9.arguments=args9
        adapter.addFragment(fr9, "Award")
        //===========================================================
        var fr11: Fragment?
        fr11 =
            PerformanceFragment()
        val args11 = Bundle()
        args11.putString("NIK", empNIK)
        args11.putString("COMPGRPID", dataDiri.compGroupID)
        fr11.arguments=args11
        adapter.addFragment(fr11, "Performance")

        //=================================================================
        var fr10: Fragment?
        fr10 =
            orgStructureFragment()
        val args10 = Bundle()
        args10.putString("NIK", empNIK)
        args10.putString("DIRID", dirId)
        fr10.arguments=args10
        adapter.addFragment(fr10, "Org Structure")
        //===========================================================

        viewPager.adapter = adapter
    }


    fun initFragment(){
        rootView.btnDataDiri.setBackgroundResource(R.drawable.shapepurple);

        val fm = this@TalentProfileFragment
        val dataDiriFragment =
            DataDiriFragment()

        val args = Bundle()
        args.putString("NIK", empNIK)
        args.putSerializable("DATA_DIRI", dataDiri)

        dataDiriFragment.arguments=args
        // Begin the transaction
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentTalentProfile, dataDiriFragment)
        ft.commit()

    }

    fun tabClicked(button: Button,fragment:Fragment){
        clearButtonColor()
        button.setBackgroundResource(R.drawable.shapepurple);

        val fm = this@TalentProfileFragment
        val dataDiriFragment =
            fragment

        val args = Bundle()
        args.putString("NIK", empNIK)
        args.putSerializable("DATA_DIRI", dataDiri)
        args.putString("COMPGRPID", dataDiri.compGroupID)
        args.putString("DIRID", dirId)
        args.putString("DISPLAYTO",displayTo)

        dataDiriFragment.arguments=args
        // Begin the transaction
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentTalentProfile, dataDiriFragment)
        ft.commit()

    }

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
