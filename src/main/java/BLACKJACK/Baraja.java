/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BLACKJACK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Baraja {

    private List<Carta> cartas;

    public Baraja() {
        cartas = new ArrayList<>();
        inicializarBaraja();
        mezclar();
    }

    private void inicializarBaraja() {
        String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (EnumPalo palo : EnumPalo.values()) {
            for (String valor : valores) {
                cartas.add(new Carta(palo, valor));
            }
        }
    }

    public void mezclar() {
        Collections.shuffle(cartas);
    }

    public synchronized Carta sacarCarta() {
        if (cartas.isEmpty()) {
            throw new IllegalStateException("La baraja está vacía");
        }
        return cartas.remove(0);
    }
}
