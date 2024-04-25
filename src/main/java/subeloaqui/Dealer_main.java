/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package subeloaqui;

import javax.swing.JFrame;

public class Dealer_main {

    public static void main(String[] args) {
        Dealer dealer = new Dealer(); // Creamos el servidor
        dealer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dealer.runDeal(); 
    }
    
}
