package main.dao;

import main.sgt.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class TurnoDAO implements Map<TurnoKey,Turno> {
    private Connection connection;
    @Override
    public int size() {
        connection = Connect.connect();
        int i = -1;
        if(connection==null) return i;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM Turno");
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
        this.connection = Connect.connect();
        if(connection==null) return false;
        if(!(key instanceof TurnoKey)){
            return false;
        }
        TurnoKey turno = (TurnoKey) key;
        boolean r = false;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT `id` FROM `Turno` WHERE `id`=? AND UC_id=? AND ePratico=?;");
            stm.setInt(1, turno.getTurno_id());
            stm.setString(2,turno.getUc_id());
            stm.setBoolean(3,turno.ePratico());
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Connect.close(connection);
        }
        return r;    }

    @Override
    public boolean containsValue(Object value) {
        return value.equals(this.get(value));
    }

    @Override
    public Turno get(Object key) {
        this.connection = Connect.connect();
        if(connection==null) return null;
        TurnoKey tKey;
        if(key instanceof TurnoKey){
            tKey= (TurnoKey) key;
        }else if(key instanceof Turno){
            tKey = new TurnoKey((Turno) key);
        }else{
            return null;
        }
        Turno t = null;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT Turno.id AS T_id," +
                    "       Turno.UC_id AS uc," +
                    "       Turno.Docente_id AS docente," +
                    "       Turno.vagas AS vagas," +
                    "       Turno.ePratico AS ePratico," +
                    "       Turno_has_Aluno.Aluno_id AS aluno," +
                    "       TurnoInfo.horaInicio AS horaInicio," +
                    "       TurnoInfo.horaFim AS horaFim," +
                    "       TurnoInfo.dia_id AS dia\n" +
                    "FROM Turno\n" +
                    "   LEFT JOIN Turno_has_Aluno ON Turno.id = Turno_has_Aluno.Turno_id AND Turno.UC_id = Turno_has_Aluno.UC_id\n" +
                    "   LEFT JOIN TurnoInfo ON Turno.id = TurnoInfo.Turno_id AND Turno.UC_id = TurnoInfo.UC_id AND Turno.ePratico = TurnoInfo.ePratico" +
                    "   WHERE Turno.UC_id=? AND Turno.id=? AND Turno.ePratico=?;");
            stm.setString(1,tKey.getUc_id());
            stm.setInt(2,tKey.getTurno_id());
            stm.setBoolean(3,tKey.ePratico());
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                int id = rs.getInt("T_id");
                String uc = rs.getString("uc");
                String docente = rs.getString("docente");
                int vagas = rs.getInt("vagas");
                boolean ePratico = rs.getBoolean("ePratico");
                List<String> alunos = new ArrayList<>();
                List<TurnoInfo> tinfo = new ArrayList<>();
                do{
                    String aluno = rs.getString("aluno");
                    if(aluno!=null && !alunos.contains(aluno)) alunos.add(aluno);

                    TurnoInfo turnoInfo = this.getTinfo(rs);
                    if(turnoInfo!=null && !tinfo.contains(turnoInfo)) tinfo.add(turnoInfo);

                }while (rs.next());
                t = new Turno(id,uc,docente,vagas,ePratico,alunos,tinfo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return t;
    }

    private TurnoInfo getTinfo(ResultSet rs) throws SQLException {
        try{
            return new TurnoInfo(rs.getTime("horaInicio").toLocalTime(),
                                 rs.getTime("horaFim").toLocalTime(),
                                 DiaSemana.fromString(rs.getString("dia")));
        }catch (NullPointerException e){
            return null;
        }
    }

    @Override
    public Turno put(TurnoKey key, Turno value) {
        this.connection = Connect.connect();
        if(connection==null) return null;
        Turno t = null;
        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("" +
                    "INSERT INTO Turno (id, UC_id, Docente_id, vagas, ePratico)\n" +
                    "   VALUES (?,?,?,?,?)\n" +
                    "ON DUPLICATE KEY UPDATE id=VALUES(id)," +
                    "                        UC_id=VALUES(UC_id)," +
                    "                        Docente_id=VALUES(Docente_id)," +
                    "                        vagas=VALUES(vagas)," +
                    "                        ePratico=VALUES(ePratico);\n"+
                    "DELETE FROM TurnoInfo WHERE UC_id=? AND Turno_id=? AND ePratico=?;\n" +
                    "DELETE FROM Turno_has_Aluno WHERE UC_id=? AND Turno_id=? AND ePratico=?");
            PreparedStatement stmTinfo = this.updateTInfos(value);
            PreparedStatement stmAlunos = this.updateAlunos(value);
            //INSERT
            stm.setInt(1,value.getId());
            stm.setString(2,value.getUcId());
            stm.setString(3,value.getDocente());
            stm.setInt(4,value.getVagas());
            stm.setBoolean(5,value.ePratico());
            //DELETE 1
            stm.setString(6,value.getUcId());
            stm.setInt(7,value.getId());
            stm.setBoolean(8,value.ePratico());
            //DELETE 2
            stm.setString(9,value.getUcId());
            stm.setInt(10,value.getId());
            stm.setBoolean(11,value.ePratico());

            stm.executeUpdate();
            stmTinfo.executeBatch();
            stmAlunos.executeBatch();
            connection.commit();
            t = value;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return t;
    }

    private PreparedStatement updateAlunos(Turno value) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "INSERT INTO Turno_has_Aluno (Turno_id, UC_id, Aluno_id,ePratico) " +
                "   VALUES (?,?,?,?);");
        for(String a: value.getAlunos()){
            stm.setInt(1,value.getId());
            stm.setString(2,value.getUcId());
            stm.setString(3,a);
            stm.setBoolean(4,value.ePratico());
            stm.addBatch();
        }
        return stm;
    }

    private PreparedStatement updateTInfos(Turno value) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("" +
                "INSERT INTO TurnoInfo (id, Turno_id, UC_id, dia_id, horaInicio, horaFim,ePratico) " +
                "   VALUES (?,?,?,?,?,?,?)" +
                "ON DUPLICATE KEY UPDATE id=VALUES(id)," +
                "                        Turno_id=VALUES(Turno_id)," +
                "                        UC_id=VALUES(UC_id)," +
                "                        dia_id=VALUES(dia_id)," +
                "                        horaInicio=VALUES(horaInicio)," +
                "                        horaFim=VALUES(horaFim)," +
                "                        ePratico=VALUES(ePratico);");
        int i=1;
        for(TurnoInfo ti: value.getTurnoInfos()){
            stm.setInt(1,i);
            stm.setInt(2,value.getId());
            stm.setString(3,value.getUcId());
            stm.setString(4,DiaSemana.toString(ti.getDia()));
            stm.setTime(5,Time.valueOf(ti.getHoraInicio()));
            stm.setTime(6,Time.valueOf(ti.getHoraFim()));
            stm.setBoolean(7,value.ePratico());
            stm.addBatch();
        }
        return stm;
    }

    @Override
    public Turno remove(Object key) {
        Turno t = this.get(key);
        connection = Connect.connect();
        if(t==null && connection==null) return null;
        try {
            for (Aula a : t.getAulas()) {
                new AulaDAO().remove(new AulaKey(a));
            }
            PreparedStatement stm = connection.prepareStatement("" +
            "DELETE FROM TurnoInfo       WHERE UC_id=? AND Turno_id=? AND ePratico=?;\n" +
            "DELETE FROM Turno_has_Aluno WHERE UC_id=? AND Turno_id=? AND ePratico=?;\n" +
            "DELETE FROM Pedido          WHERE UC_id=? AND Turno_id=? AND ePratico=?;\n" +
            "DELETE FROM Trocas          WHERE UC_id=? AND ((turnoDestino_id=? AND turnoDestino_ePratico=?) " +
            "                                            OR (turnoOrigem_id=?  AND turnoOrigem_ePratico=?));\n" +
            "DELETE FROM Turno           WHERE UC_id=? AND id=?       AND ePratico=?;\n");

            for(int i=1;i<21;i+=3){
                stm.setString(i,t.getUcId());
                stm.setInt(i+1,t.getId());
                stm.setBoolean(i+2,t.ePratico());
                if(i==7) {
                    stm.setInt(i+3, t.getId());
                    stm.setBoolean(i+4,t.ePratico());
                    i+=2;
                }
            }
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return t;
    }

    @Override
    public void putAll(Map<? extends TurnoKey, ? extends Turno> m) {
        m.values().forEach(t -> this.put(new TurnoKey(t.getUcId(),t.getId(),t.ePratico()), t));
    }

    @Override
    public void clear() {
        this.keySet().forEach(this::remove);
    }

    @Override
    public Set<TurnoKey> keySet() {
        connection = Connect.connect();
        Set<TurnoKey> keySet = new HashSet<>();
        if(connection==null) return keySet;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT id,UC_id,ePratico FROM Turno;");
            ResultSet rs = stm.executeQuery();
            while (rs.next()){
                keySet.add(new TurnoKey(rs.getString(2),rs.getInt(1),rs.getBoolean(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return keySet;
    }

    @Override
    public Collection<Turno> values() {
        return this.keySet()
                .stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<TurnoKey, Turno>> entrySet() {
        Set<Entry<TurnoKey,Turno>> entrySet = new HashSet<>();
        this.keySet().forEach(uk -> {
            Turno u = this.get(uk);
            entrySet.add(new AbstractMap.SimpleEntry<>(uk, u));
        });
        return entrySet;
    }

    public int maxID(String uc, boolean ePratico) {
        connection = Connect.connect();
        if(connection==null) return -1;
        int i = -1;
        try {
            PreparedStatement stm = connection.prepareStatement("" +
                    "SELECT max(id) FROM Turno WHERE UC_id=? AND ePratico=?;");
            stm.setString(1,uc);
            stm.setBoolean(2,ePratico);
            ResultSet rs = stm.executeQuery();
            if(rs.next()){
                i = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Connect.close(connection);
        }
        return i;
    }
}
