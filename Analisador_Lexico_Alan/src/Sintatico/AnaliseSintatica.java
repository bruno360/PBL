/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sintatico;


import TabeladeSimbolos.*;
import analisador_lexico_alan.Token;
import analisador_lexico_alan.TokenAtaul;
import java.util.*;

/**
 *
 * @author bruno
 */
public class AnaliseSintatica {
private LinkedList <Token> list;
private int size,deslocamento;
private int controle;
private boolean controleConst;
private TokenAtaul token=new TokenAtaul();
private int teste;
private long errorLinha;
private String chamouConst,tipoconst;
private TabeladeSimbolos tabela=new TabeladeSimbolos();    
private LinkedList<ErroSintatico> erro=new LinkedList<>();
private Bloco corrente;
private boolean _main;
public AnaliseSintatica() {
    }
    
    public void requesitarAnalise(LinkedList <Token> liste)
    {
        erro.clear();
        tabela.limparTabela();
        tabela.costruirTabela();
        corrente=(Bloco)this.tabela.getBlocos().getLi().getFirst();
        tipoconst=chamouConst="";
        list=liste;
        size=list.size();
        deslocamento=controle=0;
        _main=controleConst=false;
        teste=0;
        arquivo();
        errorLinha=0;
    }

    public LinkedList<ErroSintatico> getErro() {
        return erro;
    }
   
    
    
    //Funções auxiliares da AnaliseSintatica
    public void avancarToken( )
    {
        
        if(controle>=size)
        {
            token.set("$", null);
           // tabela.imprimir();
        }else
        {
            Token t=list.get(controle); 
            controle++;
            errorLinha=t.getLinha();
            if(t.getNome().equals("id"))
            {
               token.set("Identifier", t);            
            }else if(t.getNome().equals("cadeia"))
            {            
                token.set("Cadeia", t);                          
            }else if(t.getNome().equals("caractere"))
            {            
               token.set("Char1", t);                
            }else if(t.getNome().equals("numero"))
            {            
               token.set("Numero", t);                      
            }else if(t.getNome().equals("/**/"))
            {
                avancarToken( );
            }
            else
            {
                token.set(t.getToken(), t);          
            }      
            System.out.println("token---> "+token.getStokem()+" controle--> "+controle+"        "+t.getToken()+"        linha real  "+t.getLinha());
            
        }
        
       // System.out.println("controle "+controle+"   "+token.getStokem());
            
    }
    
    public String seguinte(int controle2)
    {
        if(controle2<list.size())
        {
            Token t=list.get(controle2);
            if(t.getNome().equals("id"))
            {
                return "Identifier";            
            }else if(t.getNome().equals("cadeia"))
            {            
                return "Cadeia";                          
            }else if(t.getNome().equals("caractere"))
            {            
                return "Char1";             
            }else if(t.getNome().equals("numero"))
            {            
               return "Numero";                                     
            }else
            {
               return t.getToken();          
            }
        }
        return "$";
    }
    
    
    //Estrutura do arquivo
    public void arquivo()
    {
        avancarToken();
        if(token.getStokem().equals("const"))
        {
            //System.out.println("arquivo "+"constantes");           
            chamouConst="arquivo";
            constantes();
            variaveis();
            pre_main();
            
        }else  if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char"))
        {
            variaveis();
            pre_main();
        }else if(token.getStokem().equals("class") || token.getStokem().equals("void"))
        {
            pre_main();        
        }        
    }    
    
    
    public void pre_main()
    {
        if(token.getStokem().equals("void"))
        {
            main();
            classes();     
        }else if(token.getStokem().equals("class"))
        {
            classe();
            pre_main();
         
        }
    
    }
    
    
    public void classes() // Vazio de Classe é o fim do Arquivo, logo precisa tratar erro sem o fim do arquivo
    {
        if(token.getStokem().equals("class"))
        {
            classe();
            classes();
        } else {
            
            //ERRO fim de Arquivo esperado
        }
    
    }
    
    
    public void constantes() // Vazio de constantes é variaveis
    {    
        chamouConst="constantes";
        if(token.getStokem().equals("const")){            
             Bloco x=new Bloco("BlocoConstantes",this.corrente); 
             this.corrente.getLi().add(x);  
             corrente=x;             
             Const();
             corrente=(Bloco)this.tabela.getBlocos().getLi().getFirst();
             constantes();
        }   
    }
    
    
    public void variaveis() // Vazio de Variaveis é 'void'
    {        
        //Se for diferente de void, entra aqui e trata como dentro do bloco variaveis
        //Mudaria o IF
        //Retiraria o Elsa
        if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
        {            
            declaracaoVariavel();            
            variaveis();
        }else
        {
        
        }    
    }
    
    //REgra, sempre que for sair do metodo ou entrar em outro ja passa o token.
    //Questao, sempre que pegar algo faltando avança ou não pro proximo token _ QUESTAO _ DUVIDA _
        // Essa duvida gera dois casos.
        // O primeio se vier voip em vez de void, assim se nao pegarmos o proximo, por mais que a classe main teja o resto todo certo
        // Tudo vai dar erro porque nunca teremos void aceito.
        // O segundo caso seria pegando o proximo e eu esqueco a palavra void por exemplo e começo com a Main, se mesmo so esquecendo Void tudo estiver certo
        // vou fazer a comparacao em um tempo menor, logo comparo main com void vai dar erro e eu pego (, e comparo com main, ou seja tudo errado.
     public void main()
    {
        //System.out.println(0);
        _main=true;
        if(token.getStokem().equals("void"))
        {
            avancarToken();        
            if(token.getStokem().equals("main"))
            {
                avancarToken();           
                if(token.getStokem().equals("("))
                {
                    avancarToken();                
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                } else {
                    System.err.println("faltou ( no metodo main");
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                }
            } else {
                System.err.println("faltou `main` no metodo main");
                if(token.getStokem().equals("("))
                {
                    avancarToken();                
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                } else {
                    System.err.println("faltou ( no metodo main");
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                }                
            }
        } else {
            System.err.println("faltou `void` no metodo main");
            if(token.getStokem().equals("main"))
            {
                avancarToken();           
                if(token.getStokem().equals("("))
                {
                    avancarToken();                
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                } else {
                    System.err.println("faltou ( no metodo main");
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                }
            } else {
                System.err.println("faltou `main` no metodo main");
                if(token.getStokem().equals("("))
                {
                    avancarToken();                
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                } else {
                    System.err.println("faltou ( no metodo main");
                    if(token.getStokem().equals(")"))
                    {
                        avancarToken();

                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    } else {
                        System.err.println("faltou ) no metodo main");
                        if(token.getStokem().equals("{"))
                        {
                            avancarToken();
                            conteudo_metodo();
                            if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        } else {
                           System.err.println("faltou { no metodo main"); 
                           conteudo_metodo();
                           if(token.getStokem().equals("}"))
                            {
                                avancarToken();
                                //System.out.println("f "+token.getStokem());
                            } else {
                                System.err.println("faltou } no metodo main");
                                
                            }
                        }
                    }
                }                
            }
        }
    }
   

    //Estrutura da classe
     public void classe()
    {
       
        if(token.getStokem().equals("class"))
        {    
             avancarToken();
            if(token.getStokem().equals("Identifier"))
            {         
                corrente=(Bloco)this.tabela.getBlocos().getLi().getFirst();
                corrente.getLi().add(new Identificador_2(token.getToken().getToken(),token.getToken().getLinha(),"class",0,corrente)); 
                avancarToken();               
                this.expressao_heranca();
                if(token.getStokem().equals("{"))
                {
                    Bloco x=new Bloco("BlocodeClasse",this.corrente); 
                    this.corrente.getLi().add(x);  
                    corrente=x;  
                    this.avancarToken();                   
                    conteudo_classe();
                    if(token.getStokem().equals("}"))
                    {
                        avancarToken();
                        corrente=(Bloco)this.tabela.getBlocos().getLi().getFirst();
                    }
                        
                }            
            }else 
            {
                 
            }
        }
    
    }
     
     
    public void expressao_heranca()
    {
        if(token.getStokem().equals(">"))
        {
            this.avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();
            }
        }
    
    } 
    
    
    public void conteudo_classe(){
        if(token.getStokem().equals("const"))
        {
            Bloco x=new Bloco("BlocoConstantes",this.corrente); 
            this.corrente.getLi().add(x);  
            corrente=x;             
            this.Const();
            corrente=x.getPai();
            chamouConst="conteudo_classe";
            this.conteudo_classe();        
        }else if(token.getStokem().equals("}"))
        {
        
        
        }else if(token.getStokem().equals("Identifier") || token.getStokem().equals("float") || token.getStokem().equals("int") || token.getStokem().equals("char") || token.getStokem().equals("string") || token.getStokem().equals("bool") || token.getStokem().equals("void"))
        {
            id_declaracao();
            conteudo_classe();
        
        }
   
   
   
   }
    
    //Declaração de constantes
    public void Const()
    {
        
        if(token.getStokem().equals("const"))
        {
            avancarToken();            
            if(token.getStokem().equals("{"))
            {
                avancarToken();                
            }else
            {            
               erro.add(new ErroSintatico("Faltou { no const",this.errorLinha));
            }
                bloco_constante();
                System.err.println("aqui--------------)____"+token.getStokem());
                if(token.getStokem().equals("}"))
                {                  
                   avancarToken();                                  
                }else
                {
                   erro.add(new ErroSintatico("Faltou } no const",this.errorLinha));                
                }           
        }             
    }
    
    
    public void bloco_constante()
    {
    
        if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool") )
        {              
            this.tipoconst=token.getStokem();
            tipo_primitivo();          
            lista_const();
            deslocamento=0;
        }else if(token.equals("}"))
        {                
                    
        }else if(token.getStokem().equals("Identifier"))
        {
            this.erro.add(new ErroSintatico("Faltou o tipo em constantes",this.errorLinha));
            while(true)
            {
                if(token.getStokem().equals("$"))
                {
                    break;
                }else if(token.getStokem().equals(";"))
                {
                    this.avancarToken();
                    bloco_constante();
                    break;
                }
                else if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
                {
                    bloco_constante();
                    break;
                }else if(token.getStokem().equals("}"))
                {
                    break;
                }else if(this._main && this.seguinte(controle).equals("void") && this.seguinte(controle+1).equals("main") )
                {
                    break;
                }  
                else if(token.getStokem().equals("class"))
                {
                    break;
                }else{     
                    this.avancarToken();
                }     
                
            }
        
        }  
        
    }
    
    
    public void lista_const()
    {
        if(token.getStokem().equals("Identifier"))
        {                        
            deslocamento++;
            System.out.println("cro______"+corrente.getNome());
            corrente.getLi().add(new Identificador_2(token.getToken().getToken(),token.getToken().getLinha(),this.tipoconst,deslocamento,corrente)); 
            avancarToken();             
            if(token.getStokem().equals("="))
            {                        
                 avancarToken();
                 atribuicao_costante();
                 aux_declaracao();
                            
            }else
            {
            this.erro.add(new ErroSintatico("Faltou o = na atribuição de constantes",this.errorLinha));
            while(true)
            {
                if(token.getStokem().equals("$"))
                {
                    break;
                }else if(token.getStokem().equals(","))
                {
                    aux_declaracao();
                    break;
                }                
                else if(token.getStokem().equals(";"))
                {
                    this.avancarToken();
                    bloco_constante();
                    break;
                }
                else if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
                {
                    bloco_constante();
                    break;
                }else if(token.getStokem().equals("}"))
                {
                    break;
                }else if(this._main && this.seguinte(controle).equals("void") && this.seguinte(controle+1).equals("main") )
                {
                    break;
                }  
                else if(token.getStokem().equals("class"))
                {
                    break;
                }else{     
                    this.avancarToken();
                }
            }            
            
            }      
        }else
        {
          this.erro.add(new ErroSintatico("Faltou o nome da variavel de constantes",this.errorLinha));
            while(true)
            {
                if(token.getStokem().equals("$"))
                {
                    break;
                }else if(token.getStokem().equals(","))
                {
                    aux_declaracao();
                    break;
                }                
                else if(token.getStokem().equals(";"))
                {
                    this.avancarToken();
                    bloco_constante();
                    break;
                }
                else if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
                {
                    bloco_constante();
                    break;
                }else if(token.getStokem().equals("}"))
                {
                    break;
                }else if(this._main && this.seguinte(controle).equals("void") && this.seguinte(controle+1).equals("main") )
                {
                    break;
                }  
                else if(token.getStokem().equals("class"))
                {
                    break;
                }else{     
                    this.avancarToken();
                }
            }
        
        }  
    }
    
    
    public void aux_declaracao()
    {
        if(token.getStokem().equals(";"))
            {
                         
                avancarToken();   
                bloco_constante();               
           }else if(token.getStokem().equals(","))
           {                    
                avancarToken();  
                lista_const();                
           }else
           {
               this.erro.add(new ErroSintatico("Faltou um ; ou , constantes",this.errorLinha));
            while(true)
            {
                if(token.getStokem().equals("$"))
                {
                    break;
                }
                else if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
                {
                    bloco_constante();
                    break;
                }else if(token.getStokem().equals("}"))
                {
                    break;
                }else if(this._main && this.seguinte(controle).equals("void") && this.seguinte(controle+1).equals("main") )
                {
                    break;
                }  
                else if(token.getStokem().equals("class"))
                {
                    break;
                }else{     
                    this.avancarToken();
                }     
                
            }
           
           }
        
    }
    
    
    public void atribuicao_costante()
    {
           if(token.getStokem().equals("Numero") || token.getStokem().equals("Cadeia") || token.getStokem().equals("Char1") || token.getStokem().equals("true") || token.getStokem().equals("false"))
           {              
                avancarToken();                       
           }
           else
           {
             this.erro.add(new ErroSintatico("Atribuição incorreta",this.errorLinha));
           }
    }
    
    
    public boolean Boolean()
   {
        if(token.getStokem().equals("true")|| token.getStokem().equals("false"))
        {
            return true;        
        }
        
        return false;   
   }
          
    
    //Declaração variveis, metodos e vetores
     public void declaracaoVariavel()
    {
        if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
        {
            tipo_primitivo();            
            if(token.getStokem().equals("Identifier"))
            {
                avancarToken();
                lista_variavel();
            }else
            {            
               // System.err.println("faltou id no objeto: "+this.errorLinha); 
            }
        
        }
    
    }
          
          
     public void id_declaracao()
   {
       if(token.getStokem().equals("Identifier") || token.getStokem().equals("float") || token.getStokem().equals("int") || token.getStokem().equals("char") || token.getStokem().equals("string") || token.getStokem().equals("bool"))
       {
           this.avancarToken();
           if(token.getStokem().equals("Identifier"))
           {
                this.avancarToken();
                comp_id();
           }    
       }else if(token.getStokem().equals("void"))
       {
           this.avancarToken();
           if(token.getStokem().equals("Identifier"))
           {
                this.avancarToken();
                if(token.getStokem().equals("("))
                {
                    this.avancarToken();
                    if(token.getStokem().equals(")"))
                    {
                        this.avancarToken();
                        if(token.getStokem().equals("{"))
                        {
                            this.avancarToken();
                            if(token.getStokem().equals("}"))
                            {
                                this.avancarToken();
                            
                            }else
                            {
                                this.conteudo_metodo();
                                if(token.getStokem().equals("}"))
                                {
                                    this.avancarToken();
                            
                                }
                                
                            
                            }
                        
                        }
                    }else
                    {
                        decl_parametros();
                        if(token.getStokem().equals(")"))
                        {
                            this.avancarToken();
                            if(token.getStokem().equals("{"))
                            {
                            this.avancarToken();
                            if(token.getStokem().equals("}"))
                            {
                                this.avancarToken();
                            
                            }else
                            {
                                this.conteudo_metodo();
                                if(token.getStokem().equals("}"))
                                {
                                    this.avancarToken();
                            
                                }
                                
                            
                            }
                        
                        }
                        
                        }
                        
                    }
                }
                
           }
       
       
       
       }
   
   
   }
         
     
     public void tipo()
   {
     if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
     {
        this.tipo_primitivo();     
     }else if(token.getStokem().equals("Identifier"))
     {
        avancarToken();     
     }
   
   
   }
          
     
    public void comp_id()
    {
        if(token.getStokem().equals("["))
        {
            this.avancarToken();
            if(token.getStokem().equals("Numero"))
            {
                this.avancarToken();
                if(token.getStokem().equals("]"))
                {
                    this.avancarToken();
                    this.lista_vetor2();                
                }
            }
            
        }else if(token.getStokem().equals(",") || token.getStokem().equals(";"))
        {
            this.lista_variavel();        
        }else if(token.getStokem().equals("("))
        {
            this.avancarToken();
            if(token.getStokem().equals(")"))
            {
                this.avancarToken();
                if(token.getStokem().equals("{"))
                {
                    this.avancarToken();
                    if(token.getStokem().equals("return"))
                    {
                        this.avancarToken();
                        retorno();
                        if(token.getStokem().equals("}"))
                        {
                            this.avancarToken();
                        }
                    }else
                    {
                        this.conteudo_metodo();
                        if(token.getStokem().equals("return"))
                        {
                            this.avancarToken();
                            retorno();
                            if(token.getStokem().equals("}"))
                            {
                                this.avancarToken();
                            }
                        }
                        
                    
                    
                    }
                
                }
            
            
            }else
            {
                decl_parametros();
                if(token.getStokem().equals(")"))
                {
                    this.avancarToken();          
                    if(token.getStokem().equals("{"))
                    {
                    this.avancarToken();
                    if(token.getStokem().equals("return"))
                    {
                        retorno();
                        if(token.getStokem().equals("}"))
                        {
                            this.avancarToken();
                        }
                    }else
                    {
                        this.conteudo_metodo();
                        if(token.getStokem().equals("return"))
                        {
                            retorno();
                            if(token.getStokem().equals("}"))
                            {
                                this.avancarToken();
                            }
                        }
                        
                    
                    
                    }
                
                    }
                
                }
            
            
            }
        }
    
    
    
    } 
        
    
    public void lista_variavel()
    {
            
            if(token.getStokem().equals(","))
            {
                avancarToken();
                if(token.getStokem().equals("Identifier"))
                {
                avancarToken();
                lista_variavel();
                }                
            }else if(token.getStokem().equals(";"))
            {
                avancarToken();                              
            }else
            {
                //System.err.println("faltou , ou ; na variaveil : "+this.errorLinha);
                
            }
    
    }
        
    
    public void tipo_primitivo()
    {
        if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
        {
            avancarToken();        
        }
    
    }
        
    
    //indice de declaração de vetor
    public void indice()
   {
    if(token.getStokem().equals("("))
    {
        indice();
        if(token.getStokem().equals(")"))
        {
            this.avancarToken();
        }
    }else if(token.getStokem().equals("Numero"))
    {
         this.avancarToken();
         if(this.operador_aritmetico()){
            comp_indice();
         }         
    }else if(this.operador_incremento() ||token.getStokem().equals("Identifier") )
    {
            id_indice();    
    }
   
   }
        
    
    public void comp_indice()
   {
       if(this.operador_aritmetico())
         {
           this.avancarToken();           
           exp_aritmetica();
        }
   
   
   }
        
    
    public void id_indice()
   {
    if(this.operador_incremento())
    {
            this.avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();
                if(this.operador_aritmetico()){
                    operador_indice();
                }
            }
          
    }else if(token.getStokem().equals("Identifier"))
    {
            this.avancarToken();
            if(token.getStokem().equals("(") || token.getStokem().equals(".") || token.getStokem().equals(".") || this.operador_incremento()){
            acesso_indice();
            }
            if(this.operador_aritmetico()){
                    operador_indice();
            }
    }
   }
        
    
     public void acesso_indice()
   {
        if(token.getStokem().equals("["))
       {
            this.avancarToken();
            indice();
            if(token.getStokem().equals("]"))
            {
             this.avancarToken();
            }
       
       }else if(token.getStokem().equals("("))
       {
            this.avancarToken();
            if(token.getStokem().equals("(") || token.getStokem().equals("Numero") || token.getStokem().equals("Cadeia") || token.getStokem().equals("Char1") || this.Boolean() || token.getStokem().equals("-") || this.operador_incremento() ||token.getStokem().equals("Identifier")){
                parametros();
            }            
            if(token.getStokem().equals(")"))
            {
             this.avancarToken();
            }
       }else if(token.getStokem().equals("."))
       {
            this.avancarToken(); 
            if(token.getStokem().equals("Identifier"))
            {
             this.avancarToken();
                if(token.getStokem().equals("("))
                {
                    chamada_metodo();
                }             
            }
       }else if(this.operador_incremento())
       {
            this.avancarToken();       
       }
   
   }
          
     
    public void operador_indice()
   {
        if(this.operador_aritmetico())
         {
           this.avancarToken();
           exp_aritmetica();
         }
   
   
   }
   
   
    //parametros declaração de função
     public void decl_parametros()
    {
        if(token.getStokem().equals("Identifier") || token.getStokem().equals("float") || token.getStokem().equals("int") || token.getStokem().equals("char") || token.getStokem().equals("string") || token.getStokem().equals("bool"))
        {
                this.avancarToken();
                if(token.getStokem().equals("Identifier"))
                {
                    this.avancarToken();
                    if(token.getStokem().equals("["))
                    {
                        this.var_vet();
                    }
                    if(token.getStokem().equals(","))
                    {
                        lista_parametros();
                    
                    }
                    
                    
                }
        }
    
    
    
    }
    
     
     public void lista_parametros()
    {
        if(token.getStokem().equals(","))
        {
            this.avancarToken();
            if(token.getStokem().equals("Identifier") || token.getStokem().equals("float") || token.getStokem().equals("int") || token.getStokem().equals("char") || token.getStokem().equals("string") || token.getStokem().equals("bool"))
            {
                this.avancarToken(); 
                if(token.getStokem().equals("Identifier"))
                {
                    this.avancarToken();
                    if(token.getStokem().equals("["))
                    {
                        this.var_vet();
                    }
                    if(token.getStokem().equals(","))
                    {
                        lista_parametros();
                    
                    }
                    
                    
                }
                
            }
    
    
        }
    }
     
     
     public void var_vet()
    {
        if(token.getStokem().equals("["))
        {
            this.avancarToken();
            if(token.getStokem().equals("Numero"))
            {
                this.avancarToken();
                if(token.getStokem().equals("]"))
                {
                    this.avancarToken();                
                }
                
            }
        
        }
    
    
    }
    
    
    //conteudo de metodo
     public void conteudo_metodo()
    {
        if(token.getStokem().equals("read")  || token.getStokem().equals("write") || token.getStokem().equals("Identifier") || token.getStokem().equals("new") ||token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool") || token.getStokem().equals("if") || token.getStokem().equals("while"))
        {
            comando();
            conteudo_metodo();        
        }else if(token.getStokem().equals("}"))
         {        
                                       
         }else
        {
           // System.err.println("Este tokem   " +token.getStokem()+ " nao esta correto: "+this.errorLinha); 
        
        }
    
    }
    
     
     public void comando()
   {
       System.err.println(token.getStokem()+"   "+token.getToken().getToken());
        if(token.getStokem().equals("read"))
        {            
            read();
            //System.out.println("read    "+token.getStokem()+"                read");
        }else if(token.getStokem().equals("write"))
        {
            write();
            //System.out.println("write    "+token.getStokem()+"                write");        
        }else if(token.getStokem().equals("new"))
        {
            inicializa_objeto();
            //System.out.println("new    "+token.getStokem()+"                new");        
        }else if(token.getStokem().equals("if"))
        {
            IF();
            //System.out.println("new    "+token.getStokem()+"                new");        
        } 
        else if(token.getStokem().equals("while"))
        {
            While();
            //System.out.println("new    "+token.getStokem()+"                new");        
        }else if(token.getStokem().equals("int")  || token.getStokem().equals("float") || token.getStokem().equals("string") || token.getStokem().equals("char") || token.getStokem().equals("bool"))
        {
                tipo();
                if(token.getStokem().equals("Identifier"))
                {
                    this.avancarToken();
                    this.id_decl();
                }
        }
        else if(token.getStokem().equals("Identifier"))
        {
              if(this.seguinte(controle).equals("(") || this.seguinte(controle).equals(".") || this.seguinte(controle).equals("=") || this.seguinte(controle).equals("["))
              {
                  this.avancarToken();
                  id_comando();
              }else{
                    this.tipo();
                    if(token.getStokem().equals("Identifier"))
                    {
                        this.avancarToken();
                        this.id_decl();
                    
                    }
              }
        
        } 
   }
     
     
     public void id_decl()
   {
        if(token.getStokem().equals("["))
        {
            this.avancarToken();
            if(token.getStokem().equals("Numero"))
            {
                this.avancarToken();
                if(token.getStokem().equals("]"))
                {
                     this.avancarToken();
                     lista_vetor2();
                }
            
            }
            
        }else if(token.getStokem().equals(",") || token.getStokem().equals(";"))
        {
            this.lista_variavel();        
        }
   
   
   }
     
     
     public void lista_vetor2()
    {
        if(token.getStokem().equals(";"))
        {
            this.avancarToken();
        }else if(token.getStokem().equals(","))
        {
          this.avancarToken();
          if(token.getStokem().equals("Identifier"))
          {
            this.avancarToken();
            if(token.getStokem().equals("["))
            {
                this.avancarToken();
                if(token.getStokem().equals("Numero"))
                {
                    this.avancarToken();
                if(token.getStokem().equals("]"))
                {
                    this.avancarToken();              
                    lista_vetor2();
                }
          
                }
          
            }
          
          }
        } 
    
    }
     
     
     public void id_comando()
   {
       if(token.getStokem().equals("("))
       {
           this.avancarToken();
           if(token.getStokem().equals(")"))
           {
                this.avancarToken();
                if(token.getStokem().equals(";"))
                {
                    this.avancarToken();                
                }
           }else
           {
             this.parametros();
             if(token.getStokem().equals(")"))
                {
                this.avancarToken();
                if(token.getStokem().equals(";"))
                {
                    this.avancarToken();                
                }
               }  
           
           }
           
       
       }else if(token.getStokem().equals("."))
       {
            this.avancarToken();  
            if(token.getStokem().equals("Identifier"))
                {
                    this.avancarToken(); 
                    acesso_objeto();
                    if(token.getStokem().equals(";"))
                    {
                        this.avancarToken();                
                    }
                }
       }else if(token.getStokem().equals("="))
       {
           this.avancarToken();
           this.atribuicao();
           if(token.getStokem().equals(";"))
             {
               this.avancarToken();                
             }
       
       }else if(token.getStokem().equals("["))
       {
            this.avancarToken();
            this.indice();
            if(token.getStokem().equals("]"))
             {
               this.avancarToken();   
               if(token.getStokem().equals("="))
                {
                    this.avancarToken();
                    this.atribuicao();
                    if(token.getStokem().equals(";"))
                    {
                        this.avancarToken();                
                      }
       
                }
             }
       
       }
   
   }
     
     
     public void acesso_objeto()
   {
       if(token.getStokem().equals("("))
       {
           this.avancarToken();
           if(token.getStokem().equals(")"))
           {
            this.avancarToken();           
           }else 
           {
                this.parametros();
                if(token.getStokem().equals(")"))
                {
                    this.avancarToken();           
                }
           
           }
           
       }else if(token.getStokem().equals("="))
       {
                this.avancarToken();
                this.atribuicao();       
       }
   }
     
     
     //atribuição     
   public void atribuicao()
   {
       
       if(token.getStokem().equals("("))
       {
           this.avancarToken();
           atribuicao();
           if(token.getStokem().equals(")"))
           {
                this.avancarToken();
                if(this.operador_relacional() || this.operador_aritmetico() || this.operador_igualdade() || this.operador_logico()){
                    operacao();
                }
           }
       }else if(this.operador_incremento() || token.getStokem().equals("Identifier"))
       {
            id_acesso();       
       }else if(token.getStokem().equals("Numero"))
       {
           this.avancarToken();
           if(this.operador_relacional() || this.operador_aritmetico() || this.operador_igualdade()){
                   operador_numero();
           }
          
       }else if(token.getStokem().equals("Char1"))
       {
           this.avancarToken();           
       }else if(token.getStokem().equals("Cadeia"))
       {
           this.avancarToken();           
       }else if(this.Boolean())
       {
           this.avancarToken();
           if(this.operador_igualdade() || this.operador_logico()){
                op_logico();
           }
       }else if(token.getStokem().equals("-"))
       {
           this.avancarToken();
           negativo();
       }   
   }
   
   
   public void operador_numero()
   {
       if(token.getStokem().equals(">")|| token.getStokem().equals("<") || token.getStokem().equals(">=") || token.getStokem().equals("<="))
       {
           this.avancarToken();
           this.exp_aritmetica();
           this.op_logico();       
       }else if(token.getStokem().equals("+") ||token.getStokem().equals("-") || token.getStokem().equals("*") || token.getStokem().equals("/"))
       {
           this.avancarToken();
           this.exp_aritmetica();
           this.exp_relacional_opcional();
                   
       }else if(token.getStokem().equals("==") ||token.getStokem().equals("!="))
        {
            avancarToken();
            this.exp_aritmetica();
            this.op_logico();
        }
   
   
   }
   
   
   public void negativo()
   {
       if(token.getStokem().equals("("))
       {
           this.avancarToken();
           negativo();
           if(token.getStokem().equals(")"))
           {
            this.avancarToken();           
           }
       
       }else if(token.getStokem().equals("++") || token.getStokem().equals("--") || token.getStokem().equals("Identifier"))
       {
           this.avancarToken();
           this.id_acesso();
       }else if(token.getStokem().equals("Numero"))
       {
           this.operador_numero();
       }
   
   }
   
   
   public void exp_relacional_opcional()
    {
        if(token.getStokem().equals(">")|| token.getStokem().equals("<") || token.getStokem().equals(">=") || token.getStokem().equals("<="))
        {
            this.avancarToken();
            this.exp_aritmetica();
            this.op_logico();        
        }
    
    
    }
   
     
    public void op_logico()
   {
       if(this.operador_igualdade())
       {
            this.avancarToken();
            this.exp_logica();
       }else if(this.operador_logico())
       {
            this.avancarToken();
            this.exp();
       
       }
   
   
   
   }
   
            
    public void retorno()
    {
        
     this.atribuicao();
     if(token.getStokem().equals(";"))
     {
         this.avancarToken();
     }
    
    
    }
    
    
    public void id_acesso()
    {
        if(token.getStokem().equals("++") || token.getStokem().equals("--"))
       {
            
            if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();
                this.operacao();            
            }
       }else if(token.getStokem().equals("Identifier"))
       {
           this.avancarToken();
           acesso();
           operacao();
       
       }
    
    }
    
    
    public void acesso()
    {
        if(token.getStokem().equals("["))
       {
            this.avancarToken();
            indice();
            if(token.getStokem().equals("]"))
            {
             this.avancarToken();
            }
       
       }else if(token.getStokem().equals("("))
       {
            this.avancarToken();            
            parametros();
            if(token.getStokem().equals(")"))
            {
             this.avancarToken();
            }
       }else if(token.getStokem().equals("."))
       {
            this.avancarToken(); 
            if(token.getStokem().equals("Identifier"))
            {
             this.avancarToken(); 
             chamada_metodo();
            }
       }else if(token.getStokem().equals("++") || token.getStokem().equals("--"))
       {
            this.avancarToken();      
       }
    
    }  
    
    
     public void operacao()
    {
     if(this.operador_relacional() || this.operador_aritmetico() || this.operador_logico() || this.operador_igualdade())
     {
        this.operador();
     
     }
    
    }
    
     
      public void chamada_metodo()
   {
       if(token.getStokem().equals("("))
       {
           this.avancarToken();
           if(token.getStokem().equals("(") || token.getStokem().equals("Numero") || token.getStokem().equals("Cadeia") || token.getStokem().equals("Char1") || this.Boolean() || token.getStokem().equals("-") || this.operador_incremento() ||token.getStokem().equals("Identifier")){
                this.parametros();
           }          
           if(token.getStokem().equals(")"))
           {
               this.avancarToken();               
           }
       
       }
   
   }
    
      
    public void operador()
    {
        if(this.operador_relacional())
        {                
             avancarToken();
             this.exp_aritmetica();
             if(this.operador_igualdade() || this.operador_logico()){
                this.op_logico();
             }
        }else if(this.operador_aritmetico())
        {
            avancarToken();
            exp_aritmetica();
            if(this.operador_relacional()){
            exp_relacional_opcional();
            }
        }else if(this.operador_igualdade())
        {
            avancarToken();
            this.atribuicao();
        }else if(this.operador_logico())
        {
            avancarToken();
            exp();
        }
    
    }
   
    
    public void parametros()
   {
     if(token.getStokem().equals("(") || token.getStokem().equals("Numero") || token.getStokem().equals("Cadeia") || token.getStokem().equals("Char1") || this.Boolean() || token.getStokem().equals("-") || this.operador_incremento() ||token.getStokem().equals("Identifier")){
                atribuicao();
                if(token.getStokem().equals(",")){
                    novo_parametro();  
                }
                
       }  
       
   }
   
    
   public void novo_parametro()
   {
        if(token.getStokem().equals(","))
        {
            this.avancarToken();
            parametros();
        }
   
   }
    
   //Inicialização de objetos-Recuperado erro: ok
   public void inicializa_objeto()
   {
       if(token.getStokem().equals("new"))
       {
            avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                avancarToken();
            
            }else
            {
                erro.add(new ErroSintatico("Faltou colocar o objeto que vai ser instanciado",this.errorLinha));                
            }
            
            if(token.getStokem().equals(";"))
            {
                avancarToken();             
            }else
            {
               erro.add(new ErroSintatico("Faltou colocar o ; no objeto que esta sendo instanciado",this.errorLinha));       
            }
            
        }
   
   }
    
   
   //Comandos
   
   //while
    public void While()
   {
        if(token.getStokem().equals("while"))
        {
            this.avancarToken();
            if(token.getStokem().equals("("))
            {
                this.avancarToken();
                this.exp();
                if(token.getStokem().equals(")"))
                {
                    this.avancarToken();
                    if(token.getStokem().equals("{"))
                    {
                        this.avancarToken();
                        if(!token.getStokem().equals("}"))
                        {
                            this.conteudo_metodo();                        
                        }
                        if(token.getStokem().equals("}"))
                        {
                            this.avancarToken();                        
                        }
                        
                    }
                }
            }
        
        }
   
   
   }
   
   //read
   public void read()
   {
        if(token.getStokem().equals("read"))
        {
            avancarToken();
            if(token.getStokem().equals("("))
            {
                avancarToken();
                if(token.getStokem().equals("Identifier"))
                {
                    avancarToken();
                    if(token.getStokem().equals(",")){
                        lista_read();
                        if(token.getStokem().equals(")"))
                        {
                            avancarToken();
                            if(token.getStokem().equals(";"))
                            {
                                avancarToken();                             
                            }              
                        }
                    }else if(token.getStokem().equals(")"))
                    {
                            avancarToken();
                          if(token.getStokem().equals(";"))
                            {
                                avancarToken();                             
                            }              
                    }else
                    {
                        System.err.println("faltou , ou ) no read: "+this.errorLinha);              
                    }
                }            
            }
        }
   
   }
   
   
   public void lista_read()
   {
       if(token.getStokem().equals(","))
        {
            avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                avancarToken();
                if(token.getStokem().equals(","))
                {
                    
                    lista_read();
                }
            
            }
        }
    
   
   
   }
   
   //write
   public void write()
   {
        if(token.getStokem().equals("write"))
        {
           avancarToken();
           if(token.getStokem().equals("("))
            {
                avancarToken();
            }else
           {
               this.erro.add(new ErroSintatico("Faltou ( no write",this.errorLinha));
           }
             parametros_write();  
             if(token.getStokem().equals(")"))
             {
                avancarToken();
             }else
             {
             this.erro.add(new ErroSintatico("Faltou ( no write",this.errorLinha));             
             }   
              if(token.getStokem().equals(";"))
              {
                    avancarToken();                           
              }else
              {
                  this.erro.add(new ErroSintatico("Faltou ; no write",this.errorLinha));       
              }        
        }    
   
   }
   
   
   public void parametros_write()
   {
             
            imprimiveis();
            if(token.getStokem().equals(","))
            {
                novo_parametro_write();
            }
        
   }
   
   
   public void novo_parametro_write()
   {
          if(token.getStokem().equals(","))
            {
               this.avancarToken();
               parametros_write();
            }
   
   }
   
   
   public void imprimiveis()
   {
       
        if(token.getStokem().equals("("))
        {
            avancarToken();
            imprimiveis();
            if(token.getStokem().equals(")"))
                {                      
                    avancarToken();       
                }           
        }else if(token.getStokem().equals("Identifier"))
        {
              
            avancarToken();
            if(this.operador_aritmetico())
            {
                op_write();
            }
            
        }else if(token.getStokem().equals("Numero"))
        {
              
            avancarToken();
            if(this.operador_aritmetico())
            {
                op_write();
            }
        }
        
   }
   
   
   public void op_write()
   {
            if(this.operador_aritmetico())
            {
                this.avancarToken();
                exp_aritmetica();
            } 
   }
   
   
   //if
   public void IF()
   {
      
     if(token.getStokem().equals("if"))
     {
          avancarToken();    
         if(token.getStokem().equals("("))
         {
            avancarToken();
            exp();
            if(token.getStokem().equals(")"))
                {
                    avancarToken();                    
                    if(token.getStokem().equals("{"))
                    {
                        avancarToken();
                        if(!token.getStokem().equals("}")){
                            conteudo_metodo();
                        }
                        if(token.getStokem().equals("}"))
                        {
                            avancarToken();
                            if(token.getStokem().equals("else")){
                            complemento_if();
                            }
                        }
                    
                    }
                }            
         }
     
     }
   
   }
   
   
    public void complemento_if()
   {
        if(token.getStokem().equals("else"))
        {
            avancarToken();
            if(token.getStokem().equals("{"))
            {
                avancarToken();
                if(!token.getStokem().equals("}"))
                {
                     conteudo_metodo(); 
                }                              
                if(token.getStokem().equals("}"))
                 {
                     avancarToken();                                    
                 }                          
           }
       
        }
   
   }
   
    
    //Expressões lógicas
     public void exp()
   {        
        if(token.getStokem().equals("("))
        {
            
            this.avancarToken();
            exp_logica();
           
            if(token.getStokem().equals(")"))
            {
                         
                this.avancarToken();
                if(this.operador_logico() || this.operador_igualdade() || this.operador_relacional() || this.operador_aritmetico())
                {
                    complemento_exp_logica();
                }
               
             }
        }else if(operador_incremento())
        {
            this.avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();
                if(token.getStokem().equals("[") || token.getStokem().equals("(") || token.getStokem().equals(".")){
                    this.id_exp();
                }
                if(this.operador_aritmetico() || this.operador_logico() || this.operador_igualdade() || this.operador_relacional()){
                    this.complemento_aritmetico1();
                }
            }
         
        }else if(token.getStokem().equals("Identifier"))
        {
            this.avancarToken();
            if(this.operador_incremento() || token.getStokem().equals("[") || token.getStokem().equals("(") || token.getStokem().equals(".")){
                this.id_exp_arit();
            }
            if(this.operador_aritmetico() || this.operador_logico() || this.operador_igualdade() || this.operador_relacional()){
                this.complemento_aritmetico1();
            }
        
        }else if(token.getStokem().equals("Numero"))
        {
            
            this.avancarToken();
            if(this.operador_aritmetico()){
            complemento_aritmetico();
            }
            op_relacional();
        
        }else if(Boolean())
        {
            this.avancarToken();  
            if(operador_logico() || operador_igualdade()){
                complemento_logico();
            }
        }
   }
     
     
    public void complemento_aritmetico1()
   {
       if(this.operador_aritmetico())
       {
           this.avancarToken();
           this.fator_aritmetico();          
           op_id_relacional();           
       }else if(this.operador_logico() || this.operador_relacional() || this.operador_igualdade())
       {
           this.op_id_logico();                
       }
   }
    
    
    public void exp_logica()
   {
      
       if(token.getStokem().equals("("))
        {
            
            this.avancarToken();
            exp_logica();
           
            if(token.getStokem().equals(")"))
            {
                         
                this.avancarToken();
                if(this.operador_logico() || this.operador_igualdade() || this.operador_relacional() || this.operador_aritmetico())
                {
                    complemento_exp_logica();
                }
               
             }
       }else if(token.getStokem().equals("Numero"))
       {
            
            avancarToken();  
            if(this.operador_aritmetico())
            {
               complemento_aritmetico();
            }
            if(this.operador_logico() || this.operador_relacional() || this.operador_igualdade()){
                co_op_relacional();
            }
       }else if(token.getStokem().equals("Identifier"))
       {
           this.avancarToken();
            if(this.operador_incremento() || token.getStokem().equals("[") || token.getStokem().equals("(") || token.getStokem().equals(".")){
                this.id_exp_arit();
            }           
            if(this.operador_aritmetico())
            {
               complemento_aritmetico();
            }
           if(this.operador_logico() || this.operador_relacional() || this.operador_igualdade())
           {
                this.op_id_logico();                
           }
           
       }else if(this.operador_incremento())
       {
           this.avancarToken();
           if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();
                if(token.getStokem().equals("[") || token.getStokem().equals("(") || token.getStokem().equals(".")){
                    this.id_exp();
                }
                if(this.operador_aritmetico()){
                    complemento_aritmetico();
                }
                if(this.operador_logico() || this.operador_relacional() || this.operador_igualdade())
                {
                    this.op_id_logico();                
                }
                
            }
       
       }else if(Boolean())
        {
            this.avancarToken();  
            if(operador_logico() || operador_igualdade()){
                complemento_logico();
            }
        }   
      
   }
    
     
    public void co_op_relacional()
   {
       
       if(this.operador_igualdade() || this.operador_relacional())
       {
          this.op_relacional();
                 
       }else if(this.operador_logico())
       {
           this.avancarToken();
           this.exp();
       }   
   
   }
    
    
   public void complemento_exp_logica()
   {
   //System.err.println(token.getStokem()+ " nao esta correto___11");
       if(token.getStokem().equals("==") ||token.getStokem().equals("!="))
       {
            this.avancarToken();
            this.exp_logica();
       
       }else if(token.getStokem().equals("&&") ||token.getStokem().equals("||"))
       {
            this.avancarToken();
            this.exp();       
       }else if(token.getStokem().equals(">")|| token.getStokem().equals("<") || token.getStokem().equals(">=") || token.getStokem().equals("<="))
       {
           this.avancarToken();
           this.fator_aritmetico();
           this.complemento_logico();       
       }else if(token.getStokem().equals("+") ||token.getStokem().equals("-") || token.getStokem().equals("*") || token.getStokem().equals("/"))
       {
           this.avancarToken();
           this.fator_aritmetico();
           this.complemento_logico();  
                   
       } 
   
   
   }
    
   
     public void id_exp()
   {
       //System.err.println(token.getStokem()+ " nao esta correto___10");
       if(token.getStokem().equals("["))
       {
            this.avancarToken();
            indice();
            if(token.getStokem().equals("]"))
            {
             this.avancarToken();
            }
       
       }else if(token.getStokem().equals("("))
       {
            this.avancarToken();
            if(token.getStokem().equals("(") || token.getStokem().equals("Numero") || token.getStokem().equals("Cadeia") || token.getStokem().equals("Char1") || this.Boolean() || token.getStokem().equals("-") || this.operador_incremento() ||token.getStokem().equals("Identifier")){
                parametros();
            }
            if(token.getStokem().equals(")"))
            {
             this.avancarToken();
            }
       }else if(token.getStokem().equals("."))
       {
            this.avancarToken(); 
            if(token.getStokem().equals("Identifier"))
            {
             this.avancarToken(); 
                if(token.getStokem().equals("("))
                {
                    chamada_metodo();
                }
             
            }
       }
   }
   
     public void op_id_logico()
   {
       if(this.operador_logico())
       {
           this.avancarToken();
           this.exp();       
       }else if(this.operador_relacional() || this.operador_igualdade())
       {
           this.op_id_relacional();      
       }
   
   } 
    
     
    public void complemento_logico()
   {
       if(token.getStokem().equals("==") || token.getStokem().equals("!="))
       {
           this.avancarToken();
           this.exp_logica();       
       }else if(token.getStokem().equals("&&") || token.getStokem().equals("||"))
       {
           this.avancarToken();
           this.exp();       
       }
   
   
   
   }
     
    
    public boolean operador_logico()
   {
       if(token.getStokem().equals("&&") || token.getStokem().equals("||"))
       {
       
           return true;
           
       }
        return false;
   
   }
    
    
    public boolean operador_igualdade()
   {
       if(token.getStokem().equals("==") || token.getStokem().equals("!="))
       {
       
           return true;
           
       }
        return false;
   
   }
    
    
    //Expressões relacionais
     public void op_relacional()
   {
       
       if(this.operador_relacional())
       {
            this.avancarToken();
            this.exp_aritmetica();
            if(this.operador_igualdade() || this.operador_logico()){
                this.op_logico();
            }
       }else if(this.operador_igualdade())
       {
           this.avancarToken();         
           this.exp_aritmetica();
           if(this.operador_igualdade() || this.operador_logico()){
                this.op_logico();
            }      
       }
   
   }
    
    
    public void op_id_relacional()
   {
       if(this.operador_relacional())
       {
           this.avancarToken();
           this.exp_aritmetica();
           if(this.operador_igualdade() || this.operador_logico()){
           this.op_logico();
           }
       
       }else if(this.operador_igualdade())
       {
           this.avancarToken();
           this.exp_logica();
       }
   
   
   }
   
  
     public boolean operador_relacional()
   {
        if(token.getStokem().equals(">") || token.getStokem().equals(">=") || token.getStokem().equals("<=") || token.getStokem().equals("<"))
        {
            return true;
        }
        return false;
   }
   
   
   //Expressões aritméticas
      public void exp_aritmetica()
   {
   //System.out.println(1);
            if(token.getStokem().equals("-"))
            {
                this.avancarToken();
                exp_aritmetica();
            }else if(token.getStokem().equals("Numero") || token.getStokem().equals("(") || this.operador_incremento() || token.getStokem().equals("Identifier"))
            {
                this.fator_aritmetico();
            }
   
   }
          
        
     public void fator_aritmetico()
   {
      
       if(token.getStokem().equals("Numero"))
       {          
           this.avancarToken();
           if(this.operador_aritmetico()){
           complemento_aritmetico(); 
           }
       }else if(token.getStokem().equals("("))
       {
                this.avancarToken();
                fator_aritmetico();
                if(token.getStokem().equals(")"))
                {                
                    this.avancarToken();
                    
                    if(this.operador_aritmetico()){
                    complemento_aritmetico(); 
                    }             
                }
       
       }else if(this.operador_incremento() || token.getStokem().equals("Identifier"))
       {              
                id_aritmetico();
                if(this.operador_aritmetico()){
                    complemento_aritmetico(); 
                  }   
       }
  
   
   }
   
     
   public void id_aritmetico()
   {
       // System.err.println(token.getStokem()+ " nao esta correto___8");
       if(this.operador_incremento())
        {
            this.avancarToken();
            if(token.getStokem().equals("Identifier"))
            {
                this.avancarToken();            
            }
        
        }else if(token.getStokem().equals("Identifier"))
        {
            this.avancarToken();
            if(this.operador_incremento() || token.getStokem().equals("(") || token.getStokem().equals("[") || token.getStokem().equals(".")){
                    id_exp_arit();
            }
        }  
   }
   
      
   public void id_exp_arit()
   {
       //System.err.println(token.getStokem()+ " nao esta correto___9");
       if(token.getStokem().equals("[") || token.getStokem().equals("(") || token.getStokem().equals("."))
       {
            id_exp();
       
       }else if(this.operador_incremento())        
       {
            this.avancarToken(); 
       }   
   }
    
   
    public void complemento_aritmetico()
   {      
       if(this.operador_aritmetico())
       {
            
            avancarToken();            
            fator_aritmetico();
       }
   }
    
     
   public boolean operador_aritmetico()
   {
       if(token.getStokem().equals("+") || token.getStokem().equals("-") || token.getStokem().equals("*")|| token.getStokem().equals("/"))
       {
            return true;       
       }
       return false;
   }
   
   
   public boolean operador_incremento()
   {
       if(token.getStokem().equals("++") || token.getStokem().equals("--"))
       {
       
           return true;
           
       }
        return false;
   
   }
   
     
}
