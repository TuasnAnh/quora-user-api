/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import serviceImplement.AccountServiceImplement;

/**
 *
 * @author ADMIN
 */
public class Authenticate {

    public static String authenticate(HttpServletRequest request, HttpServletResponse response, AccountServiceImplement accountService) throws IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("email") != null) {
            String email = session.getAttribute("email").toString();
            String roll = accountService.getUserRoll(email);
            return roll;
        } else {
            Map<String, String> msg = new LinkedHashMap<>();
            msg.put("authError", "Please login or register!");
            String json = new Gson().toJson(msg);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }

        return null;
    }
}
