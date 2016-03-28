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
public class Identificador {

    /**
     * Seria o autômato de Identificador
     *
     * @param contador
     * @param token
     * @param contador_Linhas
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarIdentificador(int contador, Token token, String linha, long contador_Linhas) {
        int aux = contador;
        boolean erro = false;
        String forma = "";
        int cont = 0;
        while (aux < linha.length()) {
            int tabelascII = linha.charAt(aux);
            if (Delimitador.testarDelimitador(2, linha.charAt(aux))) {
                break;
            } else if (tabelascII >= 65 && 90 >= tabelascII || tabelascII >= 97 && 122 >= tabelascII || tabelascII >= 48 && 57 >= tabelascII || tabelascII == 95) {
                forma = forma + linha.charAt(aux);
            } else {
                forma = forma + linha.charAt(aux);
                erro = true;
            }

            aux++;
        }

        if (erro) {
            token.getLista().add(new Token(true, "id_mal_formado", forma, contador_Linhas));
        } else if (testar_Palavra_Reservada(forma)) {
            token.getLista().add(new Token(false, "palavra_reservada", forma, contador_Linhas));
        } else {
            token.getLista().add(new Token(false, "id", forma, contador_Linhas));

        }
        return aux - 1;
    }

    public boolean testar_Palavra_Reservada(String termo) {
        //  int x=0;
        // int x1=0;
        //int x2=x1+x;
        boolean resultado_teste = false;
        switch (termo) {
            case "class":
                resultado_teste = true;
                break;
            case "const":
                resultado_teste = true;
                break;
            case "else":
                resultado_teste = true;
                break;
            case "if":
                resultado_teste = true;
                break;
            case "new":
                resultado_teste = true;
                break;
            case "read":
                resultado_teste = true;
                break;
            case "write":
                resultado_teste = true;
                break;
            case "return":
                resultado_teste = true;
                break;
            case "void":
                resultado_teste = true;
                break;
            case "while":
                resultado_teste = true;
                break;
            case "int":
                resultado_teste = true;
                break;
            case "float":
                resultado_teste = true;
                break;
            case "bool":
                resultado_teste = true;
                break;
            case "string":
                resultado_teste = true;
                break;
            case "char":
                resultado_teste = true;
                break;
            case "true":
                resultado_teste = true;
                break;
            case "false":
                resultado_teste = true;
                break;
            case "main":
                resultado_teste = true;
                break;

        }
        return resultado_teste;
    }
}
