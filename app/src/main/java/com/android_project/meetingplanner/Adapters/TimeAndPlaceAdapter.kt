package com.android_project.meetingplanner.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.trimmedLength
import androidx.recyclerview.widget.RecyclerView
import com.android_project.meetingplanner.Activities.FinalActivity
import com.android_project.meetingplanner.R
import com.android_project.meetingplanner.databinding.TimeAndPlaceBinding
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class TimeAndPlaceAdapter(
    var loc: Array<String>?,
    var convertedTimeList: MutableList<String>,
    var utcDateString: String,
    var selectedDateStr : String
) :
    RecyclerView.Adapter<TimeAndPlaceAdapter.TimeAndPlaceViewHolder>() {

    inner class TimeAndPlaceViewHolder(val adapterBinding: TimeAndPlaceBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeAndPlaceViewHolder {
        val binding =
            TimeAndPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeAndPlaceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return loc?.size ?: 0
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TimeAndPlaceViewHolder, position: Int) {
        holder.adapterBinding.textViewPlace.text = loc?.get(position) + " :"
        holder.adapterBinding.textViewTimeOffset.text = convertedTimeList?.get(position).toString()

        val s: List<String> = convertedTimeList.get(position).split(" ")
        val time: List<String> = s[2].split(":")

        if (9 <= time[0].toInt() && time[0].toInt() <= 17) {
            holder.adapterBinding.LL7.setBackgroundResource(R.drawable.time_and_place_green)
            holder.adapterBinding.emoji.text = "😁"
        } else if (7 <= time[0].toInt() && time[0].toInt() < 9 || 17 < time[0].toInt() && time[0].toInt() <= 20) {
            holder.adapterBinding.LL7.setBackgroundResource(R.drawable.time_and_place_yellow)
            holder.adapterBinding.emoji.text = "😕"
        } else {
            holder.adapterBinding.LL7.setBackgroundResource(R.drawable.time_and_place_red)
            holder.adapterBinding.emoji.text = "😴"
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, FinalActivity::class.java)
            intent.putExtra("loc_list", loc)
            intent.putStringArrayListExtra("loc_offset", convertedTimeList as ArrayList<String>)
            intent.putExtra("utcTime", utcDateString)
            intent.putExtra("selectedDate",selectedDateStr)
            it.context.startActivity(intent)
        }


    }


}