package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.IngredientesRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Ingrediente;

@Repository
@Primary
public class IngredientesRepositoryJPA implements IngredientesRepository {

    private final IngredienteSpringDataRepository jpaRepository;

    @Autowired
    public IngredientesRepositoryJPA(IngredienteSpringDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Ingrediente> recuperaTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Ingrediente> recuperaIngredientesReceita(long id) {
        throw new UnsupportedOperationException("Substituído pelo carregamento automático da Receita");
    }
}