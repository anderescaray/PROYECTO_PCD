/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package FinalPack;

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
        try (Socket socket = new Socket(SERVER_ADRESS, SERVER_PORT); ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true); BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            //PrintWriter salidaSocket = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Atento, comienza la partida");
            System.out.println("");
//            Thread resultadoHilo = new Thread(new ReceptionResultado(socket));
//            resultadoHilo.start();

            Juego mano = (Juego) ois.readObject(); // Lee la mano que manda el handler
            System.out.println("MANO: " + mano);
            System.out.println("VALOR: " + mano.getPuntuacion());

            String decision;

            while (true) {

                if (mano.getPuntuacion() == 21) {
                    System.out.println("BlackJack");
                    decision = teclado.readLine().toUpperCase();
                    salidaSocket.println(decision);
                    break;
                }
                System.out.println("Escoja una opcion:");
                System.out.println("A: Pedir otra carta");
                System.out.println("B: Plantarse");
                decision = teclado.readLine().toUpperCase();

                salidaSocket.println(decision);

                if (decision.equals("B")) {
                    System.out.println("Te has plantado con una puntuacion de " + mano.getPuntuacion());
                    break;
                }
                mano = (Juego) ois.readObject();
                System.out.println("Mano actual " + mano);
                System.out.println("Puntuacion actual " + mano.getPuntuacion());

                if (mano.getPuntuacion() > 21) {
                    System.out.println("Has superdao 21 .Has perdido");
                    break;
                } else if (mano.getPuntuacion() == 21) {
                    System.out.println("BlackJack");
                    break;
                }

                /*else {
                    

                    if (!decision.equals("B")) {
                        //salidaSocket.println(decision);

                        Juego manon = (Juego) ois.readObject();
                        System.out.println("MANO: " + manon);
                        System.out.println("VALOR: " + manon.getPuntuacion());
                        mano = manon;
                        if (mano.getPuntuacion() > 21) {
                            System.out.println("Has pasado la puntuacion");
                            oos.writeObject(mano);
                            break;
                        } else if (mano.getPuntuacion() == 21) {
                            System.out.println("BLACKJACK");
                            oos.writeObject(mano);
                        }
                    }

                }*/
            }
            Juego resultado = (Juego)ois.readObject();
            if(mano.equals(resultado)){
                System.out.println("Has ganado");
                
            }else{
                System.out.println("Has perdido");
            }
//            oos.writeObject(mano);
//            Thread hiloResultados = new Thread(new ReceptionResultado(socket));
//            hiloResultados.start();

            //resultadoHilo.join();
//            Thread hilo = new Thread(new ReceptionResultado(socket));
//            hilo.run();
//            Juego resultado = (Juego) ois.readObject();
//            System.out.println(resultado);
//            if (resultado.equals(mano)) {
//                System.out.println("Has ganado");
//
//            } else {
//                System.out.println("Has perdido");
//            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);

        }
    }
}
