package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoComissionadoException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;

import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.formatSalario;

public class EmpregadoAssalariadoService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Adiciona um novo empregado na base de dados (XML) e retorna o seu ID
    public String adicionarEmpregado(String nome, String endereco, String salario) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);

        EmpregadoAssalariado empregadoAssalariado = new EmpregadoAssalariado(nome, endereco, "false", salario);
        empregadoAssalariado.validarCampo("Nome", nome, "");
        empregadoAssalariado.validarCampo("Endereco", endereco, "");
        empregadoAssalariado.verificarSalario(salario);

        empregados.add(empregadoAssalariado);
        empregadoDAO.salvarEmpregadosXML(empregados, filename);

        return empregadoAssalariado.getId();
    }

    // Busca um atributo específico do empregado e retorna o valor
    public String getAtributo(Empregado empregado, String atributo) throws EmpregadoNaoComissionadoException {
        String response = null;

        response = switch (atributo) {
            case "nome" -> ((EmpregadoAssalariado) empregado).getNome();
            case "endereco" -> ((EmpregadoAssalariado) empregado).getEndereco();
            case "tipo" -> "assalariado";
            case "salario" -> formatSalario(((EmpregadoAssalariado) empregado).getSalarioMensal());
            case "sindicalizado" -> ((EmpregadoAssalariado) empregado).getSindicalizado();
            case "comissao" -> throw new EmpregadoNaoComissionadoException("Empregado nao eh comissionado.");
            default -> null;
        };

        return response;
    }
}
