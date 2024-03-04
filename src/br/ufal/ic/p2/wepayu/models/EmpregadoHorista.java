package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.interfaces.IEmpregado;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.*;

public class EmpregadoHorista extends Empregado implements IEmpregado {
    private String salarioPorHora;

    public EmpregadoHorista() {
    }

    public EmpregadoHorista(String nome, String endereco, String sindicalizado, String salarioPorHora) {
        super(nome, endereco, sindicalizado);
        this.salarioPorHora = salarioPorHora;
    }

    public String getSalarioPorHora() {
        return salarioPorHora;
    }

    public void setSalarioPorHora(String salarioPorHora) {
        this.salarioPorHora = salarioPorHora;
    }

    @Override
    public boolean isHorista() {
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
                return "horista";
            case "salario":
                return formatSalario(salarioPorHora);
            case "sindicalizado":
                return getSindicalizado();
            default:
                return null;
        }
    }
}
