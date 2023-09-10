package com.example.notesapp.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextFieldCommon(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    label: @Composable() (() -> Unit)?,
    immerAction: () -> Unit = {}
) {
    val keybaordController = LocalSoftwareKeyboardController.current

    //passando o modifier assim fica bem flexivel, alem da propridedade max posso passar outras e ira apenas concatenar
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = label,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent
        ),
        //keyboard actions e o tipo do retorno dos boteos no key board
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        //e aqui oque vai ocorrer ao clicar no botão done doo keybaord
        keyboardActions = KeyboardActions(onDone = {
            immerAction()
            keybaordController?.hide()
        })
    )
}

@Composable
@Preview(showBackground = true)
fun TextFieldCommonPreview() {
    TextFieldCommon(value = "", onValueChange = {}, label = { Text(text = "Insira os notes") })
}