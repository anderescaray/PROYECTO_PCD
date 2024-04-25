/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subeloaqui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Dealer extends JFrame {

    private JButton Repartir;
    private Baraja nuevaBaraja;
    private JTextArea displayArea; // display information to user
    private ExecutorService executor; // will run players
    private ServerSocket server; // server socket
    private SockServer[] sockServer; // Array de objetos para hacerlos hilos
    private int contador = 1; // counter of number of connections
    private String dcarta1, dcarta2;
    private ArrayList<Juego> jugadores;
    private Juego dcartas;
    private int jugadoresRestantes;
    private boolean redondear = true;

    // Configuramos la interfaz gráfica de usuario
    public Dealer() {

        super("Dealer");

        jugadores = new ArrayList();
        sockServer = new SockServer[100]; // Creamos un array para hasta 10 subprocesos del servidor
        executor = Executors.newFixedThreadPool(10); // Creamos el thread pool

        Repartir = new JButton("REPARTIR CARTAS");

        Repartir.addActionListener((ActionEvent event) -> {
            Repartir.setEnabled(false); // Desactivamos el botón una vez usado
            nuevaBaraja = new Baraja();
            redondear = false;
            repartirCartas();
            displayMessage("\n\nCARTAS REPARTIDAS\n\n");
        });

        add(Repartir, BorderLayout.SOUTH); // Ponemos el botón en la parte de abajo de la ventana

        displayArea = new JTextArea(); // Creamos un área de texto
        displayArea.setEditable(false); // Y hacemos que no se pueda editar
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        // La ponemos en el medio y hacemos que se pueda deslizar

        setSize(300, 300); // Asignamos el tamaño de la ventana
        setVisible(true); // Para que se vea la ventana
    }

    // Configuramos y ejecutamos el servidor 
    public void runDeal() {
        // Configuramos el servidor para recibir conexiones y procesamos las conexiones
        try {
            server = new ServerSocket(23555); // se puede poner ,10  para maximo 10 a la vez

            while (true) {
                try {
                    // Creamos un nuevo objeto ejecutable para el próximo cliente 
                    sockServer[contador] = new SockServer(contador);
                    // Hacemos que ese nuevo objeto espere una conexión en el nuevo servidor
                    sockServer[contador].esperarConexion();
                    // Lanzamos ese objeto de servidor en su propio hilo nuevo
                    executor.execute(sockServer[contador]);
                    // Se continúa creando otro objeto y espera (en bucle)

                } catch (EOFException e) {
                    displayMessage("\nHa terminado la conexion con el servidor");
                } finally {
                    ++contador;
                }
            }
        } catch (IOException e) {
            System.err.println("Capturada IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    // Manipula displayArea en el hilo de envío de eventos
    private void displayMessage(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(mensaje); // Añadimos el mensaje a la interfaz
        });
    }

    private void repartirCartas() {

        try {
            jugadoresRestantes = contador - 1;
            nuevaBaraja.mezclar();
            dcarta1 = nuevaBaraja.repartirCarta();
            dcarta2 = nuevaBaraja.repartirCarta();
            displayMessage("\n\n" + dcarta1 + " " + dcarta2); // Enseñamos las cartas que han tocado

            for (int i = 1; i <= contador; i++) {
                String c1 = nuevaBaraja.repartirCarta();
                String c2 = nuevaBaraja.repartirCarta();
                Juego p = new Juego(c1, c2);
                jugadores.add(p);
                sockServer[i].sendData("Te han tocado:\n" + c1 + " " + c2);
                sockServer[i].sendData("Tu puntuación: " + p.getPuntuacion());

            }
        } catch (NullPointerException e) {
            // Si se pone algo da error y se cierra la interfaz del dealer
        }
    }

    private void resultados() {

        try {
            for (int i = 1; i <= contador; i++) {
                sockServer[i].sendData("La puntuación del dealer es " + dcartas.getPuntuacion());

                if ((dcartas.getPuntuacion() <= 21) && (jugadores.get(i - 1).getPuntuacion() <= 21)) {
                    // Comparamos las puntuaciones si ninguno se sobrepasa y vemos quien gana
                    if (dcartas.getPuntuacion() > jugadores.get(i - 1).getPuntuacion()) {
                        sockServer[i].sendData("\n ¡Has perdido!");
                    }

                    if (dcartas.getPuntuacion() < jugadores.get(i - 1).getPuntuacion()) {
                        sockServer[i].sendData("\n ¡Has ganado!");
                    }

                    if (dcartas.getPuntuacion() == jugadores.get(i - 1).getPuntuacion()) {
                        sockServer[i].sendData("\n ¡Empate!");
                    }

                }

                if (dcartas.comprobarSobrepasada()) {

                    if (jugadores.get(i - 1).comprobarSobrepasada()) {
                        sockServer[i].sendData("\n ¡Empate!");
                    }
                    if (jugadores.get(i - 1).getPuntuacion() <= 21) {
                        sockServer[i].sendData("\n ¡Has ganado!");
                    }
                }

                if (jugadores.get(i - 1).comprobarSobrepasada() && dcartas.getPuntuacion() <= 21) {
                    sockServer[i].sendData("\n ¡Has perdido!");
                }
            }

        } catch (NullPointerException e) {
        }
    }

    /* Esta nueva clase interna implementa Runnable y objetos instanciados a partir de este
         la clase se convertirá en subprocesos del servidor, cada uno de los cuales servirá a un cliente diferente
     */
    private class SockServer implements Runnable {

        private ObjectOutputStream output; // Flujo de salida a la cliente
        private ObjectInputStream input;
        private Socket conexion; // Conexión al cliente
        private int conexionID;

        public SockServer(int id) {
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

            } while (!mensaje.equals("CLIENT>>> TERMINATE"));
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
}
