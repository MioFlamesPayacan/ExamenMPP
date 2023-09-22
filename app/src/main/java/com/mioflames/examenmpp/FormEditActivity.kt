package com.mioflames.examenmpp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mioflames.examenmpp.db.AppDataBase
import com.mioflames.examenmpp.db.Lugares
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            appBarForm()
        }
    }
}

//preview de las funciones
@Preview
@Composable
fun previeall(){
    appBarForm()
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun appBarForm(){
    //variable de contexto
    val contexto = LocalContext.current
    //instancia de la base de datos para el uso de funciones dao
    val dataBase = AppDataBase.getInstance(contexto)
    val dao = dataBase.lugaresDao()
    //appbar
    Scaffold(
        topBar = {
                 TopAppBar(
                     colors = topAppBarColors(
                         containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                         titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                     ),
                     title = { Text(modifier = Modifier.fillMaxWidth(),
                         textAlign = TextAlign.Center,
                         fontFamily = FontFamily.Cursive,
                         fontSize = 30.sp,
                         text = "MiApp Vacaciones") },
                     navigationIcon = {
                         IconButton(onClick = { val intent: Intent = Intent(contexto, MainActivity::class.java)
                         contexto.startActivity(intent)}) {
                             Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
                         }
                     },
                     actions = {
                         IconButton(onClick = { /*TODO*/ }) {
                             Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                         }
                     }
                     )
        },
        snackbarHost = {},
        content = {innerpadding->
            Column(modifier = Modifier.padding(innerpadding)) {
                //variables para almacenar lo que ingrese el usuario por textfield
                val nombreLugarState = remember { mutableStateOf(TextFieldValue()) }
                val ordenState = remember { mutableStateOf(TextFieldValue()) }
                val imagenRefState = remember { mutableStateOf(TextFieldValue()) }
                val longitudState = remember { mutableStateOf(TextFieldValue()) }
                val latitudState = remember { mutableStateOf(TextFieldValue()) }
                val costoAlojamientoState = remember { mutableStateOf(TextFieldValue()) }
                val costoTransporteState = remember { mutableStateOf(TextFieldValue()) }
                val comentarioState = remember { mutableStateOf(TextFieldValue()) }
                //Textfields
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    //uso de recursos llamando a la etiqueta de string para ser traducidos.
                    Text(text = stringResource(id = R.string.formTitle),
                        fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    //Campo de texto para el nombre del lugar
                    Text(text = stringResource(id = R.string.formNombre))
                    TextField(value = nombreLugarState.value,
                        onValueChange = { nombreLugarState.value = it },
                        label = { Text(text = "Ej: Pan de Azucar, Atacama, Chile") })
                    Spacer(modifier = Modifier.height(20.dp))
                    //Campo de texto para el orden de visita
                    Text(text = stringResource(id = R.string.formOrder))
                    TextField(value = ordenState.value,
                        onValueChange = { ordenState.value = it },
                        label = { Text(text = "Ej: 1") })
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto para la imagen de referencia
                    Text(text = stringResource(id = R.string.formUrl))
                    TextField(value = imagenRefState.value,
                        onValueChange = { imagenRefState.value = it },
                        label = { Text(text = "Ej: 1234534.jpg") })
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto para la longitud
                    Text(text = stringResource(id = R.string.formLongitude))
                    TextField(value = longitudState.value,
                        onValueChange = { longitudState.value = it },
                        label = { Text(text = "Ej: -30.5454454") })
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto para latitud
                    Text(text = stringResource(id = R.string.formLatitude))
                    TextField(value = latitudState.value,
                        onValueChange = { latitudState.value = it },
                        label = { Text(text = "Ej: 74.21212121") })
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto valor alojamiento
                    Text(text = stringResource(id = R.string.formAloj))
                    TextField(value = costoAlojamientoState.value,
                        onValueChange = { costoAlojamientoState.value = it },
                        label = { Text(text = "Ej: 45,000")})
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto valor transporte
                    Text(text = stringResource(id = R.string.formTrans))
                    TextField(value = costoTransporteState.value,
                        onValueChange = { costoTransporteState.value = it },
                        label = { Text(text = "Ej: 30.000") })
                    Spacer(modifier = Modifier.height(30.dp))
                    //Campo de texto para comentario
                    Text(text = stringResource(id = R.string.formComm))
                    TextField(value = comentarioState.value,
                        onValueChange = { comentarioState.value = it },
                        label = { Text(text = "Ej: 1234534.jpg") })
                    Spacer(modifier = Modifier.height(30.dp))
                    ExtendedFloatingActionButton(
                        containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        text = { R.string.formAddButton },
                        icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = null) },
                        //evento onclick que almacenará los datos, los pasará a su respectivo tipo de dato
                        //y almacenará en la base de datos.
                        onClick = {
                            val nomLugar = nombreLugarState.value.text
                            val ordLugar = ordenState.value.text.toIntOrNull()
                            val imgRefLugar = imagenRefState.value.text
                            val longLugar = longitudState.value.text.toDoubleOrNull()
                            val latLugar = latitudState.value.text.toDoubleOrNull()
                            val costoLugar = costoAlojamientoState.value.text.toDoubleOrNull()
                            val costoTrasladoLugar = costoTransporteState.value.text.toDoubleOrNull()
                            val comentLugar = comentarioState.value.text
                            if(nomLugar.isNotBlank() && ordLugar != null && imgRefLugar.isNotBlank() &&
                                longLugar != null && latLugar != null && costoLugar != null &&
                                costoTrasladoLugar != null && comentLugar.isNotBlank()){
                                val nuevoLugar = Lugares(
                                    nombre = nomLugar,
                                    orden = ordLugar,
                                    imgRef = imgRefLugar,
                                    longitud = longLugar,
                                    latitud = latLugar,
                                    costoAlojamiento = costoLugar,
                                    costoTransporte = costoTrasladoLugar,
                                    comentario = comentLugar)
                                CoroutineScope(Dispatchers.IO).launch {
                                    dao.insert(nuevoLugar)
                                }
                            } })
                }
            }
        },
        //floatingActionButton = {}
    )
}





