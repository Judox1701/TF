package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.function.Function;

public class Desconto {
    private String identificador;
    private Function<Double,Double> calculaDesconto;

    public Desconto(String id,Function<Double,Double> calculo){
        this.identificador = id;
        this.calculaDesconto = calculo;
    }

    
    public String getId(){
        return identificador;
    }
    public double calculaDesconto(double valor){
        return calculaDesconto.apply(valor);
    }
}