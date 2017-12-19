package main.dao;

import main.sgt.Troca;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TrocaDAO implements List<Troca> {
    Connection connection;
    @Override
    public int size() {
        this.connection = Connect.connect();
        try{
            PreparedStatement stm = connection.prepareStatement("SELECT count(*) FROM Trocas;");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    public boolean contains(Object o) {
        if(!(o instanceof Troca)){
            return false;
        }
        Troca troca = (Troca) o;
        boolean r;
        try {
            this.connection = Connect.connect();
            String sql = "SELECT * FROM `Trocas` WHERE `id`=?;";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, troca.getId());
            ResultSet rs = stm.executeQuery();
            LocalDateTime ldt = LocalDateTime.ofInstant(rs.getTimestamp("data").toInstant(), TimeZone.getDefault().toZoneId());
            r = rs.next()
                && troca.equals(new Troca(
                                rs.getInt("id"),
                                rs.getString("aluno_id"),
                                rs.getString("UC_id"),
                                rs.getInt("turnoOrigem_id"),
                                rs.getInt("turnoDestino_id"),
                                LocalDateTime.ofInstant(rs.getTimestamp("data").toInstant(),
                                                        TimeZone.getDefault().toZoneId())));
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(connection);
        }
        return r;
    }

    @Override
    public Iterator<Troca> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Troca troca) {
        try {
            this.connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement(
                    "INSERT INTO `Trocas` (id, dataRealizacao, aluno_id, turnoOrigem_id, UC_id, turnoDesino_id, UC_id1) \n" +
                            "VALUES (?,?,?,?,?,?,?);");
            stm.setInt(1,troca.getId());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Troca> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Troca> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Troca get(int index) {
        return null;
    }

    @Override
    public Troca set(int index, Troca element) {
        return null;
    }

    @Override
    public void add(int index, Troca element) {

    }

    @Override
    public Troca remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Troca> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Troca> listIterator(int index) {
        return null;
    }

    @Override
    public List<Troca> subList(int fromIndex, int toIndex) {
        return null;
    }

    public int maxID(){
        int maxID = 0;
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT max(`id`) FROM `Trocas`;");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                maxID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }
}
