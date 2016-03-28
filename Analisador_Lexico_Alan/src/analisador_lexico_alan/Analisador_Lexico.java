/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Alan_Bruno
 */
public class Analisador_Lexico {

    private Arquivo arquivo;
    private Token token;
    private boolean comentario_Bloco;
    private long contador_Linhas;
    private CadeiaConstante cadeia_Constante;
    private CaractereConstante caractere_Constante;
    private Identificador identificador;
    private Operadores operadores;
    private Numero numero;

    public Analisador_Lexico() {
        cadeia_Constante = new CadeiaConstante();
        caractere_Constante = new CaractereConstante();
        identificador = new Identificador();
        operadores = new Operadores();
        numero = new Numero();
        token = new Token();
    }

    /**
     *Faz análise lexica
     * @param arquivo_passado
     * @param arquivoSaida
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void fazer_Analise(String arquivo_passado, String arquivoSaida) throws FileNotFoundException, IOException {
        contador_Linhas = 0;
        comentario_Bloco = false;
        contador_Linhas = 0;
        token.getLista().clear();
        arquivo = new Arquivo(arquivo_passado, arquivoSaida);
        String linha;
        while (true) {
            try {
                linha = this.arquivo.lerLinha();
            } catch (IOException ex) {
                break;
            }
            contador_Linhas++;
            if (linha == null) {
                if (comentario_Bloco) {
                    token.getLista().add(new Token(true, "comentario malformado", "/*", contador_Linhas));
                }

                this.arquivo.gravaArquivo(this.token);
                break;
            } else {

                olhar_Linha(linha);
            }

        }
    }

    /**
     * Analisa uma linha
     *
     * @param linha
     */
    private void olhar_Linha(String linha) {

        int contador = 0;
        while (contador < linha.length()) {

            int tabelascII = linha.charAt(contador);

            if (comentario_Bloco == false && tabelascII == 47) {
                if (contador + 1 < linha.length()) {
                    tabelascII = linha.charAt(contador + 1);
                    if (tabelascII == 42) {
                        comentario_Bloco = true;
                        contador++;
                    } else if (tabelascII == 47) {
                        while (contador < linha.length()) {

                            contador++;
                        }
                        //token.getLista().add(new Token(false, "comentario", "//", contador_Linhas));
                    } else {
                        token.getLista().add(new Token(false, "operador", "/", contador_Linhas));
                    }
                } else {
                    token.getLista().add(new Token(false, "operador", "/", contador_Linhas));
                }
            } else if (comentario_Bloco && tabelascII == 42) {
                if (contador + 1 < linha.length()) {
                    tabelascII = linha.charAt(contador + 1);
                    if (tabelascII == 47) {
                        //token.getLista().add(new Token(false, "comentario", "/**/", contador_Linhas));
                        comentario_Bloco = false;
                        contador++;
                    }

                }

            } else if (comentario_Bloco) {

            } else if (tabelascII == 32 || tabelascII == 9) {

            } else if (comentario_Bloco == false && tabelascII == 34) {
                contador = cadeia_Constante.analisarCadeia_Constante(contador, token, linha, contador_Linhas);
            } else if (comentario_Bloco == false && tabelascII == 39) {
                contador = caractere_Constante.analisarCaractere_Constante(contador, linha, token, contador_Linhas);
            } else if (comentario_Bloco == false && Pertencedor.testarPertence(3, linha.charAt(contador))) {
                token.getLista().add(new Token(false, "delimitador", "" + linha.charAt(contador), contador_Linhas));

            } else if (comentario_Bloco == false && Pertencedor.testarPertence(4, linha.charAt(contador))) {
                contador = identificador.analisarIdentificador(contador, token, linha, contador_Linhas);

            } else if (comentario_Bloco == false && Pertencedor.testarPertence(1, linha.charAt(contador))) {//60

                contador = operadores.analisarOperadores(contador, token, contador_Linhas, arquivo, linha);

            } else if (tabelascII >= 48 && 57 >= tabelascII) {
                if (token.getLista().size() >= 2) {

                    int ta = token.getLista().size();

                    if (token.getLista().get(ta - 1).getToken().equals("-")) {

                        if (token.getLista().get(ta - 2).getToken().equals("-") || token.getLista().get(ta - 2).getToken().equals("==") || token.getLista().get(ta - 2).getToken().equals("=") || token.getLista().get(ta - 2).getToken().equals("/") || token.getLista().get(ta - 2).getToken().equals("*") || token.getLista().get(ta - 2).getToken().equals("+") || token.getLista().get(ta - 2).getToken().equals(".")) {

                            long cot_linha = token.getLista().get(ta - 1).getLinha();
                            token.getLista().removeLast();
                            contador = numero.analisarNumero_Negativo(contador, token, contador_Linhas, linha);

                        } else {
                            contador = numero.analisarNumero(contador, token, contador_Linhas, linha);
                        }

                    } else {
                        contador = numero.analisarNumero(contador, token, contador_Linhas, linha);
                    }
                } else {
                    contador = numero.analisarNumero(contador, token, contador_Linhas, linha);
                }

            } else if (tabelascII == 46) {
                token.getLista().add(new Token(false, "operador", "" + linha.charAt(contador), contador_Linhas));
            } else {
                String forma = "";
                while (contador < linha.length()) {
                    tabelascII = linha.charAt(contador);
                    if (Delimitador.testarDelimitador(2, linha.charAt(contador))) {
                        break;
                    } else {
                        forma = forma + linha.charAt(contador);
                    }

                    contador++;
                }
                token.getLista().add(new Token(true, "expressao_invalida", forma, contador_Linhas));
                contador--;
            }
            contador++;

        }

    }

}
