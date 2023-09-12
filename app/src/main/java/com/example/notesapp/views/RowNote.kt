package com.example.notesapp.views

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.mock.notesData
import com.example.notesapp.model.NotesModel
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
@Composable
fun RowNote(note: NotesModel,onClick: () -> Unit) {
    //como converter e usar o pattern
    //eu usei o calendar
    //https://www.baeldung.com/kotlin/current-date-time
    //https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
      val date = note.entryDate
      val formatter = SimpleDateFormat("EEE,d MMM")
      val current = formatter.format(date)

       Surface(modifier = Modifier
          //esse padding do modifier e como se fosse a margin em relação aos outros componentes
          .padding(horizontal = 10.dp, vertical = 4.dp)
           .fillMaxWidth()
           .clickable{onClick() }
          .clip(
             RoundedCornerShape(topEnd = 32.dp, bottomStart = 32.dp)
          ), color =  Color(0xffa9a9a9)
       ) {
         //padding do column e para garantir o espaço intenro ou seja, esse padding e pra dar um espaço entre o texto
          // e a cor de cima
         Column(
             modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp)
         ) {
            Text(text = note.title, style = MaterialTheme.typography.titleLarge, color = Color.White)
            Text(text = note.description, style = MaterialTheme.typography.titleMedium,color = Color.White)
            Text(text =  current, style = MaterialTheme.typography.titleSmall,color = Color.White)
         }

       }
}


@Composable
@Preview(showBackground = true)
fun RowNotePreview() {
   RowNote(note = notesData[0], onClick = {})
}