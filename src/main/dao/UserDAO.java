package main.dao;

import main.sgt.*;

import java.sql.*;
import java.util.*;

public class UserDAO implements Map<String, Utilizador> {
    private Connection connection;

    @Override
    public int size() {
        connection = Connect.connect();
        int i = 0;
        try {
            connection = Connect.connect();
            if (connection != null) {
                Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery("SELECT count(*) FROM Aluno");
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
        return this.size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        if(!(key instanceof String)){
            return false;
        }
        String user = (String) key;
        boolean r = false;
        try {
            this.connection = Connect.connect();
            String sql = "SELECT `id` FROM `Utilizador` WHERE `id`=?;";
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement(sql);
                stm.setString(1, user);
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
        return this.get(value).equals(value);
    }

    @Override
    public Utilizador get(Object key) {
        String user;
        if(key instanceof String){
            user = (String) key;
        }else if(key instanceof Utilizador){
            user = ((Utilizador) key).getUserNum();
        }else{
            return null;
        }
        Utilizador u = null;
        try {
            connection = Connect.connect();
            if (connection != null) {
                PreparedStatement stm = connection.prepareStatement(
                        "SELECT *\n" +
                                "FROM Utilizador \n" +
                                "LEFT JOIN Docente ON Utilizador.id = Docente.Utilizador_id \n" +
                                "LEFT JOIN Aluno ON Utilizador.id = Aluno.Utilizador_id \n" +
                                "LEFT JOIN DiretorDeCurso ON Utilizador.id = DiretorDeCurso.Utilizador_id\n" +
                                "WHERE id=?");
                stm.setString(1 , user);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    String id = rs.getString("id");
                    String nome = rs.getString("nome");
                    String password = rs.getString("Password");
                    String email = rs.getString("Email");
                    if(rs.getString("Docente.Utilizador_id")!=null){
                        // Descobrir se ele é docente ou coordenador
                        StringBuilder resp = new StringBuilder();
                        Map<String,List<Integer>> ucsEturnos = this.getUCsETurnosDocente(id,connection,resp);
                        u = resp.toString().isEmpty() ?
                                new Docente(id, password, email, nome, ucsEturnos) :
                                new Coordenador(id, password, email, nome, ucsEturnos, resp.toString());
                    }else if(rs.getString("Aluno.Utilizador_id")!=null){
                        Map<String,Integer> inscricoes = this.getInscricoesAluno(id,connection);
                        u = new Aluno(id,password,email,nome,rs.getBoolean("eEspecial"),inscricoes);
                    }else if(rs.getString("DiretorDeCurso.Utilizador_id")!=null){
                        u = new DiretorDeCurso(id,password,email,nome);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return u;
    }

    private Map<String, List<Integer>> getUCsETurnosDocente(String id, Connection connection, StringBuilder resp) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM Turno_has_Docente\n" +
                        "INNER JOIN UC ON Turno_has_Docente.UC_id = UC.id\n" +
                        "WHERE Turno_has_Docente.Docente_id=?;");
        Map<String,List<Integer>> ucsEturnos = new HashMap<>();
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        while (rs.next()){
            if(ucsEturnos.containsKey(rs.getString("UC_id"))){
                ucsEturnos.get(rs.getString("UC_id")).add(rs.getInt("Turno_id"));
            }else{
                List<Integer> turnos = new ArrayList<>();
                turnos.add(rs.getInt("Turno_id"));
                ucsEturnos.put(rs.getString("UC_id"),turnos);
            }
        }
        return ucsEturnos;
    }

    private Map<String, Integer> getInscricoesAluno(String id, Connection connection) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(
                "SELECT * FROM Turno_has_Aluno\n" +
                        "WHERE Aluno_id=?;");
        Map<String,Integer> inscricoes = new HashMap<>();
        stm.setString(1,id);
        ResultSet rs = stm.executeQuery();
        while(rs.next()){
            inscricoes.put(rs.getString("UC_id"),rs.getInt("idTurno"));
        }
        return inscricoes;
    }

    @Override
    public Utilizador put(String key, Utilizador value) {
        Utilizador u = null;
        try {
            connection = Connect.connect();
            //language=MySQL
            String sql = "START TRANSACTION;\n" +
                    "INSERT INTO `Utilizador` (id,nome,Password,Email)\n" +
                    "VALUES (?, ?, ?, ?)\n" +
                    "ON DUPLICATE KEY UPDATE id=VALUES(id),\n" +
                    "                        nome=VALUES(nome),\n" +
                    "                        Password=VALUES(Password),\n" +
                    "                        Email=VALUES(Email);\n";
            StringBuilder s = new StringBuilder(sql);
            if (value instanceof Docente){
                s.append("INSERT INTO `Docente` (Utilizador_id)\n" +
                         "VALUES (?)\n" +
                         "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id);");
            }else if (value instanceof Aluno){
                s.append("INSERT INTO `Aluno` (Utilizador_id, eEspecial)\n" +
                         "VALUES (?,?)\n" +
                         "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id), eEspecial=VALUES(eEspecial);");
            }else if (value instanceof DiretorDeCurso){
                s.append("INSERT INTO `DiretorDeCurso` (Utilizador_id)\n" +
                         "VALUES (?)" +
                         "ON DUPLICATE KEY UPDATE Utilizador_id=VALUES(Utilizador_id);\n");
            }else{
                return null;
            }

            PreparedStatement stm = connection.prepareStatement(s.toString(), Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getUserNum());
            stm.setString(2, value.getName());
            stm.setString(3, value.getPassword());
            stm.setString(4, value.getEmail());
            stm.setString(5,value.getUserNum());
            if(value instanceof Aluno) stm.setBoolean(6,((Aluno) value).eEspecial());

            stm.executeUpdate();

            u = value;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return u;
    }

    @Override
    public Utilizador remove(Object key) {
        Utilizador u = this.get(key);
        try {
            PreparedStatement stm = connection.prepareStatement(
            "START TRANSACTION;\n" +
                    "DELETE FROM Presencas WHERE Aluno_id=?;\n" +
                    "DELETE FROM Trocas WHERE aluno_id=?;\n" +
                    "DELETE FROM Turno_has_Docente WHERE Docente_id=?;\n" +
                    "DELETE FROM Turno_has_Aluno WHERE Aluno_id=?;\n" +
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
        for(Utilizador u : m.values()){
            this.put(u.getUserNum(),u);
        }
    }

    @Override
    public void clear() {
        try {
            PreparedStatement stm = connection.prepareStatement(
                    "START TRANSACTION;\n" +
                            "DELETE FROM Presencas WHERE TRUE;\n" +
                            "DELETE FROM Trocas WHERE TRUE;\n" +
                            "DELETE FROM Turno_has_Docente WHERE TRUE;\n" +
                            "DELETE FROM Turno_has_Aluno WHERE TRUE;\n" +
                            "DELETE FROM Pedido WHERE TRUE;\n" +
                            "DELETE FROM Docente WHERE TRUE;\n" +
                            "DELETE FROM DiretorDeCurso WHERE TRUE;\n" +
                            "DELETE FROM Aluno WHERE TRUE;\n" +
                            "DELETE FROM Utilizador WHERE TRUE;");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
    }

    @Override
    public Set<String> keySet() {
        Set<String> keySet = new HashSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT id FROM `Utilizador`;");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                keySet.add(rs.getString(1));
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return keySet;
    }

    @Override
    public Collection<Utilizador> values() {
        Collection<Utilizador> values = new HashSet<>();
        Set<String> keySet = this.keySet();
        for(String k : keySet){
            values.add(this.get(k));
        }
        return values;
    }

    @Override
    public Set<Entry<String, Utilizador>> entrySet() {
        Set<Entry<String,Utilizador>> entrySet = new HashSet<>();
        Set<String> keySet = this.keySet();
        for(String uk : keySet){
            Utilizador u = this.get(uk);
            entrySet.add(new AbstractMap.SimpleEntry<>(uk,u));
        }
        return entrySet;
    }
}
