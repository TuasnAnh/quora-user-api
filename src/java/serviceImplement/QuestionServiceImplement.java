package serviceImplement;

import connection.JDBCConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Question;
import service.QuestionService;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ADMIN
 */
public class QuestionServiceImplement implements QuestionService {

    @Override
    public int getQuestionAuthor(int questionId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select uid from question where qid = ?");) {
            state1.setInt(1, questionId);
            ResultSet rs = state1.executeQuery();
            rs.next();
            
            return rs.getInt("uid");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean addQuestion(int userId, int topicId, String content) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("insert into question (uid, tid, content, qtime, totalanswer) values (?, ?, ?, ?, ?)");) {

            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());

            state1.setInt(1, userId);
            state1.setInt(2, topicId);
            state1.setString(3, content);
            state1.setTimestamp(4, ts);
            state1.setInt(5, 0);

            state1.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Question getQuestion(int questionId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from question q, topic t where q.tid = t.tid and q.qid = ?");) {

            state1.setInt(1, questionId);
            ResultSet rs = state1.executeQuery();
            rs.next();

            return new Question(questionId, rs.getInt("uid"), rs.getInt("tid"), rs.getString("content"), rs.getTimestamp("qtime"), rs.getInt("totalanswer"), rs.getString("topicname"));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> getUserQuestion(int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from question q, topic t where q.tid = t.tid and uid = ? and qid < ? order by qid desc limit ?,?");) {

            state1.setInt(1, userId);
            if (lastId < 1) {
                state1.setInt(2, 10000);
            } else {
                state1.setInt(2, lastId);
            }
            state1.setInt(3, 0);
            state1.setInt(4, 10);

            ResultSet rs = state1.executeQuery();

            List<Question> questions = new ArrayList<>();
            while (rs.next()) {
                Question question = new Question(rs.getInt("qid"), rs.getInt("uid"), rs.getInt("tid"), rs.getString("content"), rs.getTimestamp("qtime"), rs.getInt("totalanswer"), rs.getString("topicname"));
                questions.add(question);
            }

            return questions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> getTopicQuestion(int topicId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from question q, topic t where q.tid = t.tid and t.tid = ? and q.qid < ? order by q.qid desc limit ?,?");) {

            state1.setInt(1, topicId);
            if (lastId < 1) {
                state1.setInt(2, 10000);
            } else {
                state1.setInt(2, lastId);
            }
            state1.setInt(3, 0);
            state1.setInt(4, 10);

            ResultSet rs = state1.executeQuery();

            List<Question> questions = new ArrayList<>();
            while (rs.next()) {
                Question question = new Question(rs.getInt("qid"), rs.getInt("uid"), rs.getInt("tid"), rs.getString("content"), rs.getTimestamp("qtime"), rs.getInt("totalanswer"), rs.getString("topicname"));
                questions.add(question);
            }

            return questions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Question> getSuggestQuestion(int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select q.*, q.uid, t.tid, t.topicname from question as q "
                        + "join topic as t on q.tid = t.tid "
                        + "join user_topic as ut on ut.tid = t.tid "
                        + "where ut.uid = ? and q.qid < ? order by q.qid desc limit ?, ?;");) {

            state1.setInt(1, userId);
            if (lastId < 1) {
                state1.setInt(2, 10000);
            } else {
                state1.setInt(2, lastId);
            }
            state1.setInt(3, 0);
            state1.setInt(4, 2);

            ResultSet rs = state1.executeQuery();

            List<Question> questions = new ArrayList<>();
            while (rs.next()) {
                Question question = new Question(rs.getInt("qid"), rs.getInt("uid"), rs.getInt("tid"), rs.getString("content"), rs.getTimestamp("qtime"), rs.getInt("totalanswer"), rs.getString("topicname"));
                questions.add(question);
            }

            return questions;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;

    }
}
