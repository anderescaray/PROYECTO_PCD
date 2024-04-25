/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Handler implements Runnable {
        private Socket conexion;
        private Baraja baraja;

        public Handler(Socket socket, Baraja baraja) {
            conexion = socket;
            this.baraja = baraja;
        }

        public void run() {
             try {
                ObjectOutputStream oos = new ObjectOutputStream(conexion.getOutputStream());
                Carta c1 = baraja.sacarCarta();
                Carta c2 = baraja.sacarCarta();
                Juego mano = new Juego(c1, c2);
                
                // Enviar el juego al cliente
                oos.writeObject(mano);
                
                // Cerrar la conexión con el cliente
                conexion.close();
            } catch (IOException e) {
                System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        } 

        

        

        

         
         
    }
