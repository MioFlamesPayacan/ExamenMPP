package com.mioflames.examenmpp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.mioflames.examenmpp.db.Lugares
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

//clase para enumerar las pantallas
enum class Pantalla{
    DETAILS,
    PHOTO
}

//View model para el uso canara
class CamaraAppViewModel:ViewModel() {
    val pantalla = mutableStateOf(Pantalla.DETAILS)
    var onPermisoCamara: () -> Unit = {}
    var onPermisoUbicacion: () -> Unit = {}

    var lanzadorPermisos: ActivityResultLauncher<Array<String>>? = null

    fun detailsScreen() {
        pantalla.value = Pantalla.DETAILS
    }

    fun photoScreen() {
        pantalla.value = Pantalla.PHOTO
    }
}


class DetailsActivity : ComponentActivity() {
    //Instancia de viewmodel de la camara
    val camaraAppVM: CamaraAppViewModel by viewModels()

    lateinit var CamaraController : LifecycleCameraController
//Lanzador de permisos multiples para el uso de ubicacion y camara
    val lanzadorPermisos = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        when{
            (it[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false) or
                    (it[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false) -> {
                Log.v("callback RequestMultiple", "Permisos de ubicación otorgado")
                camaraAppVM.onPermisoUbicacion()
            }
            (it[android.Manifest.permission.CAMERA] ?: false) ->{
                Log.v("CallBack", "Permisos de cámara entregados")
                camaraAppVM.onPermisoCamara()
            }
            else ->{
            }
        }
    }
//configuracion de camara
    private fun configCamara(){
        CamaraController = LifecycleCameraController(this)
        CamaraController.bindToLifecycle(this)
        //Selecciona la camara trasea como la camara predeterminada al usar la app
        CamaraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        camaraAppVM.lanzadorPermisos = lanzadorPermisos
        configCamara()
        super.onCreate(savedInstanceState)
        setContent {
                detailsAppScaffold()
            }
        }
    }






@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun detailsAppScaffold(){

    //uso de scaffold para mostrar appbars
    val contexto = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) ,
                title = { Text(modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Cursive,
                    fontSize = 30.sp,
                    text = "MiApp Vacaciones")},
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
        content = { innerpadding ->

                Column(modifier = Modifier.padding(innerpadding)) {

                }
            
        },
        floatingActionButton = {},
    )
}



@Composable
public fun detailsScreen(lugares: Lugares) {

    //pantalla de detalles que mostrará la foto con los datos ingresados por el usuario anteriormente
    Column {
        Image(painter = rememberAsyncImagePainter(model = "https://picsum.photos/300/200"),
            contentDescription = null)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "${lugares.nombre}", style = TextStyle(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Costo Alojamiento $${lugares.costoAlojamiento}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Costo Traslado $${lugares.costoTransporte}")
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null)
            Text(text = "Longitud ${lugares.longitud}")
            Text(text = "Latitud ${lugares.latitud}")
        }
        Spacer(modifier = Modifier.height(20.dp))
        //llamado a la funcion de mapa, agregando como parametro los datos ingresados por usuario.
        MapaOsmUI(latitud = lugares.latitud, longitud = lugares.longitud)
    }

}


//funcion para el uso de geolocalicación
@Composable
fun MapaOsmUI(latitud: Double, longitud:Double){
    val contexto = LocalContext.current

    AndroidView(
        factory ={
            MapView(it).also {
                it.setTileSource(TileSourceFactory.MAPNIK)
                Configuration.getInstance().userAgentValue =
                    contexto.packageName
            }
        }, update = {
            it.overlays.removeIf { true }
            it.invalidate()
            it.controller.setZoom(18.0)
            val geoPoint = GeoPoint(latitud, longitud)
            it.controller.animateTo(geoPoint)

            val marcador = Marker(it)
            marcador.position = geoPoint
            marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            it.overlays.add(marcador)
        } )
}



