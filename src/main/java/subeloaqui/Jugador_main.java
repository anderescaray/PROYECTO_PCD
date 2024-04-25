/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subeloaqui;

import javax.swing.JFrame;

public class Jugador_main {

    public static void main(String[] args) {
        Jugador jugador = new Jugador("172.18.83.19");

        jugador.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jugador.runClient(); 
    } 
    
}
