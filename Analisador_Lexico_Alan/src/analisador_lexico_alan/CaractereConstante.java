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
public class CaractereConstante {

    /**
     * Seria o autômato de CaractereConstante
     *
     * @param contador
     * @param token
     * @param contador_Linhas
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarCaractere_Constante(int contador,String linha, Token token, long contador_Linhas) {
        int aux = contador;
        boolean erro = false;
        String forma = "";
        int cont = 0;
        while (aux <linha.length()) {
            int tabelascII = linha.charAt(aux);
            if (tabelascII == 39 && cont == 0) {
                forma = forma +linha.charAt(aux);
                cont++;
            } else if (tabelascII == 39) {
                forma = forma + linha.charAt(aux);
                cont++;
                break;
            } else if ((tabelascII >= 48 && 57 >= tabelascII) || (tabelascII >= 65 && 90 >= tabelascII)|| (tabelascII >= 97 && 122 >= tabelascII)) {
                forma = forma + linha.charAt(aux);
            } else {
                forma = forma + linha.charAt(aux);
                erro = true;
            }
            aux++;
        }

        if (cont == 1 || erro || forma.length()>3  || forma.length()==2) {
            token.getLista().add(new Token(true, "caractere_mal_formado", forma, contador_Linhas));
        } else {
            token.getLista().add(new Token(false, "caractere", forma,contador_Linhas));
        }
        return aux;
    }
}
