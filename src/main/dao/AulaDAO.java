package main.dao;

import main.sgt.Aula;
import main.sgt.AulaKey;

import java.sql.*;
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
        Aula al = null;
        try {
            connection = Connect.connect();
            String sql = "START TRANSACTION;\n" +
                         "INSERT INTO `Aula` (id,Turno_id,UC_id)\n" +
                         "VALUES (?, ?, ?)\n" +
                         "ON DUPLICATE KEY UPDATE id=VALUES(id),\n" +
                         "                        Turno_id=VALUES(Turno_id),\n" +
                         "                        UC_id=VALUES(UC_id);\n";
            StringBuilder s = new StringBuilder(sql);
            s.append("INSERT INTO `Presencas` (Aula_id, Turno_id, UC_id, Aluno_id)\n" +
                     "VALUES \n");
            for(int i=0;i<value.getPresencas().size();i++){
                if(i<value.getPresencas().size()-1){
                    s.append("(?,?,?,?),\n");
                }else{
                    s.append("(?,?,?,?)\n");
                }
            }
            s.append("ON DUPLICATE KEY UPDATE `Aula_id`=VALUES(Aula_id), Turno_id=VALUES(Turno_id), `UC_id`=VALUES(UC_id), `Aula_id`=VALUES(Aluno_id)");

            PreparedStatement stm = connection.prepareStatement(s.toString(), Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, value.getNumero());
            stm.setInt(2, value.getTurno());
            stm.setString(3, value.getUc());
            for (int i=4;i<value.getPresencas().size()*4+4;i+=4){
                stm.setInt(i,value.getNumero());
                stm.setInt(i+1, value.getTurno());
                stm.setString(i+2, value.getUc());
                stm.setString(i+3,value.getPresencas().get((i-4)/4));
            }
            System.out.println(stm.toString());
            stm.executeUpdate();

            al = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    @Override
    public Aula remove(Object key) {
        Aula al = this.get(key);
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement(
                    "START TRANSACTION;\n" +
                        "DELETE FROM `Aula`\n" +
                            "WHERE      `id`=?\n" +
                            " AND `Turno_id`=?\n" +
                            " AND `UC_id`=?;\n" +
                        "DELETE FROM `Presencas`\n" +
                            "WHERE `Aula_id`=?\n" +
                            " AND `Turno_id`=?\n" +
                            " AND `UC_id`=?;\n");
            //noinspection JpaQueryApiInspection
            stm.setInt(1,al.getNumero());
            stm.setInt(2,al.getTurno());
            stm.setString(3,al.getUc());
            stm.setInt(4,al.getNumero());
            stm.setInt(5,al.getTurno());
            stm.setString(6,al.getUc());
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(connection);
        }
        return al;
    }

    @Override
    public void putAll(Map<? extends AulaKey, ? extends Aula> m) {
        for(Aula a : m.values()){
            this.put(new AulaKey(a),a);
        }
    }

    @Override
    public void clear() {
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement(
                            "START TRANSACTION;" +
                                    "DELETE FROM `Presencas` WHERE `Aula_id`>0;;" +
                                    "DELETE FROM `Aula` WHERE `id`>0;");
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(connection);
        }
    }

    @Override
    public Set<AulaKey> keySet() {
        Set<AulaKey> keySet = new HashSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT * FROM `Aula`;");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                keySet.add(new AulaKey(rs.getString("UC_id"),
                                       rs.getInt("Turno_id"),
                                       rs.getInt("id")));
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
        Set<Aula> valueSet = new HashSet<>();
        Set<AulaKey> keySet = this.keySet();
        try {
            connection = Connect.connect();
            for(AulaKey ak : keySet){
                PreparedStatement stm = connection.prepareStatement(
                        "SELECT `Aluno_id` " +
                                "FROM `Presencas`" +
                                "WHERE Aula_id=? AND Turno_id=? AND UC_id=?;");
                stm.setInt(1,ak.getAula_id());
                stm.setInt(2,ak.getTurno_id());
                stm.setString(3,ak.getUc_id());
                ResultSet rs = stm.executeQuery();
                List<String> presencas = new ArrayList<>();
                while(rs.next()){
                    presencas.add(rs.getString(1));
                }
                valueSet.add(new Aula(ak.getAula_id(),ak.getUc_id(),ak.getTurno_id(),presencas));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return valueSet;
    }

    @Override
    public Set<Entry<AulaKey, Aula>> entrySet() {
        Set<Entry<AulaKey,Aula>> entrySet = new HashSet<>();
        Set<AulaKey> keySet = this.keySet();
        for(AulaKey ak : keySet){
            Aula a = this.get(ak);
            entrySet.add(new AbstractMap.SimpleEntry<>(ak,a));
        }
        return entrySet;
    }

    public int maxID(String uc, int turno) {
        int maxID = 0;
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT max(`id`) FROM `Aula` WHERE UC_id=? AND `Turno_id`=?;");
            stm.setString(1,uc);
            stm.setInt(2,turno);
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
