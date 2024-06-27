package com.android_project.meetingplanner.Activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_project.meetingplanner.Adapters.MeetingLocAdapter
import com.android_project.meetingplanner.DataClass.Event
import com.android_project.meetingplanner.databinding.ActivityFinalBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class FinalActivity : AppCompatActivity() {

    lateinit var finalBinding: ActivityFinalBinding

    lateinit var adapter: MeetingLocAdapter

    lateinit var icsContent: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finalBinding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(finalBinding.root)

        val customTypeface =
            resources.getFont(com.android_project.meetingplanner.R.font.baguet_script_bold)
        // Set custom font for the toolbar title
        setSupportActionBar(finalBinding.toolbar)
        supportActionBar?.apply {
            val titleTextView = finalBinding.toolbar.getChildAt(0) as? TextView
            titleTextView?.typeface = customTypeface
            titleTextView?.textSize = 25f
        }

        finalBinding.textViewEvent.text = "Details for meeting on\n${
            SimpleDateFormat("dd-MMM-yyyy, EEEE").format(
                parseSelDate(
                    intent.getStringExtra("selectedDate").toString()
                )
            )
        }"
        val utcTime = intent.getStringExtra("utcTime")
        val loc_list = intent.getStringArrayExtra("loc_list")
        val loc_offset = intent.getStringArrayListExtra("loc_offset")

        finalBinding.displayUTCTime.text =
            SimpleDateFormat("dd-MMM-yyyy,\nEEEE, HH:mm").format(parseDate(utcTime))
        finalBinding.recyclerViewMeetingLocs.layoutManager = LinearLayoutManager(this@FinalActivity)
        adapter = MeetingLocAdapter(loc_list, loc_offset)
        finalBinding.recyclerViewMeetingLocs.adapter = adapter
        var des: String = "Description: "
        for (i in 0..< loc_offset!!.size) {
            if (i == loc_offset!!.size - 1) {
                des += loc_list!!.get(i) + ":" + loc_offset.get(i)
            } else {
                des += loc_list!!.get(i) + ":" + loc_offset.get(i) + "; "
            }
        }
        val event = Event(
            summary = "Meeting Schedule",
            description = des,
            startTime = parseDate(utcTime),
            endTime = Date(parseDate(utcTime).time + 3600000), // One hour later
//            endTime = Date(Date().time + 3600000), // One hour later
            location = loc_list as Array<String>
        )

        icsContent = createICsFile(event)

        finalBinding.btnCalendarEvent.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 101
                )
            } else {
                // Permission granted, proceed with file writing
                writeICsToFile(icsContent)
            }
        }

        finalBinding.btnEmailEvent.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 101
                )
            } else {
                // Permission granted, proceed with file writing
                writeICsToFile(icsContent)
            }

            val intent = Intent(this, EmailActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with file writing
            writeICsToFile(icsContent)
        } else {
            // Permission denied, handle the case
        }
    }

    //adding content to .ics file
    fun createICsFile(event: Event): String {
        val builder = StringBuilder()
        builder.appendLine("BEGIN:VCALENDAR")
        builder.appendLine("VERSION:2.0")
        builder.appendLine("PRODID:-//com.android_project.meetingplanner//Meeting Planner//EN")
        builder.appendLine("METHOD:PUBLISH")

        var loc = ""
        for (s in event.location) {
            if (s == event.location[event.location.size - 1]) {
                loc += s
            } else {
                loc += s + ", "
            }
        }

        // Event Details
        builder.appendLine("BEGIN:VEVENT")
        builder.appendLine("DTSTART:${SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(event.startTime)}")  // Adjust time zone as needed
        builder.appendLine("DTEND:${SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'").format(event.endTime)}")
        builder.appendLine("SUMMARY:${event.summary}")
        builder.appendLine("DESCRIPTION:${event.description}")
        builder.appendLine("LOCATION:$loc")
        // Add attendees, reminders, etc. as needed using relevant iCalendar properties
        builder.appendLine("END:VEVENT")

        builder.appendLine("END:VCALENDAR")
        return builder.toString()
    }

    private fun writeICsToFile(icsContent: String) {
        val fileName = "event.ics"
        val file = File(getExternalFilesDir(null), fileName)
        Log.d("test",filesDir.path)
        try {
            FileOutputStream(file).use { stream ->
                stream.write(icsContent.toByteArray())
                Toast.makeText(
                    applicationContext,
                    "Event saved to: ${file.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()  // Inform the user
                Log.d("test", file.absoluteFile.toString())
            }
        } catch (e: Exception) {
            // Handle any errors during file writing
            Toast.makeText(
                applicationContext,
                "${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun parseDate(date: String?): Date {
        val sdf = SimpleDateFormat("dd-MM-yyyy\nEE\nHH:mm", Locale.getDefault())
        return sdf.parse(date) ?: Date()
    }

    private fun parseSelDate(date: String): Date {
        val sdf = SimpleDateFormat("dd-MM-yyyy, EE", Locale.getDefault())
        return sdf.parse(date) ?: Date()
    }

}