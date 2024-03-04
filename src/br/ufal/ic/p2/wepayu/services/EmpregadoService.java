package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoAssalariado;
import br.ufal.ic.p2.wepayu.models.EmpregadoComissionado;
import br.ufal.ic.p2.wepayu.models.MembroSindicato;

import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.validarAtributo;

public class EmpregadoService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    EmpregadoComissionadoService empregadoComissionadoService = new EmpregadoComissionadoService();
    EmpregadoAssalariadoService empregadoAssalariadoService = new EmpregadoAssalariadoService();
    EmpregadoHoristaService empregadoHoristaService = new EmpregadoHoristaService();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Busca um atributo específico do empregado e retorna o valor
    public String buscarAtributo(String id, String atributo) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        String response = null;
        boolean encontrado = false;

        if (id.isEmpty()) {
            throw new AtributoNuloException("Identificacao do empregado nao pode ser nula.");
        } else {
            validarAtributo(atributo);
        }

        try {
            for (Empregado empregado : empregados) {
                if (empregado.getId().equals(id)) {
                    encontrado = true;
                    response = empregado.getAtributo(atributo);
                }
            }

            if (!encontrado) {
                throw new EmpregadoNaoExisteException();
            }

            if (response == null) {
                throw new EmpregadoNaoExisteException();
            }
        } catch (Exception e) {
            throw new EmpregadoNaoExisteException();
        }

        return response;
    }

    // Busca um empregado específico na base de dados (XML) levando em consideração
    // o seu nome
    public String buscaEmpregadoPorNome(String nome, int indice) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        List<Empregado> filtrados = empregados.stream().filter(empregado -> empregado.getNome().equals(nome)).toList();

        if (filtrados.isEmpty()) {
            throw new AtributoNuloException("Nao ha empregado com esse nome.");
        }

        return filtrados.get(indice - 1).getId();
    }

    // Remove um empregado da base de dados (XML)
    public void removerEmpregado(String id) throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);
        int index = -1;

        if (id.isEmpty()) {
            throw new AtributoNuloException("Identificacao do empregado nao pode ser nula.");
        }

        for (int i = 0; i < empregados.size(); i++) {
            if (empregados.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            empregados.remove(index);
            empregadoDAO.salvarEmpregadosXML(empregados, filename);
        } else {
            throw new EmpregadoNaoExisteException();
        }
    }

    public void alteraDadosEmpregado(String id, String atibuto, String valor) {
        empregados = empregadoDAO.getEmpregadosXML(filename);

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                if (atibuto.equals("sindicalizado")) {
                    empregado.setSindicalizado(valor);
                }

                break;
            }
        }

        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }

    public void alteraDadosEmpregado(String id, String atributo, String valor, String idSindicato, String taxaSindical)
            throws Exception {
        empregados = empregadoDAO.getEmpregadosXML(filename);

        for (Empregado empregado : empregados) {
            if (empregado.isMembroSindicato()) {
                if (((MembroSindicato) empregado).getIdMembro().equals(idSindicato)) {
                    throw new Exception("Ha outro empregado com esta identificacao de sindicato");
                }
            }
        }

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                if (atributo.equals("sindicalizado")) {
                    empregado.setSindicalizado(valor);
                    MembroSindicato membroSindicato = new MembroSindicato(empregado.getNome(), empregado.getEndereco(),
                            empregado.getSindicalizado(), idSindicato, taxaSindical, new ArrayList<>());
                    empregados.add(membroSindicato);
                }

                break;
            }
        }

        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }
}
