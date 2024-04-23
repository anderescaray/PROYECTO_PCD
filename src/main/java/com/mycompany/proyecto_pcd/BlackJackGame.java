/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_pcd;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author alumno
 */
public class BlackJackGame implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Baraja deck;
    private List<Carta> dealerHand;
    private List<Carta> playerHand;

    public BlackJackGame(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("IOException capturada: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
        deck = new Baraja();
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            startGame();
            playGame();
            endGame();
        } catch (IOException e) {
            System.err.println("IOException capturada: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException capturada: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("IOException capturada: " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    public void startGame() throws IOException {
        sendMessage("Bienvenido al juego de Blackjack.");
        // Repartir cartas iniciales
        dealInitialCards();
        // Enviar cartas iniciales al cliente
        sendHands();
    }

    public void playGame() throws IOException, ClassNotFoundException {
        // Lógica para recibir las acciones del jugador y manejarlas
        // Puedes implementar aquí la lógica para hit, stand, double down, etc.
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Sus cartas son: ");
            int puntuacion = 0;
            for (Carta carta : playerHand) {
                System.out.println(carta);
                puntuacion += Integer.parseInt(carta.getValor());
            }
            menuJuego();
            String opcion = scanner.nextLine();
            try {
                int numOpcion = Integer.parseInt(opcion);
                if (numOpcion == 1) {
                    System.out.println("Su puntuación final es: " + puntuacion);
                    break;
                } else if (numOpcion == 2) {
                    Carta carta = deck.sacarCarta();
                    playerHand.add(carta);
                    puntuacion += Integer.parseInt(carta.getValor());
                    System.out.println("La carta que ha cogido es: " + carta);
                    System.out.println("Su puntuación actual es de " + puntuacion);
                }
            } catch (NumberFormatException e) {
                System.err.println("NumberFormatException capturada: " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            }

        }

    }

    public void menuJuego() {
        System.out.println("Elija una opción: ");
        System.out.println("1. Plantarse");
        System.out.println("2. Pedir otra carta");
    }

    public void endGame() throws IOException {
        // Lógica para determinar el resultado del juego y enviarlo al cliente
    }

    public void dealInitialCards() {
        dealerHand.clear();
        playerHand.clear();
        for (int i = 0; i < 2; i++) {
            dealerHand.add(deck.sacarCarta());
            playerHand.add(deck.sacarCarta());
        }
    }

    public void sendHands() throws IOException {
        outputStream.writeObject(playerHand);
        outputStream.writeObject(dealerHand.get(0)); // Solo enviar la primera carta del crupier
    }

    public void sendMessage(String message) throws IOException {
        outputStream.writeObject(message);
    }
}
