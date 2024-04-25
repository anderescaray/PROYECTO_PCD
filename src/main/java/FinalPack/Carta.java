/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinalPack;

import java.io.Serializable;

/**
 *
 * @author alumno
 */
public class Carta implements Serializable {
    private EnumPalo palo;
    private String valor;

    public Carta(EnumPalo palo, String valor) {
        this.palo = palo;
        this.valor = valor;
    }

    public EnumPalo getPalo() {
        return palo;
    }

    public void setPalo(EnumPalo palo) {
        this.palo = palo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
    
    
}
