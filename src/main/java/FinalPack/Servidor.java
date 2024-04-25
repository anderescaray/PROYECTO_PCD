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

        try (ServerSocket listener = new ServerSocket(55555)) {

            System.out.println("Introduzca el numero de jugadores 2-8:");
            int numjug = Integer.parseInt(teclado.readLine());
            ExecutorService pool = Executors.newFixedThreadPool(numjug);

            Socket[] listaSockets = new Socket[numjug];
            for (int i = 0; i < numjug; i++) {
                listaSockets[i] = listener.accept();
                System.out.println("Jugador " + (i + 1) + " conectado, " + (numjug - i - 1) + " restantes");  
            }
            System.out.println("La partida va a comenzar");
            for(int i=0;i<numjug;i++){
                pool.execute(new Handler(listaSockets[i], baraja));
            }
            //Faltaria ver que pasa si se ejecutan mas de 8 jugadores (deberia funcionar bien, y solo usar los 8 primeros)
            

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

}
