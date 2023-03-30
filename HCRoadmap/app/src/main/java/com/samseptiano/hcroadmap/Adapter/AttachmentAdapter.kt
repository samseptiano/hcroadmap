package com.samseptiano.hcroadmap.Adapter

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.samseptiano.hcroadmap.API.APIConfig
import com.samseptiano.hcroadmap.DataClass.Attachment
import com.samseptiano.hcroadmap.Fragment.TalentMapping.SubTalentProfile.AttachmentFragment
import com.samseptiano.hcroadmap.R
import com.samseptiano.hcroadmap.Room.EmpList.EmpListDao
import com.samseptiano.hcroadmap.Room.EmpList.EmpListRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserLoginRoomDatabase
import com.samseptiano.hcroadmap.Room.UserLogin.UserloginDao
import com.samseptiano.hcroadmap.ViewModel.AttachmentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.file_attachment_item.view.*


class AttachmentAdapter(private val attachmentList: List<Attachment>, private val ctx: Context?,private val activity: Activity?,private val fm: FragmentManager, private val fg:AttachmentFragment,private val displayTo:String) : RecyclerView.Adapter<AttachmentHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): AttachmentHolder {
        return AttachmentHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.file_attachment_item, viewGroup, false))
    }

    override fun getItemCount(): Int = attachmentList.size

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        if (ctx != null) {
            if (activity != null) {
                holder.bindEmpList(attachmentList[position],ctx,activity,fm,position,fg,displayTo)
            }
        }
    }
}

class AttachmentHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvFilename = view.tvFilename
    private val imgIcon = view.imgFileType
    private val tvFileDesc = view.tvFilendesc
    private val btnRemove = view.btnRemove
    private val btnDownload = view.btnDownload

    private lateinit var database: EmpListRoomDatabase
    private lateinit var dao: EmpListDao


    fun bindEmpList(attachment: Attachment,ctx: Context,activity:Activity,fm:FragmentManager,pos:Int,fg:AttachmentFragment,displayTo: String) {
        tvFilename.text = attachment.filENAME
        tvFileDesc.text = attachment.filEDESC
        var API = APIConfig()

        if(displayTo.equals("TALENTPROFILEDB")){
            btnRemove.visibility = View.GONE
            btnDownload.visibility = View.GONE
        }
        if(attachment.filENAME!=null){
            var opt = attachment.filENAME
        when(opt?.split(".")?.get(1)) {
            "xlsx", "xlx", "csv" -> {
                imgIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.xls));
            }
            "pdf" -> {
                imgIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.pdf));
            }
            "docx", "doc" -> {
                imgIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.doc));
            }
            "ppt", "pptx" -> {
                imgIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ppt));
            }
            "jpg", "jpeg", "png", "gif" -> {
                Picasso.get().load(API.baseURLATTACHMENT + attachment.filENAME).into(imgIcon)
            }
        }

        }

        btnRemove.setOnClickListener {

            lateinit var database: UserLoginRoomDatabase
            lateinit var dao: UserloginDao
            database = ctx?.let { UserLoginRoomDatabase.getDatabase(it) }!!
            dao = database.getUserLoginDao()
            // empNIK = "190500476"

            var a = Attachment()
            a.UPDUSER = dao.getusername()
            a.attacHID = attachment.attacHID
            a.filENAME = attachment.filENAME

            val attachmentViewModel = AttachmentViewModel("", "",a,dao.getToken(), ctx!!)

            attachmentViewModel.deleteAttachment
                .observe(fg, object : Observer<String> {
                    override fun onChanged(currencyPojos: String?) {
                        if (currencyPojos != null) {
                            if(currencyPojos.isNotEmpty()) {
                                    fg.loadAttachmentViewModel()
                            }
                        }

                    }
                })

        }
        btnDownload.setOnClickListener {

            var ac = APIConfig()

            val downloadmanager =
                ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val uri = Uri.parse(ac.baseURLATTACHMENT+attachment.filENAME)
            val request = DownloadManager.Request(uri)
            request.setTitle("My File")
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());

            downloadmanager!!.enqueue(request)
        }
    }

}