/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BLACKJACK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author alumno
 */
public class Jugador {

    private static final String SERVER_ADRESS = "127.0.0.1";
    private static final int SERVER_PORT = 55557;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Conectado, esperando al resto de jugadores...");
        try (Socket socket = new Socket(SERVER_ADRESS, SERVER_PORT); // Socket para conectar con el servidor
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); // Para leer objetos que envíe el servidor (desde handler)
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); // Para enviar objetos al servidor
                PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true); // Para enviar texto al servidor
                BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) // Para leer desde el teclado al usuario
        // Usamos try-with-resources para que los recursos se cierren solos al acabar el bloque try
        { 

            System.out.println("Atento, comienza la partida...");
            System.out.println("");

            // Leemos la mano que se envía desde Handler y mostramos su información
            Juego mano = (Juego) ois.readObject();
            System.out.println("MANO: " + mano);
            System.out.println("VALOR: " + mano.getPuntuacion());

            String decision;

            while (true) {
                if (mano.getPuntuacion() == 21) { // Si tiene blackjack en el primer reparto finalizamos el bucle
                    System.out.println("BLACKJACK");
                    salidaSocket.println("BLCKJCK");
                    break;
                }
                //Si no tiene blackjack, toma una decision constantemente.
                System.out.println("Escoja una opción:");
                System.out.println("A: Pedir otra carta");
                System.out.println("B: Plantarse");
                decision = teclado.readLine().toUpperCase();

            // El usuario elige una opción y enviamos al Handler la decisión
                salidaSocket.println(decision);

                if (decision.equals("B")) { // Si se ha plantado paramos el bucle
                    System.out.println("Te has plantado con una puntuación de " + mano.getPuntuacion());
                    System.out.println("Espera a los demás jugadores");
                    break;
                }
            // En caso de que haya pedido otra carta
            // leemos la mano modificada, con una carta más, enviada desde Handler 
                mano = (Juego) ois.readObject();
                System.out.println("");
                System.out.println("");
                System.out.println("Mano actual: " + mano);
                System.out.println("Puntuación actual: " + mano.getPuntuacion());

                // Con la nueva mano, si se ha sobrepasado o tiene blackjack cortamos el bucle
                // sino volverá a tener que decidir el jugador
                if (mano.comprobarSobrepasada()) { 
                    System.out.println("Has superado el límite y has perdido");
                    System.out.println("Espera a los demás jugadores");
                    break;
                } else if (mano.getPuntuacion() == 21) {
                    System.out.println("BLACKJACK");
                    System.out.println("Espera a los demás jugadores");
                    break;
                }
            }
            
            // Leemos la mano ganadora calculada en Handler   
            Juego resultado = (Juego) ois.readObject();
            if (mano.comprobarSobrepasada()) { // Por si todos los jugadores se sobrepasan, para evitar falsos ganadores
                System.out.println("DERROTA");
            } else if (mano.equals(resultado)) { // Si es la misma mano que la del jugador es que ha ganado
                System.out.println("VICTORIA");
            } else {
                System.out.println("DERROTA"); //Sino, la mano ganadora será la de otro jugador por tanto no habra ganado
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
