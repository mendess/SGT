package main.dao;

import main.sgt.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserDAO implements Map<String, Utilizador> {
    private Connection connection;

    @Override
    public int size() {
        connection = Connect.connect();
        int i = -1;
        if (connection == null) return i;
        Statement stm = null;
        try {
            stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Aluno");
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
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
        this.connection = Connect.connect();
        if (connection == null) return false;
        if (!(key instanceof String)) {
            return false;
        }
        String user = (String) key;
        boolean r = false;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("" +
                    "SELECT `id` FROM `Utilizador` WHERE `id`=?;");
            stm.setString(1, user);
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
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
    public Utilizador get(Object key) {
        connection = Connect.connect();
        if (connection == null) return null;
        String user;
        if (key instanceof String) {
            user = (String) key;
        } else if (key instanceof Utilizador) {
            user = ((Utilizador) key).getUserNum();
        } else {
            return null;
        }
        Utilizador u = null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(
                    "SELECT * FROM Utilizador \n" +
                            "LEFT JOIN Docente ON Utilizador.id = Docente.Utilizador_id \n" +
                            "LEFT JOIN Aluno ON Utilizador.id = Aluno.Utilizador_id \n" +
                            "LEFT JOIN DiretorDeCurso ON Utilizador.id = DiretorDeCurso.Utilizador_id\n" +
                            "WHERE id=?");
            stm.setString(1, user);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String password = rs.getString("pass");
                String email = rs.getString("Email");
                if (rs.getString("Docente.Utilizador_id") != null) {
                    Map<String, List<TurnoKey>> ucsEturnos = this.getUCsETurnosDocente(id);
                    // Descobrir se ele é docente ou coordenador
                    String ucRegida = this.getUcRegida(id);
                    u = ucRegida == null ?
                            new Docente(id, password, email, nome, ucsEturnos) :
                            new Coordenador(id, password, email, nome, ucsEturnos, ucRegida);
                } else if (rs.getString("Aluno.Utilizador_id") != null) {
                    Map<String, Integer> inscricoes = this.getInscricoesAluno(id);
                    u = new Aluno(id, password, email, nome, rs.getBoolean("eEspecial"), inscricoes);
                } else if (rs.getString("DiretorDeCurso.Utilizador_id") != null) {
                    u = new DiretorDeCurso(id, password, email, nome);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return u;
    }

    private String getUcRegida(String id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "SELECT id FROM UC WHERE responsavel_id=?");
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        if(rs.next()){
            return rs.getString(1);
        }else{
            return null;
        }
    }

    private Map<String, List<TurnoKey>> getUCsETurnosDocente(String id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT id,UC_id,ePratico FROM Turno\n" +
                        "WHERE Turno.Docente_id=?;");
        Map<String, List<TurnoKey>> ucsEturnos = new HashMap<>();
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        while (rs.next()){
            String uc_id = rs.getString("UC_id");
            if(ucsEturnos.containsKey(uc_id)){
                ucsEturnos.get(uc_id).add(new TurnoKey(uc_id,
                                                       rs.getInt("id"),
                                                       rs.getBoolean("ePratico")));
            }else{
                List<TurnoKey> turnos = new ArrayList<>();
                turnos.add(new TurnoKey(uc_id,
                                        rs.getInt("id"),
                                        rs.getBoolean("ePratico")));
                ucsEturnos.put(uc_id,turnos);
            }
        }
        return ucsEturnos;
    }

    private Map<String, Integer> getInscricoesAluno(String id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM Turno_has_Aluno\n" +
                        "WHERE Aluno_id=? AND ePratico=TRUE;");
        Map<String,Integer> inscricoes = new HashMap<>();
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        while(rs.next()){
            inscricoes.put(rs.getString("UC_id"),rs.getInt("Turno_id"));
        }
        return inscricoes;
    }

    @Override
    public Utilizador put(String key, Utilizador value) {
        connection = Connect.connect();
        if (connection == null) return null;
        Utilizador u = null;
        PreparedStatement stmCoord = null;
        PreparedStatement stmTurnos = null;
        PreparedStatement stm1 = null;
        PreparedStatement stm2 = null;
        try {
            connection.setAutoCommit(false);
            //language=MySQL
            String sql =
                    "INSERT INTO `Utilizador` (id,nome,pass,Email) \n" +
                            "VALUES (?, ?, ?, ?)\n" +
                            "ON DUPLICATE KEY UPDATE id=VALUES(id),\n" +
                            "                        pass=VALUES(pass),\n" +
                            "                        nome=VALUES(nome),\n" +
                            "                        Email=VALUES(Email);\n";
            String sql2;
            stmTurnos = null;
            if (value instanceof Docente) {
                //language=MySQL
                sql2 = "INSERT INTO `Docente` (Utilizador_id)\n" +
                        "VALUES (?)\n" +
                        "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id);" +
                        "UPDATE Turno SET Docente_id=NULL WHERE Docente_id=?";
                stmTurnos = updateTurnosDocente((Docente) value);
            } else if (value instanceof Aluno) {
                sql2 = "INSERT INTO `Aluno` (Utilizador_id, eEspecial)\n" +
                        "VALUES (?,?)\n" +
                        "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id), eEspecial=VALUES(eEspecial);" +
                        "DELETE FROM Turno_has_Aluno WHERE Aluno_id=? AND ePratico=TRUE";
                stmTurnos = updateTurnosAluno((Aluno) value);
            } else if (value instanceof DiretorDeCurso) {
                sql2 = "INSERT INTO `DiretorDeCurso` (Utilizador_id)\n" +
                        "VALUES (?)" +
                        "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id);\n";
            } else {
                return null;
            }
            stm1 = connection.prepareStatement(sql);
            stm1.setString(1, value.getUserNum());
            stm1.setString(2, value.getName());
            stm1.setString(3, value.getPassword());
            stm1.setString(4, value.getEmail());
            stm1.executeUpdate();

            stm2 = connection.prepareStatement(sql2);
            stm2.setString(1, value.getUserNum());
            if (value instanceof Docente) {
                stm2.setString(2, value.getUserNum());
            }
            if (value instanceof Aluno) {
                stm2.setBoolean(2, ((Aluno) value).eEspecial());
                stm2.setString(3, value.getUserNum());
            }
            stm2.executeUpdate();

            if (stmTurnos != null) {
                stmTurnos.executeBatch();
            }

            if (value instanceof Coordenador) {
                stmCoord = connection.prepareStatement("" +
                        "UPDATE UC SET responsavel_id=? WHERE id=?;");
                stmCoord.setString(1, value.getUserNum());
                stmCoord.setString(2, ((Coordenador) value).getUcRegida());
                stmCoord.executeUpdate();
            }

            connection.commit();
            u = value;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm1);
            System.out.println(stm2);
            System.out.println(stmTurnos);
            System.out.println(stmCoord);
        } finally {
            Connect.close(connection);
        }
        return u;
    }

    private PreparedStatement updateTurnosAluno(Aluno value) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "INSERT INTO Turno_has_Aluno (Turno_id, UC_id, Aluno_id,ePratico) " +
                "VALUES (?,?,?,TRUE);");
        for(String uc : value.getHorario().keySet()){
            Integer turno = value.getHorario().get(uc);
            stm.setInt(1,turno);
            stm.setString(2,uc);
            stm.setString(3,value.getUserNum());
            stm.addBatch();
        }
        return stm;
    }

    private PreparedStatement updateTurnosDocente(Docente value) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "UPDATE Turno SET Docente_id=? WHERE id=? AND UC_id=? AND ePratico=?;\n");
        for(String uc : value.getUcsEturnos().keySet()){
            List<TurnoKey> turnos = value.getUcsEturnos().get(uc);
            for (TurnoKey turno : turnos){
                stm.setString(1,value.getUserNum());
                stm.setInt(2,turno.getTurno_id());
                stm.setString(3,uc);
                stm.setBoolean(4,turno.ePratico());
                stm.addBatch();
            }
        }
        return stm;
    }

    @Override
    public Utilizador remove(Object key) {
        Utilizador u = this.get(key);
        connection = Connect.connect();
        if(u==null && connection==null) return null;
        try {
            if(u instanceof Coordenador){
                PreparedStatement stm = connection.prepareStatement(""+
                        "UPDATE UC\n" +
                        "SET responsavel_id = NULL\n" +
                        "WHERE responsavel_id=?");
                stm.setString(1,u.getUserNum());
                stm.executeUpdate();
            }
            PreparedStatement stm = connection.prepareStatement(""+
                    "DELETE FROM Presencas WHERE Aluno_id=?;\n" +
                    "DELETE FROM Trocas WHERE aluno_id=?;\n" +
                    "UPDATE Turno SET Docente_id=NULL WHERE Docente_id=?;\n" +
                    "DELETE FROM Turno_has_Aluno WHERE Aluno_id=? AND TRUE;\n" +
                    "DELETE FROM Pedido WHERE Aluno_id=?;\n" +
                    "DELETE FROM Docente WHERE Utilizador_id=?;\n" +
                    "DELETE FROM DiretorDeCurso WHERE Utilizador_id=?;\n" +
                    "DELETE FROM Aluno WHERE Utilizador_id=?;\n" +
                    "DELETE FROM Utilizador WHERE id=?;");
            String uNum = u.getUserNum();
            for(int i=1;i<10;i++){
                stm.setString(i,uNum);
            }
            stm.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Utilizador> m) {
        m.values().forEach(u -> this.put(u.getUserNum(), u));
    }

    @Override
    public void clear() {
        this.keySet().forEach(this::remove);
    }

    @Override
    public Set<String> keySet() {
        connection = Connect.connect();
        Set<String> keySet = new HashSet<>();
        if (connection == null) return keySet;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("" +
                    "SELECT id FROM `Utilizador`;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                keySet.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return keySet;
    }

    @Override
    public Collection<Utilizador> values() {
        return this.keySet()
                .stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<String, Utilizador>> entrySet() {
        Set<Entry<String,Utilizador>> entrySet = new HashSet<>();
        this.keySet().forEach(uk -> {
            Utilizador u = this.get(uk);
            entrySet.add(new AbstractMap.SimpleEntry<>(uk, u));
        });
        return entrySet;
    }
}
