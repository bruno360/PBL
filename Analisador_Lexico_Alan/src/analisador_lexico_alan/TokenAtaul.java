/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador_lexico_alan;



/**
 *
 * @author bruno
 */
public class TokenAtaul {
    private String stokem;
    private Token token;

    public TokenAtaul() {
    }

    public void set(String to, Token t)
    {
    stokem=to;
    token=t;
    }

    public String getStokem() {
        return stokem;
    }

    public Token getToken() {
        return token;
    }
    
  
}

    
    

