package main.dao;

import main.sgt.Pedido;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PedidosDAO implements Map<String,List<Pedido>> {
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
    public List<Pedido> get(Object key) {
        return null;
    }

    @Override
    public List<Pedido> put(String key, List<Pedido> value) {
        return null;
    }
    public Pedido put (String key, Pedido value){
        return null;
    }
    @Override
    public List<Pedido> remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends List<Pedido>> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<List<Pedido>> values() {
        return null;
    }

    @Override
    public Set<Entry<String, List<Pedido>>> entrySet() {
        return null;
    }
}
