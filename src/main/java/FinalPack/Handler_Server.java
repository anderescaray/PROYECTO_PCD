/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler_Server implements Runnable {

        private ObjectOutputStream output; // Flujo de salida a la cliente
        private ObjectInputStream input;
        private Socket conexion; // Conexión al cliente
        private int conexionID;

        public Handler_Server(int id) {
            conexionID = id;
        }

        public void run() {
             
        } 

        

        

        

         
         
    }
