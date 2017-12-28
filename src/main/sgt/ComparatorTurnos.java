package main.sgt;

import java.util.Comparator;
import java.io.Serializable;

public class ComparatorTurnos implements Comparator<Turno>, Serializable
{
    public int compare(Turno t1, Turno t2) {

        Integer id_t1 = t1.getId();
        Integer id_t2 = t2.getId();
        boolean ePratico_t1 = t1.ePratico();
        boolean ePratico_t2 = t2.ePratico();

        // se são o mesmo turno
        if(id_t1==id_t2 && (ePratico_t1== ePratico_t2))
            return 0;

        // se t1 é teórico e t2 é prático
        else if (!ePratico_t1) {
            if  (ePratico_t2)
            return 1;
        }

        // se t2 é teórico e t1 é prático
        else if (!ePratico_t2) {
            if (ePratico_t1)
                return -1;
        }

        // se são os dois turnos práticos
        else
            return id_t1.compareTo(id_t2);

        return 0;
    }
}