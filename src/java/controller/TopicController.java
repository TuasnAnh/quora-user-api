/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import static controller.AccountController.accountService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.Topic;
import service.TopicService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.TopicServiceImplement;

/**
 *
 * @author ADMIN
 */
public class TopicController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static TopicService topicService = new TopicServiceImplement();

    public static void getSuggestTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());

            List<Topic> suggestedTopiclist = topicService.getSuggestTopic(userId);

            String json = new Gson().toJson(suggestedTopiclist);
            System.out.println("json: " + json);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getFollowedTopic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int userId = -1;
            if (request.getParameter("id") == null) {
                HttpSession session = request.getSession();
                userId = accountService.getUserId(session.getAttribute("email").toString());
            } else {
                userId = Integer.parseInt(request.getParameter("id"));
            }

            List<Topic> followedTopicList = topicService.getFollowedTopic(userId);

            String json = new Gson().toJson(followedTopicList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void followTopic(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int topicId = data.get("topicId").getAsInt();

            Map<String, String> responseMap = topicService.followTopic(userId, topicId);

            String json = new Gson().toJson(responseMap);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getTopicInformation(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int topicId = data.get("topicId").getAsInt();

            Topic topic = topicService.getTopicInfor(userId, topicId);

            String json = new Gson().toJson(topic);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
