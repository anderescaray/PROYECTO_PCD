/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Handler implements Runnable {

    private Socket conexion;
    private Baraja baraja;

    public Handler(Socket socket, Baraja baraja) {
        conexion = socket;
        this.baraja = baraja;
    }

    public void run() {
        try {
            //Informamos de la mano al jugador
            ObjectOutputStream oos = new ObjectOutputStream(conexion.getOutputStream());
            Carta c1 = baraja.sacarCarta();
            Carta c2 = baraja.sacarCarta();
            Juego mano = new Juego(c1, c2);
            oos.writeObject(mano);

            //Ahora esperamos la respuesta del jugador
            while (true) {
                //RECIBIR AVISO DEL JUGADOR***
                mano.pedirCarta(baraja.sacarCarta());
                oos.writeObject((mano));
            }

        } catch (IOException e) {
            System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

}
