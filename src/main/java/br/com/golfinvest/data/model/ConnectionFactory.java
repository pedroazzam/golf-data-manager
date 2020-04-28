package br.com.golfinvest.data.model;

import br.com.golfinvest.data.config.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    String aws;
    String user;
    String pass;

    public ConnectionFactory(String aws, String user, String pass){
        this.aws = aws;
        this.user = user;
        this.pass = pass;
    }
    public ConnectionFactory(){

    }

    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            String caminhoBD = "new_database.db";
            return DriverManager.getConnection("jdbc:sqlite:" + caminhoBD);
        } catch (Exception e) {
            System.out.println("nao conectou...........................");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnectionAWS() {
        try {
//            Class.forName("com.mysql.jdbc.Driver");
            String caminhoBD = aws;
            return DriverManager.getConnection("jdbc:mysql:" + caminhoBD, user, pass);
        } catch (Exception e) {
            System.out.println("nao conectou...........................");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
