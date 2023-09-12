package com.example.notesapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesapp.model.NotesModel
import com.example.notesapp.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

//e mutableState aqui
//mutableState e
// ele cria um observable

@HiltViewModel
class NotesViewModel @Inject constructor(private val repositoryNote: NotesRepository) :
    ViewModel() {
    // private var listNotes = mutableStateListOf<NotesModel>() // se não estiver usando flow ou dados assincronos o mutableState e perfeito
    //estou usando stateflow porque o tipo que retorna no room que determinei e um flow
    private val _noteList = MutableStateFlow<List<NotesModel>>(emptyList())
    val noteList = _noteList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            //distincUntilChanged ira retornar o flow
            //collect retorna nosso flow que no caso e a list de de notes
            repositoryNote.get().distinctUntilChanged().collect { listOfNotes ->
                if (listOfNotes.isEmpty()) {
                    Log.d("ListOfNodes", "List is empty")
                } else {
                    _noteList.value = listOfNotes
                }
            }
        }
    }

    //por ser funções assincronas usamos viewModelScope o launch e para retornar o courtine
    fun addNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.add(notesModel)
    }

    fun updateNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.update(notesModel)
    }

    fun deleteNote(notesModel: NotesModel) = viewModelScope.launch {
        repositoryNote.deleteOnly(notesModel)
    }


}