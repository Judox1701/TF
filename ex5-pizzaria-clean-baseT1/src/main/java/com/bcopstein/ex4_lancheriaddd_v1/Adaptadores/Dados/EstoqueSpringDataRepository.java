package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueSpringDataRepository extends JpaRepository<ItemEstoqueEntity, Long> {
}