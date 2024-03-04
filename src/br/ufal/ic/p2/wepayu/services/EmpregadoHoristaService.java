package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoHoristaException;
import br.ufal.ic.p2.wepayu.dao.CartaoDePontoDAO;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static br.ufal.ic.p2.wepayu.Utils.*;
import static br.ufal.ic.p2.wepayu.Utils.parseDate;

public class EmpregadoHoristaService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    CartaoDePontoDAO cartaoDePontoDAO = new CartaoDePontoDAO();
    List<Empregado> empregados = new ArrayList<>();
    List<CartaoDePonto> cartoesDePonto = new ArrayList<>();
    String filename = "data.xml";
    String filenameCartoes = "pontos.xml";

    // Adiciona um novo empregado na base de dados (XML) e retorna o seu ID
    public String adicionarEmpregado(String nome, String endereco, String salario) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);

        EmpregadoHorista empregadoHorista = new EmpregadoHorista(nome, endereco, "false", salario);
        empregadoHorista.validarCampo("Nome", nome, "");
        empregadoHorista.validarCampo("Endereco", endereco, "");
        empregadoHorista.verificarSalario(salario);

        empregados.add(empregadoHorista);
        empregadoDAO.salvarEmpregadosXML(empregados, filename);

        return empregadoHorista.getId();
    }

    // Busca um atributo específico do empregado e retorna o valor
    public String getAtributo(Empregado empregado, String atributo) throws EmpregadoNaoComissionadoException {
        String response = null;

        response = switch (atributo) {
            case "nome" -> ((EmpregadoHorista) empregado).getNome();
            case "endereco" -> ((EmpregadoHorista) empregado).getEndereco();
            case "tipo" -> "horista";
            case "salario" -> formatSalario(((EmpregadoHorista) empregado).getSalarioPorHora());
            case "sindicalizado" -> ((EmpregadoHorista) empregado).getSindicalizado();
            case "comissao" -> {
                throwComissaoException();
                yield null;
            }
            default -> null;
        };

        return response;
    }

    private void throwComissaoException() throws EmpregadoNaoComissionadoException {
        throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
    }

    // Calcula a quantidade de horas normais trabalhadas em determinado período
    public String buscarHorasNormaisTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception {
        double horas = 0;
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

        boolean encontrado = false;
        boolean horista = false;
        empregados = empregadoDAO.getEmpregadosXML(filename);

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                encontrado = true;
                horista = empregado.isHorista();
            }
        }

        if (!encontrado) {
            throw new EmpregadoNaoExisteException();
        }

        if (!horista) {
            throw new EmpregadoNaoHoristaException("Empregado nao eh horista.");
        }

        cartoesDePonto = cartaoDePontoDAO.getCartoesDePontoXML(filenameCartoes);
        List<CartaoDePonto> cartoesDoEmpregado = cartoesDePonto.stream()
                .filter(cartao -> cartao.getIdEmpregado().equals(id)).toList();

        horas = calcularHorasNormais(cartoesDoEmpregado, parseDate(dataInicial), parseDate(dataFinal));

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#", symbols);

        return decimalFormat.format(horas);
    }

    // Calcula a quantidade de horas extras trabalhadas em determinado período
    public String buscarHorasExtrasTrabalhadas(String id, String dataInicial, String dataFinal) throws Exception {
        double horas = 0;
        LocalDate inicioPeriodo = parseDate(dataInicial);
        LocalDate finalPeriodo = parseDate(dataFinal);

        empregados = empregadoDAO.getEmpregadosXML(filename);

        cartoesDePonto = cartaoDePontoDAO.getCartoesDePontoXML(filenameCartoes);
        List<CartaoDePonto> cartoesDoEmpregado = cartoesDePonto.stream()
                .filter(cartao -> cartao.getIdEmpregado().equals(id)).toList();

        double horasTrabalhadas = Double
                .parseDouble(buscarHorasNormaisTrabalhadas(id, dataInicial, dataFinal).replace(',', '.'));
        double horasTotais = calcularHorasTotais(cartoesDoEmpregado, inicioPeriodo, finalPeriodo);

        horas = horasTotais - horasTrabalhadas;

        return dataFormatada(horas);
    }

    // Função auxiliar para calcular as horas normais de um trabalhador
    public double calcularHorasNormais(List<CartaoDePonto> cartoes, LocalDate dataInicial, LocalDate dataFinal) {
        double total = 0;

        int intervalo = calculatePeriodCartoes(cartoes, dataInicial, dataFinal);

        for (CartaoDePonto cartaoDePonto : cartoes) {
            LocalDate data = parseDate(cartaoDePonto.getData());
            int comparisonInicio = data.compareTo(dataInicial);
            int comparisonFinal = data.compareTo(dataFinal);

            if (comparisonInicio >= 0 && comparisonFinal < 0) {
                total += Double.parseDouble(cartaoDePonto.getHoras().replace(',', '.'));
            }
        }

        if (intervalo == 1) {
            total = intervalo * 8;
        } else {
            if (total > intervalo * 8 && total != 0) {
                total = intervalo * 8;
            }
        }

        return total;
    }

    // Função auxiliar para calcular a quantidade total de horas trabalhadas em
    // determinado período
    public double calcularHorasTotais(List<CartaoDePonto> cartoes, LocalDate dataInicial, LocalDate dataFinal) {
        double total = 0;

        for (CartaoDePonto cartaoDePonto : cartoes) {
            LocalDate data = parseDate(cartaoDePonto.getData());
            int comparisonInicio = data.compareTo(dataInicial);
            int comparisonFinal = data.compareTo(dataFinal);

            if (comparisonInicio >= 0 && comparisonFinal < 0) {
                total += Double.parseDouble(cartaoDePonto.getHoras().replace(',', '.'));
            }
        }

        return total;
    }

    // Função auxiliar que calcula o intervalo de dias entre os cartões de ponto
    public int calculatePeriodCartoes(List<CartaoDePonto> cartaoDePontos, LocalDate dataInicio, LocalDate dataFinal) {
        int totalPeriod = 0;

        for (CartaoDePonto cartaoDePonto : cartaoDePontos) {
            LocalDate date = parseDate(cartaoDePonto.getData());
            int comparisonInicio = date.compareTo(dataInicio);
            int comparisonFinal = date.compareTo(dataFinal);

            if (comparisonInicio >= 0 && comparisonFinal < 0) {
                totalPeriod++;
            }
        }

        return totalPeriod;
    }
}
