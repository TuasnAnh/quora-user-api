package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.Question;
import service.AccountService;
import service.QuestionService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.QuestionServiceImplement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ADMIN
 */
public class QuestionController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static QuestionService questionService = new QuestionServiceImplement();

    public static void addQuestion(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int topicId = data.get("topicId").getAsInt();
            String content = data.get("content").getAsString();

            boolean check = questionService.addQuestion(userId, topicId, content);
            String returnStr;
            if (check) {
                returnStr = "success";
            } else {
                returnStr = "failed";
            }

            String json = new Gson().toJson(returnStr);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getQuestionInformation(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int questionId = data.get("questionId").getAsInt();
            Question question = questionService.getQuestion(questionId);

            String json = new Gson().toJson(question);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getUserQuestion(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int userId = -1;
            if (request.getParameter("id") == null) {
                HttpSession session = request.getSession();
                userId = accountService.getUserId(session.getAttribute("email").toString());
            } else {
                userId = Integer.parseInt(request.getParameter("id"));
            }
            int lastId = data.get("lastId").getAsInt();

            List<Question> questions = questionService.getUserQuestion(userId, lastId);

            String json = new Gson().toJson(questions);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getTopicQuestion(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            int lastId = data.get("lastId").getAsInt();
            int topicId = data.get("topicId").getAsInt();

            List<Question> questions = questionService.getTopicQuestion(topicId, lastId);

            String json = new Gson().toJson(questions);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getSuggestQuestion(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int lastId = data.get("lastId").getAsInt();

            List<Question> questions = questionService.getSuggestQuestion(userId, lastId);

            String json = new Gson().toJson(questions);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
