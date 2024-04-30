/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author alumno
 */
public class Juego implements Serializable {

    private boolean sobrepasado = false;
    private int puntuacion = 0;

    private ArrayList<Carta> cartas;
    private ArrayList<Carta> ases;

    // Creamos una lista para los ases y otra para el resto de cartas y en el constructor
    // Añadimos las dos primeras cartas que nos tocan a su lista correspondiente y mostramos la puntuacion
    // Tener en cuenta, que los ases cuentan distinto, depende de la puntuacion. Pueden contar 11 o 1
    public Juego(Carta c1, Carta c2) {

        cartas = new ArrayList();
        ases = new ArrayList();

        if ("A".equals(c1.getValor())) { //Si la carta es un as, se añade a su lista
            ases.add(c1);
        } else {
            cartas.add(c1); //sino a la del resto de cartas
        }

        if ("A".equals(c2.getValor())) { //lo mismo con la segunda carta
            ases.add(c2);
        } else {
            cartas.add(c2);
        }

        calcularPuntuacion(); //se actualiza la puntuacion

    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void pedirCarta(Carta ca) { //Este metodo, se usa introduciendo una carta nueva que sacaremos de la baraja

        // Primero comprobamos si nos ha tocado un AS
        if ("A".equals(ca.getValor())) {
            ases.add(ca);
        } else {
            cartas.add(ca);
        }

        // Si nos ha tocado un AS y/o teníamos ya alguno, recalculamos la puntuacion
        // sino sumamos la nueva carta de manera normal
        if (!ases.isEmpty()) {
            calcularPuntuacion();
        } else if ("J".equals(ca.getValor()) || "Q".equals(ca.getValor()) || "K".equals(ca.getValor())) {

            puntuacion += 10;
        } else {
            puntuacion += Integer.parseInt(ca.getValor());
        }

        comprobarSobrepasada();

    }

    private void calcularPuntuacion() { //este metodo se encarga de actualizar la puntuacion del Juego

        puntuacion = 0;

        // Sumamos primero las cartas normales
        for (Carta c : cartas) {
            if ("J".equals(c.getValor()) || "Q".equals(c.getValor()) || "K".equals(c.getValor())) {
                puntuacion += 10;
            } else {
                puntuacion += Integer.parseInt(c.getValor());
            }

        }

        // Y luego sumamos los ases para ver si cuentan 11 o 1
        for (Carta a : ases) {
            if (puntuacion <= 10) {
                puntuacion += 11;
            } else {
                puntuacion += 1;
            }

        }
    }

    public boolean comprobarSobrepasada() { //calcula si nos hemos pasado del 21
        sobrepasado = puntuacion > 21;
        return sobrepasado;
    }

    @Override
    public String toString() {
        String frase = "";
        for (Carta e : cartas) {
            frase = frase  + e+ ", ";
        }
        for (Carta a : ases) {
            frase = frase + a;
        }
        return frase.substring(0,frase.length()-2);
        
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Juego other = (Juego) obj;
        if (this.puntuacion != other.puntuacion) {
            return false;
        }
        if (!Objects.equals(this.cartas, other.cartas)) {
            return false;
        }
        return Objects.equals(this.ases, other.ases);
    }
    
}
