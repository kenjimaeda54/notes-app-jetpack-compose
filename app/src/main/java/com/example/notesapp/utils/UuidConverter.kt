package com.example.notesapp.utils

import androidx.room.TypeConverter
import java.util.UUID

class UuidConverter {

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun stringToUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }

}