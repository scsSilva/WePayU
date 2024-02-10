package br.ufal.ic.p2.wepayu.models;

public class EmpregadoAssalariado extends Empregado {
    private String salarioMensal;

    public EmpregadoAssalariado() {}

    public EmpregadoAssalariado(String nome, String endereco, Boolean sindicalizado, String salarioMensal) {
        super(nome, endereco, sindicalizado);
        this.salarioMensal = salarioMensal;
    }

    public String getSalarioMensal() {
        return salarioMensal;
    }

    public void setSalarioMensal(String salarioMensal) {
        this.salarioMensal = salarioMensal;
    }
}
