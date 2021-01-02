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
import service.AnswerService;

/**
 *
 * @author ADMIN
 */
public class AnswerServiceImplement implements AnswerService {

    @Override
    public int getAnswerAuthor(int answerId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select uid from answer where aid = ?");) {

            state1.setInt(1, answerId);
            ResultSet rs = state1.executeQuery();
            rs.next();
            return rs.getInt("uid");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return -1;
    }

    @Override
    public boolean addAnswer(int userId, int questionId, String content) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("insert into answer (uid, qid, content) values (?, ?, ?)");) {

            state1.setInt(1, userId);
            state1.setInt(2, questionId);
            state1.setString(3, content);
            state1.executeUpdate();

            PreparedStatement state2 = connection.prepareStatement("update question set totalanswer = totalanswer + 1 where qid = ?");
            state2.setInt(1, questionId);
            state2.executeUpdate();
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    // flow: check isVoted -> check sameVoted 
    // same: delete in user_answer, minus "upvote"/"downvote" in answer
    // diff: update "relationship" in user_answer, plus new + minus old in answer
    public Map<String, String> setVote(int userId, int answerId, String relationship) {
        Map<String, String> map = new LinkedHashMap<>();

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from user_answer where uid = ? and aid = ?");) {
            //check if user is voted
            state1.setInt(1, userId);
            state1.setInt(2, answerId);
            ResultSet rs = state1.executeQuery();

            if (rs.next()) {
                // update answer table
                // check if already have same vote before
                if (relationship.equals(rs.getString("relationship"))) {
                    // same vote
                    PreparedStatement state6;
                    if ("UPVOTE".equals(relationship)) {
                        state6 = connection.prepareStatement("update answer set upvotes = upvotes - 1 where aid = ?;");
                    } else {
                        state6 = connection.prepareStatement("update answer set downvotes = downvotes - 1 where aid = ?;");
                    }
                    state6.setInt(1, answerId);
                    state6.executeUpdate();
                    map.put("status", "no vote");
                } else {
                    // diff vote
                    PreparedStatement state6;
                    if ("UPVOTE".equals(relationship)) {
                        state6 = connection.prepareStatement("update answer set upvotes = upvotes + 1, downvotes = downvotes - 1 where aid = ?;");
                        map.put("status", "upvote");
                    } else {
                        state6 = connection.prepareStatement("update answer set upvotes = upvotes - 1, downvotes = downvotes + 1 where aid = ?;");
                        map.put("status", "downvote");
                    }
                    state6.setInt(1, answerId);
                    state6.executeUpdate();
                }

                // update user_answer table
                // check if already have same vote before
                if (relationship.equals(rs.getString("relationship"))) {
                    // same vote
                    PreparedStatement state5 = connection.prepareStatement("delete from user_answer where uid = ? and aid = ?");
                    state5.setInt(1, userId);
                    state5.setInt(2, answerId);
                    state5.executeUpdate();
                } else {
                    // diff vote
                    PreparedStatement state3 = connection.prepareStatement("update user_answer set relationship = ? where uid = ? and aid = ?");
                    state3.setString(1, relationship);
                    state3.setInt(2, userId);
                    state3.setInt(3, answerId);
                    state3.executeUpdate();
                }

            } else {
                // user did't vote before   
                PreparedStatement state2 = connection.prepareStatement("insert into user_answer (uid, aid, relationship) values (?, ?, ?);");
                state2.setInt(1, userId);
                state2.setInt(2, answerId);
                PreparedStatement state3;
                if ("UPVOTE".equals(relationship)) {
                    state2.setString(3, "UPVOTE");
                    state2.executeUpdate();
                    state3 = connection.prepareStatement("update answer set upvotes = upvotes + 1 where aid = ?;");
                    map.put("status", "upvote");
                } else {
                    state2.setString(3, "DOWNVOTE");
                    state2.executeUpdate();
                    state3 = connection.prepareStatement("update answer set downvotes = downvotes + 1 where aid = ?;");
                    map.put("status", "downvote");
                }

                state3.setInt(1, answerId);
                state3.executeUpdate();
            }

            PreparedStatement state7 = connection.prepareStatement("select upvotes, downvotes from answer where aid = ?");
            state7.setInt(1, answerId);
            ResultSet rs2 = state7.executeQuery();
            rs2.next();
            map.put("upvote", rs2.getString("upvotes"));
            map.put("downvote", rs2.getString("downvotes"));

            return map;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Answer> getAnswerInQuestion(int questionId, int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select a.*, u.*, t.tid, t.topicname from answer as a "
                        + "join user as u on a.uid = u.uid "
                        + "join question as q on a.qid = q.qid "
                        + "join topic as t on t.tid = q.tid "
                        + "where a.qid = ? and a.aid < ? order by a.aid desc limit ?,?");) {
            state1.setInt(1, questionId);
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

    @Override
    public List<Answer> getUserAnswer(int userId, int lastId, int currentUser) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select a.*, u.*, t.tid, t.topicname from answer as a "
                        + "join user as u on a.uid = u.uid "
                        + "join question as q on a.qid = q.qid "
                        + "join topic as t on t.tid = q.tid "
                        + "where a.uid = ? and a.aid < ? order by a.aid desc limit ?,?");) {
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
                state3.setInt(2, currentUser);
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

    @Override
    public List<Answer> getTopicAnswer(int topicId, int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select a.*, u.*, t.tid, t.topicname from answer as a "
                        + "join user as u on a.uid = u.uid "
                        + "join question as q on a.qid = q.qid "
                        + "join topic as t on t.tid = q.tid "
                        + "where q.tid = ? and a.aid < ? order by a.aid desc limit ?,?");) {
            state1.setInt(1, topicId);
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

    @Override
    public List<Answer> getSuggestAnswer(int userId, int lastId) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select a.*, u.*, t.tid, t.topicname from answer as a "
                        + "join question as q on a.qid = q.qid "
                        + "join topic as t on q.tid = t.tid "
                        + "join user as u on a.uid = u.uid "
                        + "join user_topic as ut on ut.tid = t.tid "
                        + "where ut.uid = ? and a.aid < ? order by a.aid desc limit ?,?");) {
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
