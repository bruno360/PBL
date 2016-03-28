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
public class Pertencedor {

    /**
     * Testa se isso pertence a um dado autômato
     *
     * @param caso
     * @param x Caractere passado
     * @return boolean true pertence e false não
     */
    public static boolean testarPertence(int caso, char x) {
        boolean teste = false;
        int tabelaAsc = x;
        switch (caso) {
            case 1: //Operadores
                if (x == '+' || x == '-' || x == '>' || x == '<' || x == '|' || x == '/' || x == '*' || x == '&' || x == '=' || x == '!') {
                    teste = true;
                }
                break;
            case 2: //Numeros
                if (tabelaAsc >= 48 && 57 >= tabelaAsc) {
                    teste = true;
                }
                break;
            case 3://Delimitadores
                if (x == ';' || x == '{' || x == '}' || x == ',' || x == '[' || x == ']' || x == '(' || x == ')') {
                    teste = true;
                }
                break;
            case 4: //Identificador
                if (tabelaAsc >= 65 && 90 >= tabelaAsc || tabelaAsc >= 97 && 122 >= tabelaAsc) {
                    teste = true;
                }
                break;
            case 5: //Operadores 2
                if (x == '+' || x == '-' || x == '>' || x == '<' || x == '|' || x == '/' || x == '*' || x == '&' || x == '=' || x == '.') {
                    teste = true;
                }
                break;

        }
        return teste;
    }
}
