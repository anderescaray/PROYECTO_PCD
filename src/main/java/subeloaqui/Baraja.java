/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subeloaqui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Baraja {

    private Carta[] baraja;
    private int contador;
    private static final int NUMERO_DE_CARTAS = 52;
    private static final Random rand = new Random();

    public Baraja() {
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        baraja = new Carta[NUMERO_DE_CARTAS];
        contador = 0;

        for (int i = 0; i < baraja.length; i++) {
            baraja[i] = new Carta(valores[i % 13]);
        }

    }//end constructor

    public void mezclar() {
        List<Carta> barajaList = Arrays.asList(baraja);
        Collections.shuffle(barajaList);
        barajaList.toArray(baraja);
        contador = 0;
    }

    public String repartirCarta() {

        if (contador < baraja.length) {
            return baraja[contador++].toString();
        } else {
            return null;
        }
    }

}
