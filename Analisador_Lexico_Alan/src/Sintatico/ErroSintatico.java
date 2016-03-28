/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sintatico;

/**
 *
 * @author bruno
 */
public class ErroSintatico {
    private String erro;
    private long linhaReferencial;

    public ErroSintatico(String erro, long linhaReferencial) {
        this.erro = erro;
        this.linhaReferencial = linhaReferencial;
    }
    
    
}
