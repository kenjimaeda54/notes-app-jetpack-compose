package com.example.notesapp.model


import java.util.Calendar
import java.util.Date
import java.util.UUID

//pegar a data atual com as horas
//https://www.baeldung.com/kotlin/current-date-time
data class NotesModel(
    var id: UUID = UUID.randomUUID(),
    var title: String,
    var description: String,
    var entryDate: Date = Calendar.getInstance().time
)