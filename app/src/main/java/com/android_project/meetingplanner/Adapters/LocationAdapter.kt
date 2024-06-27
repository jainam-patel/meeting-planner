package com.android_project.meetingplanner.Adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android_project.meetingplanner.databinding.LocationItemBinding
import com.android_project.meetingplanner.DataClass.locations
import com.android_project.meetingplanner.SwipeToDelete


class LocationAdapter(
    var location: List<locations>,
    val maxLocation: Int,
    val context: Context,
    val loc: MutableList<String>
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>(),
    SwipeToDelete.OnSwipeListener {

    lateinit var addedLocations: String

    inner class LocationViewHolder(val adapterBinding: LocationItemBinding) :
        RecyclerView.ViewHolder(adapterBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return location.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.adapterBinding.locationNum.text =
            location[position].locText.toString() + " ${position + 1}:"
        holder.adapterBinding.cities.adapter = location[position].cities
//        holder.adapterBinding.cities.setSelection(10)
        var l = ""
        val p: Int = position
        if(loc.size-1<p)
        {
            loc.add(p, l)
        }
//        if (loc[position] != null) {
//            var temp: Int = 0
//            for (i in 0..<location[position].cities.count) {
//                if (loc[position] == location[position].cities.getItem(i)) {
//                    temp = i
//                }
//            }
//            holder.adapterBinding.cities.setSelection(temp)
//        }
        if(loc[position]!="")
        {
            var temp: Int = 0
            for (i in 0..<location[position].cities.count) {
                if (loc[position] == location[position].cities.getItem(i)) {
                    temp = i
                }
            }
            holder.adapterBinding.cities.setSelection(temp)
        }
//        for(i in loc)
//        {
//            Log.d("test",i)
//        }
        //without add() here, ArrayIndexOutOfBoundException was thrown
        holder.adapterBinding.cities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        l = holder.adapterBinding.cities.selectedItem.toString()
//                        l = holder.adapterBinding.cities
                        loc.set(p, l)
                        //set() is used because it replaces l at position p whereas add() was adding even redundant selected locations in array
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
    }

    fun addLocation(newLocation: locations) {
        if (location.size < maxLocation) {
            location = location.plus(newLocation) as ArrayList<locations>
            notifyItemInserted(location.size - 1)
        } else {
            Toast.makeText(context, "You can only enter 5 locations", Toast.LENGTH_LONG).show()
        }

    }

    fun getAddedLocation(): String {
        return addedLocations
    }

    override fun onSwipe(position: Int) {
    }

    override fun onDeleteClick(position: Int) {
        (location as ArrayList<locations>).removeAt(position)
        loc.removeAt(position)
        notifyDataSetChanged()
    }

}