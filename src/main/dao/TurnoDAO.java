package main.dao;

import main.sgt.Turno;
import main.sgt.TurnoKey;

import java.util.*;

public class TurnoDAO implements Map<TurnoKey,Turno> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Turno get(Object key) {
        return null;
    }

    @Override
    public Turno put(TurnoKey key, Turno value) {
        return null;
    }

    @Override
    public Turno remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends TurnoKey, ? extends Turno> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<TurnoKey> keySet() {
        //TODO implement TurnoDAO.keySet
        return new HashSet<>();
    }

    @Override
    public Collection<Turno> values() {
        return new ArrayList<>();
    }

    @Override
    public Set<Entry<TurnoKey, Turno>> entrySet() {
        return new HashSet<>();
    }

    public int maxID() {
        //TODO implement TurnoDAO.maxID
        return 0;
    }
}
