package com.example.notesapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.model.NotesModel
import kotlinx.coroutines.flow.Flow


//criando a interface DAO para acessar o Squilite
//https://media.geeksforgeeks.org/wp-content/uploads/20210720231513/viewmodal.png
@Dao
interface NotesDao {
    //suspend and flow e assincrono do Corotines
    //para lidar com os dados via request estes são ideias
    //import kotlinx.coroutines.flow.Flow
    //get não tem suspende porque estou retornado ja o flow, preciso retornar algo no get isso e o padrão de funões em kotlin
    @Query("SELECT * from notes")
    fun getAllNotes(): Flow<List<NotesModel>>

    @Query("SELECT * from notes where id =:id")
    suspend fun getSingleNote(id: String): NotesModel

    //se der conflito ira subscrever o valor que ja possui
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(notesModel: NotesModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(notesModel: NotesModel)


    @Query("DELETE  from notes")
    suspend fun deleteAllNotes()


    @Delete
    suspend fun deleteSingleNote(notesModel: NotesModel)

}
