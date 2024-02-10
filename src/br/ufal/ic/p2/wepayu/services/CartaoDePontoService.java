package br.ufal.ic.p2.wepayu.services;

import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.CartaoDePontoEVenda.HoraInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.EmpregadoNaoHoristaException;
import br.ufal.ic.p2.wepayu.dao.EmpregadoDAO;
import br.ufal.ic.p2.wepayu.models.CartaoDePonto;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EmpregadoHorista;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.wepayu.Utils.parseDate;

public class CartaoDePontoService {
    EmpregadoDAO empregadoDAO = new EmpregadoDAO();
    List<Empregado> empregados = new ArrayList<>();
    String filename = "data.xml";

    // Cadastra um CartaoDePonto na base de dados (XML)
    public void cadastrarCartaoDePonto(String id, String data, String horas) throws Exception {
        if (id.isEmpty()) {
            throw new AtributoNuloException("Identificacao do empregado nao pode ser nula.");
        }

        if (Double.parseDouble(horas.replace(',', '.')) <= 0) {
            throw new HoraInvalidaException("Horas devem ser positivas.");
        }

        try {
            LocalDate dataFormated = parseDate(data);
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException("Data invalida.");
        }

        boolean encontrado = false;

        empregados = empregadoDAO.getEmpregadosXML(filename);

        for (Empregado empregado : empregados) {
            if (empregado.getId().equals(id)) {
                encontrado = true;

                if (empregado instanceof EmpregadoHorista) {
                    CartaoDePonto cartaoDePonto = new CartaoDePonto(data, horas);
                    ((EmpregadoHorista) empregado).getCartoes().add(cartaoDePonto);
                } else {
                    throw new EmpregadoNaoHoristaException("Empregado nao eh horista.");
                }
            }
        }

        if (!encontrado) {
            throw new EmpregadoNaoExisteException();
        }

        empregadoDAO.salvarEmpregadosXML(empregados, filename);
    }
}
