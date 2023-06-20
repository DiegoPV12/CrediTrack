package com.sabine.proyecto_final.activities.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sabine.proyecto_final.activities.Models.Usuario;

import java.util.List;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM Usuario")
    List<Usuario> getAll();

    @Query("SELECT * FROM Usuario WHERE id = :id")
    Usuario getById(int id);

    @Query("SELECT * FROM Usuario WHERE email = :correo LIMIT 1")
    Usuario findByEmail(String correo);

    @Insert
    void insert(Usuario usuario);

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);
}


