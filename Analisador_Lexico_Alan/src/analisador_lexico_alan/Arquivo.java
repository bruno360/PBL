package analisador_lexico_alan;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Sintatico.AnaliseSintatica;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Alan_Bruno
 */
public class Arquivo {

    private String nome_ArquivoSaida;
    private FileReader arquivo;
    private BufferedReader ler_Arquivo;
    private AnaliseSintatica as=new AnaliseSintatica();

    /**
     * Cronstrutor que passa as infomações de arquivo de entrada e saida
     *
     * @param nome
     * @param nomes
     * @throws FileNotFoundException
     */
    public Arquivo(String nome, String nomes) throws FileNotFoundException {
        arquivo = new FileReader(nome);
        ler_Arquivo = new BufferedReader(arquivo);
        nome_ArquivoSaida = nomes;
    }

    /**
     * Pega uma linha
     *
     * @return String
     * @throws IOException
     */
    public String lerLinha() throws IOException {
        return ler_Arquivo.readLine();
    }

    /**
     * Fechou arquivo
     *
     * @return boolean se fechou o arquivo true
     */
    public boolean fecharArquivo() {
        try {
            arquivo.close();
            ler_Arquivo.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Grava os tokens no arquivo de texto
     *
     * @param token
     * @throws IOException
     */
    public void gravaArquivo(Token token) throws IOException {
        
        FileWriter arq = new FileWriter(nome_ArquivoSaida);
        PrintWriter gravarArq = new PrintWriter(arq);
        int cont = 0;
        for (int i = 0; i < token.getLista().size(); i++) {
            Token temp = token.getLista().get(i);

            if (temp.isErro_Token() == false && !temp.getNome().equals("comentario")) {
                gravarArq.println(temp.getToken() + " " + temp.getNome() + " " + temp.getLinha());
            }
        }
        gravarArq.println();
        for (int i = 0; i < token.getLista().size(); i++) {

            Token temp = token.getLista().get(i);
            if (temp.isErro_Token()) {
                gravarArq.println(temp.getToken() + " " + temp.getNome() + " " + temp.getLinha());
                cont++;
            }
        }
        if (cont == 0) {
            gravarArq.println("Nao teve erros");
        }
        as.requesitarAnalise(token.getLista());

        arq.close();
        gravarArq.close();

    }
}
