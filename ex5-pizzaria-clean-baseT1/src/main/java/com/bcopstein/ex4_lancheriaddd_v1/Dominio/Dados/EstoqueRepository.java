package com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

public interface EstoqueRepository {
    ItemEstoque recuperaItemEstoquePorIngrediente(long ingredienteId);
    void atualizaQuantidade(long ingredienteId, int quantidade);
}
