package br.ufal.ic.p2.wepayu.models;

public class TaxaServico {
    private String data;
    private String valor;

    public TaxaServico() {
    }

    public TaxaServico(String data, String valor) {
        this.data = data;
        this.valor = valor;
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
