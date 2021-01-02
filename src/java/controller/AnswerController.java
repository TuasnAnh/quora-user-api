/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import static controller.QuestionController.accountService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.Answer;
import service.AnswerService;
import service.NotificationService;
import service.QuestionService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.AnswerServiceImplement;
import serviceImplement.NotificationServiceImplement;
import serviceImplement.QuestionServiceImplement;

/**
 *
 * @author ADMIN
 */
public class AnswerController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static AnswerService answerService = new AnswerServiceImplement();
    static QuestionService questionService = new QuestionServiceImplement();
    static NotificationService notiService = new NotificationServiceImplement();

    public static void addAnswer(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int questionId = data.get("questionId").getAsInt();
            String content = data.get("content").getAsString();

            boolean check = answerService.addAnswer(userId, questionId, content);
            String returnStr;
            if (check) {
                returnStr = "success";
                int votedUser = questionService.getQuestionAuthor(questionId);
                if (votedUser != userId) {
                    notiService.addNewAnswerNotice(votedUser, "NEW_ANSWER", questionId);
                }
            } else {
                returnStr = "failed";
            }

            String json = new Gson().toJson(returnStr);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void setUpvote(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int answerId = data.get("answerId").getAsInt();

            Map<String, String> resData = answerService.setVote(userId, answerId, "UPVOTE");
            if ("upvote".equals(resData.get("status"))) {
                int votedUser = answerService.getAnswerAuthor(answerId);
                if (votedUser != userId) {
                    notiService.addVoteNotification(votedUser, "NEW_UPVOTE", answerId);
                }
            }

            String json = new Gson().toJson(resData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void setDownvote(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int answerId = data.get("answerId").getAsInt();

            Map<String, String> resData = answerService.setVote(userId, answerId, "DOWNVOTE");
            if ("downvote".equals(resData.get("status"))) {
                int votedUser = answerService.getAnswerAuthor(answerId);
                if (votedUser != userId) {
                    notiService.addVoteNotification(votedUser, "NEW_DOWNVOTE", answerId);
                }
            }
            String json = new Gson().toJson(resData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getAnswerInQuestion(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int questionId = data.get("questionId").getAsInt();
            int lastId = data.get("lastId").getAsInt();

            List<Answer> answers = answerService.getAnswerInQuestion(questionId, userId, lastId);

            String json = new Gson().toJson(answers);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getUserAnswer(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int currentUser = accountService.getUserId(session.getAttribute("email").toString());
            int userId = -1;

            if (request.getParameter("id") == null) {
                userId = currentUser;
            } else {
                userId = Integer.parseInt(request.getParameter("id"));
            }
            int lastId = data.get("lastId").getAsInt();

            List<Answer> answers = answerService.getUserAnswer(userId, lastId, currentUser);

            String json = new Gson().toJson(answers);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getTopicAnswer(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int lastId = data.get("lastId").getAsInt();
            int topicId = data.get("topicId").getAsInt();

            List<Answer> answers = answerService.getTopicAnswer(topicId, userId, lastId);

            String json = new Gson().toJson(answers);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getSuggestAnswer(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int lastId = data.get("lastId").getAsInt();

            List<Answer> answers = answerService.getSuggestAnswer(userId, lastId);

            String json = new Gson().toJson(answers);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
