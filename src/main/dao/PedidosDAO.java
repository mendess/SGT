package main.dao;

import main.sgt.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PedidosDAO implements Map<String,List<Pedido>> {
    private Connection connection;
    @Override
    public int size() {
        this.connection = Connect.connect();
        if (this.connection == null) return -1;
        int i = -1;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "SELECT count(*) FROM Pedido;");
            ResultSet rs = stm.executeQuery();
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
        if (!(key instanceof String)) return false;
        boolean r = false;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "SELECT `UC_id` FROM Pedido" +
                    "   WHERE UC_id=?;");
            stm.setString(1, (String) key);
            r = stm.executeQuery().next();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Pedido> get(Object key) {
        String pKey;
        if (key instanceof String) {
            pKey = (String) key;
        } else {
            return null;
        }
        this.connection = Connect.connect();
        if (connection == null) return null;
        List<Pedido> pedidos = null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "SELECT * FROM Pedido" +
                    "       WHERE UC_id=?");
            stm.setString(1, pKey);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                pedidos = new ArrayList<>();
                do {
                    String aluno = rs.getString("Aluno_id");
                    int turno = rs.getInt("Turno_id");
                    String uc = rs.getString("UC_id");
                    boolean ePratico = rs.getBoolean("ePratico");
                    pedidos.add(new Pedido(aluno,
                            new UserDAO().get(aluno).getName(),
                            uc,
                            turno,
                            ePratico));
                } while (rs.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return pedidos;
    }

    @Override
    public List<Pedido> put(String key, List<Pedido> value) {
        this.connection = Connect.connect();
        if (connection == null) return null;
        List<Pedido> pedidos = null;
        PreparedStatement stmRem = null;
        PreparedStatement stmAdd = null;
        try {
            connection.setAutoCommit(false);
            stmRem = connection.prepareStatement("\n" +
                    "DELETE FROM Pedido WHERE UC_id=?");
            stmRem.setString(1, key);
            stmAdd = connection.prepareStatement("\n" +
                    "INSERT INTO Pedido (Aluno_id, Turno_id, UC_id,ePratico) " +
                    "   VALUES (?,?,?,?)");
            for (Pedido p : value) {
                stmAdd.setString(1, p.getAlunoNum());
                stmAdd.setInt(2, p.getTurno());
                stmAdd.setString(3, p.getUc());
                stmAdd.setBoolean(4, p.ePratico());
                stmAdd.addBatch();
            }
            stmRem.executeUpdate();
            stmAdd.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stmRem);
            System.out.println(stmAdd);
        } finally {
            Connect.close(connection);
        }
        return pedidos;
    }

    public Pedido put (Pedido value) {
        this.connection = Connect.connect();
        if (connection == null) return null;
        Pedido pedido = null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "INSERT INTO `Pedido` (Aluno_id, Turno_id, UC_id,ePratico)" +
                    "   VALUES (?,?,?,?);");
            stm.setString(1, value.getAlunoNum());
            stm.setInt(2, value.getTurno());
            stm.setString(3, value.getUc());
            stm.setBoolean(4, value.ePratico());
            stm.executeUpdate();
            pedido = value;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return pedido;
    }
    @Override
    public List<Pedido> remove(Object key) {
        List<Pedido> pedidos = this.get(key);
        this.connection = Connect.connect();
        if (pedidos == null || connection == null) return null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "DELETE FROM Pedido WHERE UC_id=?");
            stm.setString(1, pedidos.get(0).getUc());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return pedidos;
    }
    public Pedido remove(Pedido pedido) {
        this.connection = Connect.connect();
        if (connection == null) return null;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "DELETE FROM Pedido WHERE UC_id=? AND Turno_id=? AND Aluno_id=? AND ePratico=?");
            stm.setString(1, pedido.getUc());
            stm.setInt(2, pedido.getTurno());
            stm.setString(3, pedido.getAlunoNum());
            stm.setBoolean(4, pedido.ePratico());
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
        return pedido;
    }
    @Override
    public void putAll(Map<? extends String, ? extends List<Pedido>> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        this.keySet().forEach(this::remove);
    }

    @Override
    public Set<String> keySet() {
        this.connection = Connect.connect();
        Set<String> keySet = new HashSet<>();
        if (connection == null) return keySet;
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("\n" +
                    "SELECT UC_id FROM Pedido;");
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
    public Collection<List<Pedido>> values() {
        return this.keySet()
                .stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Entry<String, List<Pedido>>> entrySet() {
        Set<Entry<String,List<Pedido>>> entrySet = new HashSet<>();
        this.keySet().forEach(uk -> {
            List<Pedido> pedidos = this.get(uk);
            entrySet.add(new AbstractMap.SimpleEntry<>(uk, pedidos));
        });
        return entrySet;
    }
}
