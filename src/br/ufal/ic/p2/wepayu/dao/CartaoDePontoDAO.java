package br.ufal.ic.p2.wepayu.dao;

import br.ufal.ic.p2.wepayu.models.CartaoDePonto;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CartaoDePontoDAO {
    // Obtém a lista de cartões de ponto salva no xml e retorna os dados
    public List<CartaoDePonto> getCartoesDePontoXML(String nomeDoArquivo) {
        List<CartaoDePonto> cartaoDePontoList = new ArrayList<>();

        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(nomeDoArquivo)))) {
            try {
                while (true) {
                    CartaoDePonto cartaoDePonto = (CartaoDePonto) decoder.readObject();
                    cartaoDePontoList.add(cartaoDePonto);
                }
            } catch (ArrayIndexOutOfBoundsException e) {

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return cartaoDePontoList;
    }

    // Salva os cartões de ponto no arquivo xml
    public void salvarCartoesDePontoXML(List<CartaoDePonto> cartaoDePontoList, String nomeDoArquivo) {
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(nomeDoArquivo)))) {
            for (CartaoDePonto cartaoDePonto : cartaoDePontoList) {
                encoder.writeObject(cartaoDePonto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
