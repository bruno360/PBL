/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TabeladeSimbolos;

/**
 *
 * @author bruno
 */
public class TabeladeSimbolos {
    private Bloco blocos=new Bloco();
    
    public void costruirTabela()
    {
        blocos.getLi().add(new Bloco("arquivo",blocos));
    }
    public void limparTabela()
    {
        blocos.getLi().clear();    
    }

    public Bloco getBlocos() {
        return blocos;
    }
    
    public void imprimir()
    {
        int i=0;
        if(!blocos.getLi().isEmpty()){
           // System.out.println(1);
            Bloco arquivo=(Bloco)blocos.getLi().getFirst();    
         //   System.out.println(arquivo.getLi().size()+"aqui");
            while(i<arquivo.getLi().size())
            {
                Object x=arquivo.getLi().get(i);
                
                if(x instanceof Bloco)
                {
                    aux_Imprimir((Bloco)x);
                }else
                {                    
                    Identificador_2 a=(Identificador_2)x;
                    System.out.println();
                    System.out.print(a.getNome()+" "+a.getLinha()+" "+a.getTipo()+" "+a.getDeslocamento());
                    int z=0;                    
                    System.out.print(" "+a.getPai().getNome());                        
                    
                }
                i++;
            }
        }
        
    }
    
    private void aux_Imprimir(Bloco x)
    {
        int i=0;
        //System.out.println("aqui4 "+x.getNome()+"   "+x.getLi().size());
        if(!x.getLi().isEmpty()){    
            //System.out.println("aqui5");
            while(i<x.getLi().size())
            {
                Object x2=x.getLi().get(i);
                
                if(x2 instanceof Bloco)
                {
                    aux_Imprimir((Bloco)x2);
                }else
                {
                    
                    Identificador_2 a=(Identificador_2)x2;
                    System.out.println();
                    System.out.print(a.getNome()+" "+a.getLinha()+" "+a.getTipo()+" "+a.getDeslocamento());
                    int z=0;                    
                    System.out.print(" "+a.getPai().getNome()+" pai do pai "+a.getPai().getPai().getNome());                        
                    
                }
                i++;
            }
        }
    
    }
    
    
    
    
}
