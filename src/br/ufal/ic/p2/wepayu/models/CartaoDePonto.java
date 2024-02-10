package br.ufal.ic.p2.wepayu.models;

public class CartaoDePonto {
    public String data;
    public String horas;

    public CartaoDePonto () {}

    public CartaoDePonto(String data, String horas) {
        this.data = data;
        this.horas = horas;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }
}
