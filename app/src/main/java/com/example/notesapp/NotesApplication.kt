package com.example.notesapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


//Adicionar Application com Hilt
//https://www.udemy.com/course/kotling-android-jetpack-compose-/learn/lecture/29429562#questions
@HiltAndroidApp
class NotesApplication:  Application() {}