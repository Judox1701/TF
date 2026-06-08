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
        Map<String, Imposto> leisSimuladas = new HashMap<>();
        
        leisSimuladas.put("0412/2022", new Imposto("0412/2022", valor -> valor * 0.10));
        leisSimuladas.put("5762/2026", new Imposto("5762/2026", valor -> {
            if (valor <= 50.0) return 0.0;
            return (valor - 50.0) * 0.15;
        }));

        this.servicoImposto = new ServicoImposto(leisSimuladas);
    }

    @Test
    void deveCalcularDezPorCentoNaLeiAntiga() {
        // Ação: Mandamos calcular R$ 100 na lei antiga
        double resultado = servicoImposto.calcular(100.0, "0412/2022");
        
        // Verificação: 10% de 100 deve ser 10.0
        assertEquals(10.0, resultado);
    }

    @Test
    void deveRetornarZeroNaNovaLeiSeValorForMenorOuIgualCinquenta() {
        // Ação: Mandamos calcular R$ 50 na lei nova
        double resultado = servicoImposto.calcular(50.0, "5762/2026");
        
        // Verificação: Deve ser isento (0.0)
        assertEquals(0.0, resultado);
    }

    @Test
    void deveCalcularQuinzePorCentoApenasSobreOQuePassarDeCinquentaNaNovaLei() {
        // Ação: Mandamos calcular R$ 100 na lei nova. 
        // (100 - 50) = 50 tributável. 15% de 50 = 7.5.
        double resultado = servicoImposto.calcular(100.0, "5762/2026");
        
        // Verificação: Deve ser 7.5
        assertEquals(7.5, resultado);
    }

    @Test
    void deveLancarExcecaoSeALeiNaoExistir() {
        // Verificação: Se passarmos uma lei inventada, ele deve dar erro
        assertThrows(IllegalArgumentException.class, () -> {
            servicoImposto.calcular(100.0, "LEI_INVENTADA/2099");
        });
    }
}