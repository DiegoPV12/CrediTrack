package com.sabine.proyecto_final.activities.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sabine.proyecto_final.activities.Models.Tarjeta;

import java.util.List;

@Dao
public interface TarjetaDao {
    @Query("SELECT * FROM Tarjeta")
    List<Tarjeta> getAll();

    @Query("SELECT * FROM Tarjeta WHERE id = :id")
    Tarjeta getById(int id);

    @Query("SELECT * FROM Tarjeta WHERE idUsuario = :idUsuario")
    List<Tarjeta> getByUsuario(int idUsuario);

    @Query("SELECT * FROM Tarjeta WHERE idUsuario = :idUsuario and banco = :banco")
    List<Tarjeta> getByBanco(int idUsuario, String banco);



    @Insert
    void insert(Tarjeta tarjeta);

    @Update
    void update(Tarjeta tarjeta);

    @Delete
    void delete(Tarjeta tarjeta);
}
