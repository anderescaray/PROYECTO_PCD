/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author alumno
 */
public class Dealer {
    private Juego dcartas;
    private int contador = 1; // counter of number of connections
    private Carta dcarta1, dcarta2;
    private ArrayList<Juego> jugadores;
    private int jugadoresRestantes;
    private boolean redondear = true;
    private ExecutorService executor; // will run players
    private ServerSocket server; // server socket
}
