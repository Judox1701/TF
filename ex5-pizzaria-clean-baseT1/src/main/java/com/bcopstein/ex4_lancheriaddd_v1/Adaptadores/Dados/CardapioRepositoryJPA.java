package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.CardapioRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Dados.ProdutosRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.CabecalhoCardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cardapio;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

@Repository
@Primary 
public class CardapioRepositoryJPA implements CardapioRepository {

    private final CardapioSpringDataRepository jpaRepository;
    private final ProdutosRepository produtosRepository; 

    @Autowired
    public CardapioRepositoryJPA(CardapioSpringDataRepository jpaRepository, ProdutosRepository produtosRepository) {
        this.jpaRepository = jpaRepository;
        this.produtosRepository = produtosRepository;
    }

    @Override
    public Cardapio recuperaPorId(long id) {
        CardapioEntity entity = jpaRepository.findById(id).orElse(null);
        if (entity == null) return null;

        CabecalhoCardapio cabecalho = new CabecalhoCardapio(entity.getId(), entity.getTitulo());

        Cardapio cardapio = new Cardapio(cabecalho, null);
        List<Produto> produtos = produtosRepository.recuperaProdutosCardapio(id);
        cardapio.setProdutos(produtos);

        return cardapio;
    }

    @Override
    public List<CabecalhoCardapio> cardapiosDisponiveis() {
        return jpaRepository.findAll().stream()
                .map(entity -> new CabecalhoCardapio(entity.getId(), entity.getTitulo()))
                .toList();
    }

    @Override
    public List<Produto> indicacoesDoChef() {
       return List.of(produtosRepository.recuperaProdutoPorid(2L));
    }
}