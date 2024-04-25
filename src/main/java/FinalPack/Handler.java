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

        private ObjectOutputStream oos; // Flujo de salida a la cliente
        private ObjectInputStream ois;
        private Scanner in;
        private PrintWriter out;
        private Socket conexion; // Conexión al cliente
        private int conexionID;
        private Baraja baraja;

        public Handler(Socket socket, Baraja baraja) {
            conexion = socket;
            this.baraja = baraja;
        }

        public void run() {
             try {
                ObjectOutputStream out = new ObjectOutputStream(conexion.getOutputStream());
                Carta carta1 = baraja.sacarCarta();
                Carta carta2 = baraja.sacarCarta();
                Juego juego = new Juego(carta1, carta2);
                
                // Enviar el juego al cliente
                out.writeObject(juego);
                
                // Cerrar la conexión con el cliente
                conexion.close();
            } catch (IOException e) {
                System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
                e.printStackTrace(System.err);
            }
        } 

        

        

        

         
         
    }
