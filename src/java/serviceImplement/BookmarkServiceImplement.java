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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Answer;
import service.BookmarkService;

/**
 *
 * @author ADMIN
 */
public class BookmarkServiceImplement implements BookmarkService {

    @Override
    public Map<String, String> insertBookmark(int userId, int answerId) {
        Map<String, String> map = new LinkedHashMap<>();

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from bookmark where uid = ? and aid = ?");) {

            state1.setInt(1, userId);
            state1.setInt(2, answerId);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                System.out.println("user bookmarked");
                PreparedStatement state3 = connection.prepareStatement("delete from bookmark where uid = ? and aid = ?");
                state3.setInt(1, userId);
                state3.setInt(2, answerId);
                state3.executeUpdate();

                map.put("status", "removed");
            } else {
                System.out.println("user did't bookmark");
                PreparedStatement state3 = connection.prepareStatement("insert into bookmark (uid, aid) values (?, ?)");
                state3.setInt(1, userId);
                state3.setInt(2, answerId);
                state3.executeUpdate();

                map.put("status", "added");
            }

            return map;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Answer> getUserBookmark(int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select a.*, u.credential, u.url, u.first_name, u.last_name, t.tid, t.topicname from bookmark as b "
                        + "join answer as a on b.aid = a.aid "
                        + "join user as u on a.uid = u.uid "
                        + "join question as q on a.qid = q.qid "
                        + "join topic as t on t.tid = q.tid "
                        + "where b.uid = ? and a.aid < ? order by a.aid desc limit ?,?");) {
            state1.setInt(1, userId);
            if (lastId < 1) {
                state1.setInt(2, 10000);
            } else {
                state1.setInt(2, lastId);
            }
            state1.setInt(3, 0);
            state1.setInt(4, 5);
            ResultSet rs = state1.executeQuery();

            List<Answer> answers = new ArrayList<>();
            while (rs.next()) {
                int answerId = rs.getInt("aid");
                Answer answer = new Answer(answerId, rs.getInt("uid"), rs.getTimestamp("answertime").toString(), rs.getString("credential"), rs.getInt("qid"), rs.getString("content"), rs.getInt("upvotes"), rs.getInt("downvotes"), rs.getInt("tid"), rs.getString("topicname"), rs.getString("url"), rs.getString("last_name") + " " + rs.getString("first_name"));

                PreparedStatement state2 = connection.prepareStatement("select * from bookmark where aid = ?");
                state2.setInt(1, answerId);
                ResultSet rs2 = state2.executeQuery();
                if (rs2.next()) {
                    answer.setIsBookmarked(true);
                } else {
                    answer.setIsBookmarked(false);
                }

                PreparedStatement state3 = connection.prepareStatement("select relationship from user_answer where aid = ? and uid = ?");
                state3.setInt(1, answerId);
                state3.setInt(2, userId);
                ResultSet rs3 = state3.executeQuery();
                if (rs3.next()) {
                    if ("UPVOTE".equals(rs3.getString("relationship"))) {
                        answer.setIsUpvote(true);
                        answer.setIsDownvote(false);
                    } else {
                        answer.setIsUpvote(false);
                        answer.setIsDownvote(true);
                    }
                } else {
                    answer.setIsUpvote(false);
                    answer.setIsDownvote(false);
                }

                PreparedStatement state4 = connection.prepareStatement("select content from question where qid = ?");
                state4.setInt(1, answer.getQuestionId());
                ResultSet rs4 = state4.executeQuery();
                rs4.next();
                answer.setQuestion(rs4.getString("content"));

                answers.add(answer);
            }

            return answers;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
