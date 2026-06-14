package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Apresentacao;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.DefinirDescontoCorrenteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaDescontoCorrenteUC;
import com.bcopstein.ex4_lancheriaddd_v1.Aplicacao.RecuperaListaDescontosUC;

@RestController
@RequestMapping("/desconto")
public class DescontoController {

    private final RecuperaListaDescontosUC recuperaListaDescontosUC;
    private final DefinirDescontoCorrenteUC definirDescontoCorrenteUC;
    private final RecuperaDescontoCorrenteUC recuperaDescontoCorrenteUC;

    public DescontoController(RecuperaListaDescontosUC recuperaListaDescontosUC,
                              DefinirDescontoCorrenteUC definirDescontoCorrenteUC,
                              RecuperaDescontoCorrenteUC recuperaDescontoCorrenteUC) {
        this.recuperaListaDescontosUC = recuperaListaDescontosUC;
        this.definirDescontoCorrenteUC = definirDescontoCorrenteUC;
        this.recuperaDescontoCorrenteUC = recuperaDescontoCorrenteUC;
    }

    @GetMapping("/lista")
    @CrossOrigin("*")
    public List<String> recuperaListaPoliticasDesconto() {
        return recuperaListaDescontosUC.run();
    }

    @GetMapping("/definir/{id}")
    @CrossOrigin("*")
    public String definirPoliticaDescontoCorrente(@PathVariable(value = "id") String id) {
        definirDescontoCorrenteUC.run(id);
        return "A política de desconto corrente é " + id;
    }

    @GetMapping("/corrente")
    @CrossOrigin("*")
    public String recuperaPoliticaDescontoCorrente() {
        return recuperaDescontoCorrenteUC.run();
    }
}
