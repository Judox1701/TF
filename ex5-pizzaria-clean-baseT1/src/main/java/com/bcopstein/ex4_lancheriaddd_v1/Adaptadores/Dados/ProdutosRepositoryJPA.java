package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Repository
@Primary
public class ProdutosRepositoryJPA implements ProdutosRepository {

    private final ProdutoSpringDataRepository jpaRepository;

    @Autowired
    public ProdutosRepositoryJPA(ProdutoSpringDataRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Produto recuperaProdutoPorid(long id) {
        return jpaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Produto> recuperaProdutosCardapio(long id) {
        return jpaRepository.findProdutosByCardapioId(id);
    }
}