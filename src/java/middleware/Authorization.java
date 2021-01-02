/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ADMIN
 */
public class Authorization {

    public static boolean userAuthor(HttpServletResponse response, String roll) throws IOException {
        if ("USER".equals(roll)) {
            return true;
        } else {
            String json = new Gson().toJson("Permission Error: you are not user.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
        return false;
    }
}
