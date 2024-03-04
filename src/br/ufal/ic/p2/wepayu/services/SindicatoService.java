package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.Sindicato.EmpregadoNaoSindicalizadoException;
import br.ufal.ic.p2.wepayu.Exception.Sindicato.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.Utils.parseDate;
import static br.ufal.ic.p2.wepayu.Utils.validarData;

public class SindicatoService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    EmpregadoService empregadoService = new EmpregadoService();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    public double calcularTotalDeTaxas(List<TaxaServico> taxaServicoList, LocalDate dataInicial, LocalDate dataFinal)
            throws Exception {
        double total = 0;

        for (TaxaServico taxaServico : taxaServicoList) {
            LocalDate data = parseDate(taxaServico.getData());
            int comparisonInicio = data.compareTo(dataInicial);
            int comparisonFinal = data.compareTo(dataFinal);

            if (comparisonInicio >= 0 && comparisonFinal < 0) {
                total += Double.parseDouble(taxaServico.getValor().replace(',', '.'));
            }
        }

        return total;
    }

    // Calcula as vendas em determinado período, verificando os requisitos
    public String buscaTaxasDeServico(String id, String dataInicial, String dataFinal) throws Exception {
        double total = 0;
        LocalDate dataInicialFormated;
        LocalDate dataFinalFormated;
        empregados = empregadoDAO.getEmpregadosXML(filename);

        try {
            dataInicialFormated = parseDate(dataInicial);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data inicial invalida.");
        }

        if (!validarData(dataFinal)) {
            throw new DataInvalidaException("Data final invalida.");
        }

        dataFinalFormated = parseDate(dataFinal);

        if (dataInicialFormated.isAfter(dataFinalFormated)) {
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");
        }

        String nome = empregadoService.buscarAtributo(id, "nome");

        for (Empregado empregado : empregados) {
            if (empregado.getNome().equals(nome)) {
                if (empregado.isMembroSindicato()) {
                    MembroSindicato membroSindicato = (MembroSindicato) empregado;
                    List<TaxaServico> taxaServicos = membroSindicato.getTaxaServicos();

                    total = calcularTotalDeTaxas(taxaServicos, dataInicialFormated, dataFinalFormated);
                }

                if (empregado.getSindicalizado().equals("false")) {
                    throw new EmpregadoNaoSindicalizadoException("Empregado nao eh sindicalizado.");
                }
            }
        }

        Locale locale = Locale.getDefault();

        return String.format(locale, "%.2f", total);
    }

    public void cadastrarTaxa(String id, String data, String valor) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        boolean encontrado = false;
        String taxaFormatada = valor.replace(",", ".");
        LocalDate dataFormatada;

        if (id.isEmpty()) {
            throw new AtributoNuloException("Identificacao do membro nao pode ser nula.");
        }

        if (Double.parseDouble(taxaFormatada) <= 0) {
            throw new SalarioInvalidoException("Valor deve ser positivo.");
        }

        try {
            dataFormatada = parseDate(data);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data invalida.");
        }

        for (Empregado empregado : empregados) {
            if (empregado.isMembroSindicato()) {
                if (((MembroSindicato) empregado).getIdMembro().equals(id)) {
                    encontrado = true;

                    TaxaServico taxaServico = new TaxaServico(data, valor);
                    ((MembroSindicato) empregado).getTaxaServicos().add(taxaServico);
                }
            }
        }

        if (!encontrado) {
            throw new MembroNaoExisteException("Membro nao existe.");
        }

        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }
}
