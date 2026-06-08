package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.function.Function;

public class Imposto {
    private String identificador;
    private Function<Double,Double> calculaImp;

    public Imposto(String id,Function<Double,Double> calculo){
        this.identificador = id;
        this.calculaImp = calculo;
    }

    
    public String getId(){
        return identificador;
    }
    public double calculaImposto(double valor){
        return calculaImp.apply(valor);
    }
}