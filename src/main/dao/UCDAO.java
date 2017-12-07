package main.dao;

import main.sgt.UC;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UCDAO implements Map<String, UC> {
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
    public UC get(Object key) {
        return null;
    }

    @Override
    public UC put(String key, UC value) {
        return null;
    }

    @Override
    public UC remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends UC> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<UC> values() {
        return null;
    }

    @Override
    public Set<Entry<String, UC>> entrySet() {
        return null;
    }
}
