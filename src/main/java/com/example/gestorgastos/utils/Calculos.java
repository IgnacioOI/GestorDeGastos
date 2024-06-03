package com.example.gestorgastos.utils;

import java.util.List;

import modelo.Movimiento;

public class Calculos {
    public static double suma(List<Movimiento> listaNumero){
        return listaNumero.stream().mapToDouble(Movimiento::getCantidad).sum();
    }
}
