package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.Empregado.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.*;
import br.ufal.ic.p2.wepayu.services.*;

import java.util.*;

public class Facade {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    EmpregadoService empregadoService = new EmpregadoService();
    EmpregadoHoristaService empregadoHoristaService = new EmpregadoHoristaService();
    EmpregadoAssalariadoService empregadoAssalariadoService = new EmpregadoAssalariadoService();
    EmpregadoComissionadoService empregadoComissionadoService = new EmpregadoComissionadoService();
    CartaoDeVendaService cartaoDeVendaService = new CartaoDeVendaService();
    CartaoDePontoService cartaoDePontoService = new CartaoDePontoService();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Limpa a base de dados (XML)
    public void zerarSistema() {
        empregados.clear();
        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }

    // Cria um empregado na base de dados (XML) se o seu tipo for "horista" ou "assalariado"
    public String criarEmpregado(String nome, String endereco, String tipo, String salario) throws Exception {
        String id = null;

        if (tipo.equals("horista")) {
            id = empregadoHoristaService.adicionarEmpregado(nome, endereco, salario);
        } else if (tipo.equals("assalariado")) {
            id = empregadoAssalariadoService.adicionarEmpregado(nome, endereco, salario);
        } else {
            if (tipo.equals("comissionado")) {
                throw new TipoNaoAplicavelException("Tipo nao aplicavel.");
            } else {
                throw new TipoInvalidoException("Tipo invalido.");
            }
        }

        return id;
    }

    // Cria um empregado na base de dados (XML) se o seu tipo for "comissionado"
    public String criarEmpregado(String nome, String endereco, String tipo, String salario, String comissao)
            throws Exception {
        String id = null;

        if (tipo.equals("horista") || tipo.equals("assalariado")) {
            throw new TipoNaoAplicavelException("Tipo nao aplicavel.");
        } else if (tipo.equals("comissionado")) {
            id = empregadoComissionadoService.adicionarEmpregado(nome, endereco, salario, comissao);
        } else {
            throw new TipoInvalidoException("Tipo invalido.");
        }

        return id;
    }

    // Busca um atributo do empregado
    public String getAtributoEmpregado(String emp, String atributo) throws Exception {
        return empregadoService.buscarAtributo(emp, atributo);
    }

    // Busca um empregado pelo seu nome
    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        return empregadoService.buscaEmpregadoPorNome(nome, indice);
    }

    // Remove um empregado da base de dados (XML)
    public void removerEmpregado(String emp) throws Exception {
        empregadoService.removerEmpregado(emp);
    }

    // Cadastra um cartão de ponto
    public void lancaCartao(String emp, String data, String horas) throws Exception {
        cartaoDePontoService.cadastrarCartaoDePonto(emp, data, horas);
    }

    // Retorna a quantidade de horas normais trabalhadas durante um determinado período
    public String getHorasNormaisTrabalhadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return empregadoHoristaService.buscarHorasNormaisTrabalhadas(emp, dataInicial, dataFinal);
    }

    // Retorna a quantidade de horas extras trabalhadas durante um determinado período
    public String getHorasExtrasTrabalhadas(String emp, String dataInicial, String dataFinal)
            throws NumberFormatException, Exception {
        return empregadoHoristaService.buscarHorasExtrasTrabalhadas(emp, dataInicial, dataFinal);
    }

    // Cadastra uma venda
    public void lancaVenda(String emp, String data, String valor) throws Exception {
        cartaoDeVendaService.cadastrarVenda(emp, data, valor);
    }

    // Retorna a quantidade total de vendas realizadas em determinado período
    public String getVendasRealizadas(String emp, String dataInicial, String dataFinal) throws Exception {
        return cartaoDeVendaService.buscaResultadoDeVendas(emp, dataInicial, dataFinal);
    }

    // Encerra o sistema, salvando os dados
    public void encerrarSistema() {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }
}
