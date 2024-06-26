/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package BLACKJACK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Servidor {

    private static ArrayList<Juego> resultados = new ArrayList<>();
    private static int numjug;
    public static List<Socket> clientSockets = new ArrayList<>();

    public static ArrayList<Juego> getResultados() {
        return resultados;
    }

    public static void setResultados(ArrayList<Juego> resultados) {
        Servidor.resultados = resultados;
    }

    public static int getNumjug() {
        return numjug;
    }

    public static void setNumjug(int numjug) {
        Servidor.numjug = numjug;
    }

    public synchronized static void AddResultado(Juego mano) {
        resultados.add(mano);
    }

    public static void main(String[] args) {

        BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
        Baraja baraja = new Baraja();
        System.out.println("Servidor BlackJack en ejecución");

        numjug = 0;
        System.out.println("Introduzca el número de jugadores 2-8:");
        while (true) { //Pedimos al usuario determinar el num de jugadores, que será el numero de sockets y clientes
            try {
                numjug = Integer.parseInt(teclado.readLine());
                if (numjug >= 2 && numjug <= 8) {
                    break;
                }
                System.out.println("Introduzca numero de jugadores valido entre 2 y 8");
            } catch (NumberFormatException | IOException e) {
                System.out.println("Introduzca numero de jugadores valido entre 2 y 8");
            }
        }

        try (ServerSocket listener = new ServerSocket(55557)) {//Puerto de escucha

            ExecutorService pool = Executors.newFixedThreadPool(numjug); //Para ejecutar todos los hilos (tantos como jugadores)

            for (int i = 0; i < numjug; i++) { //Vamos aceptando a los jugadores que se conectan.
                System.out.println("Esperando jugadores..."); //No empieza la partida hasta estar todos conectados
                Socket clientSocket = listener.accept();
                clientSockets.add(clientSocket);
                System.out.println("Jugador " + (i + 1) + " conectado, " + (numjug - i - 1) + " restantes");
            }
            System.out.println("La partida va a comenzar");
            for (int i = 0; i < numjug; i++) {
                pool.execute(new Handler(clientSockets.get(i), baraja, i + 1));
            }//cuando se han conectado todos, lanzamos los hilos y comienza la partida
            pool.shutdown();

        } catch (IOException e) {
            System.err.println("Error de escritura: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
