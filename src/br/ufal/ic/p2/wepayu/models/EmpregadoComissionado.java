package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.Empregado.ComissaoInvalidaException;
import br.ufal.ic.p2.wepayu.interfaces.IEmpregado;

import java.time.LocalDate;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.formatSalario;
import static br.ufal.ic.p2.wepayu.Utils.parseDate;

public class EmpregadoComissionado extends Empregado implements IEmpregado {
    private String salarioMensal;
    private String taxaDeComissao;

    public EmpregadoComissionado() {
    }

    public EmpregadoComissionado(String nome, String endereco, String sindicalizado, String salarioMensal,
            String taxaDeComissao) {
        super(nome, endereco, sindicalizado);
        this.salarioMensal = salarioMensal;
        this.taxaDeComissao = taxaDeComissao;
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

    @Override
    public boolean isComissionado() {
        return true;
    }

    @Override
    public String getAtributo(String atributo) {
        switch (atributo) {
            case "nome":
                return getNome();
            case "endereco":
                return getEndereco();
            case "tipo":
                return "comissionado";
            case "comissao":
                return formatSalario(taxaDeComissao);
            case "salario":
                return formatSalario(salarioMensal);
            case "sindicalizado":
                return getSindicalizado();
            default:
                return null;
        }
    }
}
