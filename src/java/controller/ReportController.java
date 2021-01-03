/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import middleware.VerifyRequest;
import service.ReportService;
import serviceImplement.AccountServiceImplement;
import serviceImplement.ReportServiceImplement;

/**
 *
 * @author ADMIN
 */
public class ReportController {

    static AccountServiceImplement accountService = new AccountServiceImplement();
    static ReportService reportService = new ReportServiceImplement();

    public static void addReport(HttpServletRequest request, HttpServletResponse response, JsonObject data) throws IOException {
        if (VerifyRequest.verifyUserRequest(request, response, accountService)) {

            int answerId = data.get("answerId").getAsInt();
            boolean isAdded = reportService.addReport(answerId);

            String json = new Gson().toJson(isAdded);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }
}
