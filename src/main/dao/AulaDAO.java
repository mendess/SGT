package main.dao;

import main.sgt.Aula;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AulaDAO implements Map<Integer, Aula> {

    private Connection connection;

    @Override
    public int size() {
        connection = Connect.connect();
        int i = 0;
        try {
            connection = Connect.connect();
            if (connection != null) {
                Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery("SELECT count(*) FROM Aula");
                if(rs.next()) {
                    i = rs.getInt(1);
                }
            }
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(connection);
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r = false;
        try {
            this.connection = Connect.connect();
            String sql = "SELECT `idAula` FROM `Aula` WHERE `idAula`=?;";
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement(sql);
                stm.setInt(1, Integer.parseInt(key.toString()));
                ResultSet rs = stm.executeQuery();
                r = rs.next();
            }
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(connection);
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Aula aula = (Aula) value;
        return this.containsKey(aula.getNumero());
    }

    @Override
    public Aula get(Object key) {
        Aula al = null;
        try {
            connection = Connect.connect();
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement("SELECT * FROM Aula WHERE idAula=?");
                stm.setInt(1, (Integer)key);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    return null;
                    //TODO fix this
                    //al = new Aula(rs.getString("nome"),rs.getInt("id"),rs.getString("email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return null;//al;
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
