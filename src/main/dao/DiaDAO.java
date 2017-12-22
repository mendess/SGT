package main.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DiaDAO {
    public void initDias() {
        Connection connection = Connect.connect();
        if (connection == null) return;
        Statement stm = null;
        try {
            stm = connection.createStatement();
            stm.execute("" +
                    "INSERT IGNORE INTO DiaSemana (dia) " +
                    "VALUES ('SEG')," +
                    "       ('TER')," +
                    "       ('QUA')," +
                    "       ('QUI')," +
                    "       ('SEX')," +
                    "       ('SAB')," +
                    "       ('DOM');");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(stm);
        } finally {
            Connect.close(connection);
        }
    }
}
