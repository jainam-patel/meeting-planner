package com.android_project.meetingplanner.Activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_project.meetingplanner.Adapters.TimeAdapter
import com.android_project.meetingplanner.R
import com.android_project.meetingplanner.databinding.ActivityTimeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeActivity : AppCompatActivity() {

    lateinit var timeBinding: ActivityTimeBinding

    lateinit var adapter : TimeAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeBinding = ActivityTimeBinding.inflate(layoutInflater)
        setContentView(timeBinding.root)

        val customTypeface = resources.getFont(R.font.baguet_script_bold)
        // Set custom font for the toolbar title
        setSupportActionBar(timeBinding.toolbar)
        supportActionBar?.apply {
            val titleTextView = timeBinding.toolbar.getChildAt(0) as? TextView
            titleTextView?.typeface = customTypeface
            titleTextView?.textSize = 25f
        }

        val locs : Array<String>? = intent.getStringArrayExtra("locations")
        val context : Context = applicationContext
//        val addedLocations: List<locations>? = intent.getParcelableArrayListExtra("addedLocations")
        val selectedDateStr = intent.getStringExtra("date") ?: ""
        val selectedDate = parseDate(selectedDateStr)
//        val utcDate = convertToUTC(selectedDate)

        val utcDatesList = generateUTCDates(selectedDate)
        val utcDateStrings = mutableListOf<String>()
        val sdfDisplay = SimpleDateFormat("dd-MM-yyyy\nEE\nHH:mm", Locale.getDefault())

        for(utcDate in utcDatesList){
            utcDateStrings.add(sdfDisplay.format(utcDate))
        }

//        val convertedTimeLists = mutableListOf<List<String>>()

//        for (timeZone in locs!!) {
//            val convertedTimes = mutableListOf<String>()
//            for (utcDate in utcDatesList) {
////                val convertedDate = convertToTimeZone(utcDate, timeZone)
////                convertedTimes.add(sdfDisplay.format(convertedDate))
//            }
//            convertedTimeLists.add(convertedTimes)
//        }

        timeBinding.textViewTime.text = "-- Find a suitable time for ${SimpleDateFormat("dd-MMM-yyyy, EEEE").format(selectedDate)} --"

        timeBinding.recyclerViewTime.layoutManager = LinearLayoutManager(this@TimeActivity)
        adapter = TimeAdapter(locs, context, utcDateStrings, selectedDateStr)
        timeBinding.recyclerViewTime.adapter = adapter


    }

    private fun parseDate(date : String) : Date{
        val sdf = SimpleDateFormat("dd-MM-yyyy, EE", Locale.getDefault())
        return sdf.parse(date) ?: Date()
    }

//    private fun parseTime(date : String) : Date{
//        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
//        return sdf.parse(date) ?: Date()
//    }

//    private fun convertToUTC(date : Date) : String{
//        val calendar = Calendar.getInstance()
//        calendar.time = date
//        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//        utcCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//
//        val sdfUTC = SimpleDateFormat("dd-MM-yyyy\nEEEE", Locale.getDefault())
//        sdfUTC.timeZone = TimeZone.getTimeZone("UTC")
//        return sdfUTC.format(utcCalendar.time)
//    }

    private fun generateUTCDates(selectedDate : Date) : List<Date>{
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate

        val utcDatesList = mutableListOf<Date>()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        while(calendar.get(Calendar.DAY_OF_MONTH) == selectedDate.date){
            utcDatesList.add(calendar.time)
            calendar.add(Calendar.HOUR_OF_DAY, 1)
        }

        return utcDatesList
    }

//    fun generateUTCOffset(date: Date, locs: Array<String>?) : List<Date> {
//        val calendar = Calendar.getInstance()
//        calendar.time = date
//
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//
//        val offsetList = mutableListOf<Date>()
//
//        for(loc in locs!!){
//
//            fun convertOffset(loc : String) : String{
//                val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
//                utcCalendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
//
//                val sdfUTC = SimpleDateFormat("HH:mm", Locale.getDefault())
//                sdfUTC.timeZone = TimeZone.getTimeZone("$loc")
//                return sdfUTC.format(utcCalendar.time)
//            }
//
//            val offset = convertOffset(loc)
//            offsetList.add(parseTime(offset))
//
//        }
//
//        return offsetList
//    }

//    private fun convertToTimeZone(date: Date, timeZone: String): Date {
//        val sdf = SimpleDateFormat("dd-MMM-yyyy, EE HH:mm", Locale.getDefault())
//        sdf.timeZone = TimeZone.getTimeZone("UTC")
//        val utcDateTime = sdf.format(date)
//        sdf.timeZone = TimeZone.getTimeZone(timeZone)
//        return sdf.parse(utcDateTime) ?: date
//    }


}

