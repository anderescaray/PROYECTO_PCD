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
        
        List<Juego> jugadores = new ArrayList<>();
        jugadores.

        System.out.println("El jugador se ha conectado");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59002)) {

            Carta carta1;
            Carta carta2;
            
            for(int i = 0;i<jugadores.size();i++){
                carta1=baraja.sacarCarta();
                carta2=baraja.sacarCarta();
                jugadores[i] = new Juego(carta1,carta2);
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
