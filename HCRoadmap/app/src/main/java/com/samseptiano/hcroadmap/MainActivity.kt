package com.samseptiano.hcroadmap

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewConfiguration
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.samseptiano.hcroadmap.Adapter.LDPSettingHolder
import com.samseptiano.hcroadmap.Application.MyApp
import com.samseptiano.hcroadmap.Fragment.*
import com.samseptiano.hcroadmap.Fragment.TalentMapping.CompareFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.FilterTalentMappingCandidateFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.orgStructureFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.SubTalentReview.TalentReviewDetailFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentProfileFragment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.TalentmappingFragment
import com.samseptiano.hcroadmap.Fragment.TalentReview.TalentReviewFragment
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.customPreference
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.userId
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private val homeFragment = HomeFragment()
    val CUSTOM_PREF_NAME = "User_data"
    lateinit var globals: MyApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        globals= application as MyApp
        globals.compId="0"
        globals.dirId="0"
        globals.posId="0"
        val prefs = customPreference(this, CUSTOM_PREF_NAME)


        //====================== CUSTOM APPBAR LAYOUT =============================================
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        title = ""


        if(prefs.userId.toString()!=""){
            /* Display First Fragment initially */
                displayHome()
        }
        else{
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.logout -> logoudt()
            else -> return super.onOptionsItemSelected(item)

        }
        return true
    }

    fun logoudt():Unit{
        val prefs = customPreference(this, CUSTOM_PREF_NAME)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = UserLoginRoomDatabase.getDatabase(applicationContext)
        dao = database.getUserLoginDao()

        dao.deleteAll()
        dao.deleteAllModule()

        intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun displayHome():Unit{
        if(isNavigationBarAvailable()){
            myFragment.setPadding(0,0,0,30)
        }
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.myFragment, homeFragment)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.myFragment)
        when (f) {
            is HomeFragment -> {
                //super.onBackPressed()
                finish()
            }
            is TalentmappingFragment -> {
                supportFragmentManager.popBackStack()
            }
            is FilterTalentMappingCandidateFragment -> {
                supportFragmentManager.popBackStack()
            }
            is TalentProfileFragment -> {
                supportFragmentManager.popBackStack()
            }
            is TalentReviewFragment -> {
                supportFragmentManager.popBackStack()
            }
            is TalentReviewDetailFragment -> {
                supportFragmentManager.popBackStack()
            }
            is CompareFragment -> {
                supportFragmentManager.popBackStack()
            }
            is TalentProfileDBFragment -> {
                supportFragmentManager.popBackStack()
            }
            is orgStructureFragment -> {
                supportFragmentManager.popBackStack()
            }
            is LDPMasterSettingFragment -> {
                supportFragmentManager.popBackStack()
            }
            is CustomCategoryFragment -> {
                supportFragmentManager.popBackStack()
            }
            is UserManagementFragment -> {
                supportFragmentManager.popBackStack()
            }
        }
    }

    fun isNavigationBarAvailable(): Boolean {
        return ViewConfiguration.get(applicationContext).hasPermanentMenuKey();
    }

    override fun onUserInteraction() {

        //TODO://do your work on user interaction
    }

}
