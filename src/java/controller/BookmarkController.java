package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import middleware.VerifyRequest;
import model.Answer;
import service.BookmarkService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.BookmarkServiceImplement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ADMIN
 */
public class BookmarkController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static BookmarkService bookmarkService = new BookmarkServiceImplement();

    public static void addBookmark(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int answerId = data.get("answerId").getAsInt();

            Map<String, String> resData = bookmarkService.insertBookmark(userId, answerId);

            String json = new Gson().toJson(resData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }

    public static void getUserBookmark(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {
            HttpSession session = request.getSession();
            int userId = accountService.getUserId(session.getAttribute("email").toString());
            int lastId = data.get("lastId").getAsInt();

            List<Answer> answers = bookmarkService.getUserBookmark(userId, lastId);

            String json = new Gson().toJson(answers);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
