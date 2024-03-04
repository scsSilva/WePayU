package br.ufal.ic.p2.wepayu.models;

public class CartaoDePonto {
    public String data;
    public String horas;
    public String idEmpregado;

    public CartaoDePonto() {
    }

    public CartaoDePonto(String data, String horas, String idEmpregado) {
        this.data = data;
        this.horas = horas;
        this.idEmpregado = idEmpregado;
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

    public String getIdEmpregado() {
        return idEmpregado;
    }

    public void setIdEmpregado(String idEmpregado) {
        this.idEmpregado = idEmpregado;
    }
}
