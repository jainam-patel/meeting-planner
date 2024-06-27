package com.android_project.meetingplanner.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_project.meetingplanner.databinding.MeetingLocationsBinding
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class MeetingLocAdapter(var loc: Array<String>?, var loc_offset: ArrayList<String>?) :
    RecyclerView.Adapter<MeetingLocAdapter.MeetingLocViewHolder>() {

    inner class MeetingLocViewHolder(val adapterBinding: MeetingLocationsBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingLocViewHolder {
        val binding =
            MeetingLocationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeetingLocViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return loc?.size ?: 0
    }

    override fun onBindViewHolder(holder: MeetingLocViewHolder, position: Int) {

        holder.adapterBinding.meetingLoc.text = loc?.get(position) + " :"
        holder.adapterBinding.meetingTime.text = SimpleDateFormat("dd-MMM-yyyy, EEEE,   HH:mm").format(
            parseDate(loc_offset?.get(position))
        )
    }

    private fun parseDate(date: String?): Date {
        val sdf = SimpleDateFormat("dd-MMM-yyyy, EE, HH:mm", Locale.getDefault())
        return sdf.parse(date) ?: Date()
    }

}