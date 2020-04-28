package br.com.golfinvest.data.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class ActivationLogDAO {

    String aws;
    String user;
    String pass;

    public ActivationLogDAO(String aws, String user, String pass){
        this.aws = aws;
        this.user = user;
        this.pass = pass;
    }

        public boolean validateCredential(String credential) {
            boolean result = false;
            try {
                String sql = "select activation from access_validation where credential = ?";
                Connection con = new ConnectionFactory(aws, user, pass).getConnectionAWS();
                PreparedStatement stmt = con.prepareStatement(sql);

                stmt.setString(1,credential);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    result = rs.getBoolean("activation");
                }
                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.getStackTrace());
            }
            return result;
    }

    public void logRegister(boolean result){
        try {
            String sql = "INSERT INTO golf_log(dia, log_activation) VALUES(?, ?)";

            Connection con = new ConnectionFactory(aws, user, pass).getConnectionAWS();
            PreparedStatement stmt = con.prepareStatement(sql);

            Calendar cal = Calendar.getInstance();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(cal.getTimeInMillis());

            stmt.setTimestamp(1, timestamp);
            stmt.setBoolean(2, result);

            stmt.execute();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.out.println(ex.getStackTrace());
        }
    }
}
