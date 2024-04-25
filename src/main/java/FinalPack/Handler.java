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

        public Handler(Socket socket) {
            conexion = socket;
        }

        public void run() {
             
        } 

        

        

        

         
         
    }
