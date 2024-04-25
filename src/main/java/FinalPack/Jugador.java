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
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author alumno
 */
public class Jugador {
    private static final String SERVER_ADRESS = "127.0.0.1";
    private static final int SERVER_PORT = 59002;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       try {
            Socket socket = new Socket(SERVER_ADRESS, SERVER_PORT);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()) ;
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            
            while(true){
                
            }
            
    }catch(IOException e){
        System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
    }
    
}
}
