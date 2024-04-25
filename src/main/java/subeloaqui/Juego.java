/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package subeloaqui;

import java.util.ArrayList;

public class Juego {

    private boolean sobrepasado = false;
    private int puntuacion = 0;

    private ArrayList<String> cartas;
    private ArrayList<String> ases;

    // Creamos una lista para los ases y otra para el resto de cartas y en el constructor
    // añadimos las dos primeras cartas que nos tocan a su lista correspondiente y mostramos la puntuacion
    public Juego(String c1, String c2) {

        cartas = new ArrayList();
        ases = new ArrayList();

        if ("A".equals(c1)) {
            ases.add(c1);
        } else {
            cartas.add(c1);
        }

        if ("A".equals(c2)) {
            ases.add(c2);
        } else {
            cartas.add(c2);
        }

        calcularPuntuacion();

    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void pedirCarta(String ca) {

        // Primero comprobamos si nos ha tocado un AS
        
        if ("A".equals(ca)) {
            ases.add("A");
        } else {
            cartas.add(ca);
        }

        // Si nos ha tocado un AS y/o teníamos ya alguno, recalculamos la puntuacion
        // sino sumamos la nueva carta de manera normal
        
        if (!ases.isEmpty()) {
            calcularPuntuacion();
        } else if ("J".equals(ca) || "Q".equals(ca) || "K".equals(ca)) {

            puntuacion += 10;
        } else {
            puntuacion += Integer.parseInt(ca);
        }

        comprobarSobrepasada();

    }
     
    private void calcularPuntuacion() {

        puntuacion = 0;

        // Sumamos primero las cartas normales
        for (String c : cartas) {
            if ("J".equals(c) || "Q".equals(c) || "K".equals(c)) {
                puntuacion += 10;
            } else {
                puntuacion += Integer.parseInt(c);
            }

        }

        // Y luego sumamos los ases para ver si cuentan 11 o 1
        for (String a : ases) {
            if (puntuacion <= 10) {
                puntuacion += 11;
            } else {
                puntuacion += 1;
            }

        }
    }

    public boolean comprobarSobrepasada() {
        sobrepasado = puntuacion > 21;
        return sobrepasado;
    }
}
