/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Servidor {

    public static void main(String[] args) {

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        Baraja baraja = new Baraja();
        System.out.println("Servidor BlackJack en ejecución");
        
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket listener = new ServerSocket(55555)) {

            System.out.println("Introduzca el numero de jugadores 2-8:");
            int numjug = Integer.parseInt(teclado.readLine());//PONER ESTO MEJOR, Y EVITAR QUE META UNA LETRA EN VEZ DE UN NUMERO
            for (int i = 0; i < numjug; i++) {
                Socket socket = listener.accept();
                System.out.println("Jugador "+(i+1)+" conectado, "+(numjug-i-1)+" restantes");
                //Thread jug=new Thread(new Handler(socket, baraja));
                //jug.start();
                pool.execute(new Handler(socket, baraja));
                
            }

            /*
            
       
        Juego[] jugadores = new Juego[numjug];

        Carta carta1;
        Carta carta2;

        for (int i = 0; i < jugadores.length; i++) {
            carta1 = baraja.sacarCarta();
            carta2 = baraja.sacarCarta();
            jugadores[i] = new Juego(carta1, carta2);
        }

        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(59002)) {

            Socket socket = listener.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            // Enviar los datos del juego al cliente
            out.writeObject(jugadores);

            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
             */
            //socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
