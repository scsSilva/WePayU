package br.ufal.ic.p2.wepayu.models;

import java.util.List;

public class MembroSindicato extends Empregado {
    private String idMembro;
    private String taxaSindical;
    private List<TaxaServico> taxaServicos;

    public MembroSindicato() {
    }

    public MembroSindicato(String nome, String endereco, String sindicalizado, String idMembro, String taxaSindical,
            List<TaxaServico> taxaServicos) {
        super(nome, endereco, sindicalizado);
        this.idMembro = idMembro;
        this.taxaSindical = taxaSindical;
        this.taxaServicos = taxaServicos;
    }

    public String getIdMembro() {
        return idMembro;
    }

    public void setIdMembro(String idMembro) {
        this.idMembro = idMembro;
    }

    public String getTaxaSindical() {
        return taxaSindical;
    }

    public void setTaxaSindical(String taxaSindical) {
        this.taxaSindical = taxaSindical;
    }

    public List<TaxaServico> getTaxaServicos() {
        return taxaServicos;
    }

    public void setTaxaServicos(List<TaxaServico> taxaServicos) {
        this.taxaServicos = taxaServicos;
    }

    @Override
    public boolean isMembroSindicato() {
        return true;
    }
}
