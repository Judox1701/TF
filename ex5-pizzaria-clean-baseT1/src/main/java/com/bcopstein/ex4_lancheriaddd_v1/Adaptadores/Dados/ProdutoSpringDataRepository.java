package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Produto;

public interface ProdutoSpringDataRepository extends JpaRepository<Produto, Long> {
    @Query(value = "SELECT p.* FROM produtos p JOIN cardapio_produto cp ON p.id = cp.produto_id WHERE cp.cardapio_id = ?1", nativeQuery = true)
    List<Produto> findProdutosByCardapioId(long cardapioId);
}