package main.dao;

import main.sgt.Troca;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class TrocaDAO implements List<Troca> {
    private Connection connection;
    @Override
    public int size() {
        this.connection = Connect.connect();
        if(connection==null) return -1;
        int i=-1;
        try{
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT count(*) FROM Trocas;");
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                i = rs.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    public boolean contains(Object o) {
        this.connection = Connect.connect();
        if(connection==null) return false;
        if(!(o instanceof Troca)){
            Connect.close(connection);
            return false;
        }
        Troca troca = (Troca) o;
        boolean r=false;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT * FROM `Trocas` WHERE `id`=?;");
            stm.setInt(1, troca.getId());
            ResultSet rs = stm.executeQuery();
            r = rs.next()
                && troca.equals(new Troca(
                                rs.getInt("id"),
                                rs.getString("aluno_id"),
                                rs.getString("UC_id"),
                                rs.getInt("turnoOrigem_id"),
                                rs.getBoolean("turnoOrigem_ePratico"),
                                rs.getInt("turnoDestino_id"),
                                rs.getBoolean("turnoDestino_id"),
                                rs.getTimestamp("data").toLocalDateTime()));
        } catch (Exception e) {
            e.printStackTrace();
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
        int size = this.size();
        this.connection = Connect.connect();
        if (connection == null) return null;
        Object[] array = null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("" +
                    "SELECT * FROM Trocas;");
            ResultSet rs = stm.executeQuery();
            array = new Object[size];
            for (int i = 0; i < size && rs.next(); i++) {
                array[i] = new Troca(rs.getInt("id"),
                        rs.getString("aluno_id"),
                        rs.getString("UC_id"),
                        rs.getInt("turnoOrigem_id"),
                        rs.getBoolean("turnoOrigem_ePratico"),
                        rs.getInt("turnoDestino_id"),
                        rs.getBoolean("turnoDestino_ePratico"),
                        rs.getTimestamp("dataRealizacao").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Troca troca) {
        this.connection = Connect.connect();
        if (connection == null) return false;
        boolean r = false;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(
                    "INSERT INTO `Trocas` (dataRealizacao, aluno_id, turnoOrigem_id,turnoOrigem_ePratico, UC_id, turnoDestino_id,turnoDestino_ePratico, UC_id1) \n" +
                            "VALUES (NOW(),?,?,?,?,?,?,?)\n" +
                            "ON DUPLICATE KEY UPDATE dataRealizacao=VALUES(dataRealizacao),\n" +
                            "                        aluno_id=VALUES(aluno_id),\n" +
                            "                        turnoOrigem_id=VALUES(turnoOrigem_id),\n" +
                            "                        turnoOrigem_ePratico=VALUES(turnoOrigem_ePratico),\n" +
                            "                        UC_id=VALUES(UC_id),\n" +
                            "                        turnoDestino_id=VALUES(turnoDestino_id),\n" +
                            "                        turnoDestino_ePratico=VALUES(turnoDestino_ePratico),\n" +
                            "                        UC_id1=VALUES(UC_id1)\n;",
                    Statement.RETURN_GENERATED_KEYS);
//            stm.setInt(1,troca.getId());
            stm.setString(1, troca.getAluno());
            stm.setInt(2, troca.getTurnoOrigem());
            stm.setBoolean(3, troca.isTurnoOrigem_Pratico());
            stm.setString(4, troca.getUc());
            stm.setInt(5, troca.getTurnoDestino());
            stm.setBoolean(6, troca.isTurnoDestino_Pratico());
            stm.setString(7, troca.getUc());
            stm.executeUpdate();
            /*ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()){
                troca = this.get(rs.getInt(1));
                r = true;
            }*/
        } catch (Exception e) {
            System.out.println(stm);
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return r;
    }

    @Override
    public boolean remove(Object o) {
        return o instanceof Troca && this.remove(((Troca) o).getId()) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    @Override
    public boolean addAll(Collection<? extends Troca> c) {
        return c.stream().allMatch(this::add);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Troca> c) {
        return this.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return c.stream().allMatch(this::remove);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.connection = Connect.connect();
        if (connection == null) return;
        Statement stm = null;
        try {
            stm = connection.createStatement();
            stm.execute("DELETE FROM Trocas WHERE TRUE;");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
    }

    @Override
    public Troca get(int index) {
        this.connection = Connect.connect();
        if (connection == null) return null;
        Troca t = null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("" +
                    "SELECT * FROM Trocas WHERE id=?;");
            stm.setInt(1, index);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                t = new Troca(index,
                        rs.getString("aluno_id"),
                        rs.getString("UC_id"),
                        rs.getInt("turnoOrigem_id"),
                        rs.getBoolean("turnoOrigem_ePratico"),
                        rs.getInt("turnoDestino_id"),
                        rs.getBoolean("turnoDestino_ePratico"),
                        rs.getTimestamp("dataRealizacao").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return t;
    }

    @Override
    public Troca set(int index, Troca element) {
        return this.add(element) ? element : null;
    }

    @Override
    public void add(int index, Troca element) {
        this.add(element);
    }

    @Override
    public Troca remove(int index) {
        Troca t = this.get(index);
        if (t == null) return t;
        this.connection = Connect.connect();
        if (connection == null) return null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("" +
                    "DELETE FROM Trocas WHERE id=?");
            stm.setInt(1, index);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return t;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Troca> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Troca> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Troca> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public int maxID() {
        connection = Connect.connect();
        if (connection == null) return -1;
        int maxID = -1;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(
                    "SELECT max(`id`) FROM `Trocas`;");
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                maxID = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return maxID;
    }
}
