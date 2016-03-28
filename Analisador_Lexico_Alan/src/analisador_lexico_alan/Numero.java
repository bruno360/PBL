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
public class Numero {

    /**
     * Seria o autômato de Numero Negativo
     * @param contador
     * @param token
     * @param contador_Linhas    
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarNumero_Negativo(int contador, Token token, long contador_Linhas,String linha) {
        int aux = contador;
        boolean erro = false;
        String forma = "-";
        int cont = 0;
        while (aux < linha.length()) {
            int tabelascII = linha.charAt(aux);
            if (Delimitador.testarDelimitador(1,linha.charAt(aux) )) {
                break;
            } else if (tabelascII == 46 && cont == 0) {
                 if (aux + 1 < linha.length()) {
                    tabelascII = linha.charAt(aux + 1);
                    if (tabelascII >= 48 && 57 >= tabelascII) {
                        forma = forma + linha.charAt(aux);
                        cont++;
                    } else {
                        break;
                    }
                } else {
                    erro = true;
                }
            } else if (tabelascII == 46 && cont == 1) {
                break;
            } else if (tabelascII >= 48 && 57 >= tabelascII) {
                forma = forma + linha.charAt(aux);
            } else {
                forma = forma + linha.charAt(aux);
                erro = true;
            }
            aux++;
        }

        if (erro) {
            token.getLista().add(new Token(true, "nro_mal_formado", forma, contador_Linhas));
        } else {
            token.getLista().add(new Token(false, "numero", forma,contador_Linhas));
        }
        return aux - 1;
    }
  /**
     * Seria o autômato de Numero
     * @param contador
     * @param token
     * @param contador_Linhas    
     * @param linha
     * @return int esse inteiro seria a onde parou a leitura
     */
    public int analisarNumero(int contador,  Token token, long contador_Linhas,String linha) {
        int aux = contador;
        boolean erro = false;
        String forma = "";
        int cont = 0;
        while (aux < linha.length()) {
            int tabelascII = linha.charAt(aux);
            if (Delimitador.testarDelimitador(1,linha.charAt(aux) )) {
                break;
            } else if (tabelascII == 46 && cont == 0) {
                 if (aux + 1 < linha.length()) {
                    tabelascII = linha.charAt(aux + 1);
                    if (tabelascII >= 48 && 57 >= tabelascII) {
                        forma = forma + linha.charAt(aux);
                        cont++;
                    } else {
                        break;
                    }
                } else {
                    erro = true;
                }
            } else if (tabelascII == 46 && cont == 1) {
                break;
            } else if (tabelascII >= 48 && 57 >= tabelascII) {
                forma = forma + linha.charAt(aux);
            } else {
                forma = forma + linha.charAt(aux);
                erro = true;
            }
            aux++;
        }

        if (erro) {
            token.getLista().add(new Token(true, "nro_mal_formado", forma, contador_Linhas));
        } else {
            token.getLista().add(new Token(false, "numero", forma,contador_Linhas));
        }
        return aux - 1;
    }
}
