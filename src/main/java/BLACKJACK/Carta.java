package BLACKJACK;

import java.io.Serializable;
import java.util.Objects;

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
        return valor + " de " + String.valueOf(palo).toLowerCase();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Carta other = (Carta) obj;
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        return this.palo == other.palo;
    }
    
    
}
