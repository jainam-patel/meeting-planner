package com.android_project.meetingplanner.DataClass

import android.os.Parcel
import android.os.Parcelable
import android.widget.ArrayAdapter
import android.widget.Spinner

data class locations(
    val locText : String,
    val cities : ArrayAdapter<String>
)