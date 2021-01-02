/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.User;
import serviceImplement.AccountServiceImplement;

/**
 *
 * @author ADMIN
 */
public class AccountController {

    static AccountServiceImplement accountService = new AccountServiceImplement();

    public static void register(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException, MessagingException {
        String email = data.get("email").getAsString();
        String password = data.get("password").getAsString();
        String firstname = data.get("firstname").getAsString();
        String lastname = data.get("lastname").getAsString();
        Map<String, String> status = new LinkedHashMap<>();

        // check if existed email
        boolean isExisted = accountService.checkExistedEmail(email);
        if (isExisted) {
            status.put("status", "Duplicate Email");
        } else {
            int userId = accountService.insertUser(firstname, lastname, email, password);
            System.out.println("new user: " + userId);
            if (userId != -1) {
                status.put("status", "success");
                status.put("roll", accountService.getUserRoll(email));

                // store email in http session
                HttpSession session = request.getSession();
                session.setAttribute("email", email);
                // set session expire in 30 days
                session.setMaxInactiveInterval(30 * 24 * 60 * 60);

                new Thread(() -> {
                    try {
                        // send verify email
                        String verifyUrl = env.env.API_URL + "/verify?email=" + email;
                        utils.EmailUtils.sendEmail(email, verifyUrl);
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }

        String json = new Gson().toJson(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            System.out.println("logout email: " + session.getAttribute("email"));
            session.invalidate();

            Map<String, String> message = new LinkedHashMap<>();
            message.put("message", "Logged out");
            String json = new Gson().toJson(message);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void login(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        String email = data.get("email").getAsString();
        String password = data.get("password").getAsString();
        User user = accountService.login(email, password);

        Map<String, String> message = new LinkedHashMap<>();
        message.put("status", user.getLoginStatus());
        if ("login success".equalsIgnoreCase(user.getLoginStatus())) {
            message.put("roll", user.getRoll());
            // store email in session
            HttpSession session = request.getSession();
            // expire in 30 days
            session.setMaxInactiveInterval(30 * 24 * 60 * 60);
            session.setAttribute("email", email);
        } else {
            System.out.println("Invalid account");
        }

        String json = new Gson().toJson(message);
        System.out.println(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static void verifyEmail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        Map<String, String> message = new LinkedHashMap<>();
        System.out.println(env.env.API_URL);
        if (email != null) {
            boolean isVerified = accountService.verifyEmail(email);
            if (isVerified) {
                message.put("status", "verified");
            } else {
                message.put("status", "failed verify");
            }
        } else {
            message.put("status", "Invalid parameter email");
        }

        String json = new Gson().toJson(message);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static void sendEmailForgotPassword(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        String email = data.get("email").getAsString();
        boolean isExistedEmail = accountService.checkExistedEmail(email);
        if (isExistedEmail) {
            int code = (int) (Math.random() * (99999999 - 10000000) + 10000000);
            boolean check = accountService.addForgotCode(email, code);
            if (check) {
                new Thread(() -> {
                    try {
                        utils.EmailUtils.sendForgotEmail(email, code);
                    } catch (MessagingException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else {
            String json = new Gson().toJson("wrong email");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }

    }

    public static void forgotPassword(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        String newPassword = data.get("newPassword").getAsString();
        String code = data.get("code").getAsString();

        Map<String, String> res = accountService.fogotPassword(newPassword, code);

        String json = new Gson().toJson(res);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static void changePassword(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String oldPassword = data.get("oldPassword").getAsString();
            String newPassword = data.get("newPassword").getAsString();

            User user = accountService.login(session.getAttribute("email").toString(), oldPassword);
            Map<String, String> message = new LinkedHashMap<>();
            if ("login success".equals(user.getLoginStatus())) {
                message = accountService.changePassword(userId, newPassword);
            } else {
                message.put("message", "wrong password");
            }
            String json = new Gson().toJson(message);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        }
    }

    public static void addDescription(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String content = data.get("content").getAsString();

            String check = "failed insert description";
            if (accountService.insertDescription(userId, content)) {
                check = "insert description success";
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void addCredential(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String credential = data.get("credential").getAsString();

            String check = "failed insert credential";
            if (accountService.insertCredential(userId, credential)) {
                check = "insert credential success";
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void addLocation(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String location = data.get("location").getAsString();

            String check = "failed insert location";
            if (accountService.insertLocation(userId, location)) {
                check = "insert location success";
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void addEducation(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String school = data.get("school").getAsString();
            String degreeType = data.get("degreeType").getAsString();
            String graduateTime = data.get("graduateTime").getAsString();

            String check = "failed insert education";
            if (accountService.insertEducation(userId, school, degreeType, graduateTime)) {
                check = "insert education success";
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void addAvatar(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String url = data.get("url").getAsString();
            System.out.println(url);
            String check = "failed insert avatar";
            if (accountService.insertUrl(userId, url)) {
                check = "insert avatar success";
            }

            String json = new Gson().toJson(check);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getUserInformation(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int userId = -1;
            if (request.getParameter("id") == null) {
                HttpSession session = request.getSession();
                userId = accountService.getUserId(session.getAttribute("email").toString());
            } else {
                userId = Integer.parseInt(request.getParameter("id"));
            }
            User user = accountService.getUserInformation(userId);
            accountService.getUserTotalAQB(user);

            String json = new Gson().toJson(user);
            System.out.println(json);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
