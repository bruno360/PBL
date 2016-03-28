/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;

import analisador_lexico_alan.Token;

/**
 *
 * @author Alan_Bruno
 */
public class CadeiaConstante {

    /**
     * Seria o autômato de CadeiaConstante
     *
     * @param contador
     * @param token
     * @param contador_Linhas
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarCadeia_Constante(int contador, Token token, String linha, long contador_Linhas) {
        int aux = contador;
        boolean erro = false;
        String forma = "";
        int cont = 0;
        while (aux < linha.length()) {
            int tabelascII = linha.charAt(aux);
            if (tabelascII == 34 && cont == 0) {
                forma = forma + linha.charAt(aux);
                cont++;
            } else if (tabelascII == 34) {
                forma = forma + linha.charAt(aux);
                cont++;
                break;
            } else if (tabelascII >= 32 && 126 >= tabelascII) {
                forma = forma + linha.charAt(aux);
            } else {
                forma = forma + linha.charAt(aux);
                erro = true;
            }
            aux++;
        }

        if (cont == 1 || erro) {
            token.getLista().add(new Token(true, "cadeia_mal_formada", forma, contador_Linhas));
        } else {
            token.getLista().add(new Token(false, "cadeia", forma, contador_Linhas));
        }
        return aux;
    }
}
