package com.samseptiano.hcroadmap

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.room.ColumnInfo
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.User
import com.samseptiano.hcroadmap.DataClass.UserBodyParameter
import com.samseptiano.hcroadmap.DataClass.UserLogin
import com.samseptiano.hcroadmap.DataClass.UserLoginResponse
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoom
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.ModuleRoom
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoom
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.companyCode
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.customPreference
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.password
import com.samseptiano.hcroadmap.SharedPreferenced.PreferenceHelper.userId
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val CUSTOM_PREF_NAME = "User_data"

    lateinit var token:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )
        this.supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        title = ""


        btnLogin.setOnClickListener {
            checkLogin(edtNIK.text.toString(),edtPassword.text.toString())
        }
    }

    fun checkLogin(username:String?=null,password:String?=null):Unit{
        val prefs = customPreference(this, CUSTOM_PREF_NAME)

        APIConfig().getService()
            .getToken(UserLogin("mario","secret"))
            ?.enqueue(object : Callback<UserLoginResponse?> {

                override fun onFailure(call: Call<UserLoginResponse?>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<UserLoginResponse?>,
                    response: Response<UserLoginResponse?>
                ) {
                    if(response.body()?.token.toString()!=null) {
                        token = response.body()?.token.toString()

                        APIConfig().getService()
                            .getUserLogin(UserBodyParameter(username,password),"bearer "+token)
                            ?.enqueue(object : Callback<User?> {

                                override fun onFailure(call: Call<User?>, t: Throwable) {
                                }

                                override fun onResponse(
                                    call: Call<User?>,
                                    response: Response<User?>
                                ) {
                                    if(response.body()?.empnik!=null) {
                                        Toast.makeText(applicationContext,
                                            response.body()!!.empnik,Toast.LENGTH_LONG).show()
                                            prefs.password = edtPassword.text.toString()
                                            prefs.userId = edtNIK.text.toString()
                                            prefs.companyCode = response.body()!!.compgrpid
                                            addToRoom(response.body()!!)
                                            intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()

                                    }
                                }
                            })

                    }
                }
            })

    }

    fun addToRoom(user:User):Unit{
         lateinit var database: UserLoginRoomDatabase
         lateinit var dao: UserloginDao
        database = UserLoginRoomDatabase.getDatabase(applicationContext)
        dao = database.getUserLoginDao()

        if (dao.getById(Integer.parseInt(user.empnik!!)).isEmpty()){

            var userRoom = UserLoginRoom()
            userRoom?.id=Integer.parseInt(user.empId!!)
            userRoom?.empNIK= user.empnik!!
            userRoom?.empName= user.empname!!
            userRoom?.username= edtNIK.text.toString()
            userRoom?.token= token
            userRoom?.empFirstName= user.empfirstname.toString()
            userRoom?.empLastName= user.emplastname.toString()
            userRoom?.empInisial= user.empinisial.toString()
            userRoom?.empJobTtlId= user.empjobttlid.toString()
            userRoom?.empPositionId= user.emppositionid.toString()
            userRoom?.empOrgId= user.emporgid.toString()
            userRoom?.empJobLvl= user.joblevel.toString()
            userRoom?.empLocId= user.emplocid.toString()
            userRoom?.empCompId= user.empcompid.toString()
            userRoom?.empTypeId= user.emptypeid.toString()
            userRoom?.empVendorId= user.empvendorid.toString()
            userRoom?.empDateStart= user.empdatestart.toString()
            userRoom?.empEndDate= user.empdateend.toString()
            userRoom?.empJoinDate= user.empjoindate.toString()
            userRoom?.empSignDate= user.empsigndate.toString()
            userRoom?.empCostCentreId= user.empcostctreid.toString()
            userRoom?.empLocRecruitId= user.emplocrecruitid.toString()
            userRoom?.empExportDate= user.exportdate.toString()
            userRoom?.labourSt= user.labourst.toString()
            userRoom?.empDateFrom= user.empdatefrom.toString()
            userRoom?.empBirthPlace= user.birthplace.toString()
            userRoom?.empDateBirth= user.birthdate.toString()
            userRoom?.lastPromotionDate= user.lastpromotiondate.toString()
            userRoom?.lastEducation= user.lasteducation.toString()
            userRoom?.maritalStatus= user.maritalstatus.toString()
            userRoom?.homebase= user.homebase.toString()
            userRoom?.fgSuperAdmin =  user.flaGSUPERADMIN.toString()
            userRoom?.fgAdmin= user.flaGADMIN.toString()
            userRoom?.fgNonAdmin= user.flaGNONADMIN.toString()
            userRoom?.fgSalary= user.flaGSALARY.toString()
            userRoom?.fgActive= user.flaGACTIVE.toString()
            userRoom?.organization= user.organization.toString()
            userRoom?.compGrpId= user.compgrpid.toString()
            userRoom?.location= user.location.toString()

            dao.insert(userRoom)

            for(i in 0 until (user.modulelist?.size ?: 0)){
                var moduleRoom = ModuleRoom();
                moduleRoom.fgapp = user.modulelist?.get(i)?.fgapp
                moduleRoom.icon = user.modulelist?.get(i)?.icon
                moduleRoom.modulecode = user.modulelist?.get(i)?.modulecode
                moduleRoom.moduledesc = user.modulelist?.get(i)?.moduledesc
                moduleRoom.ordinal = user.modulelist?.get(i)?.ordinal
                moduleRoom.pagename = user.modulelist?.get(i)?.pagename

                dao.insertModule(moduleRoom)
            }
            }
        }


    }

