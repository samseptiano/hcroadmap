package com.samseptiano.hcroadmap.Repo

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TalentMappingRepo {

    lateinit var context: Context

    fun getcontext(context: Context){
        this.context = context
    }

    fun getCompany(nik:String,token:String,compId:String): MutableLiveData<List<CompanyList>> {

        val mutableLiveData: MutableLiveData<List<CompanyList>> =
            MutableLiveData<List<CompanyList>>()

        nik?.let {
            APIConfig().getService()
                .getCompany(compId,it,"Bearer "+token)
                .enqueue(object : Callback<List<CompanyList>> {
                    override fun onFailure(call: Call<List<CompanyList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<CompanyList>>,
                        response: Response<List<CompanyList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun gettargetPosisi(nik:String,token:String,compId:String): MutableLiveData<List<TargetPosisiList>> {

        val mutableLiveData: MutableLiveData<List<TargetPosisiList>> =
            MutableLiveData<List<TargetPosisiList>>()

        nik?.let {
            APIConfig().getService()
                .getTargetPosisi(compId,it,"Bearer "+token)
                .enqueue(object : Callback<List<TargetPosisiList>> {
                    override fun onFailure(call: Call<List<TargetPosisiList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<TargetPosisiList>>,
                        response: Response<List<TargetPosisiList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun getdirektoratAll(nik:String,token:String,compId:String): MutableLiveData<List<DirektoratList>> {

        val mutableLiveData: MutableLiveData<List<DirektoratList>> =
            MutableLiveData<List<DirektoratList>>()

        nik?.let {
            APIConfig().getService()
                .getDirektorat(it,compId,"ALL","Bearer "+token)
                .enqueue(object : Callback<List<DirektoratList>> {
                    override fun onFailure(call: Call<List<DirektoratList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<DirektoratList>>,
                        response: Response<List<DirektoratList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }

    fun getdirektorat(nik:String,token:String,compId:String): MutableLiveData<List<DirektoratList>> {

        val mutableLiveData: MutableLiveData<List<DirektoratList>> =
            MutableLiveData<List<DirektoratList>>()

        nik?.let {
            APIConfig().getService()
                .getDirektorat(it,compId,"NO","Bearer "+token)
                .enqueue(object : Callback<List<DirektoratList>> {
                    override fun onFailure(call: Call<List<DirektoratList>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<DirektoratList>>,
                        response: Response<List<DirektoratList>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun getEmpAfterMapping(nik:String,token:String,compId:String,targetPosisi:String,dirID:String,dirGroupID:String): MutableLiveData<List<EmpTalentMappingAfter>> {

        val mutableLiveData: MutableLiveData<List<EmpTalentMappingAfter>> =
            MutableLiveData<List<EmpTalentMappingAfter>>()

        compId?.let {
            APIConfig().getService()
                .getUsersAfterMapping(it,dirID,dirGroupID,targetPosisi,"Bearer "+token)
                .enqueue(object : Callback<List<EmpTalentMappingAfter>> {
                    override fun onFailure(call: Call<List<EmpTalentMappingAfter>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<List<EmpTalentMappingAfter>>,
                        response: Response<List<EmpTalentMappingAfter>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }
    fun insertCompanyList(companyListObj: MutableList<CompanyList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.compname.toString())
        }

       mutableLiveData.setValue(companyList)
        return mutableLiveData
    }
    fun insertPosisiList(companyListObj: MutableList<TargetPosisiList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.jobttlname.toString())
        }

        mutableLiveData.setValue(companyList)
        return mutableLiveData
    }
    fun insertDirektoratList(companyListObj: MutableList<DirektoratList>): MutableLiveData<MutableList<String>> {

        val mutableLiveData: MutableLiveData<MutableList<String>> =
            MutableLiveData<MutableList<String>>()


        var companyList = mutableListOf<String>()

        for (i in 0 until companyListObj?.size!!){
            companyList!!.add(companyListObj!![i]?.direktoratname.toString())
        }

        mutableLiveData.setValue(companyList)
        return mutableLiveData
    }
    fun insertToEmpList(empTalentMappingList: MutableList<EmpTalentMappingAfter>): MutableLiveData<MutableList<EmpList>> {

        val mutableLiveData: MutableLiveData<MutableList<EmpList>> =
            MutableLiveData<MutableList<EmpList>>()


        var listEmpListAdapter = mutableListOf<EmpList>()
        for (i in 0 until empTalentMappingList!!.size) {
            var empList = EmpList()
            empList.id = i.toString()
            empList.empNIK = empTalentMappingList!![i].empnik
            empList.empName = empTalentMappingList!![i].empname
            empList.empDept = empTalentMappingList!![i].direktoratname
            empList.empBranch = empTalentMappingList!![i].orgname
            empList.empCompany = empTalentMappingList!![i].locationname
            empList.empPhoto = empTalentMappingList!![i].empphoto
            empList.empAge = empTalentMappingList!![i].age
            empList.empOrg = empTalentMappingList!![i].orgname
            empList.empJobTitle = empTalentMappingList!![i].empjobtitlename
            empList.compGroupID = empTalentMappingList!![i].compgrpid
            empList.empJobLevel = empTalentMappingList!![i].empjoblvl
            empList.archievement = empTalentMappingList!![i].speciaLACHV
            empList.recommendation = empTalentMappingList!![i].reCREASON
            empList.posId = empTalentMappingList!![i].targeTPOSITIONID
            empList.dirId = empTalentMappingList!![i].origiNDIRID
            empList.compID = empTalentMappingList!![i].origiNCOMPID

            var dataDiri:EmpListDataDiri = EmpListDataDiri()
            dataDiri.empSignDate = empTalentMappingList!![i].empsigndate
            dataDiri.empLastPromotion = empTalentMappingList!![i].lastpromotiondate
            dataDiri.location = empTalentMappingList!![i].locationname
            dataDiri.emplastEducation = empTalentMappingList!![i].lasteducation
            dataDiri.empHomeBase = empTalentMappingList!![i].homebase
            dataDiri.empBirthPlace = empTalentMappingList!![i].empbirthplace
            dataDiri.empBirthDate = empTalentMappingList!![i].empdatebirth
            dataDiri.empAge = empTalentMappingList!![i].age
            dataDiri.empMaritalStatus = empTalentMappingList!![i].maritalstatus
            dataDiri.empRetirementDate = empTalentMappingList!![i].retirementdate
            dataDiri.compGroupID = empTalentMappingList!![i].compgrpid

            empList.dataDiri = dataDiri

            listEmpListAdapter.add(empList)
        }


        mutableLiveData.setValue(listEmpListAdapter)
        return mutableLiveData
    }
    fun getOrgStructure(token:String,dirID:String): MutableLiveData<MutableList<OrgStructure>> {

        val mutableLiveData: MutableLiveData<MutableList<OrgStructure>> =
            MutableLiveData<MutableList<OrgStructure>>()

        dirID?.let {
            APIConfig().getService()
                .getstructure(it,"Bearer "+token)
                .enqueue(object : Callback<MutableList<OrgStructure>> {
                    override fun onFailure(call: Call<MutableList<OrgStructure>>, t: Throwable) {
                        Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<MutableList<OrgStructure>>,
                        response: Response<MutableList<OrgStructure>>
                    ) {

                        if(response.body()!=null){
                            mutableLiveData.setValue(response.body())

                        }
                    }
                })
        }
        return mutableLiveData
    }

}