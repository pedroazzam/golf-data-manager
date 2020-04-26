package br.com.golfinvest.data.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String caminhoBD = "new_database.db";////vlg01db.mysql.uhserver.com/vlg01db";
            return DriverManager.getConnection("jdbc:sqlite:" + caminhoBD);
        } catch (Exception e) {
            System.out.println("nao conectou...........................");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
