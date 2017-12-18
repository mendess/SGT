/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.dao;
 
import java.sql.Connection;
import java.sql.DriverManager;

class Connect {
    private static final String URL = "localhost";
    private static final String DATABASE = "SGT";
    private static final String USERNAME = "aplicacao";
    private static final String PASSWORD = "ap123";
    
    static Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://"+URL
                    +"/"+DATABASE
                    +"?user="+USERNAME
                    +"&password="+PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static void close(Connection connection) {
        try {
            connection.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
