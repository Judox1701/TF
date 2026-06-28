package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ReceitasRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Receita;

@Repository
@Primary
public class ReceitasRepositoryJPA implements ReceitasRepository {

    private final ReceitaSpringDataRepository jpaRepository;

    @Autowired
    public ReceitasRepositoryJPA(ReceitaSpringDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Receita recuperaReceita(long id) {
        return jpaRepository.findById(id).orElse(null);
    }
}