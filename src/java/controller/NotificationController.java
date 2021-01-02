/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import static controller.AnswerController.accountService;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.Notification;
import service.NotificationService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.NotificationServiceImplement;

/**
 *
 * @author ADMIN
 */
public class NotificationController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static NotificationService notiService = new NotificationServiceImplement();

    public static void getTotalUnseen(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());

            int total = notiService.getTotalUnseenNotification(userId);
            Map<String, Integer> res = new LinkedHashMap<>();
            res.put("totalUnseen", total);
            String json = new Gson().toJson(res);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void markAsSeenNotification(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String type = data.get("type").getAsString();

            String res = "";
            boolean check = notiService.markAsSeen(userId, type);
            if (check) {
                res = "Mark success";
            } else {
                res = "Mark fail";
            }

            String json = new Gson().toJson(res);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void removeNotification(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int notiId = data.get("notiId").getAsInt();
            boolean isRemoved = notiService.removeNotification(notiId);
            Map<String, Boolean> res = new LinkedHashMap<>();
            res.put("isRemoved", isRemoved);

            String json = new Gson().toJson(res);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getNotification(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            String type = data.get("type").getAsString();
            
            List<Notification> notifications = notiService.getNotification(userId, type);

            String json = new Gson().toJson(notifications);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

}
