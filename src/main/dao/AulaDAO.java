package main.dao;

import main.sgt.Aula;

import java.util.*;

public class AulaDAO implements Map<Integer, Aula> {
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
    public Aula get(Object key) {
        return null;
    }

    @Override
    public Aula put(Integer key, Aula value) {
        return null;
    }

    @Override
    public Aula remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Aula> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return new HashSet<>();
    }

    @Override
    public Collection<Aula> values() {
        return new ArrayList<>();
    }

    @Override
    public Set<Entry<Integer, Aula>> entrySet() {
        return new HashSet<>();
    }

    public int maxID() {
        //TODO implement AulaDAO.maxID
        return 0;
    }
}
