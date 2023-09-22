package com.mioflames.examenmpp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lugares(
    //Llave primaria con el id del lugar que se genere automaticamente
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //variable nombre que contendrá el nombre del lugar a visitar
    var nombre:String,
    //variable orden para la visita de lugares agregados en el orden dado
    var orden:Int,
    //variable que almacenará la url de referencia al lugar
    var imgRef:String,
    //variable que almacenará la url de la foto tomada por el usuario

    //variable longitud y latitud que almacenará las coordenadas del lugar
    var longitud:Double,
    var latitud:Double,
    //var para almacenar el costo del alojamiento
    var costoAlojamiento:Double,
    //variable para almacenar el costo del transporte del lugar
    var costoTransporte:Double,
    //variable para almacenar los comentarios adicionales agregados por el usuario.
    var comentario:String
)
