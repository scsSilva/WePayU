package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;

public class ResultadoDeVenda implements Serializable {
    private String data;
    private String valor;
    private String idEmpregado;

    public ResultadoDeVenda() {
    }

    public ResultadoDeVenda(String idEmpregado, String data, String valor) {
        this.idEmpregado = idEmpregado;
        this.data = data;
        this.valor = valor;
    }

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}