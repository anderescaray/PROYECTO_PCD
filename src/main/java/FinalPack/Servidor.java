/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Servidor BlackJack en ejecucion");
        System.out.println("Esperando Jugadores: 2 restantes");
        ExecutorService pool=new Executors.newFixedThreadPool(400);
    }
    
}
