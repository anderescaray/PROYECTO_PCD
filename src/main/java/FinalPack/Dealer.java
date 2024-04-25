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
    private Handler_Server[] sockServer; // Array de objetos para hacerlos hilos
    
    
    
    
    
    private void DealerGo() {
            dcartas = new Juego(dcarta1, dcarta2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println("Capturada InterruptedException: " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            }
            // Hacemos que el dealer se plante si tiene menos de 16 (se podría cambiar a lo que queramos)
            if (dcartas.getPuntuacion() < 16) {
                while (dcartas.getPuntuacion() < 16) {
                    String carta1 = nuevaBaraja.repartirCarta();
                    dcartas.pedirCarta(carta1);
                    displayMessage("El dealer golpea..." + carta1 + "\n" + "Total:" + dcartas.getPuntuacion() + "\n");
                // El dealer coge otra carta y mostramos lo que le ha tocado
                }
            }
            if (dcartas.comprobarSobrepasada()) {
                displayMessage("¡El dealer se ha sobrepasado!");
            } else {
                displayMessage("Puntuación del dealer " + dcartas.getPuntuacion());
            }

            resultados();
            // Mostramos los resultados con la función que hemos creado
        }

        private void tomarCarta() {

            String nextc = nuevaBaraja.repartirCarta();
            sendData(nextc);
            jugadores.get(this.conexionID - 1).pedirCarta(nextc);
            sendData("Tu puntuación: " + jugadores.get(this.conexionID - 1).getPuntuacion());
            // Si un jugador se sobrepasa, se lo informamos y lo quitamos de los jugadores restantes
            if (jugadores.get(this.conexionID - 1).comprobarSobrepasada()) {			
                sendData("¡Sobrepasado!\n");
                jugadoresRestantes--;
                if (jugadoresRestantes == 0) {
                    DealerGo();
                }
            }

        }

        private void ComprobarFinalizado() {

            if (jugadoresRestantes == 0) {

                DealerGo();
            }
        }
    
}
