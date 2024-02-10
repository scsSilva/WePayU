package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.Utils.parseDate;
import static br.ufal.ic.p2.wepayu.Utils.validarData;

public class CartaoDeVendaService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Função auxiliar (principal é a buscaResultadoVendas) para calcular o total de vendas em determinado período
    public double calcularTotalDeVendas(List<ResultadoDeVenda> resultadoDeVendas, LocalDate dataInicial, LocalDate dataFinal) throws Exception {
        double total = 0;

        for (ResultadoDeVenda resultadoDeVenda : resultadoDeVendas) {
            LocalDate data = parseDate(resultadoDeVenda.getData());
            int comparisonInicio = data.compareTo(dataInicial);
            int comparisonFinal = data.compareTo(dataFinal);

            if (comparisonInicio >= 0 && comparisonFinal < 0) {
                total += Double.parseDouble(resultadoDeVenda.getValor().replace(',', '.'));
            }
        }

        return total;
    }

    // Calcula as vendas em determinado período, verificando os requisitos
    public String buscaResultadoDeVendas(String id, String dataInicial, String dataFinal) throws Exception {
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

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                if (empregado instanceof EmpregadoComissionado) {
                    total = calcularTotalDeVendas(((EmpregadoComissionado) empregado).getResultadoDeVendas(), dataInicialFormated,
                            dataFinalFormated);
                } else {
                    throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
                }
            }
        }

        Locale locale = Locale.getDefault();

        return String.format(locale, "%.2f", total);
    }

    // Cadastra uma venda na base de dados (XML)
    public void cadastrarVenda(String id, String data, String valor) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        boolean encontrado = false;
        String salarioFormatado = valor.replace(",", ".");
        LocalDate dataFormatada;

        if (id.isEmpty()) {
            throw new AtributoNuloException("Identificacao do empregado nao pode ser nula.");
        }

        if (Double.parseDouble(salarioFormatado) <= 0) {
            throw new SalarioInvalidoException("Valor deve ser positivo.");
        }

        try {
            dataFormatada = parseDate(data);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data invalida.");
        }

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                encontrado = true;

                if (empregado instanceof EmpregadoComissionado) {
                    ResultadoDeVenda resultadoDeVenda = new ResultadoDeVenda(data, valor);
                    ((EmpregadoComissionado) empregado).getResultadoDeVendas().add(resultadoDeVenda);
                } else {
                    throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
                }
            }
        }

        if (!encontrado) {
            throw new EmpregadoNaoExisteException();
        }

        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }
}
