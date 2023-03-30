package com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.samseptiano.hcroadmap.Adapter.AttachmentAdapter
import com.samseptiano.hcroadmap.CameraApp.BitmapUtils
import com.samseptiano.hcroadmap.DataClass.Attachment
import com.samseptiano.hcroadmap.DataClass.AttachmentGet
import com.samseptiano.hcroadmap.DataClass.fileOrgStructure
import com.samseptiano.hcroadmap.FileUtils
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.AttachmentViewModel
import kotlinx.android.synthetic.main.fragment_attachment.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class AttachmentFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var dialog:Dialog
    lateinit var rootView:View
    var empNIK:String?=""
    var compGrpId:String?=""
    var displayTo:String?=""

    var attachmentGet = AttachmentGet()

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
    private val FILE_PROVIDER_AUTHORITY ="com.samseptiano.hcroadmap.fileprovider"
    private var mTempPhotoPath: String? = ""
    var mCurrentPhotoPath: String? = ""


    private var mResultsBitmap: Bitmap? = null

    var attachmentList = mutableListOf<Attachment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_attachment, container, false)
        empNIK = arguments?.getString("NIK")
        compGrpId = arguments?.getString("COMPGRPID")
        displayTo = arguments?.getString("DISPLAYTO")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode)
        }

        if(displayTo.equals("TALENTPROFILEDB")){
            rootView.btnAddAttachment.visibility=View.VISIBLE
            rootView.btnAddAttachment.setOnClickListener {
                showAddAttachmentDialog()
            }
        }



        loadAttachmentViewModel()

        return rootView
    }

    fun loadAttachmentViewModel(){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
       // empNIK = "190500476"

        val attachmentViewModel = AttachmentViewModel(empNIK?:"", compGrpId?:"",null,dao.getToken(), context!!)

        attachmentViewModel.attachment
            .observe(viewLifecycleOwner, object : Observer<List<Attachment?>?> {
                override fun onChanged(currencyPojos: List<Attachment?>?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {
                            attachmentList = currencyPojos as MutableList<Attachment>
                            insertToRV()
                        }
                    }
                }
            })
    }


    fun insertToRV():Unit{
        val attachmentAdapter = displayTo?.let {
            AttachmentAdapter(attachmentList, context,activity,
                this.fragmentManager!!,this, it.toString())
        }

        rootView.rvAttachment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = attachmentAdapter
        }
    }

    private fun showAddAttachmentDialog(){
        dialog = context?.let { Dialog(it) }!!
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setContentView(R.layout.add_attachment_dialog)
        dialog!!.show()
        val edtDesc = dialog.findViewById(R.id.edtAttachment_desc) as EditText
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button
        val btnUpload = dialog.findViewById(R.id.btnUpload) as Button
        val btnAttach = dialog.findViewById(R.id.btnAttach) as Button

        btnAttach.setOnClickListener {
            showPictureDialog()
        }

        btnUpload.setOnClickListener {
            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()
            attachmentGet.FILE_DESC = RequestBody.create("text/plain".toMediaTypeOrNull(), edtDesc.text.toString())
            attachmentGet.UPDUSER = RequestBody.create("text/plain".toMediaTypeOrNull(), dao.getusername())

            uploadAttachment(attachmentGet)
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
                    1 -> dispatchTakePictureIntent()
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
        //dialog.tvFilename.text = file.path

        //creating request body for file
        val requestFile: RequestBody =
            RequestBody.create(
                context?.getContentResolver()?.getType(fileUri).toString().
                toMediaTypeOrNull(), file)
        val fileComp: File = file//Compressor(context).compressToFile(file)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), fileComp)
        val fileupload: MultipartBody.Part =
            MultipartBody.Part.createFormData("Image", file.getName(), requestBody)

        attachmentGet = AttachmentGet()
        attachmentGet.ATTACH_ID = RequestBody.create("text/plain".toMediaTypeOrNull(), "dirID.toString()")
        attachmentGet.COMP_GRP_ID = RequestBody.create("text/plain".toMediaTypeOrNull(), compGrpId.toString())
        attachmentGet.EMPNIK = RequestBody.create("text/plain".toMediaTypeOrNull(), empNIK.toString())
        attachmentGet.FILE_DESC = RequestBody.create("text/plain".toMediaTypeOrNull(), "")

        attachmentGet.UPDUSER = RequestBody.create("text/plain".toMediaTypeOrNull(), "")
        attachmentGet.extension = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
        attachmentGet.FILE_NAME = RequestBody.create("text/plain".toMediaTypeOrNull(), file.name)
        attachmentGet.image =fileupload
        //uploadStructure(orgStructure);
    }


    private fun launchCamera() {

        // Create the capture image intent
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context!!.packageManager) != null) {
            // Create the temporary File where the photo should go
            var photoFile: File? = null
            //File TempPhotoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(context)
                //photoFile = new Compressor(getContext()).compressToFile(TempPhotoFile);
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.absolutePath
                mCurrentPhotoPath = photoFile.name
                // Get the content URI for the image file
                val photoURI = FileProvider.getUriForFile(
                    context!!,
                    FILE_PROVIDER_AUTHORITY,
                    photoFile
                )
                try { // Add the URI so the camera can store the image
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                    // Launch the camera activity
                    startActivityForResult(
                        takePictureIntent,
                        CAMERA
                    )
                } catch (e: Exception) {
                }
            }
        }
    }


    fun uploadAttachment(orgStructure: AttachmentGet){

        lateinit var database: UserLoginRoomDatabase
        lateinit var dao: UserloginDao
        database = context?.let { UserLoginRoomDatabase.getDatabase(it) }!!
        dao = database.getUserLoginDao()
        //empNIK = "190500476"

        val attachmentViewModel = AttachmentViewModel(empNIK?:"", compGrpId?:"",orgStructure,dao.getToken(), context!!)

        attachmentViewModel.uploadAttachment
            .observe(viewLifecycleOwner, object : Observer<String> {
                override fun onChanged(currencyPojos: String?) {
                    if (currencyPojos != null) {
                        if(currencyPojos.isNotEmpty()) {

                        }
                    }
                        loadAttachmentViewModel()
                }
            })
    }


    private fun takePhotoFromCamera() {
        try{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA)
        }catch (e:SecurityException){
            requestPermissions(permissions, requestCode)
        }

    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, CAMERA)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
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
        else if (requestCode == CAMERA)
        {
            try {
                val contentURI = data!!.data


//                // Resample the saved image to fit the ImageView
//                mResultsBitmap = BitmapUtils.resamplePic(context, mTempPhotoPath)
//
//                val baos = ByteArrayOutputStream()
//                BitmapFactory.decodeFile(mTempPhotoPath)
//                    .compress(Bitmap.CompressFormat.JPEG, 50, baos)
//                val b = baos.toByteArray()
//                val temp =
//                    Base64.encodeToString(b, Base64.DEFAULT)
////                String temp = "";

                if (contentURI != null) {
                    uploadFile(contentURI, "My File");
                }

            }catch(e:RuntimeException){
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
                requestPermissions(permissions, requestCode)
            }
        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
        val wallpaperDirectory = File (
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs()
        }
        try
        {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".png"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.getPath()), arrayOf("image/png"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException){
            e1.printStackTrace()
        }
        return ""
    }

    companion object {
        private val IMAGE_DIRECTORY = "/HCROADMAP/UPLOADS/"
    }

}
