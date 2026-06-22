package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades;

import java.util.function.BiFunction;

public class Desconto {
    private String identificador;
    private String desc;
    private BiFunction<Double,Integer,Double> calculaDesconto;

    public Desconto(String id, String descricao, BiFunction<Double,Integer,Double> calculo){
        this.identificador = id;
        this.desc = descricao;
        this.calculaDesconto = calculo;
    }

    public String getDesc(){
        return this.desc;
    }
    public String getId(){
        return identificador;
    }
    public double calculaDesconto(double valor,int pedidosRecentes){
        return calculaDesconto.apply(valor,pedidosRecentes);
    }
}