package com.android_project.meetingplanner.Activities

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.TimeZoneNames
import android.icu.util.ULocale
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.android_project.meetingplanner.Adapters.LocationAdapter
import com.android_project.meetingplanner.databinding.ActivityDateBinding
import com.android_project.meetingplanner.DataClass.locations
import com.android_project.meetingplanner.SwipeToDelete
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateActivity : AppCompatActivity() {

    lateinit var dateBinding: ActivityDateBinding

    lateinit var adapter: LocationAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dateBinding = ActivityDateBinding.inflate(layoutInflater)
        val view = dateBinding.root
        setContentView(view)


        val customTypeface = resources.getFont(com.android_project.meetingplanner.R.font.baguet_script_bold)
        // Set custom font for the toolbar title
        setSupportActionBar(dateBinding.toolbar)
        supportActionBar?.apply {
            val titleTextView = dateBinding.toolbar.getChildAt(0) as? TextView
            titleTextView?.typeface = customTypeface
            titleTextView?.textSize = 25f
//            setHomeAsUpIndicator(com.android_project.meetingplanner.R.drawable.tz_icon) // Set your icon here
//            setDisplayHomeAsUpEnabled(true)
        }

//        dateBinding.toolbar.inflateMenu(com.android_project.meetingplanner.R.menu.toolbar_end)

//        dateBinding.toolbar1.setOnMenuItemClickListener { item ->
//            when(item.itemId){
//                com.android_project.meetingplanner.R.id.tzConverter ->
//                    startActivity(Intent(this, TimeZoneConverter::class.java))
//            }
//            return@setOnMenuItemClickListener true
//        }

        val loc= mutableListOf<String>()

        dateBinding.todayDate.setOnClickListener {
            dateBinding.date.setText(SimpleDateFormat("dd-MM-yyyy, EE").format(Date()))
        }
        dateBinding.selectDate.setOnClickListener {
            setDate()
        }
        dateBinding.btnShowTime.setOnClickListener {
            if(loc.isEmpty())
            {
                Toast.makeText(this,"Please select locations",Toast.LENGTH_LONG).show()
            }
            else
            {
                val intent = Intent(this@DateActivity, TimeActivity::class.java)
                intent.putExtra("locations",loc.toTypedArray())
                intent.putExtra("date",dateBinding.date.text.toString())
                Log.d("test",loc.toString())
                startActivity(intent)
            }

        }

//        val data  = ArrayList<locations>()
//        for (i in 1..5){
//            data.add(locations("Location $i:", citySpinner()))
//        }

//        val current = ZoneId.systemDefault().toString()

        dateBinding.recyclerViewLocations.layoutManager = LinearLayoutManager(this)
        adapter = LocationAdapter(mutableListOf(), 5, this, loc)
        dateBinding.recyclerViewLocations.adapter = adapter

        val swipeToDelete = SwipeToDelete(adapter, this, dateBinding.recyclerViewLocations)
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(dateBinding.recyclerViewLocations)

        dateBinding.fabAddLocation.setOnClickListener {
//            dateBinding.textViewDate.text = "${offset.availableMetaZoneIDs}"

            if(dateBinding.date.text==""){
                Toast.makeText(applicationContext, "Please select date first",Toast.LENGTH_LONG).show()
            }else{
                val newLocation = locations("Location",citySpinner())
                adapter.addLocation(newLocation)
            }
        }

    }

    fun setDate(){

        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
//            R.style.,
            {_:DatePicker, year : Int, month : Int, dayOfMonth : Int ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy, EE", Locale.getDefault())
                val formattedDate = dateFormat.format(calendar.time)
                dateBinding.date.text = "$formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun citySpinner() : ArrayAdapter<String>{
//        val cities : Array<String> = (ZoneId.getAvailableZoneIds().toTypedArray())
//        val cities : MutableList<String> = (ZoneId.getAvailableZoneIds().toMutableList())
//        val cities : Array<String> = android.icu.util.TimeZone.getAvailableIDs()
        val cities : MutableList<String> = android.icu.util.TimeZone.getAvailableIDs().toMutableList()
//        val cities : MutableList<String> = TimeZoneNames.getInstance(ULocale.getDefault()).availableMetaZoneIDs.toMutableList()
        cities.add(0,"Select a location")
//        for(city in cities){
//            city.split("/")
//        }


        var citiesAdapter = ArrayAdapter<String>(
            this,
            R.layout.simple_spinner_dropdown_item,
            cities.toTypedArray())
        citiesAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        return citiesAdapter
    }


}