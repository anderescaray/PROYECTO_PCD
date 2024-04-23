/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.proyecto_pcd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author alumno
 */
public class BlackJack_Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Servidor Blackjack esperando conexiones...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new BlackJackGame(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("IOException capturada: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
}
