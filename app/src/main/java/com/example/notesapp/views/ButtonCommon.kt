package com.example.notesapp.views

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ButtonCommon(modifier: Modifier = Modifier, onCLick: () -> Unit, textButton: String) {
    Button(
        modifier = modifier,
        onClick = onCLick, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue
        )
    ) {
        Text(text = textButton, style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
@Preview(showBackground = true)
fun ButtonCommonPreview() {
    ButtonCommon(onCLick = {}, textButton = "Salvar")
}