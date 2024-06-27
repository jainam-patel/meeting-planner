package com.android_project.meetingplanner.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android_project.meetingplanner.Activities.FinalActivity
import com.android_project.meetingplanner.databinding.TimeIntervalItemBinding
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class TimeAdapter(
    var loc: Array<String>?,
    var context: Context,
//    var utcDate: String,
    var utcDateStrings: List<String>,
    var selectedDateStr : String
//    var convertedTimeLists: MutableList<List<String>>
) : RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(val adapterBinding: TimeIntervalItemBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding =
            TimeIntervalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return utcDateStrings.size
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
//        holder.adapterBinding.textViewUTCDay.text = loc?.get(position)
//        holder.adapterBinding.textViewUTCDate.text = utcDate
        holder.adapterBinding.textViewUTCHour.text = utcDateStrings[position]
        val convertedTimeList = mutableListOf<String>()
        holder.adapterBinding.recyclerViewTimeAndPlace.layoutManager = LinearLayoutManager(context)
        for (l in loc!!) {
            convertedTimeList.add(convertToTimeZone(parseDate(utcDateStrings[position]), l))
        }
        val adapter : TimeAndPlaceAdapter = TimeAndPlaceAdapter(loc, convertedTimeList, utcDateStrings[position], selectedDateStr)
        holder.adapterBinding.recyclerViewTimeAndPlace.adapter = adapter
        holder.itemView.setOnClickListener{
            val intent = Intent(it.context, FinalActivity::class.java)
            intent.putExtra("loc_list", loc)
            intent.putStringArrayListExtra("loc_offset", convertedTimeList as ArrayList<String>)
            intent.putExtra("utcTime",utcDateStrings[position])
            intent.putExtra("selectedDate",selectedDateStr)
            it.context.startActivity(intent)
        }

    }

    private fun convertToTimeZone(date: Date, timeZone: String): String {
//        val sdf = SimpleDateFormat("dd-MMM-yyyy, EE HH:mm", Locale.getDefault())
//        sdf.timeZone = TimeZone.getTimeZone("UTC")
//        val utcDateTime = sdf.format(date)
//        sdf.timeZone = TimeZone.getTimeZone(timeZone)
//        return sdf.parse(utcDateTime) ?: date

        val calendar = Calendar.getInstance()
        calendar.time = date
        val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utcCalendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        val sdfUTC = SimpleDateFormat("dd-MMM-yyyy, EE, HH:mm")
        sdfUTC.timeZone = TimeZone.getTimeZone(timeZone)
        return sdfUTC.format(utcCalendar.time)

    }

    private fun parseDate(date: String): Date {
        val sdf = SimpleDateFormat("dd-MM-yyyy\nEE\nHH:mm", Locale.getDefault())
        return sdf.parse(date) ?: Date()
    }


}