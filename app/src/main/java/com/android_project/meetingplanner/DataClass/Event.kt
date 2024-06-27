package com.android_project.meetingplanner.DataClass

import java.util.ArrayList
import java.util.Date

data class Event(
    val summary: String,
    val description: String,
    val startTime: Date,
    val endTime: Date,
    val location: Array<String>
)