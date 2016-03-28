/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TabeladeSimbolos;

import java.util.*;

/**
 *
 * @author bruno
 */
public class Identificador_2 {
    private String nome;
    private long linha;
    private String tipo;
    private int deslocamento;
    private Bloco pai;  

    public Identificador_2(String nome, long linha, String tipo, int deslocamento, Bloco pai) {
        this.nome = nome;
        this.linha = linha;
        this.tipo = tipo;
        this.deslocamento = deslocamento;
        this.pai = pai;
    }

    public Identificador_2() {
    }

    public String getNome() {
        return nome;
    }

    public long getLinha() {
        return linha;
    }

    public String getTipo() {
        return tipo;
    }

    public int getDeslocamento() {
        return deslocamento;
    }

    public Bloco getPai() {
        return pai;
    }
    
    
}
