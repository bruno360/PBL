/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Alan_Bruno
 */
public class Analisador_Lexico_Alan {

    private static Analisador_Lexico analisador = new Analisador_Lexico();
    private static Scanner sc = new Scanner(System.in);
    private static int terminar = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (3 != 3) {

        }
        //Aqui se passa os nomes do arquivo de entrada e saida
        while (terminar == 0) {
            try {
                System.out.println("Passe o nome do arquivo");
                String arquivo = sc.next();                        
                //sc.next();
                System.out.println("Passe o nome do arquivo de saida");
                // analisador.fazer_Analise(arquivo, sc.next());
                analisador.fazer_Analise(arquivo,sc.next());
                System.out.println("Se quiser terminar digite 1 ou 0 para continuar");
                terminar = sc.nextInt();
            } catch (FileNotFoundException ex2) {
                System.out.println("Veja se o arquivo realmente existe");
            } catch (IOException ex) {
                System.out.println("Veja se o arquivo realmente existe");
            } catch (Exception ex) {
                System.out.println("Erro inesperado " + ex.getMessage());
            }

        }
    }
    
    
    
}
