package br.ufal.ic.p2.wepayu.dao;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.ResultadoDeVenda;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultadoDeVendaDAO {
    // Obtém a lista de vendas salva no xml e retorna os dados
    public List<ResultadoDeVenda> getVendasXML(String nomeDoArquivo) {
        List<ResultadoDeVenda> resultados = new ArrayList<>();

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nomeDoArquivo)))) {
            try {
                while (true) {
                    ResultadoDeVenda resultado = (ResultadoDeVenda) decoder.readObject();
                    resultados.add(resultado);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // Salva os vendas no arquivo xml
    public void salvarVendasXML(List<ResultadoDeVenda> resultadoDeVendas, String nomeDoArquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nomeDoArquivo)))) {
            for (ResultadoDeVenda resultado : resultadoDeVendas) {
                encoder.writeObject(resultado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
