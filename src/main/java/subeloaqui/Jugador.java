/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subeloaqui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Jugador extends JFrame {

    private JButton golpear;
    private JButton plantarse;
    private JPanel botones;
    private JTextArea displayArea; 
    private ObjectOutputStream output; 
    private ObjectInputStream input; 
    private String mensaje = ""; 
    private String chatServer; // Servidor host para esta aplicación
    private Socket cliente; // Socket para comunicarse con el server
    private int cardamt = 0;
    
    // Inicializamos chatServer y configura la interfaz gráfica del usuario
    public Jugador(String host) {
        super("Jugador");

        chatServer = host; // Establecer el servidor al que se conecta este cliente
        // Creamos los dos botones de la interfaz del jugador
        botones = new JPanel();
        botones.setLayout(new GridLayout(1, 2));
        golpear = new JButton("GOLPEAR");
        plantarse = new JButton("PLANTARSE");
        // Al pulsarse los botones hacemos que se envíen los mensajes al servidor con el método sendData
        golpear.addActionListener((ActionEvent event) -> {
            sendData("golpear");
        }); 

        plantarse.addActionListener((ActionEvent event) -> {
            sendData("plantarse"); 
        }); 
        // Ponemos los dos botones juntos y abajo de la ventana de la interfaz del jugador
        // El resto lo hacemos como en el dealer
        botones.add(golpear, BorderLayout.SOUTH);
        botones.add(plantarse, BorderLayout.SOUTH);
        botones.setVisible(true);
        add(botones, BorderLayout.SOUTH);
        displayArea = new JTextArea(); 
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setSize(300, 300); 
        setVisible(true); 
    } 

    // Nos conectamos al servidor y procesamos los mensajes desde ahí
    public void runClient() {
        try {
            conectarAlServidor(); 
            getStreams(); 
            procesarConexion(); 
        } catch (EOFException e) {
            displayMessage("\nEl cliente ha terminado la conexion");
        } catch (IOException e) {
            System.err.println("Capturada IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            cerrarConexion(); 
        } 
    } 

    // Nos conectamos al server
    private void conectarAlServidor() throws IOException {
        displayMessage("Intentando conectarse\n");

        // Creamos el Socket para conectarse al servidor
        cliente = new Socket(InetAddress.getByName(chatServer), 23555);

        // Mostramos la información de la conexion
        displayMessage("Conectado a: " + cliente.getInetAddress().getHostName());
    } 

    // El mismo método que en Dealer pero con los streams del cliente
    private void getStreams() throws IOException {
            
            output = new ObjectOutputStream(cliente.getOutputStream());
            output.flush(); 

            
            input = new ObjectInputStream(cliente.getInputStream());

            displayMessage("\nObtenidos los flujos de E/S\n");
        }

    
    private void procesarConexion() throws IOException {

        do {
            try {
                mensaje = (String) input.readObject(); // Leemos el mensaje nuevo
                displayMessage("\n" + mensaje); 
                // No dejamos que toque los botones si se ha sobrepasado o si tiene que esperar
                if (mensaje.contains("¡Sobrepasado!") || mensaje.contains("Por favor espere")) {
                    botones.setVisible(false);
                }

            } catch (ClassNotFoundException e) {
                displayMessage("\nObjeto recibido de tipo desconocido");
            } 

        } while (!mensaje.equals("SERVER>>> TERMINATE"));
    } 

    // Cerramos los streams y el socket
    private void cerrarConexion() {
        displayMessage("\nTerminando conexion");

        try {
            output.close(); 
            input.close(); 
            cliente.close(); 
        } 
        catch (IOException e) {
            System.err.println("Capturada IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        } 
    } 

    // Método para mandar mensajes al servidor
    private void sendData(String mensaje) {
        try {
            output.writeObject(mensaje);
            output.flush(); 

        } catch (IOException ioException) {
            displayArea.append("\nError escribiendo el objeto");
        } 
    } 

    
    private void displayMessage(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(mensaje);
        }); 
    } 
}
