package com.mioflames.examenmpp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface LugaresDao {

    //USO DE METODOS ENTREGADOS POR ROOM PARA INTERACTUAR CON LOS DATOS DE LA TABLA
    //SELECCIONA TODOS LOS ELEMENTOS DE LA LISTA LUGARES
    @Query("SELECT * FROM lugares ORDER BY orden DESC")
    fun getAll(): List<Lugares>

    //FUNCION PARA CONTAR LOS ELEMENTOS
    @Query("SELECT COUNT(*) FROM lugares")
    fun count():Int

    //FUNCION PARA INSTERTAR NUEVOS ELEMENTOS
    @Insert
    fun insert(lugares: Lugares):Long

    @Update
    //FUNCION PARA EDITAR ELEMENTOS DE UNA COLUMNA
    fun update(lugares: Lugares)

    @Delete
    //FUNCION PARA ELIMINAR ELEMENTOS DE LA LISTA
    fun delete(lugares: Lugares)




}