package main.dao;

import main.sgt.Aula;
import main.sgt.AulaKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class AulaDAO implements Map<AulaKey,Aula> {

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
        if(!(key instanceof AulaKey)){
            return false;
        }
        AulaKey a = (AulaKey) key;
        boolean r = false;
        try {
            this.connection = Connect.connect();
            String sql = "SELECT `id` FROM `Aula` WHERE `id`=? AND `Turno_id`=? AND `UC_id`=?;";
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement(sql);
                stm.setInt(1, a.getAula_id());
                stm.setInt(2,a.getTurno_id());
                stm.setString(3, a.getUc_id());
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
        return this.containsKey(new AulaKey(aula));
    }

    @Override
    public Aula get(Object key) {
        //TODO implement get for Value too
        if(!(key instanceof AulaKey)){
            return null;
        }
        AulaKey aKey = (AulaKey) key;
        Aula al = null;
        try {
            connection = Connect.connect();
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement(
                        "SELECT `id` AS `Aula`," +
                                "  Aula.`Turno_id` AS `Turno`," +
                                "  Aula.`UC_id` AS `UC`," +
                                " `Aluno_id` AS `Aluno`" +
                                "FROM Aula " +
                                "INNER JOIN Presencas ON Aula.id = Presencas.Aula_id " +
                                "                     AND Aula.Turno_id = Presencas.Turno_id " +
                                "                     AND Aula.UC_id = Presencas.UC_id " +
                                "WHERE Aula.`id`=?" +
                                "  AND Aula.`Turno_id`=?" +
                                "  AND Aula.`UC_id`=?");
                stm.setInt(1, aKey.getAula_id());
                stm.setInt(2,aKey.getTurno_id());
                stm.setString(3 , aKey.getUc_id());
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("Aula");
                    int turno = rs.getInt("Turno");
                    String uc = rs.getString("UC");
                    List<String> presencas = new ArrayList<>();
                    String aluno = rs.getString("Aluno");
                    presencas.add(aluno);
                    do {
                        aluno = rs.getString("Aluno");
                        presencas.add(aluno);
                    }while(rs.next());

                    al = new Aula(id,uc,turno,presencas);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    @Override
    public Aula put(AulaKey key, Aula value) {
        return null;
    }

    @Override
    public Aula remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends AulaKey, ? extends Aula> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<AulaKey> keySet() {
        return new HashSet<>();
    }

    @Override
    public Collection<Aula> values() {
        return new ArrayList<>();
    }

    @Override
    public Set<Entry<AulaKey, Aula>> entrySet() {
        return new HashSet<>();
    }

    public int maxID() {
        //TODO implement AulaDAO.maxID
        return 0;
    }
}
