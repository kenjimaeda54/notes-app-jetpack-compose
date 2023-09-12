package com.example.notesapp.di

import android.content.Context
import androidx.room.Room
import com.example.notesapp.data.NotesDatabase
import com.example.notesapp.data.NotesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//tive que colocar javaversion para 17 por causa da dependencia KP
//aqui exmeplo como fazer
//https://stackoverflow.com/questions/69079963/how-to-set-compilejava-task-11-and-compilekotlin-task-1-8-jvm-target-com
//   kotlin("kapt")
//depois de pronto precisamos rebuildar o projeto
//dai ir uma pasta java com nome generated apos rebuildar projeto
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //aqui ocorre os providers para injeção dependencia, estou provendo o Dao e tambem database
    //nos criamos uma função abstrata do notesDatabase que da acesso ao dao
    @Singleton
    @Provides
    fun notesDatabaseDao(notesDatabase: NotesDatabase): NotesDao = notesDatabase.NotesDao()


    //aaqui ocorre os providers para injeção dependencia do dabastae
    //fallbackToDestructiveMigration se não dar match com a versão do dabase vamos permitir recriar a tabelas
    @Singleton
    @Provides
    fun notesDatabase(@ApplicationContext context: Context): NotesDatabase = Room.databaseBuilder(
        context,
        NotesDatabase::class.java,
        "notes_db"
    )
        .fallbackToDestructiveMigration()
        .build()

}