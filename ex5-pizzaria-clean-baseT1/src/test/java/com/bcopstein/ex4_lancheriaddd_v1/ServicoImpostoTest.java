package com.bcopstein.ex4_lancheriaddd_v1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Entidades.Imposto;
import com.bcopstein.ex4_lancheriaddd_v1.Dominio.Servicos.ServicoImposto;

class ServicoImpostoTest {

    private ServicoImposto servicoImposto;

    @BeforeEach
    void prepararCenario() {
        // Configuramos o cenário para usar a lei "0412/2022"
        String leiVigente = "0412/2022";
        servicoImposto = new ServicoImposto(leiVigente);
    }

    @Test
    void calcularImposto(){
        // Ação: Mandamos calcular R$ 100 na lei antiga
        double resultado = servicoImposto.calcular(100.0);
        
        // Verificação: 10% de 100 deve ser 10.0
        assertEquals(10.0, resultado);


    }
}