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
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.AccountService;

/**
 *
 * @author ADMIN
 */
public class AccountServiceImplement implements AccountService {

    @Override
    public boolean checkExistedEmail(String email) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from user where email = ?");) {

            state1.setString(1, email);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    @Override
    public int insertUser(String firstName, String lastName, String email, String password) {

        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("insert into user (email, password, first_name, last_name, roll, register_date) values (?, ?, ?, ?, ?, ?);");
                PreparedStatement state2 = connection.prepareStatement("SELECT LAST_INSERT_ID();");) {

            Date date = new java.util.Date();
            Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            String hash = BCrypt.hashpw(password, BCrypt.gensalt(10));
            state1.setString(1, email);
            state1.setString(2, hash);
            state1.setString(3, firstName);
            state1.setString(4, lastName);
            state1.setString(5, "USER");
            state1.setTimestamp(6, timestamp);

            int check = state1.executeUpdate();
            if (check == 1) {
                ResultSet rs = state2.executeQuery();

                if (rs.next()) {
                    System.out.println("new user: " + rs.getInt("LAST_INSERT_ID()"));
                    return rs.getInt("LAST_INSERT_ID()");
                } else {
                    System.out.println("failed get user id");
                }

            } else {
                System.out.println("Failed insert user");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public String getUserRoll(String email) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("select roll from user where email = ?");) {
            state.setString(1, email);

            ResultSet rs = state.executeQuery();

            if (rs.next()) {
                return rs.getString("roll");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public User login(String email, String password) {

        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("select * from user where email = ?");) {
            state.setString(1, email);

            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                if (BCrypt.checkpw(password, rs.getString("password"))) {
                    if (rs.getString("status").equals("VERIFIED")) {
                        return new User(rs.getInt("uid"), email, rs.getString("roll"), "login success");
                    } else if (rs.getString("status").equals("NOT_VERIFY")) {
                        System.out.println("Email not verified");
                        return new User("account not verified");
                    } else if (rs.getString("status").equals("BANNED")) {
                        System.out.println("banned account");
                        return new User("banned account");
                    }
                } else {
                    System.out.println("Incorrect password");
                    return new User("incorrect password");
                }
            } else {
                System.out.println("Invalid email");
                return new User("email not found");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean verifyEmail(String email) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set status = ? where email = ?");) {
            state.setString(1, "VERIFIED");
            state.setString(2, email);
            state.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, String> changePassword(int userId, String newPassword) {
        Map<String, String> msg = new LinkedHashMap<>();
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set password = ? where uid = ?");) {

            String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt(10));
            state.setString(1, hash);
            state.setInt(2, userId);
            state.executeUpdate();

            msg.put("status", "password changed successfully");
            return msg;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        msg.put("status", "password change failed");
        return msg;
    }

    @Override
    public int getUserId(String email) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("select uid from user where email = ?");) {
            state.setString(1, email);

            ResultSet rs = state.executeQuery();
            rs.next();

            return rs.getInt("uid");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    @Override
    public User getUserInformation(int userId) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("select * from user where uid = ?");) {
            state.setInt(1, userId);

            ResultSet rs = state.executeQuery();
            rs.next();

            String description = rs.getString("description");
            String credential = rs.getString("credential");
            String school = rs.getString("school");
            String degreetype = rs.getString("degreetype");
            String graduationTime = rs.getString("graduateTime");
            String location = rs.getString("location");
            String url = rs.getString("url");

            return new User(rs.getInt("uid"), rs.getString("email"), rs.getString("first_name"), rs.getString("last_name"), description, credential, school, degreetype, graduationTime, location, url);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insertDescription(int userId, String content) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set description = ? where uid = ?");) {
            state.setString(1, content);
            state.setInt(2, userId);
            state.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean insertCredential(int userId, String credential) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set credential = ? where uid = ?");) {
            state.setString(1, credential);
            state.setInt(2, userId);
            state.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean insertLocation(int userId, String location) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set location = ? where uid = ?");) {
            state.setString(1, location);
            state.setInt(2, userId);
            state.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean insertEducation(int userId, String school, String degreeType, String graduateTime) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set school = ?, degreetype = ?, graduateTime = ? where uid = ?");) {
            state.setString(1, school);
            state.setString(2, degreeType);
            state.setString(3, graduateTime);
            state.setInt(4, userId);
            state.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean insertUrl(int userId, String url) {
        try (Connection connection = JDBCConnection.getConnection(); PreparedStatement state = connection.prepareStatement("update user set url = ? where uid = ?");) {
            state.setString(1, url);
            state.setInt(2, userId);
            state.executeUpdate();

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public void getUserTotalAQB(User user) {
        try (Connection connection = JDBCConnection.getConnection();) {
            int totalAnswer = 0;
            int totalQuestion = 0;
            int totalBookmark = 0;

            System.out.println(user.getUid());
            PreparedStatement state = connection.prepareStatement("select uid from answer where uid = " + user.getUid());
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                totalAnswer += 1;
            }

            PreparedStatement state2 = connection.prepareStatement("select uid from question where uid = " + user.getUid());
            ResultSet rs2 = state2.executeQuery();
            while (rs2.next()) {
                totalQuestion += 1;
            }

            PreparedStatement state3 = connection.prepareStatement("select uid from bookmark where uid = " + user.getUid());
            ResultSet rs3 = state3.executeQuery();
            while (rs3.next()) {
                totalBookmark += 1;
            }

            user.setTotalAnswer(totalAnswer);
            user.setTotalQuestion(totalQuestion);
            user.setTotalBookmark(totalBookmark);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean addForgotCode(String email, int code) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from verifyCode where email = ?");) {

            state1.setString(1, email);

            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                PreparedStatement state2 = connection.prepareStatement("update verifyCode set vCode = ? where email = ?");
                state2.setString(1, code + "");
                state2.setString(2, email);
                state2.executeUpdate();
            } else {
                PreparedStatement state2 = connection.prepareStatement("insert into verifyCode (vCode, email) values (?, ?);");
                state2.setString(1, code + "");
                state2.setString(2, email);
                state2.executeUpdate();
            }

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public Map<String, String> fogotPassword(String newPass, String code) {
        try (Connection connection = JDBCConnection.getConnection();
                PreparedStatement state1 = connection.prepareStatement("select * from verifyCode where vCode = ?");) {

            Map<String, String> res = new LinkedHashMap<>();

            state1.setString(1, code);
            ResultSet rs = state1.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                String hash = BCrypt.hashpw(newPass, BCrypt.gensalt(10));
                PreparedStatement state2 = connection.prepareStatement("update user set password = ? where email = ?");
                state2.setString(1, hash);
                state2.setString(2, email);
                state2.executeUpdate();

                PreparedStatement state3 = connection.prepareStatement("delete from verifyCode where vCode = ?");
                state3.setString(1, code);
                state3.executeUpdate();

                res.put("msg", "changed password");
            } else {
                res.put("msg", "wrong code");
            }

            return res;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}
