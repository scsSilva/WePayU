package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.Empregado.AtributoNuloException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.ComissaoInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.Empregado.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.models.Empregado;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {
    // Recebe uma string contendo um salário e o formata adequadamente
    public static String formatSalario(String salario) {
        try {
            if (salario.contains(",")) {
                return salario;
            }

            double s = Double.parseDouble(salario);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();

            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat("0.00", symbols);

            return df.format(s);
        } catch (NumberFormatException e) {
            return "Formato de salário inválido";
        }
    }

    // Transforma uma data (em string) para LocalDate
    public static LocalDate parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        return LocalDate.parse(date, formatter);
    }

    // Faz a validação de data
    public static boolean validarData(String data) {
        String[] parts = data.split("/");
        int dia = Integer.parseInt(parts[0]);
        int mes = Integer.parseInt(parts[1]);
        int ano = Integer.parseInt(parts[2]);
        if (mes < 1 || mes > 12) {
            return false;
        }
        if (dia < 1) {
            return false;
        }
        if (mes == 2) {
            if (anoBissexto(ano)) {
                return dia <= 29;
            } else {
                return dia <= 28;
            }
        }
        if (mes == 4 || mes == 6 || mes == 9 || mes == 11) {
            return dia <= 30;
        }
        return dia <= 31;
    }

    // Verifica se o ano é bissexto
    public static boolean anoBissexto(int ano) {
        return (ano % 4 == 0 && ano % 100 != 0) || (ano % 400 == 0);
    }

    // Faz a validação de um determinado atributo
    public static void validarAtributo(String atributo) throws Exception {
        if (!atributo.equals("nome") && !atributo.equals("endereco") && !atributo.equals("tipo")
                && !atributo.equals("comissao") && !atributo.equals("salario")
                && !atributo.equals("sindicalizado")) {
            throw new AtributoNuloException("Atributo nao existe.");
        }
    }

    // Retorna uma data formatada corretamente
    public static String dataFormatada(double qtde) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.#", symbols);

        return decimalFormat.format(qtde);
    }
}
