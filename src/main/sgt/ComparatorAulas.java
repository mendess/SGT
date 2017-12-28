package main.sgt;

import java.util.Comparator;
import java.io.Serializable;

public class ComparatorAulas implements Comparator<Aula>, Serializable
{
    public int compare(Aula a1, Aula a2) {
       Integer num_a1 = a1.getNumero();
       Integer num_a2 = a2.getNumero();
       return num_a1.compareTo(num_a2);
    }
}