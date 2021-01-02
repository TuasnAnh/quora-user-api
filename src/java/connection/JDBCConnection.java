/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ADMIN
 */
public class JDBCConnection {

    static String host = "localhost";
    static int port = 3306;
    static String databaseName = "quora-app";

    public static Connection getConnection() {
        final String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName +"?useSSL = false";
        final String user = "root";
        final String password = "ainsoft99";

        try {
            Class.forName("com.mysql.jdbc.Driver"); 
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
