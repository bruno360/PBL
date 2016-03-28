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
public class Bloco {
    private String nome;
    private Bloco pai;
    private LinkedList li=new LinkedList();

    
    public Bloco(String nome) {
        this.nome = nome;
    }

    public Bloco(String nome, Bloco pai) {
        this.nome = nome;
        this.pai = pai;
    }
    
    
    public Bloco() {
    }

    public String getNome() {
        return nome;
    }

    public LinkedList getLi() {
        return li;
    }

    public Bloco getPai() {
        return pai;
    }

    public void setPai(Bloco pai) {
        this.pai = pai;
    }
    
    public void retrocederDeterminado(String x,Bloco retrocede)
    {
        if(!retrocede.getNome().equals(x))
        {
             retrocede=retrocede.getPai();
             System.err.println("----"+retrocede.getNome());
             retrocederDeterminado(x,retrocede);
        }
    
    }
    
    
}
