package com.example.notesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.notesapp.model.NotesModel
import com.example.notesapp.utils.DateConverter
import com.example.notesapp.utils.UuidConverter


//enties são as classes que crianos no model
@Database(entities = [NotesModel::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class,UuidConverter::class)
abstract class NotesDatabase: RoomDatabase() {
    abstract  fun NotesDao(): NotesDao
}