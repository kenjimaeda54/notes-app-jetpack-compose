package com.example.notesapp.utils

import androidx.room.TypeConverter
import java.security.Timestamp
import java.util.Calendar
import java.util.Date

class  DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return   Date(timestamp)
    }

    @TypeConverter
    fun longTimeStamp(date: Date): Long {
        return  date.time
    }
}