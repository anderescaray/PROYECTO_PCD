/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler_Server implements Runnable {

        private ServerSocket server;
        private ObjectOutputStream output; // Flujo de salida a la cliente
        private ObjectInputStream input;
        private Socket conexion; // Conexión al cliente
        private int conexionID;

        public Handler_Server(int id) {
            conexionID = id;
        }

        public void run() {
            try {
                try {
                    getStreams(); // Usamos los métodos que hemos definido
                    procesarConexion(); 

                } catch (EOFException e) {
                    displayMessage("\nLa conexion al servidor de " + conexionID + " ha terminado");
                } finally {
                    cerrarConexion();
                }
            } catch (IOException e) {
                System.err.println("Capturada IOException: " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            } 
        } 

        // Metodo para esperar una conexión, y cuando llegue mostramos la información
        private void esperarConexion() throws IOException {

            displayMessage("Esperando a la conexion" + conexionID + "\n");
            conexion = server.accept();             
            displayMessage("Conexion " + conexionID + " recibida desde: " + conexion.getInetAddress().getHostName());
        } 

        private void getStreams() throws IOException {
            
            output = new ObjectOutputStream(conexion.getOutputStream());
            output.flush(); 

            
            input = new ObjectInputStream(conexion.getInputStream());

            displayMessage("\nObtenidos los flujos de E/S\n");
        } 

        
        private void procesarConexion() throws IOException {
            String mensaje = "Conexion " + conexionID + " exitosa";
            sendData(mensaje); 

            // Procesamos los mensajes enviados desde el cliente
            do {
                try {
                    
                    if (mensaje.contains("golpear")) {
                        tomarCarta();
                    }
                    // Si se planta restamos 1 a los jugadores que quedan 
                    // Y utilizamos el método para ver si se ha acabado la partida
                    if (mensaje.contains("plantarse")) {
                        this.sendData("Por favor espere");
                        jugadoresRestantes--;
                        ComprobarFinalizado();
                    }

                    mensaje = (String) input.readObject(); // Leer el nuevo mensaje

                } 
                catch (ClassNotFoundException e) {
                    displayMessage("\nObjeto recibido de tipo desconocido");
                } 

            } while (!mensaje.equals("hola")); // CLIENT>>> TERMINATE
        } 

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

        // close streams and socket
        private void cerrarConexion() {
            displayMessage("\nTerminando conexion " + conexionID + "\n");

            try {
                output.close(); 
                input.close(); 
                conexion.close(); 
            } 
            catch (IOException e) {
                System.err.println("Capturada IOException: " + e.getMessage());
                e.printStackTrace(System.err);
                System.exit(1);
            } 
        } 
        // Método para mandar mensajes al cliente
        private void sendData(String mensaje) {
            
            try {
                // Mandamos la información al cliente
                output.writeObject(mensaje);
                output.flush(); 

            } 
            catch (IOException e) {
                displayArea.append("\nError escribiendo el objeto");
            } 
        } 
    }
