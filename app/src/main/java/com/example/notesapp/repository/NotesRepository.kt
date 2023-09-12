package com.example.notesapp.repository

import com.example.notesapp.data.NotesDao
import com.example.notesapp.data.NotesDatabase
import com.example.notesapp.model.NotesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NotesRepository @Inject constructor(private val notesDatabase: NotesDao) {

    suspend fun add(notesModel: NotesModel) = notesDatabase.addNote(notesModel)
    suspend fun update(notesModel: NotesModel) = notesDatabase.updateNote(notesModel)
    suspend fun deleteOnly(notesModel: NotesModel) = notesDatabase.deleteSingleNote(notesModel)
    suspend fun deleteAll() = notesDatabase.deleteAllNotes()
    //conflate sempre ira pegar o valor atulizado do suspense
    suspend fun get(): Flow<List<NotesModel>> = notesDatabase.getAllNotes().flowOn(Dispatchers.IO).conflate()


}