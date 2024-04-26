/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        try {
            System.out.println("Conectado, esperando al resto de jugadores...");
            Socket socket = new Socket(SERVER_ADRESS, SERVER_PORT);
            BufferedReader entradaSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Atento, comienza la partida");
            System.out.println("");
            Juego mano = (Juego) ois.readObject();
            System.out.println("MANO: " + mano);
            System.out.println("VALOR: " + mano.getPuntuacion());
            String decision = "";
            while (true) {
                if (decision.equals("B")) {
                    System.out.println("Te has plantado con una puntuacion de "+mano.getPuntuacion());
                    break;
                } else {
                    System.out.println("Escoja una opcion:");
                    System.out.println("A: Pedir otra carta");
                    System.out.println("B: Plantarse");
                    decision = teclado.readLine().toUpperCase();
                    //AVISAR AL HANDLER (no hace falta mandarle A)***
                    
                    //ESPERAR RESPUESTA HANDLER
                    Juego manon=(Juego)ois.readObject();
                    System.out.println("MANO: "+manon);
                    System.out.println("VALOR: " + mano.getPuntuacion());
                }

            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }

    }
}
