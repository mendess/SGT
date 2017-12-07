package main.dao;

import main.sgt.Aula;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
        return null;
    }

    @Override
    public Collection<Aula> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, Aula>> entrySet() {
        return null;
    }
}
