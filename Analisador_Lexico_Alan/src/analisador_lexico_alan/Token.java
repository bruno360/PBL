/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;

import java.util.*;

/**
 *
 * @author Alan_Bruno
 */
public class Token {

    private LinkedList<Token> lista;
    private boolean erro_Token;
    private String nome;
    private String token;
    private long linha;

    public Token() {
        lista = new LinkedList<>();
    }

    /**
     * Construtor de Token
     *
     * @param erro_Token especificar se erro true, se não false
     * @param nome tipo do Token
     * @param token o Token em si
     * @param linha a linha do Token
     */
    public Token(boolean erro_Token, String nome, String token, long linha) {
        this.erro_Token = erro_Token;
        this.nome = nome;
        this.token = token;
        this.linha = linha;
    }

    public LinkedList<Token> getLista() {
        return lista;
    }

    public boolean isErro_Token() {
        return erro_Token;
    }

    public String getNome() {
        return nome;
    }

    public String getToken() {
        return token;
    }

    /**
     * Pega linha do Token
     *
     * @return long
     */
    public long getLinha() {
        return linha;
    }

}
