package com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.Responses;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private long id;
    private String status;
    private LocalDateTime dataHoraPagamento;
    private double valor;
    private double impostos;
    private double desconto;
    private double valorCobrado;
    private List<String> itens;

    public PedidoResponse(long id, String status, LocalDateTime dataHoraPagamento, double valor, double impostos,
            double desconto, double valorCobrado, List<String> itens) {
        this.id = id;
        this.status = status;
        this.dataHoraPagamento = dataHoraPagamento;
        this.valor = valor;
        this.impostos = impostos;
        this.desconto = desconto;
        this.valorCobrado = valorCobrado;
        this.itens = itens;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public double getValor() {
        return valor;
    }

    public double getImpostos() {
        return impostos;
    }

    public double getDesconto() {
        return desconto;
    }

    public double getValorCobrado() {
        return valorCobrado;
    }

    public List<String> getItens() {
        return itens;
    }
}
