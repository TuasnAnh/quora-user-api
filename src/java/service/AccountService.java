/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Map;
import model.User;

/**
 *
 * @author ADMIN
 */
public interface AccountService {

    public boolean checkExistedEmail(String email);

    public int insertUser(String firstName, String lastName, String email, String password);

    public String getUserRoll(String email);

    public User login(String email, String password);

    public boolean verifyEmail(String email);

    public Map<String, String> changePassword(int userId, String newPassword);

    public int getUserId(String email);

    public User getUserInformation(int userId);

    public boolean insertDescription(int userId, String content);

    public boolean insertCredential(int userId, String credential);

    public boolean insertLocation(int userId, String location);

    public boolean insertEducation(int userId, String school, String degreeType, String graduateTime);

    public boolean insertUrl(int userId, String url);
    
    public void getUserTotalAQB(User user);
    
    public boolean addForgotCode(String email, int code);
    
    public Map<String, String> fogotPassword(String newPass, String code);
}
