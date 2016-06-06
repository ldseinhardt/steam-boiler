package controlecaldeiravapor;

/*
    Controlar a quantidade de água em uma caldeira a vapor.

    O sistema físico é composto das seguintes unidades: 
    - uma caldeira a vapor
    - um sensor que mede a quantidade de água na caldeira
    - quatro bombas que fornecem água para a caldeira 
    - quatro sensores que supervisionam as bombas 
    - um sensor que mede a quantidade de vapor que sai da caldeira.

    O modelo deve considerar a implementação do controle como
    também do sistema físico para que a simulação do sistema possa ser realizada.
*/

public class ControleCaldeiraVapor {
    
    private final int NUMERO_BOMBAS = 4;
    
    private final CaldeiraVapor caldeira;
    
    private final Bomba bombas[];
    
    public ControleCaldeiraVapor() {
        caldeira = new CaldeiraVapor();
        
        bombas = new Bomba[NUMERO_BOMBAS];
        
        for (int i = 0; i < NUMERO_BOMBAS; i++) {
            bombas[i] = new Bomba();
        }
        
        while (true) {
            for (int i = 0; i < NUMERO_BOMBAS; i++) {
                caldeira.addAgua(bombas[i].getAgua());                
                System.out.println(caldeira.getNivelAgua());
            }
            
            if (caldeira.getNivelAgua() >= caldeira.getLimiteAgua()) {          
                System.out.println("Caldeira no limite");
                break;                
            }
        }
    }
            
    public static void main(String[] args) {
        new ControleCaldeiraVapor();
    }
    
}