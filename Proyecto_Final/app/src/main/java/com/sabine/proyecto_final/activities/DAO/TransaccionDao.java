package com.sabine.proyecto_final.activities.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;

import java.util.List;

@Dao
public interface TransaccionDao {


    @Query("SELECT Transaccion.* FROM Transaccion " +
            "INNER JOIN Tarjeta ON Transaccion.idTarjeta = Tarjeta.id " +
            "WHERE Tarjeta.idUsuario = :idUsuario")
    List<Transaccion> getTransactionsByUser(int idUsuario);

    @Query("SELECT Transaccion.* FROM Transaccion " +
            "INNER JOIN Tarjeta ON Transaccion.idTarjeta = Tarjeta.id " +
            "WHERE Tarjeta.idUsuario = :idUsuario AND Transaccion.tipo = :tipo AND Tarjeta.id = :idTarjeta")
    List<Transaccion> getAllTransactionsByType(int idUsuario, boolean tipo, int idTarjeta);

    @Query("SELECT Transaccion.* FROM Transaccion " +
            "INNER JOIN Tarjeta ON Transaccion.idTarjeta = Tarjeta.id " +
            "WHERE Tarjeta.idUsuario = :idUsuario AND Tarjeta.id = :idTarjeta ")
    List<Transaccion> getAllTransactionsByUserAndCard(int idUsuario, int idTarjeta);

    @Query("SELECT Transaccion.* FROM Transaccion " +
            "INNER JOIN Tarjeta ON Transaccion.idTarjeta = Tarjeta.id " +
            "WHERE Tarjeta.idUsuario = :idUsuario AND Transaccion.tipo = :tipo")
    List<Transaccion> getAllTransactions(int idUsuario, boolean tipo);

    @Query("SELECT SUM(Transaccion.monto) " +
            "FROM Transaccion " +
            "INNER JOIN Tarjeta ON Transaccion.idTarjeta = Tarjeta.id " +
            "WHERE Tarjeta.idUsuario = :idUsuario AND Transaccion.tipo = :tipo")
    float sumaTransaccionPorUsuario(int idUsuario, boolean tipo);

    @Query("SELECT SUM(monto) FROM Transaccion WHERE tipo = :tipo AND idTarjeta = :idTarjeta")
    float sumaTransaccionPorTarjeta(int idTarjeta, boolean tipo);








    @Insert
    void insert(Transaccion transaccion);

    @Update
    void update(Transaccion transaccion);

    @Delete
    void delete(Transaccion transaccion);
}

