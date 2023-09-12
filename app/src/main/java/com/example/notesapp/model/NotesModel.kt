package com.example.notesapp.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.notesapp.utils.DateConverter
import java.time.Instant
import java.util.Calendar
import java.util.Date
import java.util.UUID

//pegar a data atual com as horas
//https://www.baeldung.com/kotlin/current-date-time

@Entity(tableName = "notes")
//se não colocar o nome da entity considerara nomne da classe
data class NotesModel(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    //pode usar column info se não usar a propriedade sera a mesma que definimos aqui
    var title: String,
    var description: String,
    var entryDate: Date = Date()
)