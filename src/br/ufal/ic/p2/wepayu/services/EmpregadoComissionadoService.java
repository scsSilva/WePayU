package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;

import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.formatSalario;

public class EmpregadoComissionadoService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Adiciona um novo empregado na base de dados (XML) e retorna o seu ID
    public String adicionarEmpregado(String nome, String endereco, String salario, String comissao) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        List<ResultadoDeVenda> resultadoDeVendas = new ArrayList<>();

        EmpregadoComissionado empregadoComissionado = new EmpregadoComissionado(nome, endereco, false, salario, comissao, resultadoDeVendas);
        empregadoComissionado.validarCampo("Nome", nome, "");
        empregadoComissionado.validarCampo("Endereco", endereco, "");
        empregadoComissionado.verificarSalario(salario);
        empregadoComissionado.verificarComissao(comissao);

        empregados.add(empregadoComissionado);
        empregadoDAO.salvarEmpregadosXML(empregados, filename);

        return empregadoComissionado.getId();
    }

    // Busca um atributo específico do empregado e retorna o valor
    public String getAtributo(Empregado empregado, String atributo) {
        String response = null;

        response = switch (atributo) {
            case "nome" -> ((EmpregadoComissionado) empregado).getNome();
            case "endereco" -> ((EmpregadoComissionado) empregado).getEndereco();
            case "tipo" -> "comissionado";
            case "comissao" -> formatSalario(((EmpregadoComissionado) empregado).getTaxaDeComissao());
            case "salario" -> formatSalario(((EmpregadoComissionado) empregado).getSalarioMensal());
            case "sindicalizado" -> ((EmpregadoComissionado) empregado).getSindicalizado();
            default -> null;
        };

        return response;
    }
}
