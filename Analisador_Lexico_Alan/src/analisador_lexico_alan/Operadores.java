/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;




/**
 *
 * @author Alan_Bruno
 */
public class Operadores {

    /**
     * Seria o autômato de Operadores
     * @param contador
     * @param token
     * @param contador_Linhas
     * @param arquivo
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarOperadores(int contador, Token token, long contador_Linhas,Arquivo arquivo,String linha) {
        int tabelascII;
        if (linha.charAt(contador) == '!') {
            if (contador + 1 <linha.length()) {
              
                tabelascII = linha.charAt(contador + 1);

                if (linha.charAt(contador + 1) == '=') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else if (contador + 1 < linha.length() && Pertencedor.testarPertence(5,linha.charAt(contador + 1))) {
                 
                    token.getLista().add(new Token(true, "operador_mal_formado", "" +linha.charAt(contador) + linha.charAt(contador + 1), contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
            }
        } else if (linha.charAt(contador) == '&') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);

                if (linha.charAt(contador + 1) == '&') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else if (contador + 1 < linha.length() && Pertencedor.testarPertence(5,linha.charAt(contador + 1))) {
                    token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador) + linha.charAt(contador + 1), contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
            }
        } else if (linha.charAt(contador) == '|') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);

                if (linha.charAt(contador + 1) == '|') {
                    token.getLista().add(new Token(false, "operador", "" +linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else if (contador + 1 < linha.length() && Pertencedor.testarPertence(5,linha.charAt(contador + 1))) {
                    token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador) + linha.charAt(contador + 1), contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(true, "operador_mal_formado", "" + linha.charAt(contador), contador_Linhas));
            }
        } else if (linha.charAt(contador) == '=') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);
                if (linha.charAt(contador + 1) == '=') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
            }
        } else if (linha.charAt(contador) == '>') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);
                if (linha.charAt(contador + 1) == '=') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
            }
        } else if (linha.charAt(contador) == '<') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);
                if (linha.charAt(contador + 1) == '=') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
            }
        } else if (linha.charAt(contador) == '+') {
            if (contador + 1 < linha.length()) {
                tabelascII = linha.charAt(contador + 1);
                if (linha.charAt(contador + 1) == '+') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
            }
        } else if (linha.charAt(contador) == '*') {
            token.getLista().add(new Token(false, "Operador", "" + linha.charAt(contador),contador_Linhas));
        } else if (linha.charAt(contador) == '-') {
            if (contador + 1 <linha.length()) {
                tabelascII = linha.charAt(contador + 1);
                if (linha.charAt(contador + 1) == '-') {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador) + linha.charAt(contador + 1),contador_Linhas));
                    contador++;
                } else {
                    token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador),contador_Linhas));
                }
            } else {
                token.getLista().add(new Token(false, "operador", "" +linha.charAt(contador),contador_Linhas));
            }
        }
        return contador;
    }
}
