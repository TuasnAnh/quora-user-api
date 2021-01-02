/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceImplement;

import com.mysql.cj.protocol.Resultset;
import connection.JDBCConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Notification;
import service.NotificationService;

/**
 *
 * @author ADMIN
 */
public class NotificationServiceImplement implements NotificationService {

    @Override
    public boolean addVoteNotification(int userId, String type, int answerId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("insert into notification (uid, qid, aid, notitype, content) values (?, ?, ?, ?, ?)");) {

            PreparedStatement state2 = connection.prepareStatement("select * from notification where uid = ? and aid = ?");
            state2.setInt(1, userId);
            state2.setInt(2, answerId);
            ResultSet rs2 = state2.executeQuery();

            if (rs2.next()) {
                PreparedStatement state3 = connection.prepareStatement("delete from notification where uid = ? and aid = ?");
                state3.setInt(1, userId);
                state3.setInt(2, answerId);
                state3.executeUpdate();
            }

            String content = "";
            if ("NEW_UPVOTE".equals(type)) {
                content = "You have new upvote!";
            } else if ("NEW_DOWNVOTE".equals(type)) {
                content = "You have new downvote!";
            }

            PreparedStatement state4 = connection.prepareStatement("select qid from answer where aid = ?");
            state4.setInt(1, answerId);
            ResultSet rs4 = state4.executeQuery();
            rs4.next();

            state1.setInt(1, userId);
            state1.setInt(2, rs4.getInt("qid"));
            state1.setInt(3, answerId);
            state1.setString(4, type);
            state1.setString(5, content);

            state1.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean addNewAnswerNotice(int userId, String type, int questionId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("insert into notification (uid, qid, notitype, content) values (?, ?, ?, ?)");) {

            String content = "You have new answer!";
            state1.setInt(1, userId);
            state1.setInt(2, questionId);
            state1.setString(3, type);
            state1.setString(4, content);

            state1.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean removeNotification(int notiId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("delete from notification where nid = ?");) {

            state1.setInt(1, notiId);
            state1.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean markAsSeen(int userId, String type) {
        try (Connection connection = JDBCConnection.getConnection();) {

            PreparedStatement state1 = connection.prepareStatement("update notification set notistatus = ? where uid = ? and notitype = ?");

            if ("NEW_VOTE".equals(type)) {
                state1 = connection.prepareStatement("update notification set notistatus = ? where uid = ? and notitype = ? or notitype = ?");
                state1.setString(1, "SEEN");
                state1.setInt(2, userId);
                state1.setString(3, "NEW_UPVOTE");
                state1.setString(4, "NEW_DOWNVOTE");
            } else {
                state1.setString(1, "SEEN");
                state1.setInt(2, userId);
                state1.setString(3, type);
            }

            state1.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public int getTotalUnseenNotification(int userId) {
        int total = 0;
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select nid from notification where uid = ? and notistatus = ?");) {

            state1.setInt(1, userId);
            state1.setString(2, "UNSEEN");
            ResultSet rs = state1.executeQuery();

            while (rs.next()) {
                total += 1;
            }
            return total;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return total;
    }

    @Override
    public List<Notification> getNotification(int userId, String type) {
        try (Connection connection = JDBCConnection.getConnection();) {
            PreparedStatement state1 = connection.prepareStatement("select * from notification where uid = ? and notitype = ? order by nid desc");
            if ("NEW_VOTE".equals(type)) {
                state1 = connection.prepareStatement("select * from notification where uid = ? and notitype = ? or notitype = ? order by nid desc");
                state1.setInt(1, userId);
                state1.setString(2, "NEW_UPVOTE");
                state1.setString(3, "NEW_DOWNVOTE");
            } else {
                state1.setInt(1, userId);
                state1.setString(2, type);
            }

            ResultSet rs = state1.executeQuery();

            List<Notification> notifications = new ArrayList<>();
            while (rs.next()) {
                int notiId = rs.getInt("nid");
                int questionId = rs.getInt("qid");
                int answerId = rs.getInt("aid");
                String notiStatus = rs.getString("notistatus");
                String notitype = rs.getString("notitype");
                String notitime = rs.getTimestamp("notitime").toString();
                String content = rs.getString("content");
                notifications.add(new Notification(notiId, userId, notiStatus, notitype, notitime, content, questionId, answerId));
            }

            return notifications;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
