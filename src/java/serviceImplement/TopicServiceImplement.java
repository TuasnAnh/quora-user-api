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
import model.Topic;
import service.TopicService;

/**
 *
 * @author ADMIN
 */
public class TopicServiceImplement implements TopicService {

    @Override
    public List<Topic> getFollowedTopic(int userId) {
        List<Topic> followedTopicList = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from topic a, user_topic b where a.tid = b.tid and b.uid = ?");) {

            state1.setInt(1, userId);
            ResultSet rs = state1.executeQuery();

            while (rs.next()) {
                System.out.println(userId);
                followedTopicList.add(new Topic(rs.getInt("tid"), rs.getString("topicname"), rs.getString("imageurl"), rs.getInt("follower")));
            }

            return followedTopicList;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> followTopic(int userId, int topicId) {
        Map<String, String> map = new LinkedHashMap<>();

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select tid from user_topic where uid = ? and tid = ?");) {

            state1.setInt(1, userId);
            state1.setInt(2, topicId);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                System.out.println("user followed");
                PreparedStatement state3 = connection.prepareStatement("delete from user_topic where uid = ? and tid = ?");
                state3.setInt(1, userId);
                state3.setInt(2, topicId);
                state3.executeUpdate();

                PreparedStatement state2 = connection.prepareStatement("update topic set follower = follower - 1 where tid = ?");
                state2.setInt(1, topicId);
                state2.executeUpdate();

                map.put("isFollowed", "false");
            } else {
                System.out.println("user did't follow");
                PreparedStatement state3 = connection.prepareStatement("insert into user_topic (uid, tid) values (?, ?)");
                state3.setInt(1, userId);
                state3.setInt(2, topicId);
                state3.executeUpdate();

                PreparedStatement state2 = connection.prepareStatement("update topic set follower = follower + 1 where tid = ?");
                state2.setInt(1, topicId);
                state2.executeUpdate();

                map.put("isFollowed", "true");
            }

            // get topic current follower amount
            PreparedStatement state4 = connection.prepareStatement("select follower from topic where tid = ?");
            state4.setInt(1, topicId);
            ResultSet rs2 = state4.executeQuery();
            rs2.next();

            map.put("follower", rs2.getInt("follower") + "");

            return map;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;

    }

    @Override
    public List<Topic> getSuggestTopic(int userId) {
        List<Integer> followedTopicId = new ArrayList<>();
        List<Topic> suggestTopic = new ArrayList<>();
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select tid from user_topic where uid = ?");) {

            state1.setInt(1, userId);
            ResultSet rs = state1.executeQuery();

            while (rs.next()) {
                followedTopicId.add(rs.getInt("tid"));
            }

            if (followedTopicId.size() > 0) {
                String tmpString = "(";
                for (int i = 0; i < followedTopicId.size(); i++) {
                    tmpString += "'" + followedTopicId.get(i) + "',";
                    if (i == followedTopicId.size() - 1) {
                        tmpString = tmpString.substring(0, tmpString.length() - 1);
                    }
                }
                tmpString += ")";
                System.out.println("Followed topic id: " + tmpString);

                String queryString = "select * from topic where tid not in " + tmpString;

                PreparedStatement state2 = connection.prepareStatement(queryString);
                ResultSet rs2 = state2.executeQuery();

                while (rs2.next()) {
                    suggestTopic.add(new Topic(rs2.getInt("tid"), rs2.getString("topicname"), rs2.getString("imageurl"), rs2.getInt("follower")));
                }
            } else {
                String queryString = "select * from topic";

                PreparedStatement state2 = connection.prepareStatement(queryString);
                ResultSet rs2 = state2.executeQuery();

                while (rs2.next()) {
                    suggestTopic.add(new Topic(rs2.getInt("tid"), rs2.getString("topicname"), rs2.getString("imageurl"), rs2.getInt("follower")));
                }
            }

            return suggestTopic;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Topic getTopicInfor(int userId, int topicId) {
        boolean isFollowed = false;

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from user_topic  where uid = ? and tid = ?");
                PreparedStatement state2 = connection.prepareStatement("select * from topic where tid = ?");) {

            state1.setInt(1, userId);
            state1.setInt(2, topicId);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                isFollowed = true;
            }

            state2.setInt(1, topicId);
            ResultSet rs2 = state2.executeQuery();

            if (rs2.next()) {
                return new Topic(topicId, rs2.getString("topicname"), rs2.getString("imageurl"), rs2.getInt("follower"), isFollowed);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
