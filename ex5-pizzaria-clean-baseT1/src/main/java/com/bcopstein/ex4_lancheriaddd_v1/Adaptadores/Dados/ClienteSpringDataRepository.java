package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Cliente;

public interface ClienteSpringDataRepository extends JpaRepository<Cliente, String> {
    
}