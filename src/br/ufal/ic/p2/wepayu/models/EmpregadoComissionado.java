package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.Empregado.ComissaoInvalidaException;

import java.io.Serializable;
import java.util.List;

public class EmpregadoComissionado extends Empregado {
    private String salarioMensal;
    private String taxaDeComissao;
    private List<ResultadoDeVenda> resultadoDeVendas;

    public EmpregadoComissionado() {
    }

    public EmpregadoComissionado(String nome, String endereco, Boolean sindicalizado, String salarioMensal,
            String taxaDeComissao, List<ResultadoDeVenda> resultadoDeVendas) {
        super(nome, endereco, sindicalizado);
        this.salarioMensal = salarioMensal;
        this.taxaDeComissao = taxaDeComissao;
        this.resultadoDeVendas = resultadoDeVendas;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public String getTaxaDeComissao() {
        return taxaDeComissao;
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    public void setTaxaDeComissao(String taxaDeComissao) {
        this.taxaDeComissao = taxaDeComissao;
    }

    public List<ResultadoDeVenda> getResultadoDeVendas() {
        return resultadoDeVendas;
    }

    public void setResultadoDeVendas(List<ResultadoDeVenda> resultadoDeVendas) {
        this.resultadoDeVendas = resultadoDeVendas;
    }

    // Recebe o valor da taxa de comissão e verifica se atende aos requisitos
    public void verificarComissao(String comissao) throws Exception {
        validarCampo("Comissao", comissao, "Comissao nao pode ser nula.");

        comissao = comissao.replace(',', '.');

        try {
            double comissaoDouble = Double.parseDouble(comissao);

            if (comissaoDouble < 0) {
                throw new ComissaoInvalidaException("Comissao deve ser nao-negativa.");
            }
        } catch (NumberFormatException e) {
            throw new ComissaoInvalidaException("Comissao deve ser numerica.");
        }
    }
}
