package com.sabine.proyecto_final.activities.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Usuario.class,
        parentColumns = "id",
        childColumns = "idUsuario",
        onDelete = ForeignKey.CASCADE))
public class Tarjeta {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String nombre;
    public String numero;
    public String fecha;

    public String csv;



    public String banco;
    public float limite;
    public String moneda;
    public int idUsuario;

    public String color;

    public Tarjeta(String nombre, String numero, String fecha, String banco, float limite, String moneda, int idUsuario, String color,String csv) {
        this.nombre = nombre;
        this.numero = numero;
        this.fecha = fecha;
        this.banco = banco;
        this.limite = limite;
        this.moneda = moneda;
        this.idUsuario = idUsuario;
        this.color = color;
        this.csv = csv;
    }

    public  Tarjeta(){

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public float getLimite() {
        return limite;
    }

    public void setLimite(float limite) {
        this.limite = limite;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }
}

