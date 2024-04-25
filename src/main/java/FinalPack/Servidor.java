/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Servidor {

    public static void main(String[] args) {

        Baraja baraja = new Baraja();

        System.out.println("El jugador se ha conectado");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59002)) {

            Carta carta1 = baraja.sacarCarta();
            Carta carta2 = baraja.sacarCarta();
            Carta carta3 = baraja.sacarCarta();
            Carta carta4 = baraja.sacarCarta();
// aqui se pone las dos cartas a cada uno con un for del array de jugadores y en handler cada uno elije si se planta o golpea
            Juego juegojug1 = new Juego(carta1, carta2);
            Juego juegojug2 = new Juego(carta3, carta4);
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
