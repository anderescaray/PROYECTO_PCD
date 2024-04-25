/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Servidor {

    public static void main(String[] args) {

        Baraja baraja = new Baraja();
        System.out.println("Servidor BlackJack en ejecución, esperando jugadores");
        
        
        //1. ESPERAR A QUE SE CONECTEN n<8 JUGADORES
        //CUANDO SE HAYAN CONECTADO 8 JUGADORES, O CUANDO ELIJAS TU (CON UN MENU), INICIAR PARTIDA
       
        int numjug=8;//AQUI HAY Q CONTAR EL NUM DE JUGADORES
        Juego[] jugadores = new Juego[numjug];

        
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59002)) {

            Carta carta1;
            Carta carta2;
            
            for(int i = 0;i<jugadores.length;i++){
                carta1=baraja.sacarCarta();
                carta2=baraja.sacarCarta();
                jugadores[i] = new Juego(carta1,carta2);
                //mandar al cliente
            }
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
