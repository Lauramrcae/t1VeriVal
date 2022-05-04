package com.example;

public class main {
    
    public static void main( String[] args )
    {
        
        System.out.println("**CD");
        CentroDistribuicao cd = new CentroDistribuicao(250, 5000, 625, 625);
        cd.defineSituacao();
        System.out.println(cd.getSituacao());

    }
    
}
