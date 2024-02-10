package br.ufal.ic.p2.wepayu.dao;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmpregadoDAO {
    // Obtém a lista de empregados salva no xml e retorna os dados
    public List<Empregado> getEmpregadosXML(String nomeDoArquivo) {
        List<Empregado> empregados = new ArrayList<>();

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nomeDoArquivo)))) {
            try {
                while (true) {
                    Empregado empregado = (Empregado) decoder.readObject();
                    empregados.add(empregado);
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return empregados;
    }

    // Salva os empregados no arquivo xml
    public void salvarEmpregadosXML(List<Empregado> empregados, String nomeDoArquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nomeDoArquivo)))) {
            for (Empregado empregado : empregados) {
                encoder.writeObject(empregado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
