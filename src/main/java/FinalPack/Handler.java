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
            //Informamos de la mano al jugador
            oos = new ObjectOutputStream(conexion.getOutputStream());
            ois = new ObjectInputStream(conexion.getInputStream());
            Carta c1 = baraja.sacarCarta();
            Carta c2 = baraja.sacarCarta();
            Juego mano = new Juego(c1, c2);
            oos.writeObject(mano);
            oos.reset();

            //Ahora esperamos la respuesta del jugador
            BufferedReader signalReader = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String signal;
            while (((signal = signalReader.readLine()) != null)) {
                //String signal = signalReader.readLine();
                if (signal.equalsIgnoreCase("B")) {
                    System.out.println("El jugador " + id + "  se ha plantado con " + mano + " y puntuación " + mano.getPuntuacion());
                    break;
                } else if (signal.equalsIgnoreCase("A")) {
                    System.out.println("MANO INICIAL " + mano + " con puntuación " + mano.getPuntuacion());
                    mano.pedirCarta(baraja.sacarCarta());
                    oos.writeObject(mano);
                    oos.reset();
                    System.out.println("Nueva mano " + mano + " con puntuación " + mano.getPuntuacion());
                    if (mano.comprobarSobrepasada()) {
                        System.out.println("El jugador " + id + " ha superado 21 puntos y pierde");
                        break;
                    } else if (mano.getPuntuacion() == 21) {
                        System.out.println("El jugador " + id + "  tiene Blackjack " + mano);
                        break;
                    }
                } else {
                    System.out.println("El jugador " + id + "  tiene Blackjack " + mano);
                    break;
                }

                //mano.pedirCarta(baraja.sacarCarta());
                //oos.writeObject(mano);
                //oos.reset();
            }
            Servidor.AddResultado(mano);
            while (resultados.size() != Servidor.getNumjug()) {
                System.out.println("Esperando a que los demas jugadores acaben la partida");
                Thread.sleep(10000);
            }
            Juego ganador = calcularGanador();
            //System.out.println("El ganador es el jugador con "+ganador+" con puntuación "+ganador.getPuntuacion());
            oos.writeObject(ganador);

            if (ganador.equals(mano)) {
                System.out.println("El ganador es el jugador " + id + "  con " + ganador + " con puntuación " + ganador.getPuntuacion());

            }
            //Juego ganador = (Juego)ois.readObject();
            //System.out.println("El ganador es el jugador con la mano "+  ganador+" con puntuacion "+ganador.getPuntuacion());

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
            /*} finally {
            try {
                ois = new ObjectInputStream(conexion.getInputStream()) ;
                Juego mano = (Juego) ois.readObject();
                System.out.println("La mano del "+Thread.currentThread().getName()+ " es "+ mano);
                conexion.close();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
                e.printStackTrace(System.err);

            }*/
        }
    }

    private Juego calcularGanador() {
        Juego ganador = resultados.get(0);
        for (Juego juego : resultados) {
            if (ganador.getPuntuacion() < juego.getPuntuacion() && juego.getPuntuacion() <= 21) {
                ganador = juego;
            }
        }
        return ganador;
    }

}
