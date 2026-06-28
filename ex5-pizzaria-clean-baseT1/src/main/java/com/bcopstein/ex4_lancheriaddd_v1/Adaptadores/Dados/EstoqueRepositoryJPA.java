package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.EstoqueRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.ItemEstoque;

@Repository
@Primary
public class EstoqueRepositoryJPA implements EstoqueRepository {

    private final EstoqueSpringDataRepository jpaRepository;
    private final IngredienteSpringDataRepository ingredienteJpa;

    @Autowired
    public EstoqueRepositoryJPA(EstoqueSpringDataRepository jpaRepository, IngredienteSpringDataRepository ingredienteJpa) {
        this.jpaRepository = jpaRepository;
        this.ingredienteJpa = ingredienteJpa;
    }

    @Override
    public ItemEstoque recuperaItemEstoquePorIngrediente(long ingredienteId) {
        ItemEstoqueEntity estoqueEntity = jpaRepository.findById(ingredienteId).orElse(null);
        if (estoqueEntity == null) return null;

        Ingrediente ingrediente = ingredienteJpa.findById(ingredienteId).orElse(null);

        return new ItemEstoque(ingrediente, estoqueEntity.getQuantidade());
    }

    @Override
    public void atualizaQuantidade(long ingredienteId, int quantidade) {
        ItemEstoqueEntity estoqueEntity = jpaRepository.findById(ingredienteId)
                .orElse(new ItemEstoqueEntity(ingredienteId, 0));
        
        estoqueEntity.setQuantidade(quantidade);
        jpaRepository.save(estoqueEntity);
    }
}