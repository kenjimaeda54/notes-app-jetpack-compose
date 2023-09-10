package com.example.notesapp.screens.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.mock.notesData
import com.example.notesapp.model.NotesModel
import com.example.notesapp.views.ButtonCommon
import com.example.notesapp.views.TextFieldCommon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    listNotes: List<NotesModel>,
    addNotes: () -> Unit,
    removeNotes: () -> Unit,
) {
    var notes by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        ) {
        //para pegar alguma string que esta na pasta res
        TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }, actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Icone Notification"
            )
        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFA6B1B9)
        )
        )
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextFieldCommon(value = notes, onValueChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace()
                    }) notes = it
            }, label = {
                Text(text = "Add your note")
            })
            Spacer(modifier = Modifier.height(10.dp))
            TextFieldCommon(value = description, onValueChange = {
                if (it.all { char ->
                        char.isLetter() || char.isWhitespace()
                    }) description = it
            }, label = {
                Text(text = "Add your description")
            })
            Spacer(modifier = Modifier.height(30.dp))
            ButtonCommon(onCLick = { /*TODO*/ }, textButton = "Salvar")
        }
        LazyColumn {
            items(listNotes) {

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun NotesScreenPreview() {
    NotesScreen(listNotes =  notesData, addNotes = {}, removeNotes = {})
}