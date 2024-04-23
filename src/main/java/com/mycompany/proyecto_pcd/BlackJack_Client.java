/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.proyecto_pcd;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author alumno
 */
public class BlackJack_Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Conectado al servidor Blackjack.");

            
            Thread game = new Thread(new BlackJackGame(socket));
            game.start();
            
            socket.close();
        } catch (IOException e) {
            System.err.println("IOException capturada: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
}
