package com.bcopstein.ex4_lancheriaddd_v1.Adaptadores.Dados;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pizzaria.imposto")
public class ImpostoConfigProperties {
    private String leiVigente;
    public String getLeiVigente() { return leiVigente; }
    public void setLeiVigente(String leiVigente) { this.leiVigente = leiVigente; }
}