package com.android_project.meetingplanner.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android_project.meetingplanner.R
import com.android_project.meetingplanner.databinding.ActivityEmailBinding
import java.io.File

class EmailActivity : AppCompatActivity() {

    lateinit var emailBinding: ActivityEmailBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emailBinding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(emailBinding.root)

        val customTypeface =
            resources.getFont(com.android_project.meetingplanner.R.font.baguet_script_bold)
        // Set custom font for the toolbar title
        setSupportActionBar(emailBinding.toolbar)
        supportActionBar?.apply {
            val titleTextView = emailBinding.toolbar.getChildAt(0) as? TextView
            titleTextView?.typeface = customTypeface
            titleTextView?.textSize = 25f
        }

        emailBinding.sendEmail.setOnClickListener {
            val userAddress = emailBinding.emails.text.toString()
            val userSubject = emailBinding.emailSubject.text.toString()
            val userMessage = emailBinding.emailDescription.text.toString()

            sendEmail(userAddress, userSubject, userMessage)
        }

    }

    fun sendEmail(userAddress: String, userSubject: String, userMessage: String) {
        var emailAddresses = arrayOf(userAddress)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 123
            )
        } else {
            // Permission granted, proceed with file writing

        }

        val file = File(getExternalFilesDir(null), "event.ics")
        Log.d("test", file.name)
//        val uri = Uri.fromFile(file)
        val uri = FileProvider.getUriForFile(
            this,
            "com.android_project.meetingplanner.fileprovider",
            file
        )
//        val emailIntent = Intent(Intent.ACTION_SENDTO)
//        emailIntent.data = Uri.parse("mailto:")
////        val emailIntent = Intent(Intent.ACTION_SEND)
////        emailIntent.type = "*/*"
//        emailIntent.putExtra(Intent.EXTRA_EMAIL,emailAddresses)
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT,userSubject)
//        emailIntent.putExtra(Intent.EXTRA_TEXT,userMessage)
//        emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
//        emailIntent.type = "text/calendar"  // MIME Type for ics file

        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_EMAIL, emailAddresses)
            putExtra(Intent.EXTRA_SUBJECT, userSubject)
            putExtra(Intent.EXTRA_TEXT,userMessage)
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "text/calendar"  // MIME Type for ics file
        }

//        if(emailIntent.resolveActivity(packageManager) != null){
//            startActivity(Intent.createChooser(emailIntent,"CHOOSE AN APP"))
//        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}