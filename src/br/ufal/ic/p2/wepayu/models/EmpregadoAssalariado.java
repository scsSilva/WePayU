package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.interfaces.IEmpregado;

import static br.ufal.ic.p2.wepayu.Utils.formatSalario;

public class EmpregadoAssalariado extends Empregado implements IEmpregado {
    private String salarioMensal;

    public EmpregadoAssalariado() {
    }

    public EmpregadoAssalariado(String nome, String endereco, String sindicalizado, String salarioMensal) {
        super(nome, endereco, sindicalizado);
        this.salarioMensal = salarioMensal;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }

    @Override
    public String getAtributo(String atributo) {
        switch (atributo) {
            case "nome":
                return getNome();
            case "endereco":
                return getEndereco();
            case "tipo":
                return "assalariado";
            case "salario":
                return formatSalario(salarioMensal);
            case "sindicalizado":
                return getSindicalizado();
            default:
                return null;
        }
    }
}
