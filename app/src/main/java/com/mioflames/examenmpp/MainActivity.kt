package com.mioflames.examenmpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.mioflames.examenmpp.db.AppDataBase
import com.mioflames.examenmpp.db.Lugares
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//Pantalla principal
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Instancia de la base de datos
        lifecycleScope.launch(Dispatchers.IO) {
            val lugaresDao = AppDataBase.getInstance(this@MainActivity).lugaresDao()
            val cantRegistros = lugaresDao.count()
            //condicional en caso de que la app no cuente con registros
        }
        setContent {
            AppUI()
        }
    }
}
//Preview para la app
@Preview
@Composable
fun AppUI(){
    appBarScaffold()
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun appBarScaffold(){
    //Scaffold para uso de appbar
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) ,
                title = { Text(modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 30.sp,
                    text = "MiApp Vacaciones")},
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = null)
                    }
                }
            )
        },
        snackbarHost = {},
        content = { innerpadding ->
            Column(modifier = Modifier.padding(innerpadding)) {
                //llamado a la funcion lista lugares UI
                listaLugaresUI()
            }
        },
        floatingActionButton = {},
    )
}


@Composable
fun listaLugaresUI(){
    //variable de contexto local
    val contexto = LocalContext.current
    //variable para setear Lugares
    val (lugares, setLugares) = remember {mutableStateOf(emptyList<Lugares>())}
    //Launched effect para actualizar la lista y mostrar lo que contenga
    LaunchedEffect(lugares){
        withContext(Dispatchers.IO){
            val dao  = AppDataBase.getInstance(contexto).lugaresDao()
            setLugares(dao.getAll())
        }
    }
    //uso de lazycolumn con los item lugares
    LazyColumn(modifier = Modifier.fillMaxWidth()){
        items(lugares){lugares->
            lugaresItemUI(lugares){
                setLugares(emptyList<Lugares>())
            }
        }
    }
}

@Composable
fun lugaresItemUI(lugares: Lugares, onSave:() -> Unit = {}){

    //alcance de corrutina
    val corrutina = rememberCoroutineScope()
    val contexto = LocalContext.current

    Row (modifier = Modifier.fillMaxWidth()){
        Card(modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary),
            shape = MaterialTheme.shapes.large)
        {
            Image(painter = rememberAsyncImagePainter(model = "https://picsum.photos/300/200"),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .aspectRatio(3f / 2f)
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = lugares.nombre, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                //intent para generar evento al clickear el botón, redirija a la pantalla de detalles.
                Button(onClick = { val intent: Intent = Intent(contexto, DetailsActivity::class.java)
                contexto.startActivity(intent)}) {
                    Text(text = "Más info")
                }

            }
        }
    }
}



