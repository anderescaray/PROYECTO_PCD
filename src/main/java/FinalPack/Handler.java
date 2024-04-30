/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Handler implements Runnable {

    private Socket conexion;
    private Baraja baraja;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int id;
    private static ArrayList<Juego> resultados = Servidor.getResultados();

    public Handler(Socket socket, Baraja baraja, int id) {
        conexion = socket;
        this.baraja = baraja;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(conexion.getOutputStream());
            ois = new ObjectInputStream(conexion.getInputStream());
            Carta c1 = baraja.sacarCarta();
            Carta c2 = baraja.sacarCarta();
            Juego mano = new Juego(c1, c2); //Esta es la primera mano de cada jugador, que iremos actualizando con los metodos de la clase Juego
            oos.writeObject(mano);//Informamos de la mano al jugador
            oos.reset();

            //Ahora esperamos la respuesta del jugador
            BufferedReader signalReader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String signal;
            while (((signal = signalReader.readLine()) != null)) { //Leemos la respuesta del jugador, hasta que hagamos un break o haya blackjack
                if (signal.equalsIgnoreCase("B")) {
                    System.out.println("JUGADOR " + id + ": Se ha plantado con: " + mano + " Y puntuación: " + mano.getPuntuacion());
                    break;
                } else if (signal.equalsIgnoreCase("A")) {
                    System.out.println("JUGADOR " + id + ": Mano inicial: " + mano + " Y puntuación: " + mano.getPuntuacion());
                    mano.pedirCarta(baraja.sacarCarta()); //Ha decidio pedir otra carta
                    oos.writeObject(mano); //Devolvemos dicha carta al jugador
                    oos.reset();
                    System.out.println("JUGADOR " + id + ": Nueva mano: " + mano + " Y puntuación: " + mano.getPuntuacion());
                    if (mano.comprobarSobrepasada()) { //despues de pedir otra carta, si se ha pasado pierde, y salimos del bucle
                        System.out.println("JUGADOR " + id + ": Ha superado 21 puntos y pierde");
                        break;
                    } else if (mano.getPuntuacion() == 21) {
                        System.out.println("JUGADOR " + id + ": Tiene Blackjack: " + mano);
                        break; //si tiene justo 21, tiene blackjack asique salimos del bucle también
                    }
                } else {//En caso de que tenga BlackJack en el primer reparto de cartas, no enviará A ni B, y entrara directamente aqui
                    System.out.println("JUGADOR " + id + ": Tiene Blackjack: " + mano);
                    break;
                }
            }
            
            //Por ultimo debemos determinar el ganador
            Servidor.AddResultado(mano); //añadimos la mano al array
            while (resultados.size() != Servidor.getNumjug()) {
                System.out.println("Esperando a que los demas jugadores acaben la partida");
                Thread.sleep(20000);
            }//Esperamos a que todos terminen, es decir que todos hayan añadido una mano al array
            Juego ganador = calcularGanador(); //determinamos el ganador 

            oos.writeObject(ganador); //devolvemos la mano ganadora a cada jugador

            if (ganador.equals(mano)) { //Mostramos el resultado final de la partida en el servidor
                if (ganador.getPuntuacion() > 21) {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("FIN DE LA PARTIDA");
                    System.out.println("NO HAY GANADOR");
                } else {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("FIN DE LA PARTIDA");
                    System.out.println("GANADOR: Jugador " + id);
                    System.out.println("Mano: " + ganador + " / Puntuación: " + ganador.getPuntuacion());
                }
            }


        } catch (IOException | InterruptedException e) {
            System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
                if (conexion != null) {
                    conexion.close();
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);
            } 
        }
    }

    //Mencionar, que en caso de empate, ganará el jugador que antes haya logrado la mano, ya que se valora como criterio de desempate conseguir la puntuacion con mayor rapidez.
    private Juego calcularGanador() {
        Juego ganador = resultados.get(0);
        for (Juego juego : resultados) {
            if (ganador.getPuntuacion() > 21) {//Para evitar que si el primero tiene mas de 21, sea finalmente el ganador
                ganador = juego;
            } else if (ganador.getPuntuacion() < juego.getPuntuacion() && juego.getPuntuacion() <= 21) {
                ganador = juego; //vamos comparando todos los juegos, y determinamos el ganador
            }
        }
        return ganador;
    }

}
