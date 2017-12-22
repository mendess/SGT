package main.dao;

import main.sgt.Turno;
import main.sgt.TurnoKey;
import main.sgt.UC;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class UCDAO implements Map<String, UC> {
    private Connection connection;
    @Override
    public int size() {
        connection = Connect.connect();
        int i = -1;
        if(connection==null) return i;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM UC");
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (SQLException e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(connection);
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        connection = Connect.connect();
        if(connection==null) return false;
        if(!(key instanceof String)){
            return false;
        }
        String user = (String) key;
        boolean r = false;
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT `id` FROM `UC` WHERE `id`=?;");
            stm.setString(1, user);
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (Exception e) {
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
    public UC get(Object key) {
        connection = Connect.connect();
        if(connection==null) return null;
        String user;
        if(key instanceof String){
            user = (String) key;
        }else if(key instanceof UC){
            user = ((UC) key).getId();
        }else{
            return null;
        }
        UC uc = null;
        try {
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT UC.id AS UC_id," +
                            "  UC.nome AS nome," +
                            "  UC.acron AS acron," +
                            "  UC.responsavel_id AS responsavel," +
                            "  Turno.id AS turno," +
                            "  Turno.Docente_id AS docente," +
                            "  Turno_has_Aluno.Aluno_id AS aluno\n" +
                            "FROM UC " +
                            "INNER JOIN Turno ON UC.id = Turno.UC_id\n" +
                            "LEFT JOIN Turno_has_Aluno ON Turno.id = Turno_has_Aluno.Turno_id AND Turno.UC_id = Turno_has_Aluno.UC_id\n" +
                            "WHERE UC.id=?");
            stm.setString(1 , user);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String id = rs.getString("UC_id");
                String nome = rs.getString("nome");
                String acron = rs.getString("acron");
                String responsavel_id = rs.getString("responsavel");

                List<String> docentes = new ArrayList<>();
                List<String> alunos = new ArrayList<>();
                do{
                    String docente = rs.getString("docente");
                    if(docente!=null && !docentes.contains(docente)){
                        docentes.add(docente);
                    }
                    String aluno = rs.getString("aluno");
                    if(aluno!=null && !alunos.contains(aluno)){
                        alunos.add(aluno);
                    }
                }while (rs.next());
                uc = new UC(id,nome,acron,responsavel_id,docentes,alunos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return uc;
    }

    @Override
    public UC put(String key, UC value) {
        UC uc = null;
        this.connection = Connect.connect();
        if(this.connection==null) return null;
        try {
            PreparedStatement stm = connection.prepareStatement(""+
                    "INSERT INTO `UC`(id, nome, acron, responsavel_id) \n" +
                            "VALUES (?, ?, ?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE id=VALUES(id),\n" +
                            "                        nome=VALUES(nome),\n" +
                            "                        acron=VALUES(acron),\n" +
                            "                        responsavel_id=VALUES(responsavel_id);\n");
            stm.setString(1,value.getId());
            stm.setString(2,value.getNome());
            stm.setString(3,value.getAcron());
            stm.setString(4,value.getResponsavel());
            stm.executeUpdate();
            uc = value;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return uc;
    }

    @Override
    public UC remove(Object key) {
        UC uc = this.get(key);
        this.connection = Connect.connect();
        if(uc==null && connection==null) return null;
        try {
            for (Turno t : uc.getTurnos()) {
                new TurnoDAO().remove(new TurnoKey(t));
            }
            PreparedStatement stm = connection.prepareStatement("" +
                    "DELETE FROM UC WHERE id=?;");
            stm.setString(1,uc.getId());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends UC> m) {
        m.values().forEach(uc -> this.put(uc.getId(), uc));
    }

    @Override
    public void clear() {
        this.keySet().forEach(this::remove);
    }

    @Override
    public Set<String> keySet() {
        this.connection = Connect.connect();
        Set<String> keySet = new HashSet<>();
        if(this.connection==null) return keySet;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT id FROM UC;");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                keySet.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return keySet;
    }

    @Override
    public Collection<UC> values() {
        return this.keySet()
                .stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Set<Entry<String, UC>> entrySet() {
        Set<Entry<String,UC>> entrySet = new HashSet<>();
        Set<String> keySet = this.keySet();
        for(String uck : keySet){
            UC uc = this.get(uck);
            entrySet.add(new AbstractMap.SimpleEntry<>(uck,uc));
        }
        return entrySet;
    }
}
