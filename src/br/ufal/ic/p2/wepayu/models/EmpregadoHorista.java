package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.List;

public class EmpregadoHorista extends Empregado {
    private String salarioPorHora;
    private List<CartaoDePonto> cartoes;

    public EmpregadoHorista() {}

    public EmpregadoHorista(String nome, String endereco, Boolean sindicalizado, String salarioPorHora, List<CartaoDePonto> cartoes) {
        super(nome, endereco, sindicalizado);
        this.salarioPorHora = salarioPorHora;
        this.cartoes = cartoes;
    }

    public String getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(String salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    public List<CartaoDePonto> getCartoes() {
        return cartoes;
    }

    public void setCartoes(List<CartaoDePonto> cartoes) {
        this.cartoes = cartoes;
    }
}
