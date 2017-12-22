package main.dao;

import main.sgt.Aula;
import main.sgt.AulaKey;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class AulaDAO implements Map<AulaKey,Aula> {

    private Connection connection;

    @Override
    public int size() {
        connection = Connect.connect();
        int i = -1;
        if(connection==null) return i;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Aula");
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
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
        this.connection = Connect.connect();
        if(connection==null) return false;
        if(!(key instanceof AulaKey)){
            return false;
        }
        AulaKey a = (AulaKey) key;
        boolean r = false;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT `id` FROM `Aula` WHERE `id`=? AND `Turno_id`=? AND `UC_id`=? AND ePratico=?;");
            stm.setInt(1, a.getAula_id());
            stm.setInt(2,a.getTurno_id());
            stm.setString(3, a.getUc_id());
            stm.setBoolean(4,a.ePratico());
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        return value.equals(this.get(value));
    }

    @Override
    public Aula get(Object key) {
        connection = Connect.connect();
        if(connection==null) return null;
        AulaKey aKey;
        if(key instanceof AulaKey){
            aKey = (AulaKey) key;
        }else if(key instanceof Aula){
            aKey = new AulaKey((Aula) key);
        }else{
            return null;
        }
        Aula al = null;
        try {
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT `id` AS `Aula`," +
                            "  Aula.`Turno_id` AS `Turno`," +
                            "  Aula.`UC_id` AS `UC`," +
                            " `Aluno_id` AS `Aluno`," +
                            "  Aula.ePratico AS ePratico\n" +
                            "FROM Aula " +
                            "LEFT JOIN Presencas ON Aula.id = Presencas.Aula_id " +
                            "                   AND Aula.Turno_id = Presencas.Turno_id " +
                            "                   AND Aula.UC_id = Presencas.UC_id " +
                            "                   AND Aula.ePratico = Presencas.ePratico\n" +
                            "WHERE Aula.id=?" +
                            "  AND Aula.Turno_id=?" +
                            "  AND Aula.UC_id=?" +
                            "  AND Aula.ePratico=?");
            stm.setInt(1, aKey.getAula_id());
            stm.setInt(2,aKey.getTurno_id());
            stm.setString(3 , aKey.getUc_id());
            stm.setBoolean(4,aKey.ePratico());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("Aula");
                int turno = rs.getInt("Turno");
                String uc = rs.getString("UC");
                boolean ePratico = rs.getBoolean("ePratico");
                List<String> presencas = new ArrayList<>();
                do {
                    String aluno = rs.getString("Aluno");
                    if(aluno!=null) presencas.add(aluno);
                }while(rs.next());

                al = new Aula(id,uc,turno,ePratico,presencas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    @Override
    public Aula put(AulaKey key, Aula value) {
        this.connection = Connect.connect();
        if(this.connection==null) return null;
        Aula al = null;
        try {
            this.connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("" +
                    "INSERT INTO Aula (id, Turno_id, UC_id,ePratico) " +
                    "   VALUES (?,?,?,?)" +
                    "ON DUPLICATE KEY UPDATE id=VALUES(id)," +
                    "                        Turno_id=VALUES(Turno_id)," +
                    "                        UC_id=VALUES(UC_id)," +
                    "                        ePratico=VALUES(ePratico);" +
                    "DELETE FROM Presencas WHERE UC_id=? AND Turno_id=? AND Aula_id=? AND ePratico=?;");

            PreparedStatement stmAulas = updateAulas(value);
            stm.setInt(1,value.getNumero());
            stm.setInt(2,value.getTurno());
            stm.setString(3,value.getUc());
            stm.setBoolean(4,value.ePratico());
            stm.setString(5,value.getUc());
            stm.setInt(6,value.getTurno());
            stm.setInt(7,value.getNumero());
            stm.setBoolean(8,value.ePratico());
            stm.executeUpdate();
            stmAulas.executeBatch();

            connection.commit();
            al = value;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    private PreparedStatement updateAulas(Aula value) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "INSERT INTO Presencas (Aula_id, Turno_id, UC_id, Aluno_id,ePratico) " +
                "   VALUES (?,?,?,?,?);");
        for(String a: value.getPresencas()){
            stm.setInt(1,value.getNumero());
            stm.setInt(2,value.getTurno());
            stm.setString(3,value.getUc());
            stm.setString(4,a);
            stm.setBoolean(5,value.ePratico());
            stm.addBatch();
        }
        return stm;
    }

    @Override
    public Aula remove(Object key) {
        Aula al = this.get(key);
        connection = Connect.connect();
        if(al==null && connection==null) return null;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "DELETE FROM Presencas WHERE Aula_id=? AND Turno_id=? AND UC_id=? AND ePratico=?;" +
                    "DELETE FROM Aula      WHERE id=?      AND Turno_id=? AND UC_id=? AND ePratico=?;");
            for(int i=0;i<2;i++){
                stm.setInt(    (4*i)+1,al.getNumero());
                stm.setInt(    (4*i)+2,al.getTurno());
                stm.setString( (4*i)+3,al.getUc());
                stm.setBoolean((4*i)+4,al.ePratico());
            }
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    @Override
    public void putAll(Map<? extends AulaKey, ? extends Aula> m) {
        m.values().forEach(a -> this.put(new AulaKey(a), a));
    }

    @Override
    public void clear() {
        this.keySet().forEach(this::remove);
    }

    @Override
    public Set<AulaKey> keySet() {
        Set<AulaKey> keySet = new HashSet<>();
        connection = Connect.connect();
        if(connection==null) return keySet;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT id,Turno_id,UC_id,ePratico FROM `Aula`;");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                keySet.add(new AulaKey(rs.getString("UC_id"),
                                       rs.getInt("Turno_id"),
                                       rs.getInt("id"),
                                       rs.getBoolean("ePratico")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return keySet;
    }

    @Override
    public Collection<Aula> values() {
        return this.keySet()
            .stream()
            .map(this::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<AulaKey, Aula>> entrySet() {
        Set<Entry<AulaKey,Aula>> entrySet = new HashSet<>();
        this.keySet().forEach(uk -> {
            Aula u = this.get(uk);
            entrySet.add(new AbstractMap.SimpleEntry<>(uk, u));
        });
        return entrySet;
    }

    public int maxID(String uc, int turno, boolean ePratico) {
        connection = Connect.connect();
        int maxID = -1;
        if(connection==null) return maxID;
        try {
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT max(`id`) FROM `Aula` WHERE UC_id=? AND `Turno_id`=? AND ePratico=?;");
            stm.setString(1,uc);
            stm.setInt(2,turno);
            stm.setBoolean(3,ePratico);
            ResultSet rs = stm.executeQuery();
            if(rs.next()) maxID = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return maxID;
    }
}
