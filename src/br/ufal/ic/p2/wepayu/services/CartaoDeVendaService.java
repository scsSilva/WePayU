package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.dao.ResultadoDeVendaDAO;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.parseDate;
import static br.ufal.ic.p2.wepayu.Utils.validarData;

public class CartaoDeVendaService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    ResultadoDeVendaDAO resultadoDeVendaDAO = new ResultadoDeVendaDAO();
    List<Empregado> empregados = new ArrayList<>();
    List<ResultadoDeVenda> resultados = new ArrayList<>();
    String filename = "vendas.xml";
    String filenameEmpregado = "data.xml";

    public void cadastrarVenda(String idEmpregado, String data, String valor) throws Exception {
        resultados = resultadoDeVendaDAO.getVendasXML(filename);
        empregados = empregadoDAO.getEmpregadosXML(filenameEmpregado);
        boolean encontrado = false;
        boolean comissionado = false;

        if (idEmpregado.isEmpty()) {
            throw new AtributoNuloException("Identificacao do empregado nao pode ser nula.");
        }

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(idEmpregado)) {
                encontrado = true;
                comissionado = empregado.isComissionado();
            }
        }

        if (!encontrado) {
            throw new EmpregadoNaoExisteException();
        }

        if (!comissionado) {
            throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
        }

        String salarioFormatado = valor.replace(",", ".");
        if (Double.parseDouble(salarioFormatado) <= 0) {
            throw new SalarioInvalidoException("Valor deve ser positivo.");
        }

        LocalDate dataFormatada;
        try {
            dataFormatada = parseDate(data);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data invalida.");
        }

        ResultadoDeVenda resultadoDeVenda = new ResultadoDeVenda(idEmpregado, data, valor);
        resultados.add(resultadoDeVenda);

        resultadoDeVendaDAO.salvarVendasXML(resultados, filename);
    }

    public double calcularTotalDeVendas(String idEmpregado, LocalDate dataInicial, LocalDate dataFinal)
            throws Exception {
        double total = 0;
        resultados = resultadoDeVendaDAO.getVendasXML(filename);
        List<ResultadoDeVenda> resultadosDoEmpregado = resultados.stream()
                .filter(resultado -> resultado.getIdEmpregado().equals(idEmpregado)).toList();

        for (ResultadoDeVenda resultado : resultadosDoEmpregado) {
            LocalDate data = parseDate(resultado.getData());
            if (data.compareTo(dataInicial) >= 0 && data.compareTo(dataFinal) < 0) {
                total += Double.parseDouble(resultado.getValor().replace(',', '.'));
            }
        }

        return total;
    }

    public String buscaResultadoDeVendas(String idEmpregado, String dataInicial, String dataFinal) throws Exception {
        LocalDate dataInicialFormated;
        LocalDate dataFinalFormated;
        empregados = empregadoDAO.getEmpregadosXML(filenameEmpregado);
        boolean comissionado = false;

        try {
            dataInicialFormated = parseDate(dataInicial);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data inicial invalida.");
        }

        if (!validarData(dataFinal)) {
            throw new DataInvalidaException("Data final invalida.");
        }

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(idEmpregado)) {
                comissionado = empregado.isComissionado();
            }
        }

        if (!comissionado) {
            throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
        }

        dataFinalFormated = parseDate(dataFinal);

        if (dataInicialFormated.isAfter(dataFinalFormated)) {
            throw new DataInvalidaException("Data inicial nao pode ser posterior aa data final.");
        }

        double total = calcularTotalDeVendas(idEmpregado, dataInicialFormated, dataFinalFormated);
        return String.format("%.2f", total);
    }
}
