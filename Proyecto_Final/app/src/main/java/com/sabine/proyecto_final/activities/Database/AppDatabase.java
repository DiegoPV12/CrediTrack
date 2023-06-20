package com.sabine.proyecto_final.activities.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sabine.proyecto_final.activities.DAO.TarjetaDao;
import com.sabine.proyecto_final.activities.DAO.TransaccionDao;
import com.sabine.proyecto_final.activities.DAO.UsuarioDao;
import com.sabine.proyecto_final.activities.Models.Tarjeta;
import com.sabine.proyecto_final.activities.Models.Transaccion;
import com.sabine.proyecto_final.activities.Models.Usuario;

@Database(entities = {Usuario.class, Tarjeta.class, Transaccion.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract TarjetaDao tarjetaDao();
    public abstract TransaccionDao transaccionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "miapp").fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
