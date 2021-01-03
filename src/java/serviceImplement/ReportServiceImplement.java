/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceImplement;

import connection.JDBCConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import service.ReportService;

/**
 *
 * @author ADMIN
 */
public class ReportServiceImplement implements ReportService {

    @Override
    public boolean addReport(int answerId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from report where aid = ?");) {

            state1.setInt(1, answerId);
            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                PreparedStatement state2 = connection.prepareStatement("update report set number = number + 1 where aid = ?");
                state2.setInt(1, answerId);
                state2.executeUpdate();
            } else {
                PreparedStatement state2 = connection.prepareStatement("insert into report (aid) values (?)");
                state2.setInt(1, answerId);
                state2.executeUpdate();
            }
            
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
