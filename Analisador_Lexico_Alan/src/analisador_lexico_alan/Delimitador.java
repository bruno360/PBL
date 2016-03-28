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
public class Delimitador {

    /**
     * Serve paras aber quando e que sai do autômato
     * @param caso
     * @param x
     * @return 
     */
    public static boolean testarDelimitador(int caso, char x) {
        boolean teste = false;
        int asc=x;
        switch (caso) {
            case 1: //Numero
                if (x == ' ' || x == '+' || x == '-' || x == '>' || x == '<' || x == '|' || x == '/' || x == '*' || x == '&' || x == '=' || x == '(' || x == ')' || x == ';' || x == '{' || x == '}' || x == ',' || x == '[' || x == ']' || asc==9 || x == '!') {
                    teste = true;
                }
                break;
            case 2: //Identificador 
                 if (x == ' ' || x == '+' || x == '-' || x == '>' || x == '<' || x == '|' || x == '/' || x == '*' || x == '&' || x == '=' || x == '(' || x == ')' || x == ';' || x == '{' || x == '}' || x == ',' || x == '[' || x == ']' || x == '.' || asc==9) {
                    teste = true;
                }
                break;
                
        }
        return teste;
    }
}
