package br.ufal.ic.p2.wepayu.models;

import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.SalarioInvalidoException;

import java.io.Serializable;
import java.util.UUID;

public class Empregado implements Serializable {
    private String id;
    private String nome;
    private String endereco;
    private String sindicalizado;

    public Empregado() {}

    public Empregado(String nome, String endereco, Boolean sindicalizado) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.endereco = endereco;
        this.sindicalizado = sindicalizado.toString();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getSindicalizado() {
        return sindicalizado;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setSindicalizado(String sindicalizado) {
        this.sindicalizado = sindicalizado;
    }

    // Recebe o valor de algum campo da classe empregado e faz a sua validação. Também recebe uma mensagem para ser exibida, caso ocorra algum erro
    public void validarCampo(String nomeCampo, String valor, String message) throws Exception {
        if (valor.isEmpty()) {
            if (!message.isEmpty()) {
                throw new AtributoNuloException(message);
            } else {
                throw new AtributoNuloException(nomeCampo + " nao pode ser nulo.");
            }
        }
    }

    // Recebe o valor do campo salário e verifica se atende aos requisitos
    public void verificarSalario(String salario) throws Exception {
        validarCampo("Salario", salario, "");

        salario = salario.replace(',', '.');

        try {
            double salarioDouble = Double.parseDouble(salario);

            if (salarioDouble < 0) {
                throw new SalarioInvalidoException("Salario deve ser nao-negativo.");
            }
        } catch (NumberFormatException e) {
            throw new SalarioInvalidoException("Salario deve ser numerico.");
        }
    }
}
