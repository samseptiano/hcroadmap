package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.github.barteksc.pdfviewer.PDFView
import com.github.chrisbanes.photoview.PhotoView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.*
import com.samseptiano.hcroadmap.FileUtils
import com.samseptiano.hcroadmap.MainActivity
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.OrgStructureViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentMappingViewModel
import com.samseptiano.hcroadmap.ViewModel.TalentReviewViewModel
import com.squareup.picasso.Picasso
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_org_structure.*
import kotlinx.android.synthetic.main.fragment_org_structure.btnUpload
import kotlinx.android.synthetic.main.fragment_org_structure.view.*
import kotlinx.android.synthetic.main.fragment_talentmapping.view.*
import kotlinx.android.synthetic.main.fragment_talentmapping.view.lnFilterArea
import kotlinx.android.synthetic.main.upload_structure_dialog.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


class orgStructureFragment : Fragment() {

    lateinit var rootView:View
    var empNIK:String=""
    var compID:String?="0"
    var dirID:String?="0"
    var url:String?=""
    var structureID:String?=""
    var orgStructure = fileOrgStructure()

    lateinit var dialog:Dialog
    private val GALLERY = 1
    private val CAMERA = 2
    val requestCode = 200
    private val permissions = arrayOf(
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.READ_PHONE_STATE",
        "android.permission.SYSTEM_ALERT_WINDOW",
        "android.permission.CAMERA"
    )

    private var companyList: MutableList<String>? = null
    private var direktoratList: MutableList<String>? = null

    private var companyListObj: MutableList<CompanyList>? = null
    private var direktoratListObj: MutableList<DirektoratList>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_org_structure, container, false)
        empNIK = arguments?.getString("NIK")?:""
        dirID = arguments?.getString("DIRID")?:""

        if(empNIK.equals("")){
            (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Organization Structure")
            (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
            rootView.lnFilterArea.visibility=View.VISIBLE;
            rootView.btnChange.visibility = View.GONE
            rootView.btnUpload.visibility = View.GONE
            rootView.btnDelete.visibility = View.GONE

            rootView.btnDelete.setOnClickListener {

               lateinit var database: UserLoginRoomDatabase
                lateinit var dao: UserloginDao
                database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
                dao = database.getUserLoginDao()

                var orgStructureGet = OrgStructureGet()
                orgStructureGet.dirid=dirID
                orgStructureGet.upduser = dao.getusername()
                orgStructureGet.filename = url
                orgStructureGet.structureid = structureID

                val orgStructureViewModel = OrgStructureViewModel(orgStructureGet,dao.getToken(),
                    context!!
                )

                orgStructureViewModel.deleteOrgStructure
                    .observe(this, object : Observer<String?> {
                        override fun onChanged(currencyPojos: String?) {
                           loadViewModelStructure()
                        }
                    })
            }
            rootView.btnUpload.setOnClickListener {
                showUploadDialog()
            }
            rootView.btnChange.setOnClickListener {
                var orgStructureGet = OrgStructureGet()
                    orgStructureGet.structureid = structureID
                    orgStructureGet.filename = url
                    orgStructureGet.dirid = dirID
                showUploadDialog(orgStructureGet)
            }

            loadViewModel("COMPANY")
            initSpinner()

        }
        else {
            loadViewModelStructure()
        }


        return rootView

    }


    fun loadViewModelStructure(){

        var pdf = rootView.findViewById(R.id.pdfView) as PDFView
        var imgZoom = rootView.findViewById<PhotoView>(R.id.imgFullScreen)
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
//        empNIK = "190700650"

       // dirId="11"

        val talentMappingViewModel = TalentMappingViewModel(empNIK,dao.getToken(),dao.getCompGrpId()?:"","",dirID?:"","",null,
            context!!
        )
        talentMappingViewModel.orgStructure
            .observe(this, object : Observer<List<OrgStructure?>?> {
                override fun onChanged(currencyPojos: List<OrgStructure?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            rootView.btnUpload.visibility = View.GONE
                            val baseUrl = APIConfig();
                            var URL = baseUrl.baseURLStructure+ (currencyPojos?.get(0)?.structurEFILE ?: "")
                            url = currencyPojos?.get(0)?.structurEFILE
                            structureID = currencyPojos?.get(0)?.structurEID
                            if (URL.contains(".pdf")) {
                                webViewPDF.visibility=View.VISIBLE
                                imgZoom.visibility=View.GONE

                                var web_view = rootView.findViewById<WebView>(R.id.webViewPDF);
                                web_view.requestFocus();
                                web_view.getSettings().setJavaScriptEnabled(true);
                                //URL = "https://www.introprogramming.info/wp-content/uploads/2013/07/Books/CSharpEn/Fundamentals-of-Computer-Programming-with-CSharp-Nakov-eBook-v2013.pdf";
                                var url ="https://docs.google.com/viewerng/viewer?url="+URL;
                                web_view.loadUrl(url);

                                web_view.webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                      //  view?.loadUrl("javascript:document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none';");
                                        return true
                                        //ndfHFb-c4YZDc-K9a4Re
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?){
                                      //  view?.loadUrl("javascript:document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.display='none';");

                                    }
                                }


//                                val downloadmanager =
//                                    context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?
//                                val uri =
//                                    Uri.parse(URL)
//                                val request = DownloadManager.Request(uri)
//                                request.setTitle("Structure")
//                                request.setDescription("Downloading")
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                                request.setDestinationInExternalFilesDir(context,
//                                    Environment.DIRECTORY_DOWNLOADS,"CountryList.pdf")
//                                downloadmanager!!.enqueue(request)
//
//
//
//                                pdf.fromUri(uri)
//                                    .enableSwipe(true) // allows to block changing pages using swipe
//                                    .swipeHorizontal(true)
//                                    .enableDoubletap(true)
//                                    .defaultPage(0)
//                                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//                                    .password(null)
//                                    .scrollHandle(null)
//                                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//                                    // spacing between pages in dp. To define spacing color, set view background
//                                    .spacing(0)
//                                    .pageFitPolicy(FitPolicy.WIDTH)
//                                    .load();
                            }
                            else{
                                webViewPDF.visibility=View.GONE
                                imgZoom.visibility=View.VISIBLE
                                try {
                                    Picasso.get()
                                        .load(URL)
                                        .into(imgZoom)
                                }
                                catch (i:IllegalArgumentException){
                                    Toast.makeText(context,i.toString(),Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                        else{
                            btnDelete.visibility=View.GONE
                            btnChange.visibility=View.GONE
                            webViewPDF.visibility=View.GONE
                        }
                    }
                }
            })
    }

    private fun initSpinner() {
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>

        val spinnerDirektirat =
            rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>


        if(!(activity as MainActivity?)?.globals?.direktoratState.equals("")) {
            spinnerDirektirat.setHint((activity as MainActivity?)?.globals?.direktoratState)
            spinnerCompany.setHint((activity as MainActivity?)?.globals?.companyState)
        }

        spinnerCompany!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(context, companyList!![position], Toast.LENGTH_SHORT).show()
                spinnerCompany.setFloatingLabelText(companyList!![position])
                (activity as MainActivity?)?.globals?.companyState = companyList!![position]

                for(i in 0 until companyListObj!!.size){
                    if(companyListObj!![i].compname!!.equals(spinnerCompany.floatingLabelText)){
                        compID = companyListObj!![i].compid.toString()

                        direktoratList = mutableListOf()
                        direktoratListObj = mutableListOf()

                        loadViewModel("DIREKTORAT")
                    }
                }

                spinnerCompany.setErrorText("")
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        spinnerDirektirat!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerDirektirat.setFloatingLabelText(direktoratList!![position])
                (activity as MainActivity?)?.globals?.direktoratState = direktoratList!![position]

                for(i in 0 until direktoratListObj!!.size){
                    if(direktoratListObj!![i].direktoratname!!.equals(spinnerDirektirat.floatingLabelText)){
                        dirID = direktoratListObj!![i].direktoratid.toString()
                    }
                }
                Toast.makeText(context, direktoratList!![position], Toast.LENGTH_SHORT).show()
                spinnerDirektirat.setErrorText("")
                loadViewModelStructure()

                btnChange.visibility = View.VISIBLE
                btnUpload.visibility = View.VISIBLE
                btnDelete.visibility = View.VISIBLE

            }
            //Uploads/OrganizationStructure/StrukturOrganisasi_4_1.pdf

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

    }
    fun loadViewModel(type:String){
        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()

        when(type){
            "COMPANY" ->{
                val talentMappingViewModel = TalentMappingViewModel(dao.getNIK(),dao.getToken(),dao.getCompGrpId()?:"","","","",null,
                    context!!
                )

                talentMappingViewModel.company
                    .observe(this, object : Observer<List<CompanyList?>?> {
                        override fun onChanged(currencyPojos: List<CompanyList?>?) {
                            companyListObj = currencyPojos as MutableList<CompanyList>?

                            insertToSpinner("COMPANY")


                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentReviewViewModel = TalentReviewViewModel(dao.getNIK(),dao.getToken(),compID?:"","","","",null,
                    context!!
                )

                talentReviewViewModel.direktorat
                    .observe(this, object : Observer<List<DirektoratList?>?> {
                        override fun onChanged(currencyPojos: List<DirektoratList?>?) {
                            direktoratListObj = currencyPojos as MutableList<DirektoratList>?
                            insertToSpinner("DIREKTORAT")
                        }
                    })
            }
        }

    }

    fun insertToSpinner(type:String){
        val spinnerCompany = rootView.findViewById(R.id.spinnerCompany) as SmartMaterialSpinner<Any>
        val spinnerDirektirat = rootView.findViewById(R.id.spinnerDirektorat) as SmartMaterialSpinner<Any>

        when(type){
            "COMPANY" ->{
                val talentMappingViewModel = context?.let {
                    TalentMappingViewModel("","","","","","",companyListObj as MutableList<Any>,
                        it
                    )
                }

                talentMappingViewModel?.insertIntoCompanyList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            companyList = currencyPojos as MutableList<String>?
                            spinnerCompany!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
            "DIREKTORAT" ->{
                val talentMappingViewModel = context?.let {
                    TalentMappingViewModel("","","","","","",direktoratListObj as MutableList<Any>,
                        it
                    )
                }

                talentMappingViewModel?.insertintoDirektoratList
                    ?.observe(this, object : Observer<List<String?>?> {
                        override fun onChanged(currencyPojos: List<String?>?) {
                            direktoratList = currencyPojos as MutableList<String>?
                            spinnerDirektirat!!.setItem(currencyPojos!! as List<Any>)
                        }
                    })
            }
        }
    }

    fun showUploadDialog(orgStructureGet: OrgStructureGet?=null){
        dialog = context?.let { Dialog(it) }!!
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.upload_structure_dialog)
        dialog!!.show()

        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnUpload = dialog.findViewById(R.id.btnUpload) as Button
        val btnChooseFile = dialog.findViewById(R.id.btnChooseFile) as Button
        val tvFileName = dialog.findViewById(R.id.tvFilename) as TextView
        val imgFile = dialog.findViewById(R.id.imgFileType) as ImageView


        if(orgStructureGet!=null){
            tvFileName.text = orgStructureGet.filename
        }


        btnChooseFile.setOnClickListener {
            chooseImageFromGallery()
        }

        btnUpload.setOnClickListener {
            uploadStructure(orgStructure)
            dialog.dismiss()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }
    }



    private fun showPictureDialog() {
        try {
            val pictureDialog = AlertDialog.Builder(context)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItems =
                arrayOf("Select image from gallery", "Capture photo from camera")
            pictureDialog.setItems(
                pictureDialogItems
            ) { dialog, which ->
                when (which) {
                    0 -> chooseImageFromGallery()
                }
            }
            pictureDialog.show()
        }catch (e:SecurityException){
            requestPermissions(permissions, requestCode)

        }
    }

    fun chooseImageFromGallery() {

        val intent = Intent()
        intent.type = "*/*" // intent.setType("video/*"); to select videos to upload
        val mimetypes = arrayOf(
            "application/pdf",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Pilih dokumen"),
            GALLERY
        )
    }



    private fun uploadFile(fileUri: Uri, desc: String) {

        //creating a file
        var fileUtils = FileUtils()
        val file = File(fileUtils.getRealPath(context,fileUri))
        dialog.tvFilename.text = file.path

        //creating request body for file
        val requestFile: RequestBody =
            RequestBody.create(
                context?.getContentResolver()?.getType(fileUri).toString().
                toMediaTypeOrNull(), file)
        val descBody: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)


        val fileComp: File = file//Compressor(context).compressToFile(file)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), fileComp)
        val fileupload: MultipartBody.Part =
        createFormData("Image", file.getName(), requestBody)

        orgStructure = fileOrgStructure()
        orgStructure.dirid = RequestBody.create("text/plain".toMediaTypeOrNull(), dirID.toString())
        orgStructure.upduser = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        orgStructure.filename = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
        orgStructure.image =fileupload
        //uploadStructure(orgStructure);



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data

                try {

                    if (contentURI != null) {
                        uploadFile(contentURI, "My File");
                    }
                    Toast.makeText(context, "Image Show!", Toast.LENGTH_SHORT).show()

                    //imageview!!.setImageBitmap(bitmap)
                }
                catch (e: IOException)
                {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    fun uploadStructure(orgStructure: fileOrgStructure){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        // empNIK = "190500476"

        val orgStructureViewModel = OrgStructureViewModel(orgStructure,dao.getToken(), context!!)

        orgStructureViewModel.uploadOrgStructure
            .observe(this, object : Observer<String> {
                override fun onChanged(currencyPojos: String?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {

                        }
                    }
                    loadViewModelStructure()
                }
            })
    }

}
